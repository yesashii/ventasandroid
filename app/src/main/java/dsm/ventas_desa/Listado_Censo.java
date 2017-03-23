package dsm.ventas_desa;

import mdls.ventas_desa.Titulos;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import db.ventas_desa.DatosAplicacion;

public class Listado_Censo extends Activity 
{
	private static final int VIEW_CONTACT = Menu.FIRST;
	
	AdaptadorTitulares adapter;
	Titulos[] datatit;
	ListView lstClientes;
	DatosAplicacion dadb, dadb2;
	SQLiteDatabase db, db2;
	SimpleCursorAdapter mAdapter;
	Cursor c,c2;
	ArrayAdapter<String> adap;
	EditText txtFiltro;
	Intent verInicioCenso;
	AlertDialog.Builder alerta;
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);	       
	    menu.add(0, VIEW_CONTACT, 0, "Ver Informacion"); 
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case VIEW_CONTACT:
				verInicioCenso = new Intent(Listado_Censo.this,Inicio_Censo.class);     
				startActivity(verInicioCenso);
				finish();		
		}
		
		return false;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadocenso);        
       
        lstClientes = (ListView)findViewById(R.id.lstClientes); 
        txtFiltro = (EditText)findViewById(R.id.txtFiltro);
        txtFiltro.addTextChangedListener(filterTextWatcher);

        SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);
		String idctacte=sp.getString("idctacte", "null");
		
		
        dadb = new DatosAplicacion(Listado_Censo.this,"PrincipalDB", null, 1);
        db = dadb.getWritableDatabase();
        
        c = db.rawQuery(" SELECT T0.sigla, T0.nombre, T0.idctacte, T1.nombre as nomcenso , T1.idactivacion FROM Clientes AS T0 INNER JOIN CENSO_CTACTE T1 ON T0.idctacte=T1.idctacte " + 
						" Where T0.idctacte='"+idctacte+"' ORDER BY T0.idctacte", null);
         
        //registerForContextMenu(lstClientes);
        
        String[] datos = new String[c.getCount()];
        String[] datos2 = new String[c.getCount()];
        
        c.moveToFirst();
        
        dadb2 = new DatosAplicacion(Listado_Censo.this,"InsertarDB", null, 1);
        db2 = dadb2.getWritableDatabase();
        
        for(int i=0;i<=datos.length-1;i++)
        {
        	
        	c2=db2.rawQuery(" SELECT count(*) FROM CENSOS " + 
					" Where ctacte='"+c.getString(2)+"' and idactivacion="+c.getString(4)+" AND estadocenso='Y'", null);
        	
        	c2.moveToFirst();

        	if(c2.getInt(0)==0)
        	{
        		datos[i]= "DISPONIBLE";
        	}
        	else
        	{
        		datos[i]= "NO DISPONIBLE";
        	}
        	
        	datos2[i]=(c.getString(4)==null)?"NULO":(c.getString(4)+".- "+c.getString(3)); 
        	c.moveToNext();
        }
        
        //String[] datosDefinitivos = new String[datos.length];
        
        datatit = new Titulos[datos.length];
        
        for(int i=0;i<datos.length;i++)
        {
        	datatit[i]=new Titulos(datos2[i],datos[i]);        	
        }
        
        adapter = new AdaptadorTitulares(Listado_Censo.this);
        lstClientes.setAdapter(adapter);       
     
        db.close();
        
        lstClientes.setOnItemClickListener(new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				Titulos tit = new Titulos();
				tit = (Titulos)lstClientes.getAdapter().getItem(arg2);				
				@SuppressWarnings("unused")
				
				String disponible =tit.getSubtitulo();
		        String[] aux = tit.getTitulo().split(".- ");
				String idcensoactivacion =aux[0].trim().toString();
		        String nombrecenso =aux[1].trim().toString();
		        
		        SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("idactivacion",idcensoactivacion);
				editor.putString("nombrecenso",nombrecenso);
				editor.putString("disponibilidad",disponible);
				editor.commit();  
				
				if(disponible.equals("DISPONIBLE"))
				{
					Intent intent = new Intent(Listado_Censo.this,Inicio_Censo.class);      
					startActivity(intent);	
					finish();
				}
				else
				{
 			        alerta = new AlertDialog.Builder(Listado_Censo.this);
 			        alerta.setTitle("ENCUESTA NO DISPONIBLE");
 			        
 			        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
 			        {
 						public void onClick(DialogInterface dialog, int which) 
 						{
 							dialog.cancel();
 						}
 					});
 			        
 			        alerta.show();
				}
			}
		}); 
    }
    
    private TextWatcher filterTextWatcher = new TextWatcher() 
    {
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{
			
		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,int after) 
		{

		}
		
		public void afterTextChanged(Editable s) 
		{
			adapter.getFilter().filter(s);
		}
	};
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_listadocenso, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Listado_Censo.this,Clientes_Censo.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
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
    
}
