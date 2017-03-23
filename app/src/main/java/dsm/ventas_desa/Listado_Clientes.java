package dsm.ventas_desa;

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

public class Listado_Clientes extends Activity 
{
	private static final int VIEW_CONTACT = Menu.FIRST;
	
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
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);	       
	    menu.add(0, VIEW_CONTACT, 0, "Ver Informacion"); 
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		String select = (String)lstClientes.getAdapter().getItem(info.position);
		String[] clienteCortado = select.split("\n");
		@SuppressWarnings("unused")
		String nombrecliente =clienteCortado[0].trim();
        String idctacte =clienteCortado[1].trim();
        SharedPreferences sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		switch(item.getItemId())
		{
			case VIEW_CONTACT:
								verCliente = new Intent(Listado_Clientes.this,Ver_Cliente.class);
								editor.putString("idctacte",idctacte);
								editor.commit();        
								startActivity(verCliente);
								finish();		
		}
		
		return false;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado__clientes);        
       
        lstClientes = (ListView)findViewById(R.id.lstClientes); 
        txtFiltro = (EditText)findViewById(R.id.txtFiltro);
        txtFiltro.addTextChangedListener(filterTextWatcher);

        dadb = new DatosAplicacion(Listado_Clientes.this,"PrincipalDB", null, 1);
        db = dadb.getWritableDatabase();
        
        c = db.rawQuery("SELECT sigla,nombre FROM Clientes ", null);
        //registerForContextMenu(lstClientes);
        
        String[] datos = new String[c.getCount()];
        
        c.moveToFirst();
        
        for(int i=0;i<=datos.length-1;i++)
        {
        	datos[i]= (c.getString(0)==null)?"NULO":(c.getString(1)+" \n "+c.getString(0));
        	c.moveToNext();
        }
        
        c = db.rawQuery("SELECT idctacte FROM Clientes ", null);
        
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
        
        adapter = new AdaptadorTitulares(Listado_Clientes.this);
        lstClientes.setAdapter(adapter);       
     
        db.close();
        
        lstClientes.setOnItemClickListener(new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				Titulos tit = new Titulos();
				tit = (Titulos)lstClientes.getAdapter().getItem(arg2);				
				@SuppressWarnings("unused")
				String nombrecliente =tit.getSubtitulo();
		        String idctacte =tit.getTitulo();
		        SharedPreferences sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				Intent intent = new Intent(Listado_Clientes.this,VerCliente2.class);
				editor.putString("idctacte",idctacte);
				editor.commit();
				startActivity(intent);
				finish();		
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
        getMenuInflater().inflate(R.menu.activity_listado__clientes, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Listado_Clientes.this,Clientes.class);
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
