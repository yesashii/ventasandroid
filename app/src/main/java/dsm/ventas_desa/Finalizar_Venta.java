package dsm.ventas_desa;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import mdls.ventas_desa.Pedido;
import org.ksoap2.serialization.SoapObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import com.AccesoServicioWeb._ventas_desa.GPSListener;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;
import android.widget.DatePicker;
import android.app.DatePickerDialog;

@SuppressLint("SimpleDateFormat")
public class Finalizar_Venta extends Activity 
{
	EditText observacion,observacionbod;
	EditText oc;
	Button enviar;
	DatosInsercion dadi;
	SQLiteDatabase db;
	ProgressDialog pdialog;
	SharedPreferences sp;
	
	SimpleDateFormat formatoFecha,formatoHora;
	Calendar calendario;
	SoapObject ic;
	SoapObject resultado = null;
	
	DatosAplicacion dadbsp;
	SQLiteDatabase dbsp;
	String[] arreglobodega;
	ArrayAdapter<String> adaptador;
	String aux1;
	
	Spinner bdg;
	Pedido ped;
	
	double latitude=0;
	double longitude=0;
	long time=0;
	
	private TextView tvDisplayDate;
	private Button btnChangeDate;

	private int year;
	private int month;
	private int day;
	
	private String Syear;
	private String Smonth;
	private String Sday;

	private String Syear2;
	private String Smonth2;
	private String Sday2;
	
