package dsm.ventas_desa;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import mdls.ventas_desa.Censos;
import mdls.ventas_desa.Titulos;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;

public class Envio_Censo extends Activity 
{
	Titulos[] titulos;
	Titulos[] titulos2;
	
	private ListView pendientes;
	private ListView enviados;
	private DatosInsercion dadb2;
	private DatosAplicacion dadb;
	private SQLiteDatabase db,db2;
	private Cursor c, c2;
	private static final int ENVIAR = Menu.FIRST;
	private static final int ENVIAR_TODOS = Menu.FIRST+1;
	private static final int BORRAR = Menu.FIRST+2;
	
	//private String select;
	private String ctacte;
	private String idactivacion;
	ProgressDialog pd;
	Boolean fallo;
	long time=0;
	boolean result;
	int env=0;
	int tot=0;
	
	ArrayAdapter<String> adap,adap1;	
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, ENVIAR, 0, "Enviar");
	    menu.add(0, ENVIAR_TODOS, 0, "Enviar Todos"); 
	    menu.add(0,BORRAR,0,"Borrar");
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		Titulos tit = new Titulos();
		tit=(Titulos)pendientes.getAdapter().getItem(info.position);	
		idactivacion = String.valueOf(tit.getTitulo().split(".-")[0]);
		ctacte= String.valueOf(tit.getSubtitulo());

		switch(item.getItemId())
		{
			case ENVIAR:
						new	EnviarPedido().execute();
						break;
						
			case ENVIAR_TODOS:
						new Enviar_Todos().execute();
						break;
			
			case BORRAR:
						final AlertDialog.Builder alerta = new AlertDialog.Builder(Envio_Censo.this);
				        alerta.setTitle("Alerta");
				        alerta.setMessage("¿Esta Seguro que desea borrar el censo Pendiente? ");
				        
				        alerta.setPositiveButton("Borrar", new DialogInterface.OnClickListener() 
				        {
				        	public void onClick(DialogInterface dialog, int which) 
				        	{
				        		dialog.cancel();	
				        		dadb2= new DatosInsercion(Envio_Censo.this,"InsertarDB",null,1);
								db2=dadb2.getWritableDatabase();				
								db2.execSQL("DELETE FROM CENSOS WHERE idactivacion="+idactivacion+"ctacte='"+ctacte+"'");
								db2.close();
								Intent intent= new Intent(Envio_Censo.this,Pedidos.class);
								startActivity(intent);
								finish();
				        	}
				        });
	        
	        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{				
					dialog.cancel();
				}
			});
	        
	        alerta.show();
	        
			break;
		}
		
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_envio_censo);
		
		TabHost tabs = (TabHost)findViewById(R.id.tabh);
		tabs.setup();
		
        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");   
        
        spec.setContent(R.id.Pendientes);
        spec.setIndicator("Pendientes");
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Enviados);
        spec.setIndicator("Enviados");
        tabs.addTab(spec);
        
        tabs.setCurrentTab(0);        
        
        enviados=(ListView)findViewById(R.id.lstEnviados);
        pendientes = (ListView)findViewById(R.id.lstPendientes);

        dadb=new DatosAplicacion(Envio_Censo.this,"PrincipalDB",null,1);
        dadb2= new DatosInsercion(Envio_Censo.this,"InsertarDB",null,1);
        
        db=dadb.getWritableDatabase();
        db2=dadb2.getWritableDatabase();
         
        registerForContextMenu(pendientes);
         
        c2= db2.rawQuery("SELECT distinct ctacte,idactivacion FROM CENSOS WHERE estadocenso='P'", null);
         
        String[] ctacte = new String[c2.getCount()];
        String[] idactivacion = new String[c2.getCount()];
        String[] nombrecenso=new String[c2.getCount()];
        
        c2.moveToFirst();
         
        if(c2.getCount()>0)
        {
        	for(int i=0;i<c2.getCount();i++)
        	{
        		ctacte[i]=c2.getString(0);
        		idactivacion[i]=c2.getString(1);     		 
        		 
        		c2.moveToNext();
        	}  	 

        	for(int i=0;i<ctacte.length;i++)
        	{        	 
        		c = db.rawQuery("SELECT distinct nombre FROM CENSO_DETALLE WHERE idactivacion='"+idactivacion[i]+"'",null);
        		c.moveToFirst();
        		nombrecenso[i]=c.getString(0);
        	}
        	
        	titulos = new Titulos[ctacte.length];        	

        	for(int i=0;i<ctacte.length;i++)
        	{
        		titulos[i]= new Titulos(idactivacion[i]+".-"+nombrecenso[i], ctacte[i] );
        	}
        	 
        	AdaptadorTitulares adapter1 = new AdaptadorTitulares(Envio_Censo.this,titulos);
 			pendientes.setAdapter(adapter1); 
 			
 			db.close();
        }
        
        db2.close();
        
        db2=dadb2.getWritableDatabase();  
        c2= db2.rawQuery("SELECT distinct ctacte,idactivacion FROM CENSOS WHERE estadocenso='Y'", null);
        
        ctacte = new String[c2.getCount()];
        idactivacion = new String[c2.getCount()];
        nombrecenso=new String[c2.getCount()];
         
        c2.moveToFirst();

        if(c2.getCount()>0)
        {
        	for(int i=0;i<c2.getCount();i++)
        	{
        		ctacte[i]=c2.getString(0);
        		idactivacion[i]=c2.getString(1);     		 
        		c2.moveToNext();
        	}  	 
        	
        	db=dadb.getWritableDatabase();
        	
        	for(int i=0;i<ctacte.length;i++)
        	{        	 
        		c = db.rawQuery("SELECT distinct nombre FROM CENSO_DETALLE WHERE idactivacion='"+idactivacion[i]+"'",null);
        		c.moveToFirst();
        		nombrecenso[i]=c.getString(0);
        	}	 
        	
        	titulos2 = new Titulos[ctacte.length];        	
        	 
        	for(int i=0;i<ctacte.length;i++)
        	{
        		titulos2[i]= new Titulos(idactivacion[i]+".-"+nombrecenso[i], ctacte[i] );
        	}       	 
        	 
        	AdaptadorTitulares adapter2 = new AdaptadorTitulares(Envio_Censo.this,titulos2);
 			enviados.setAdapter(adapter2);
 			 
 			db.close(); 
        }    
        
        db2.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pedidos, menu);
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		Intent intent=new Intent(Envio_Censo.this,MenuCenso.class);
		startActivity(intent);
		finish();
		super.onBackPressed();
	}

	public class EnviarPedido extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPostExecute(Void result) 
		{
			pd.dismiss();
			AlertDialog.Builder alerta = new AlertDialog.Builder(Envio_Censo.this);
	        alerta.setTitle("Censo");
	        alerta.setMessage("Error al enviar el Censo... \n respuestas enviadas: "+env+"/"+tot);
	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
	        	public void onClick(DialogInterface dialog, int which) 
	        	{				
					dialog.cancel();				
				}
			});	
	        
			if(fallo)
			{
				alerta.show();
			}
			else
			{	
				alerta.setMessage("Censo ingresado correctamente \n respuestas enviadas: "+env+"/"+tot);
				
				alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
				{			
					public void onClick(DialogInterface dialog, int which) 
					{			
						dialog.cancel();	
						Intent intent=new Intent(Envio_Censo.this,Envio_Censo.class);
				    	startActivity(intent);
				    	finish();
					}
				});		
				
				alerta.show();
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{	
			pd = new ProgressDialog(Envio_Censo.this);
			pd.setTitle("Enviando");
			pd.setMessage("Enviando censos espere un momento...");
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{	
			Censos cen = new Censos();
			AccesoWebService acc = new AccesoWebService();
			
			time = System.currentTimeMillis();
			
			SharedPreferences sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
			String idempresa= String.valueOf(sp.getInt("idempresa", 1));
			String idvendedor= String.valueOf(sp.getInt("idvendedor", 0));
			
			db2=dadb2.getWritableDatabase();
			c2 = db2.rawQuery("SELECT * FROM Censos WHERE idactivacion='"+idactivacion+"' and estadocenso='P' AND ctacte='"+ctacte+"' ",null);
			
			Log.d("ENVIO CENSO: ","SELECT * FROM Censos WHERE idactivacion='"+idactivacion+"' and estadocenso='P' AND ctacte='"+ctacte+"'");

			c2.moveToFirst();
			
			env=0;
			tot=c2.getCount();
			
			fallo=false;

			if(isOnline())
			{
		    	for(int i=0;i<c2.getCount(); i++)
		    	{		
					cen.setIdactivacion(String.valueOf(c2.getInt(0)));
					cen.setCtacte(c2.getString(1));
					cen.setFecha(convertTime(time));
					cen.setCalibre(String.valueOf(c2.getInt(2)));
					cen.setItem(String.valueOf(c2.getInt(3)));
					cen.setMarca(String.valueOf(c2.getInt(4)));
					cen.setRespuesta(c2.getString(5));	
					cen.setMotivo(c2.getString(6));
					
					try 
					{
						Log.d("ENVIO CENSO: ","ENVIANDO");
						
						result=acc.insertarCensos(cen,idempresa,idvendedor);

						if(result==true)
						{
							Log.d("ENVIO CENSO: ","ACTUALIZANDO");
							db2.execSQL(" UPDATE Censos SET estadocenso='Y' Where idactivacion="+c2.getInt(0)+" and ctacte='"+c2.getString(1)+
										"' and calibre="+c2.getInt(2)+" and item="+c2.getInt(3)+" and marca="+c2.getInt(4)+"");

							env=env+1;
						}
					} 
					catch (Exception e) 
					{
						fallo=true;
						e.printStackTrace();
					}
					
					c2.moveToNext();
		    	}
			}
			else
			{
				fallo=true;
			}
			
			db2.close();
			return null;
		}
	}
	
	public class Enviar_Todos extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPostExecute(Void result) 
		{
			pd.dismiss();
			
			final AlertDialog.Builder alerta = new AlertDialog.Builder(Envio_Censo.this);
	        alerta.setTitle("Censo");
	        alerta.setMessage("Ingreso de censos finalizados \n Ing :"+env+"/"+tot);
	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{	
					dialog.cancel();
					Intent intent = new Intent(Envio_Censo.this,Envio_Censo.class);
					startActivity(intent);
					finish();
				}
			});		
	        
	        alerta.show();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pd = new ProgressDialog(Envio_Censo.this);
			pd.setTitle("Enviando");
			pd.setMessage("Enviando todos los censos pendientes espere un momento...");
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			Censos cen = new Censos();
			AccesoWebService acc = new AccesoWebService();
		
			time = System.currentTimeMillis();
		
			SharedPreferences sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
			String idempresa= String.valueOf(sp.getInt("idempresa", 1));
			String idvendedor= String.valueOf(sp.getInt("idvendedor", 0));

			db2=dadb2.getWritableDatabase();
			c2 = db2.rawQuery("SELECT * FROM Censos WHERE estadocenso='P'",null);
			c2.moveToFirst();
			
			env=0;
			tot=c2.getCount();
			
			if(isOnline())
			{
				for(int i=0;i<c2.getCount(); i++)
				{		
					cen.setIdactivacion(String.valueOf(c2.getInt(0)));
					cen.setCtacte(c2.getString(1));
					cen.setFecha(convertTime(time));
					cen.setCalibre(String.valueOf(c2.getInt(2)));
					cen.setItem(String.valueOf(c2.getInt(3)));
					cen.setMarca(String.valueOf(c2.getInt(4)));
					cen.setRespuesta(c2.getString(5));	
					cen.setMotivo(c2.getString(6));
				
					try 
					{
						result=acc.insertarCensos(cen,idempresa,idvendedor);

						if(result==true)
						{
							db2.execSQL("UPDATE Censos SET estadocenso='Y' Where idactivacion="+c2.getInt(0)+" and ctacte='"+c2.getString(1)+
									   "' and calibre="+c2.getInt(2)+" and item="+c2.getInt(3)+" and marca="+c2.getInt(4)+"");
							env=env+1;
						}
					} 
					catch (Exception e) 
					{
						fallo=true;
						e.printStackTrace();
					}
				
					c2.moveToNext();
				}
			}
			else
			{
				fallo=true;
			}
		
			db2.close();
			return null;
		}
			
	}
	
	public boolean isOnline() 
	{
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	    if (netInfo != null && netInfo.isConnected()) 
	    {
	        return true;
	    }
	    
	    return false;
	}
	
	class AdaptadorTitulares extends ArrayAdapter<Titulos>
	{	
		Activity context;
		private LayoutInflater vi;	
		private static final int mResource = R.layout.listitem_titulos;
	    
	    public	AdaptadorTitulares(Activity context,Titulos[] datos) 
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
	}

	public String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");		
		return format.format(date).toString();
	}
}