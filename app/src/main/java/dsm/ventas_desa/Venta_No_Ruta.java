package dsm.ventas_desa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import mdls.ventas_desa.Titulos;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;
import dsm.ventas_desa.Venta_En_Ruta.Onback;

public class Venta_No_Ruta extends Activity 
{	
	private static final int VENDER = Menu.FIRST+1;
	private static final int VER_ESTADO =Menu.FIRST;	

	Titulos[] datos;
	ListView vnr;
	EditText buscar;
	DatosAplicacion dadb;
	SQLiteDatabase db,db1;
	DatosInsercion dadi;	
	String diaSemana;
	int semana;
	int dia;
	Date d;
	Vector<String> nombreCliente;
	String[] arraySiglas;
	String[] arrayCLiente;
	String[] idsClientes;
	ArrayAdapter<String> adap;
	ProgressDialog pd;
	Intent iniciarVenta,estado;
	AdaptadorTitulares adaptador;
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, VER_ESTADO, 0, "Ver datos Cliente");
	    menu.add(0, VENDER, 0, "Iniciar Venta");	            
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		Titulos tit = new Titulos();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		tit = (Titulos)vnr.getAdapter().getItem(info.position);		
		
		String nombrecliente =tit.getSubtitulo();
        String idctacte =tit.getTitulo();
        SharedPreferences sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();	
        
		switch(item.getItemId())
		{
			case VER_ESTADO:
							estado = new Intent(Venta_No_Ruta.this,VerCliente3.class);			
							editor.putString("idctacte",idctacte);
							editor.commit();
							startActivity(estado);
							break;		
			case VENDER:
							nombrecliente=nombrecliente.split("\n")[0].trim();
							dadi= new DatosInsercion(this, "InsertarDB", null, 1);
							db1= dadi.getWritableDatabase();
							iniciarVenta = new Intent(Venta_No_Ruta.this,Inicio_Venta.class);		
					        Integer idcanal;
					        String idpagador;	        				
							db=dadb.getWritableDatabase();
							db1.execSQL("DELETE FROM Insercion_Productos");
							Cursor c= db.rawQuery("SELECT idflete,idcanal,codlegal FROM Clientes WHERE idctacte = '"+idctacte+"';", null);
							c.moveToFirst();
							String idtipocliente = c.getString(0);
							idcanal = Integer.parseInt(c.getString(1).toString());
							idpagador = c.getString(2);
							c.close();
							db.close();
							db1.close();
							editor.putInt("Contador", 0);
							editor.putString("idpagador", idpagador);
							editor.putInt("idcanal",idcanal);
							editor.putString("NombreCliente",nombrecliente);
							editor.putString("idflete",idtipocliente);
							editor.putString("idctacte",idctacte);
							editor.commit();
							startActivity(iniciarVenta);
							finish();
		}
		
		return false;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta__no__ruta);  
        
        buscar = (EditText)findViewById(R.id.txtFilter);
        buscar.addTextChangedListener(filterTextWatcher);
        vnr =(ListView)findViewById(R.id.lstVentaNoRuta);        

        d = new Date();
        semana = getWeekOfDate(d)%4;
        dia = getDayOfTheWeek(d);
        
        registerForContextMenu(vnr);

    	final AlertDialog.Builder alerta = new AlertDialog.Builder(Venta_No_Ruta.this);
        alerta.setTitle("Error");
        alerta.setMessage("No hay rutas disponibles los dias Domingos");
        
        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
				onBackPressed();
			}
		});
        
        if(semana==0)
        {
        	semana=4;
        }

        switch(dia)
        {
        	case 1:
        			diaSemana ="Error";
        			break;
		    case 2:
		        	diaSemana ="lun";
		        	break;
		    case 3:
		        	diaSemana ="mar";
		        	break;
		    case 4:
		        	diaSemana ="mie";
		        	break;
		    case 5:
		        	diaSemana ="jue";
		        	break;
		    case 6:
		        	diaSemana ="vie";
		        	break;
		    case 7:
		        	diaSemana ="sab";
		        	break;
        }
        
        
        if(diaSemana.equals("Error"))
        {	
            alerta.show();
        }
        else
        {
            pd = new ProgressDialog(Venta_No_Ruta.this);
            pd.setTitle("Cargando");
            pd.setMessage("Espere un momento...");        
            
            new OnBack().execute();	
        }
        
    }
    
    public static int getDayOfTheWeek(Date d)
    {
    	GregorianCalendar cal = new GregorianCalendar();
    	cal.setTime(d);
    	return cal.get(Calendar.DAY_OF_WEEK);		
    }
    
    public static int getWeekOfDate(Date d)
    {
    	GregorianCalendar cal = new GregorianCalendar();
    	cal.setTime(d);
    	return cal.get(Calendar.WEEK_OF_YEAR);
    }
        
    public class OnBack extends AsyncTask<Void, Void, Void>
    {
		@Override
		protected void onPostExecute(Void result) 
		{
			datos = new Titulos[arrayCLiente.length];
			
			for(int i=0;i<arrayCLiente.length;i++)
			{
				datos[i]=new Titulos(idsClientes[i],arrayCLiente[i]+" \n "+arraySiglas[i]);
			}
			
			adaptador= new AdaptadorTitulares(Venta_No_Ruta.this);
			vnr.setAdapter(adaptador);			
			pd.dismiss();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			dadb = new DatosAplicacion(Venta_No_Ruta.this, "PrincipalDB", null, 1);
	        db = dadb.getWritableDatabase();	        
	        Cursor c =  db.rawQuery("SELECT idctacte,nombre,sigla FROM Clientes WHERE idctacte NOT IN(SELECT idcliente FROM Rutas WHERE "+diaSemana+"cob = 1 AND semana"+semana+"cob = 1)" , null);
	        
	        db = dadb.getWritableDatabase(); 
	       
	        idsClientes = new String[c.getCount()];
	        arrayCLiente= new String[c.getCount()];
	        arraySiglas=new String[c.getCount()];
	        
	        c.moveToFirst();
	        
	        for(int i=0;i<c.getCount();i++)
	        {
	        	idsClientes[i] = c.getString(0).trim();
	        	arrayCLiente[i]=c.getString(1);
	        	arraySiglas[i]=c.getString(2);
	        	
	        	c.moveToNext();
	        }	       
	        
	        db.close();	        

			return null;
		}
    }
    
    private TextWatcher filterTextWatcher = new TextWatcher() 
    {
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{

		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,int after) 
		{
			// TODO Auto-generated method stub
		}
		
		public void afterTextChanged(Editable s) 
		{
			AdaptadorTitulares adaptor = (AdaptadorTitulares)vnr.getAdapter();	            
	        adaptor.getFilter().filter(s);
	        adaptor.notifyDataSetChanged();
		}
	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_venta__no__ruta, menu);
        return true;
    }
    
	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(Venta_No_Ruta.this,Principal.class);
		startActivity(intent);
		super.onBackPressed();
	}
	
	class AdaptadorTitulares extends ArrayAdapter<Titulos>
	{	
		Activity context;
		private LayoutInflater vi;	
		private static final int mResource = R.layout.listitem_titulos;

		public	AdaptadorTitulares(Activity context) 
		{
    		super(context, mResource,datos);
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
    	
    	@Override
    	public void notifyDataSetChanged() 
    	{
    	    super.notifyDataSetChanged();
    	}	
   }
	
}