package dsm.ventas_desa;

import java.text.DecimalFormat;
import mdls.ventas_desa.Titulos;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import db.ventas_desa.DatosAplicacion;

public class VerDetalle2 extends Activity
{
	AdaptadorTitulares adapter;
	Titulos[] datatit;
	ListView lstClientes;
	DatosAplicacion dadb;
	SQLiteDatabase db;
	SimpleCursorAdapter mAdapter;
	Cursor c,c1;
	ArrayAdapter<String> adap;
	EditText txtFiltro;
	Intent verCliente;
	String clienteFinal;
	DecimalFormat formato;
	String rut;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalle);
         
        lstClientes = (ListView)findViewById(R.id.lstDetalle); 
        txtFiltro = (EditText)findViewById(R.id.txtFilt);
        txtFiltro.addTextChangedListener(filterTextWatcher);
        
        SharedPreferences sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);              
        clienteFinal =sp.getString("idctacte","NO DATA");        
        formato = new DecimalFormat("'$'###,###,###.##");
        Log.d("ERROR APLICACION",clienteFinal+"");
        
        dadb = new DatosAplicacion(VerDetalle2.this,"PrincipalDB", null, 1);
        db = dadb.getWritableDatabase();
        
        c = db.rawQuery("SELECT codlegal FROM Clientes WHERE idctacte='"+clienteFinal+"'", null);
        
        c.moveToFirst();
        
        rut=c.getString(0);
        
        c = db.rawQuery("SELECT fechavcto,saldo FROM CliDet WHERE '"+rut+"' =rut", null);
        //registerForContextMenu(lstClientes);
        
        String[] datos = new String[c.getCount()];
        
        c.moveToFirst();
        
        for(int i=0;i<=datos.length-1;i++)
        {
        	datos[i]= (c.getString(0)==null)?"NULO":("MONTO: "+formato.format(c.getInt(1))+"  F.VCTO : "+c.getString(0));
        	c.moveToNext();
        }
        
        c = db.rawQuery("SELECT numero FROM CliDet WHERE '"+rut+"' =rut", null);
        
        String[] datos2 = new String[c.getCount()];
        
        c.moveToFirst();
        
        for(int i=0;i<=datos.length-1;i++)
        {
        	datos2[i]= (c.getString(0)==null)?"NULO":c.getString(0);
        	c.moveToNext();
        }
        
        //String[] datosDefinitivos = new String[datos.length];
        
        datatit = new Titulos[datos.length];
        
        for(int i=0;i<datos.length;i++)
        {
        	datatit[i]=new Titulos(datos2[i],datos[i]);        	
        }
        
        adapter = new AdaptadorTitulares(VerDetalle2.this);
        lstClientes.setAdapter(adapter);       
     
        db.close();
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() 
	{	
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{
			// TODO Auto-generated method stub	
		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,int after) 
		{
			// TODO Auto-generated method stub	
		}
		
		public void afterTextChanged(Editable s) 
		{
			adapter.getFilter().filter(s);	
		}
	};
	

    class AdaptadorTitulares extends ArrayAdapter<Titulos>
    {	
    	Activity context;
    	private LayoutInflater vi;	
    	
    	private static final int mResource = R.layout.listitem_titulos;
        	
        public	AdaptadorTitulares(Activity context) 
        {
        	super(context, mResource,datatit);
        	this.context = context;		
        	vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
        } 
        
        public  class ViewHolder
        {
        	private TextView textViewTitulo;
        	private TextView textViewSubTitulo;
        		
        	public ViewHolder(View v)
        	{
        		this.textViewTitulo =(TextView) v.findViewById(R.id.LblTitulo);
        		this.textViewSubTitulo=(TextView) v.findViewById(R.id.LblSubTitulo);
        	}
        } 	
        	
        public View getView(int position, View convertView, ViewGroup parent) 
        {		
        	ViewHolder holder;   		
        	
        	if (convertView == null) 
        	{		
        		convertView = vi.inflate(mResource, null);    			
        		holder = new ViewHolder(convertView);    			
        		convertView.setTag(holder);
        			
        	} 
        	else 
        	{    			
        		holder = (ViewHolder) convertView.getTag();    			
        	}
        		
        	final Titulos item = getItem(position);
    			
        	if(item!=null)
        	{
        		holder.textViewSubTitulo.setText(Html.fromHtml(item.getSubtitulo()));
        		holder.textViewTitulo.setText(Html.fromHtml(item.getTitulo()));		
        	}
        		
        	return convertView;
   		}     	   
    }

    public void onBackPressed()
    {
    	Intent intent=new Intent(VerDetalle2.this,VerCliente2.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
	
}

