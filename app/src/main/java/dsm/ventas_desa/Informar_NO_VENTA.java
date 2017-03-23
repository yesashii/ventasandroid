package dsm.ventas_desa;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import com.AccesoServicioWeb._ventas_desa.GPSListener;
import mdls.ventas_desa.Pedido;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class Informar_NO_VENTA extends Activity 
{
	Spinner informe;
	TextView idctacte,razonSocial,sigla;
	Button enviar;
	DatosAplicacion dadb;
	SQLiteDatabase db;
	ProgressDialog pdialog;
	String[] incidencia;
	ArrayAdapter<String> adaptador;
	String idcliente,nombreVendedor,nombreEmpresa,fecha,hora,nombreCliente;
	String ssigla;
	int idvendedor;
	SharedPreferences sp;
	private SimpleDateFormat formatoFecha;
	private SimpleDateFormat formatoHora;
	Boolean validar;
	
	double latitude=0;
	double longitude=0;
	long time=0;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        new obtenerGPS().execute();
        
        setContentView(R.layout.activity_informar__no__vent);
        
        informe = (Spinner)findViewById(R.id.spNVincidencia);
        idctacte = (TextView)findViewById(R.id.tvNVCliente);
        razonSocial =(TextView)findViewById(R.id.tvNVRS);
        sigla = (TextView)findViewById(R.id.tvNVSigla);
        enviar=(Button)findViewById(R.id.btnNVenviar);
        
        formatoFecha = new SimpleDateFormat("yyMMdd");
		formatoHora = new SimpleDateFormat("HHmmss");
		
        sp = getSharedPreferences("DatosSistema", Context.MODE_PRIVATE);
        
        nombreVendedor= sp.getString("nombreVendedor","NODATA");   
        nombreEmpresa = sp.getString("nombreEmpresa", "NODATA");
        idvendedor=sp.getInt("idvendedor", 0);
        
        sp=getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
        
        idcliente = sp.getString("idctacte","NODATA");
        nombreCliente = sp.getString("NombreCliente","NODATA");
        
        incidencia = new String[] {"LOCAL CERRADO","TIENE STOCK","NO ESTA ENCARGADO O DUEÑO","CUENTA CORRIENTE BLOQUEADA","POR DIF. PRECIO COMPRA A DIST.","PEND. GENERAR N/C O CANJE PUB."};
        
        pdialog = new ProgressDialog(Informar_NO_VENTA.this);
        pdialog.setCancelable(false);
        
        new TareaAsincrona().execute();
        
        enviar.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				new OnBack().execute();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_informar__no__vent, menu);
        return true;
    }
    
    public class TareaAsincrona extends AsyncTask<Void, Void, Void>
    {
		@Override
		protected void onPostExecute(Void result) 
		{	
			adaptador = new ArrayAdapter<String>(Informar_NO_VENTA.this,android.R.layout.simple_spinner_item,incidencia);
			adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			informe.setAdapter(adaptador);
			
			idctacte.setText(idcliente);
			razonSocial.setText(nombreCliente);
			sigla.setText(ssigla);
			
			pdialog.dismiss();
		}

		@Override
		protected void onPreExecute() 
		{
			pdialog.setTitle("Cargando");
			pdialog.setMessage("Espere un momento porfavor...");
			pdialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			dadb = new DatosAplicacion(Informar_NO_VENTA.this,"PrincipalDB", null, 1);
			db = dadb.getWritableDatabase();
			
			Cursor c = db.rawQuery("SELECT sigla FROM Clientes WHERE idctacte = '"+idcliente+"';",null);			
			
			c.moveToFirst();			
			ssigla = c.getString(0);			
			db.close();
	
			return null;
		}
    }
    
    public class OnBack extends AsyncTask<Void, Void, Void>
    {
		@Override
		protected void onPostExecute(Void result) 
		{
			AlertDialog.Builder alerta = new AlertDialog.Builder(Informar_NO_VENTA.this);
	        alerta.setTitle("Informacion");	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{	
					Intent intent= new Intent(Informar_NO_VENTA.this,Venta_En_Ruta.class);
					startActivity(intent);
					finish();
					dialog.cancel();				
				}
			});		
	        
			pdialog.dismiss();
			
			if(validar)
			{				
				alerta.setMessage("Informe ingresado Correctamente");
				alerta.show();				
			}
			else
			{
				alerta.setMessage("Error en el envio del Informe");
				alerta.show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pdialog.setTitle("Enviando Informe");
			pdialog.setMessage("Espere un momento porfavor");
			pdialog.show();
			
			super.onPreExecute();
		}

		@SuppressWarnings("unused")
		@Override
		protected Void doInBackground(Void... params) 
		{
			DatosInsercion dadi = new DatosInsercion(Informar_NO_VENTA.this,"InsertarDB", null,1);
			SQLiteDatabase db = dadi.getWritableDatabase();
			SharedPreferences sp;
			sp=getSharedPreferences("InsercionProductos", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			Pedido ped = new Pedido();			
			AccesoWebService acc = new AccesoWebService();
			Calendar calendario = Calendar.getInstance();
			String fechaF =formatoFecha.format(calendario.getTime());
			String horaF = formatoHora.format(calendario.getTime());
			
			Cursor	c= db.rawQuery("SELECT MAX(id) FROM Pedidos",null);
			
			int notamovil;
			
			notamovil = sp.getInt("notamovil",0);
			
			ped.setVendedor(nombreVendedor);
			ped.setFecha(fechaF);
			ped.setHora(horaF);
			ped.setCliente(idcliente);
			ped.setNota("0");
			ped.setOc("");
			ped.setProducto01("");
			ped.setCantidad01("0");
			ped.setDescuento01("0");
			ped.setProducto02("");
			ped.setCantidad02("0");
			ped.setDescuento02("0");
			ped.setProducto03("");
			ped.setCantidad03("0");
			ped.setDescuento03("0");
			ped.setProducto04("");
			ped.setCantidad04("0");
			ped.setDescuento04("0");
			ped.setProducto05("");
			ped.setCantidad05("0");
			ped.setDescuento05("0");
			ped.setProducto06("");
			ped.setCantidad06("0");
			ped.setDescuento06("0");
			ped.setProducto07("");
			ped.setCantidad07("0");
			ped.setDescuento07("0");
			ped.setProducto08("");
			ped.setCantidad08("0");
			ped.setDescuento08("0");
			ped.setProducto09("");
			ped.setCantidad09("0");
			ped.setDescuento09("0");
			ped.setProducto10("");
			ped.setCantidad10("0");
			ped.setDescuento10("0");
			ped.setProducto11("");
			ped.setCantidad11("0");
			ped.setDescuento11("0");
			ped.setProducto12("");
			ped.setCantidad12("0");
			ped.setDescuento12("0");
			ped.setOBS1("");
			ped.setEstado("NOVENTA");
			ped.setGPS_latitud(""+latitude);
			ped.setGPS_longitud(""+longitude);
			ped.setGPS_tiempo(convertTime(time));
			ped.setBodega("0");
			
			String informeF;
			
			if(informe.getSelectedItem().toString().length()>24)
			{
				informeF = informe.getSelectedItem().toString().substring(0, 24);
			}
			else
			{
				informeF = informe.getSelectedItem().toString();
			}
				
			ped.setOpcion(informeF);
			ped.setFechaentrega("");
			ped.setOBS21("");
			ped.setEmpresa(nombreEmpresa);
			ped.setNotamovil(""+(notamovil+1));
			
			Boolean ins =insertarPedido(ped);
			
			if(isOnline())
			{		
				if(ins)
				{
					editor.putInt("notamovil", notamovil+1);
					editor.commit();
					
					try 
					{
						validar=acc.insertarPedido(ped,1,idvendedor);
					} 
					catch (Exception e) 
					{
						validar=false;
						e.printStackTrace();
					} 
				}
				else
				{
					validar=false;
				}
				
				if(validar)
				{
					db=dadi.getWritableDatabase();
					db.execSQL("UPDATE Pedidos SET estadopedido ='OK' WHERE id="+(notamovil+1));
				}	
			}
			else
			{
				validar=false;
			}
			
			db.close();
			return null;
		}
    }
    
    public Boolean insertarPedido(Pedido ped)
	{
		SQLiteDatabase db;
		DatosInsercion dadi = new DatosInsercion(Informar_NO_VENTA.this,"InsertarDB", null,1);
		Boolean validar = true;
		db = dadi.getWritableDatabase();
		ContentValues reg = new ContentValues();
		SharedPreferences pref = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		
		int notamovil = pref.getInt("notamovil",0);
		
		reg.put("id",notamovil+1);
		reg.put("vendedor",ped.getVendedor());
		reg.put("fecha",ped.getFecha());
		reg.put("hora",ped.getHora());
		reg.put("cliente",ped.getCliente());
		reg.put("nota",ped.getNota());
		reg.put("oc",ped.getOc());
		reg.put("producto01",ped.getProducto01());
		reg.put("cantidad01",ped.getCantidad01());
		reg.put("descuento01",ped.getDescuento01());
		reg.put("producto02",ped.getProducto02());
		reg.put("cantidad02",ped.getCantidad02());
		reg.put("descuento02",ped.getDescuento02());
		reg.put("producto03",ped.getProducto03());
		reg.put("cantidad03",ped.getCantidad03());
		reg.put("descuento03",ped.getDescuento03());
		reg.put("producto04",ped.getProducto04());
		reg.put("cantidad04",ped.getCantidad04());
		reg.put("descuento04",ped.getDescuento04());
		reg.put("producto05",ped.getProducto05());
		reg.put("cantidad05",ped.getCantidad05());
		reg.put("descuento05",ped.getDescuento05());
		reg.put("producto06",ped.getProducto06());
		reg.put("cantidad06",ped.getCantidad06());
		reg.put("descuento06",ped.getDescuento06());
		reg.put("producto07",ped.getProducto07());
		reg.put("cantidad07",ped.getCantidad07());
		reg.put("descuento07",ped.getDescuento07());
		reg.put("producto08",ped.getProducto08());
		reg.put("cantidad08",ped.getCantidad08());
		reg.put("descuento08",ped.getDescuento08());
		reg.put("producto09",ped.getProducto09());
		reg.put("cantidad09",ped.getCantidad09());
		reg.put("descuento09",ped.getDescuento09());
		reg.put("producto10",ped.getProducto10());
		reg.put("cantidad10",ped.getCantidad10());
		reg.put("descuento10",ped.getDescuento10());
		reg.put("producto11",ped.getProducto11());
		reg.put("cantidad11",ped.getCantidad11());
		reg.put("descuento11",ped.getDescuento11());
		reg.put("producto12",ped.getProducto12());
		reg.put("cantidad12",ped.getCantidad12());
		reg.put("descuento12",ped.getDescuento12());
		reg.put("OBS",ped.getOBS1());
		reg.put("estado",ped.getEstado());
		reg.put("opcion",ped.getOpcion());
		reg.put("fechaentrega",ped.getFechaentrega());
		reg.put("OBS2",ped.getOBS21());
		reg.put("empresa", ped.getEmpresa());
		reg.put("estadopedido","NO");
		reg.put("gps_latitud",ped.getGPS_latitud());
		reg.put("gps_longitud",ped.getGPS_longitud());
		reg.put("gps_tiempo",ped.getGPS_tiempo());
		reg.put("idbodega",ped.getBodega());
		
		try
		{
			db.insert("Pedidos", null, reg);
		}
		catch(Exception e)
		{
			validar = false;
		}
		
		db.close();
		
		return validar;
	}
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Informar_NO_VENTA.this,Venta_En_Ruta.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
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
    
	public class obtenerGPS extends AsyncTask<Void,Void,Void> 
	{
		Boolean gpsActivo=true;
		ProgressDialog pd = new ProgressDialog(Informar_NO_VENTA.this);
		Boolean obtenido=false;
		
		LocationManager mlocManager=null;
		LocationListener mlocListener=new GPSListener();	

		@Override
		protected void onPostExecute(Void result) 
		{
			if(gpsActivo==false)
			{
				pd.dismiss();
				
				final AlertDialog.Builder alerta = new AlertDialog.Builder(Informar_NO_VENTA.this);
		        alerta.setTitle("GPS Desactivado");
		        alerta.setMessage("Active su GPS porfavor...");
		        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
		        {			
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent intent = new Intent(Informar_NO_VENTA.this,Venta_En_Ruta.class);
						startActivity(intent);
						dialog.cancel();
						finish();			
					}
				});	
		        
		        alerta.show();
			}
			else
			{				
				Log.d("GPS",""+latitude+" "+longitude);
				
				if(time==0)
				{
					time = System.currentTimeMillis();
				}
				
				Log.d("TIME",convertTime(time));
				pd.dismiss();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{			
			mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, mlocListener);
			
			pd.setTitle("Obteniendo Posición global");
			pd.setMessage("Espere un momento porfavor..");
			pd.show();
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			if(mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				int contador=30000;
				
				while(obtenido==false&&contador!=0)
				{					
					if(GPSListener.latitude>0)
					{
						latitude = GPSListener.latitude;
						longitude = GPSListener.longitude;
						time=GPSListener.time;
						
						obtenido=true;
					}
					else
					{
						contador = contador-1;
						Log.d("CONTAR",""+contador);
					}
				}
				
				if(GPSListener.latitude>0)
				{
					Log.d("OK","Estado OK");
				}
				else
				{
					try
					{
						Location loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						latitude=loc.getLatitude();
						longitude=loc.getLongitude();
						time=loc.getTime();
						obtenido=true;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						latitude=-33.437967;
						longitude=-70.6504;
						time=0;
						obtenido=false;
					}
				}
			}
			else
			{				
				gpsActivo=false;
				
			}
			
			return null;
		}
	}
	
	public String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");		
		return format.format(date).toString();
	}

}
