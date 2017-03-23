package dsm.ventas_desa;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import mdls.ventas_desa.Pedido;
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
import android.content.ContentValues;
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

public class Pedidos extends Activity 
{
	Titulos[] titulos;
	Titulos[] titulos2;
	
	private ListView pendientes;
	private ListView enviados;
	private DatosInsercion dadi;
	private DatosAplicacion dadb;
	private SQLiteDatabase db;
	private static final int ENVIAR = Menu.FIRST;
	private static final int ENVIAR_TODOS = Menu.FIRST+1;
	private static final int BORRAR = Menu.FIRST+2;
	
	//private String select;
	private int notamovil=0;
	ProgressDialog pd;
	Boolean fallo;
	
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
		notamovil = Integer.parseInt(tit.getTitulo().split("-")[0]);
	
		switch(item.getItemId())
		{
			case ENVIAR:
						new	EnviarPedido().execute();
						break;
						
			case ENVIAR_TODOS:
						new Enviar_Todos().execute();
						break;
			
			case BORRAR:
						final AlertDialog.Builder alerta = new AlertDialog.Builder(Pedidos.this);
				        alerta.setTitle("Alerta");
				        alerta.setMessage("¿Esta Seguro que desea borrar el pedido Pendiente? ");
				        alerta.setPositiveButton("Borrar", new DialogInterface.OnClickListener() 
				        {
				        	public void onClick(DialogInterface dialog, int which) 
				        	{
				        		dialog.cancel();					
								db=dadi.getWritableDatabase();				
								db.execSQL("DELETE FROM PEDIDOS WHERE id="+notamovil);
								db.close();
								Intent intent= new Intent(Pedidos.this,Pedidos.class);
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
		setContentView(R.layout.activity_pedidos);
		
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
        
        dadi= new DatosInsercion(Pedidos.this,"InsertarDB",null,1);
        dadb=new DatosAplicacion(Pedidos.this,"PrincipalDB",null,1);
        
        db=dadi.getWritableDatabase();
         
        registerForContextMenu(pendientes);
         
        Cursor c= db.rawQuery("SELECT hora,cliente,id FROM PEDIDOS WHERE estadopedido='NO'", null);
         
        String[] horas = new String[c.getCount()];
        String[] ids = new String[c.getCount()];
        String[] notamovil = new String[c.getCount()];
        String sql = "";
        String[] sigla = new String[c.getCount()];
         
        c.moveToFirst();
         
        if(c.getCount()>0)
        {
        	for(int i=0;i<c.getCount();i++)
        	{
        		horas[i]=c.getString(0);
        		ids[i]=c.getString(1);
        		notamovil[i]=c.getString(2);
        		sql=sql+"?,";        		 
        		 
        		c.moveToNext();
        	}
        	 
        	SimpleDateFormat f1 = new SimpleDateFormat("HHmmss");
        	SimpleDateFormat f2 = new SimpleDateFormat("HH:mm:ss");
        	 
        	Date[] fecha = new Date[horas.length];
        	 
        	for(int i=0;i<horas.length;i++)
        	{
        		try 
        		{
					fecha[i]= f1.parse(horas[i]);
				} 
        		catch (ParseException e) 
        		{
					e.printStackTrace();
				}
        		 
        		horas[i]=f2.format(fecha[i]);
        	}
        	 
        	sql=sql.substring(0,sql.length()-1);
        	 
        	db.close();
        	 
        	db=dadb.getWritableDatabase();
        	 
        	for(int i=0;i<ids.length;i++)
        	{        	 
        		c = db.rawQuery("SELECT sigla FROM Clientes WHERE idctacte='"+ids[i]+"'",null);
        		c.moveToFirst();
        		sigla[i]=c.getString(0);
        	}
        	
        	titulos = new Titulos[ids.length];        	

        	for(int i=0;i<ids.length;i++)
        	{
        		titulos[i]= new Titulos(notamovil[i]+"-"+horas[i],sigla[i]);
        	}
        	 
        	AdaptadorTitulares adapter1 = new AdaptadorTitulares(Pedidos.this,titulos);
 			pendientes.setAdapter(adapter1); 
        }
         
        db.close();
         
        db=dadi.getWritableDatabase();
         
        c= db.rawQuery("SELECT hora,cliente,id FROM PEDIDOS WHERE estadopedido='OK'", null);
         
        horas = new String[c.getCount()];
        ids = new String[c.getCount()];
        notamovil = new String[c.getCount()];
        sql = "";
        sigla = new String[c.getCount()];
         
        c.moveToFirst();
        Log.e("PEDIDOS",""+c.getCount());
        
        if(c.getCount()>0)
        {
        	for(int i=0;i<c.getCount();i++)
        	{
        		horas[i]=c.getString(0);
        		ids[i]=c.getString(1);
        		notamovil[i]=c.getString(2);
        		sql=sql+"?,";

        		c.moveToNext();
        	}
        	 
        	SimpleDateFormat f1 = new SimpleDateFormat("HHmmss");
        	SimpleDateFormat f2 = new SimpleDateFormat("HH:mm:ss");
        	 
        	Date[] fecha = new Date[horas.length];
        	 
        	for(int i=0;i<horas.length;i++)
        	{
        		try 
        		{
        			fecha[i]= f1.parse(horas[i]);
				} 
        		catch (ParseException e) 
        		{	
					e.printStackTrace();
				}
        		 
        		horas[i]=f2.format(fecha[i]);
        	}
        	         	 
        	sql = sql.substring(0,sql.length()-1);
        	Log.e("SQL",sql);
        	db.close();
        	 
        	db=dadb.getWritableDatabase();
        	  
        	for(int i=0;i<ids.length;i++)
        	{
        		c = db.rawQuery("SELECT sigla FROM Clientes WHERE idctacte='"+ids[i]+"'",null);
        		c.moveToFirst();
        		sigla[i]=c.getString(0);
        	}       	 
        	
        	titulos2 = new Titulos[ids.length];        	
        	 
        	for(int i=0;i<ids.length;i++)
        	{
        		titulos2[i]= new Titulos(notamovil[i]+"-"+horas[i],sigla[i]);
        	}       	 
        	 
        	AdaptadorTitulares adapter2 = new AdaptadorTitulares(Pedidos.this,titulos2);
 			enviados.setAdapter(adapter2);
 			 
 			db.close(); 
        }

        db.close();        
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
		Intent intent=new Intent(Pedidos.this,Principal.class);
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
			AlertDialog.Builder alerta = new AlertDialog.Builder(Pedidos.this);
	        alerta.setTitle("Pedido");
	        alerta.setMessage("Error al enviar el pedido..");
	        
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
				alerta.setMessage("Pedido ingresado correctamente");
				
				alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
				{			
					public void onClick(DialogInterface dialog, int which) 
					{			
						dialog.cancel();	
						Intent intent=new Intent(Pedidos.this,Pedidos.class);
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
			pd = new ProgressDialog(Pedidos.this);
			pd.setTitle("Enviando");
			pd.setMessage("Enviando pedido espere un momento...");
			pd.show();
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{	
			DatosInsercion dadi = new DatosInsercion(Pedidos.this, "InsertarDB", null, 1);			
			db=dadi.getWritableDatabase();
			SharedPreferences s = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
			
			int idvendedor = s.getInt("idvendedor",-1);
			int idempresa = s.getInt("idempresa",-1);
			
			AccesoWebService acc = new AccesoWebService();
			SimpleDateFormat f1 = new SimpleDateFormat("HHmmss");
			Calendar calendario = Calendar.getInstance();
			String hora = f1.format(calendario.getTime());
			Pedido p = new Pedido();
			fallo = false;
			int nota;			
			SoapObject resultado=null;
			
			if(isOnline())
			{
				try 
				{
					resultado = acc.usarWebService_2P_idven_idemp("obtenerNota",""+idvendedor,""+idempresa);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					fallo=true;
				}			
				
				Log.e("TEST0", ""+fallo);
				
				nota=-1;
				
				try 
				{
					nota = Integer.parseInt(resultado.getProperty(0).toString())+1;
				} 
				catch (NumberFormatException e1) 
				{
					e1.printStackTrace();
					fallo=true;
				}
				
				Cursor c= db.rawQuery("SELECT * FROM Pedidos WHERE estadopedido = 'NO' AND id ="+notamovil,null);
				c.moveToFirst();
				
				Log.e("TEST1",""+c.getCount());
				
				if(c.getCount()>0&&fallo==false)
				{
					p.setVendedor(c.getString(1));
					p.setFecha(c.getString(2));
					p.setHora(""+hora);
					p.setCliente(c.getString(4));
					p.setNota(""+nota);
					p.setOc(c.getString(6));
					p.setProducto01(c.getString(7));
					p.setCantidad01(c.getString(8));
					p.setDescuento01(c.getString(9));
					p.setProducto02(c.getString(10));
					p.setCantidad02(c.getString(11));
					p.setDescuento02(c.getString(12));
					p.setProducto03(c.getString(13));
					p.setCantidad03(c.getString(14));
					p.setDescuento03(c.getString(15));
					p.setProducto04(c.getString(16));
					p.setCantidad04(c.getString(17));
					p.setDescuento04(c.getString(18));
					p.setProducto05(c.getString(19));
					p.setCantidad05(c.getString(20));
					p.setDescuento05(c.getString(21));
					p.setProducto06(c.getString(22));
					p.setCantidad06(c.getString(23));
					p.setDescuento06(c.getString(24));
					p.setProducto07(c.getString(25));
					p.setCantidad07(c.getString(26));
					p.setDescuento07(c.getString(27));
					p.setProducto08(c.getString(28));
					p.setCantidad08(c.getString(29));
					p.setDescuento08(c.getString(30));
					p.setProducto09(c.getString(31));
					p.setCantidad09(c.getString(32));
					p.setDescuento09(c.getString(33));
					p.setProducto10(c.getString(34));
					p.setCantidad10(c.getString(35));
					p.setDescuento10(c.getString(36));
					p.setProducto11(c.getString(37));
					p.setCantidad11(c.getString(38));
					p.setDescuento11(c.getString(39));
					p.setProducto12(c.getString(40));
					p.setCantidad12(c.getString(41));
					p.setDescuento12(c.getString(42));
					p.setOBS1(c.getString(43));
					p.setEstado(c.getString(44));
					p.setOpcion(c.getString(45));
					p.setFechaentrega(c.getString(46));
					p.setOBS21(c.getString(47));
					p.setEmpresa(c.getString(48));
					p.setGPS_latitud(c.getString(50));
					p.setGPS_longitud(c.getString(51));
					p.setGPS_tiempo(c.getString(52));
					p.setNotamovil(""+(notamovil));
					p.setBodega(c.getString(53));//**Bodega
					
					Boolean valido=true;
					
					try 
					{
						valido = acc.insertarPedido(p,idempresa,idvendedor);
					} 
					catch (XmlPullParserException e) 
					{
						valido=false;
						e.printStackTrace();
					} 
					catch (IOException e) 
					{
						valido=false;
						e.printStackTrace();
					}
					
					Log.e("TEST_Valido", ""+valido);
					
					if(valido)
					{
						fallo=false;
					}
					else
					{
						fallo=true;
					}
					
					if(!fallo)
					{					
						ContentValues cv = new ContentValues();
						cv.put("estadopedido","OK");
						db.update("Pedidos", cv, "id="+(notamovil),null);
					}
				}
			}
			else
			{
				fallo=true;
			}
			
			db.close();
			return null;
		}
	}
	
	public class Enviar_Todos extends AsyncTask<Void, Void, Void>
	{
		int contador=0;
		int contadorMax;

		@Override
		protected void onPostExecute(Void result) 
		{
			pd.dismiss();
			
			final AlertDialog.Builder alerta = new AlertDialog.Builder(Pedidos.this);
	        alerta.setTitle("Pedidos");
	        alerta.setMessage("Ingreso de pedidos Finalizados \n Ing :"+contador+"/"+contadorMax);
	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{	
					dialog.cancel();
					Intent intent = new Intent(Pedidos.this,Pedidos.class);
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
			pd = new ProgressDialog(Pedidos.this);
			pd.setTitle("Enviando");
			pd.setMessage("Enviando todos los pedidos pendientes espere un momento...");
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			DatosInsercion dadi = new DatosInsercion(Pedidos.this, "InsertarDB", null, 1);			
			db=dadi.getWritableDatabase();
			SharedPreferences s = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
			
			int idvendedor = s.getInt("idvendedor",-1);
			int idempresa = s.getInt("idempresa",-1);
			
			AccesoWebService acc = new AccesoWebService();
			SimpleDateFormat f1 = new SimpleDateFormat("HHmmss");
			Calendar calendario = Calendar.getInstance();
			String hora = f1.format(calendario.getTime());
			Pedido p = new Pedido();
			fallo = false;
			int nota=-1;
			contador=0;
			SoapObject resultado=null;			
			
			if(isOnline())
			{
				try 
				{
					resultado = acc.usarWebService_2P_idven_idemp("obtenerNota",""+idvendedor,""+idempresa);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					fallo=true;
				}
				
				try 
				{
					nota = Integer.parseInt(resultado.getProperty(0).toString());
				} 
				catch (NumberFormatException e1) 
				{
					e1.printStackTrace();
					fallo=true;
				}				
				
				if(!fallo)
				{
					Cursor c= db.rawQuery("SELECT * FROM Pedidos WHERE estadopedido ='NO'",null);
					c.moveToFirst();				
					contadorMax = c.getCount();
					
					if(c.getCount()>0)
					{
						for(int i=0;i<c.getCount();i++)
						{
							nota=nota+1;
							
							p.setVendedor(c.getString(1));
							p.setFecha(c.getString(2));
							p.setHora(""+hora);
							p.setCliente(c.getString(4));
							p.setNota(""+nota);
							p.setOc(c.getString(6));
							p.setProducto01(c.getString(7));
							p.setCantidad01(c.getString(8));
							p.setDescuento01(c.getString(9));
							p.setProducto02(c.getString(10));
							p.setCantidad02(c.getString(11));
							p.setDescuento02(c.getString(12));
							p.setProducto03(c.getString(13));
							p.setCantidad03(c.getString(14));
							p.setDescuento03(c.getString(15));
							p.setProducto04(c.getString(16));
							p.setCantidad04(c.getString(17));
							p.setDescuento04(c.getString(18));
							p.setProducto05(c.getString(19));
							p.setCantidad05(c.getString(20));
							p.setDescuento05(c.getString(21));
							p.setProducto06(c.getString(22));
							p.setCantidad06(c.getString(23));
							p.setDescuento06(c.getString(24));
							p.setProducto07(c.getString(25));
							p.setCantidad07(c.getString(26));
							p.setDescuento07(c.getString(27));
							p.setProducto08(c.getString(28));
							p.setCantidad08(c.getString(29));
							p.setDescuento08(c.getString(30));
							p.setProducto09(c.getString(31));
							p.setCantidad09(c.getString(32));
							p.setDescuento09(c.getString(33));
							p.setProducto10(c.getString(34));
							p.setCantidad10(c.getString(35));
							p.setDescuento10(c.getString(36));
							p.setProducto11(c.getString(37));
							p.setCantidad11(c.getString(38));
							p.setDescuento11(c.getString(39));
							p.setProducto12(c.getString(40));
							p.setCantidad12(c.getString(41));
							p.setDescuento12(c.getString(42));
							p.setOBS1(c.getString(43));
							p.setEstado(c.getString(44));
							p.setOpcion(c.getString(45));
							p.setFechaentrega(c.getString(46));
							p.setOBS21(c.getString(47));
							p.setEmpresa(c.getString(48));
							p.setGPS_latitud(c.getString(50));
							p.setGPS_longitud(c.getString(51));
							p.setGPS_tiempo(c.getString(52));
							p.setNotamovil(""+(notamovil));
							p.setBodega(c.getString(53));
							
							Boolean valido=true;
							
							try 
							{
								valido = acc.insertarPedido(p,idempresa,idvendedor);
							} 
							catch (XmlPullParserException e) 
							{
								valido=false;
								e.printStackTrace();
							} 
							catch (IOException e) 
							{
								valido=false;
								e.printStackTrace();
							}
							
							if(valido)
							{
								fallo=false;
							}
							else
							{
								fallo=true;
							}
							
							if(!fallo)
							{					
								ContentValues cv = new ContentValues();
								cv.put("estadopedido","OK");
								db.update("Pedidos", cv, "id="+(c.getInt(0)),null);
								contador=contador+1;
							}
							
							c.moveToNext();
						}
					}
					else
					{
						fallo=true;
					}
				}
				else
				{
					fallo = true;
				}
			}
			
			db.close();
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

}