	static final int DATE_DIALOG_ID = 999;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finalizar__venta);
		
		observacion 		= (EditText)findViewById(R.id.fnltxtOBS);
		observacionbod 		= (EditText)findViewById(R.id.fnltxtOBSbod);
		oc 					= (EditText)findViewById(R.id.fnltxtOC);
		enviar 				= (Button)findViewById(R.id.fnlbtnEnviar);
		bdg					= (Spinner)findViewById(R.id.spBodega);
		
		Calendar c = Calendar.getInstance();
		
		int dia 	= c.get(Calendar.DAY_OF_WEEK);
		int hora 	= c.get(Calendar.HOUR_OF_DAY);
		
		setCurrentDateOnView();
		addListenerOnButton();
		
		
		pdialog = new ProgressDialog(Finalizar_Venta.this);
	    pdialog.setCancelable(false);
	    
	    dadi = new DatosInsercion(Finalizar_Venta.this,"InsertarDB", null,1);
	    
		formatoFecha = new SimpleDateFormat("yyMMdd");
		formatoHora = new SimpleDateFormat("HHmmss");
		
		new obtenerGPS().execute();

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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_finalizar__venta, menu);
		return true;
	}
	
	public class OnBack extends AsyncTask<Void,Void,Void>
	{
		private boolean validar=true;

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Void doInBackground(Void... params)  
		{
			Pedido ped = new Pedido();
			sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
			AccesoWebService acc = new AccesoWebService();
			db = dadi.getWritableDatabase();
			
			int idvendedor = sp.getInt("idvendedor", -1);
			int idempresa = sp.getInt("idempresa", -1);
			
			String nombreVendedorF = sp.getString("nombreVendedor", "No DATA");
			String nombreEmpresaF = sp.getString("nombreEmpresa","No DATA");
			calendario = Calendar.getInstance();
			String fechaF =formatoFecha.format(calendario.getTime());
			String horaF = formatoHora.format(calendario.getTime());
			
			sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
			
			String idctacteF = sp.getString("idctacte","NO DATA");

			if(isOnline())
			{
				try 
				{
					resultado = acc.usarWebService_2P_idven_idemp("obtenerNota", ""+idvendedor, ""+idempresa);
					
				} catch (Exception e) 
				{
					e.printStackTrace();
					validar=false;
				}
			}			
			else
			{
				validar=false;
			}
			
			int notaF=0;
			
			if(validar)
			{
				try
				{
					notaF= Integer.parseInt(resultado.getProperty(0).toString())+1;
				}
				catch(Exception e)
				{
					validar=false;
				}
			}			
			 
			String ocF = oc.getText().toString();
			String OBSF = observacion.getText().toString();
			String ObsBod=observacionbod.getText().toString();
		
			Cursor c = db.rawQuery("SELECT sku,cantidad,descuento FROM Insercion_Productos",null);
			
			String[] listaProductos = new String[12];
			int[] listaCantidad = new int[12];
			Float[] listaDescuentos= new Float[12];
			
			c.moveToFirst();
			
			for(int i = 0 ; i<c.getCount();i++)
			{
				listaProductos[i]=c.getString(0);
				listaCantidad[i]=c.getInt(1);
				listaDescuentos[i]=c.getFloat(2);
				c.moveToNext();
			}			
			
			for(int i = c.getCount();i<12;i++)
			{
				listaProductos[i]="";
				listaCantidad[i]=0;
				listaDescuentos[i]=0f;				
			}		
			
			String estadoF = "NP";
			String opcionF ="VENTA";
			
			Calendar cal= Calendar.getInstance();			
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int day = cal.get(Calendar.DAY_OF_WEEK);			
			
			Log.d("HORA",""+hour);
			Log.d("DIA",""+day);
			
			String fechaEF = tvDisplayDate.getText().toString();
			Log.d("fechaEntregaF 1", fechaEF);
			Log.d("fechaEntregaF A", fechaEF.substring(6, 9));
			Log.d("fechaEntregaF M", fechaEF.substring(3, 4));
			Log.d("fechaEntregaF D", fechaEF.substring(0,1));
			String fechaEntregaF=fechaEF.substring(6, 10)+fechaEF.substring(3, 5)+fechaEF.substring(0,2);	
			
			//***************************
			//***************************
			//***************************
			
			Log.d("FECHA", fechaEntregaF);
			String[] sdescuento = new String[12];
			
			for(int i=0;i<12;i++)
			{
				sdescuento[i]=listaDescuentos[i].toString().replace('.', ',');
			}			
			
			int notamovil = sp.getInt("notamovil",0);
			
			ped.setVendedor(nombreVendedorF);
			ped.setFecha(fechaF);
			ped.setHora(horaF);
			ped.setCliente(idctacteF);
			
			if(validar)
			{
				ped.setNota(""+notaF);
			}
			else
			{
				ped.setNota("0");
			}
			
			ped.setOc(ocF.toString());
			ped.setProducto01(listaProductos[0].toString());
			ped.setCantidad01(""+listaCantidad[0]);
			ped.setDescuento01(sdescuento[0]);
			ped.setProducto02(listaProductos[1].toString());
			ped.setCantidad02(""+listaCantidad[1]);
			ped.setDescuento02(sdescuento[1]);
			ped.setProducto03(listaProductos[2].toString());
			ped.setCantidad03(""+listaCantidad[2]);
			ped.setDescuento03(sdescuento[2]);
			ped.setProducto04(listaProductos[3].toString());
			ped.setCantidad04(""+listaCantidad[3]);
			ped.setDescuento04(sdescuento[3]);
			ped.setProducto05(listaProductos[4].toString());
			ped.setCantidad05(""+listaCantidad[4]);
			ped.setDescuento05(sdescuento[4]);
			ped.setProducto06(listaProductos[5].toString());
			ped.setCantidad06(""+listaCantidad[5]);
			ped.setDescuento06(sdescuento[5]);
			ped.setProducto07(listaProductos[6].toString());
			ped.setCantidad07(""+listaCantidad[6]);
			ped.setDescuento07(sdescuento[6]);
			ped.setProducto08(listaProductos[7].toString());
			ped.setCantidad08(""+listaCantidad[7]);
			ped.setDescuento08(sdescuento[7]);
			ped.setProducto09(listaProductos[8].toString());
			ped.setCantidad09(""+listaCantidad[8]);
			ped.setDescuento09(sdescuento[8]);
			ped.setProducto10(listaProductos[9].toString());
			ped.setCantidad10(""+listaCantidad[9]);
			ped.setDescuento10(sdescuento[9]);
			ped.setProducto11(listaProductos[10].toString());
			ped.setCantidad11(""+listaCantidad[10]);
			ped.setDescuento11(sdescuento[10]);
			ped.setProducto12(listaProductos[11].toString());
			ped.setCantidad12(""+listaCantidad[11]);
			ped.setDescuento12(sdescuento[11]);
			ped.setOBS1(OBSF);
			ped.setEstado(estadoF);
			ped.setOpcion(opcionF);
			ped.setFechaentrega(fechaEntregaF);
			ped.setOBS21(ObsBod);
			ped.setEmpresa(nombreEmpresaF);
			ped.setNotamovil(""+(notamovil+1));
			ped.setGPS_latitud(""+latitude);
			ped.setGPS_longitud(""+longitude);
			ped.setGPS_tiempo(convertTime(time));
			
			//Obtiene la Bodega
			aux1=bdg.getSelectedItem().toString();
			
			dadbsp = new DatosAplicacion(Finalizar_Venta.this,"PrincipalDB", null, 1);
		    dbsp = dadbsp.getWritableDatabase();
		    Cursor csp = dbsp.rawQuery("SELECT DISTINCT idbodega FROM Bodega Where nombre='"+aux1+"'", null); 
		    
		    //Se genera una arreglo donde se guardaran los datos de las cadenas y se guardan.
		    csp.moveToFirst();
		    
		    try
		    {
		    	ped.setBodega(csp.getInt(0)+"");
		    }
		    catch (Exception e)
		    {
		    	ped.setBodega("99");
		    }
		    
		    dbsp.close();
		    
			Boolean ins =insertarPedido(ped);
			
			Log.d("VALIDAR", validar+"");
			Log.d("INS", ins+"");
			
			if(validar)
			{				
				if(ins)
				{
					SharedPreferences.Editor editor = sp.edit();	
					editor.putInt("notamovil", (notamovil+1));
					editor.commit();
					try 
					{
						validar=acc.insertarPedido(ped,idempresa,idvendedor);
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
					ContentValues cv = new ContentValues();
					cv.put("estadopedido", "OK");
					db.update("Pedidos", cv, "id="+(notamovil+1),null);
					db.close();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			db=dadi.getWritableDatabase();
			AlertDialog.Builder alerta = new AlertDialog.Builder(Finalizar_Venta.this);
	        alerta.setTitle("Informacion Pedido");	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{	
					Intent intent= new Intent(Finalizar_Venta.this,Principal.class);
					dialog.cancel();	
					startActivity(intent);
					finish();			
				}
			});		
	        
			pdialog.dismiss();

			if(validar)
			{				
				alerta.setMessage("Pedido Ingresado Correctamente");
				alerta.show();
				db.execSQL("DELETE FROM Insercion_Productos");
			}
			else
			{
				alerta.setMessage("Error en el envio del Pedido");
				alerta.show();
			}
			
			db.close();
		}

		@Override
		protected void onPreExecute() 
		{
			pdialog.setTitle("Enviando Pedido");
			pdialog.setMessage("Preparando Datos a Enviar");
			pdialog.show();
			super.onPreExecute();
		}
	}
	
	public Boolean insertarPedido(Pedido ped)
	{
		SQLiteDatabase db;
		DatosInsercion dadi = new DatosInsercion(Finalizar_Venta.this,"InsertarDB", null,1);
		Boolean validar = true;
		db = dadi.getWritableDatabase();
		ContentValues reg = new ContentValues();
		SharedPreferences pf = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		int notamovil = pf.getInt("notamovil",0);
		
		reg.put("id",(notamovil+1));
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
		reg.put("idbodega", ped.getBodega());
		
		try
		{
			db.insert("Pedidos", null, reg);
			sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();	
			editor.putInt("notamovil", (notamovil+1));
			editor.commit();
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
    	Intent intent=new Intent(Finalizar_Venta.this,Inicio_Venta.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }	
	
	public boolean isOnline() 
	{
	    ConnectivityManager cm = 
	         (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
		ProgressDialog pd = new ProgressDialog(Finalizar_Venta.this);
		Boolean obtenido=false;
		
		LocationManager mlocManager=null;
		LocationListener mlocListener=new GPSListener();	

		@Override
		protected void onPostExecute(Void result) 
		{
	    	adaptador = new ArrayAdapter<String>(Finalizar_Venta.this,android.R.layout.simple_spinner_item,arreglobodega);
			adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			bdg.setAdapter(adaptador);
			bdg.setSelection(0);
			
			if(gpsActivo==false)
			{
				pd.dismiss();
				
				final AlertDialog.Builder alerta = new AlertDialog.Builder(Finalizar_Venta.this);
		        alerta.setTitle("GPS Desactivado");
		        alerta.setMessage("Active su GPS porfavor...");
		        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
		        {			
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent intent = new Intent(Finalizar_Venta.this,Inicio_Venta.class);
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
				
				mlocManager.removeUpdates(mlocListener);
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
			//Dando los datos que debe tener el SPINNER
			dadbsp = new DatosAplicacion(Finalizar_Venta.this,"PrincipalDB", null, 1);
		    dbsp = dadbsp.getWritableDatabase();
		    Cursor csp = dbsp.rawQuery("SELECT DISTINCT nombre FROM Bodega", null); 
		    //Se genera una arreglo donde se guardaran los datos de las cadenas y se guardan.
		    arreglobodega = new String[csp.getCount()];
		    csp.moveToFirst();
		    
		    for(int i=0;i<arreglobodega.length;i++)
		    {
		    	arreglobodega[i]= csp.getString(0);
		    	csp.moveToNext();
		    } 
		    
		    //Se cierra la BD.
					
			if(mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				int contador=10000;
				
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
			
		    dbsp.close();
			return null;
		}
	}
	
	public String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");		
		return format.format(date).toString();
	}
	
	public void setCurrentDateOnView() {

		tvDisplayDate = (TextView) findViewById(R.id.tvDate);

		final Calendar c = Calendar.getInstance();	

		Calendar cal= Calendar.getInstance();	
		
		int hourweek = cal.get(Calendar.HOUR_OF_DAY);
		int dayweek = cal.get(Calendar.DAY_OF_WEEK);
		
		if(dayweek==1||dayweek==7)
		{
			AlertDialog.Builder alerta = new AlertDialog.Builder(Finalizar_Venta.this);
	        alerta.setTitle("");
	        alerta.setMessage("No se puede Enviar Pedidos los Fines de Semana");
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{						
					dialog.cancel();
					finish();
				}
			});
	        
	        alerta.show();
		}
		
		if((dayweek==2||dayweek==3||dayweek==4||dayweek==5)&&(hourweek>18))
		{
			c.add(Calendar.DAY_OF_MONTH, 2);
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			
			Sday = "00"+ (day);
			Sday = Sday.substring(Sday.length()-2, Sday.length());
			Smonth = "00" + (month+1);
			Smonth = Smonth.substring(Smonth.length()-2, Smonth.length());
			Syear = String.valueOf(year);
		}
		
		
		if((dayweek==2||dayweek==3||dayweek==4||dayweek==5)&&(hourweek<=18))
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			
			Sday = "00"+ (day);
			Sday = Sday.substring(Sday.length()-2, Sday.length());
			Smonth = "00" + (month+1);
			Smonth = Smonth.substring(Smonth.length()-2, Smonth.length());
			Syear = String.valueOf(year);
		}
		
		if(dayweek==6&&(hourweek>16))
		{
			c.add(Calendar.DAY_OF_MONTH, 3);
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			
			Sday = "00"+ (day);
			Sday = Sday.substring(Sday.length()-2, Sday.length());
			Smonth = "00" + (month+1);
			Smonth = Smonth.substring(Smonth.length()-2, Smonth.length());
			Syear = String.valueOf(year);
		}
		
		if(dayweek==6&&(hourweek<=16))
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			
			Sday = "00"+ (day);
			Sday = Sday.substring(Sday.length()-2, Sday.length());
			Smonth = "00" + (month+1);
			Smonth = Smonth.substring(Smonth.length()-2, Smonth.length());
			Syear = String.valueOf(year);
		}

		// set current date into textview
		tvDisplayDate.setText(new StringBuilder().append(Sday).append("-").append(Smonth).append("-").append(Syear).append(" "));
		
	}

	public void addListenerOnButton() {

		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

		btnChangeDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener,
                         year, month,day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener
                = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;		
			
			Sday  = "00"+ day;
			Sday = Sday.substring(Sday.length()-2, Sday.length());
			Smonth = "00" + (month+1);
			Smonth = Smonth.substring(Smonth.length()-2, Smonth.length());
			Syear = String.valueOf(year);
		
			Calendar cal= Calendar.getInstance();	
			
			int hourweek = cal.get(Calendar.HOUR_OF_DAY);
			int dayweek = cal.get(Calendar.DAY_OF_WEEK);
			
			Calendar c2 = Calendar.getInstance();
			
			if((dayweek==2||dayweek==3||dayweek==4||dayweek==5)&&(hourweek>18))
			{
				c2.add(Calendar.DAY_OF_MONTH, 2);
				year = c2.get(Calendar.YEAR);
				month = c2.get(Calendar.MONTH);
				day = c2.get(Calendar.DAY_OF_MONTH);
				
				Sday2 = "00"+ (day);
				Sday2 = Sday2.substring(Sday2.length()-2, Sday2.length());
				Smonth2 = "00" + (month+1);
				Smonth2 = Smonth2.substring(Smonth2.length()-2, Smonth2.length());
				Syear2 = String.valueOf(year);
				
				if(Integer.parseInt(Syear+Smonth+Sday)<Integer.parseInt(Syear2+Smonth2+Sday2))
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday2)
					   .append("-").append(Smonth2).append("-").append(Syear2)
					   .append(" "));
				}
				else
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday)
					   .append("-").append(Smonth).append("-").append(Syear)
					   .append(" "));
				}
			}
			
			
			if((dayweek==2||dayweek==3||dayweek==4||dayweek==5)&&(hourweek<=18))
			{			
				c2.add(Calendar.DAY_OF_MONTH, 1);
				year = c2.get(Calendar.YEAR);
				month = c2.get(Calendar.MONTH);
				day = c2.get(Calendar.DAY_OF_MONTH);
				
				Sday2 = "00"+ (day);
				Sday2 = Sday2.substring(Sday2.length()-2, Sday2.length());
				Smonth2 = "00" + (month+1);
				Smonth2 = Smonth2.substring(Smonth2.length()-2, Smonth2.length());
				Syear2 = String.valueOf(year);
				
				if(Integer.parseInt(Syear+Smonth+Sday)<Integer.parseInt(Syear2+Smonth2+Sday2))
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday2)
					   .append("-").append(Smonth2).append("-").append(Syear2)
					   .append(" "));
				}
				else
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday)
					   .append("-").append(Smonth).append("-").append(Syear)
					   .append(" "));
				}
			}
			
			if(dayweek==6&&(hourweek>16))
			{			
				c2.add(Calendar.DAY_OF_MONTH, 3);
				year = c2.get(Calendar.YEAR);
				month = c2.get(Calendar.MONTH);
				day = c2.get(Calendar.DAY_OF_MONTH);
				
				Sday2 = "00"+ (day);
				Sday2 = Sday2.substring(Sday2.length()-2, Sday2.length());
				Smonth2 = "00" + (month+1);
				Smonth2 = Smonth2.substring(Smonth2.length()-2, Smonth2.length());
				Syear2 = String.valueOf(year);
				
				if(Integer.parseInt(Syear+Smonth+Sday)<Integer.parseInt(Syear2+Smonth2+Sday2))
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday2)
					   .append("-").append(Smonth2).append("-").append(Syear2)
					   .append(" "));
				}
				else
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday)
					   .append("-").append(Smonth).append("-").append(Syear)
					   .append(" "));
				}
			}
			
			if(dayweek==6&&(hourweek<=16))
			{
				c2.add(Calendar.DAY_OF_MONTH, 1);
				year = c2.get(Calendar.YEAR);
				month = c2.get(Calendar.MONTH);
				day = c2.get(Calendar.DAY_OF_MONTH);
				
				Sday2 = "00"+ (day);
				Sday2 = Sday2.substring(Sday2.length()-2, Sday2.length());
				Smonth2 = "00" + (month+1);
				Smonth2 = Smonth2.substring(Smonth2.length()-2, Smonth2.length());
				Syear2 = String.valueOf(year);
				
				if(Integer.parseInt(Syear+Smonth+Sday)<Integer.parseInt(Syear2+Smonth2+Sday2))
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday2)
					   .append("-").append(Smonth2).append("-").append(Syear2)
					   .append(" "));
				}
				else
				{
					// set selected date into textview
					tvDisplayDate.setText(new StringBuilder().append(Sday)
					   .append("-").append(Smonth).append("-").append(Syear)
					   .append(" "));
				}
			}
			



		}
	};
	
}
