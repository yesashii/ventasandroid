package dsm.ventas_desa;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mdls.ventas_desa.Censos;
import mdls.ventas_desa.Pedido;
import org.ksoap2.serialization.SoapObject;
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

@SuppressLint("SimpleDateFormat")
public class Finalizar_Censo extends Activity 
{
	Button Enviar;
	DatosInsercion dadi;
	SQLiteDatabase db;
	ProgressDialog pdialog;
	SharedPreferences sp;
	
	SoapObject ic;
	SoapObject resultado = null;

	DatosAplicacion dadbsp;
	SQLiteDatabase dbsp;
	
	Spinner bdg;
	Pedido ped;
	long time=0;
	
	TextView txt01, txt02, txt03, txt04, txt05, txt06;
	EditText txtObs;
	
	int aux=0;
	boolean result;
	
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finalizar_censo);
		
		txt01 = (TextView)findViewById(R.id.FCtextView01);
		txt02 = (TextView)findViewById(R.id.FCtextView02);
		txt03 = (TextView)findViewById(R.id.FCtextView03);
		txt04 = (TextView)findViewById(R.id.FCtextView04);
		txt05 = (TextView)findViewById(R.id.FCtextView05);
		txt06 = (TextView)findViewById(R.id.FCtextView06);
		
		Enviar = (Button)findViewById(R.id.FCbtnEnviar);

		pdialog = new ProgressDialog(Finalizar_Censo.this);
	    pdialog.setCancelable(false);
	    
	    sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);
		
		String idactivacion= sp.getString("idactivacion", "0");
		String ctacte=sp.getString("idctacte", "NO DATA");
		String nombrecenso = sp.getString("nombrecenso", "NO DATA");
		
		time = System.currentTimeMillis();
		
	    dadi = new DatosInsercion(Finalizar_Censo.this,"InsertarDB", null,1);
	    
		db = dadi.getWritableDatabase();
		
		Cursor c = db.rawQuery("SELECT count(*) FROM Censos WHERE idactivacion='"+idactivacion+"' and estadocenso='P' AND ctacte='"+ctacte+"' ",null);

		c.moveToFirst();
		
		txt02.setText(idactivacion + ".- " + nombrecenso);
		txt04.setText(String.valueOf(c.getInt(0)));
		txt06.setText(convertTime(time));
		
		db.close();
	    		
	    Enviar.setOnClickListener(new OnClickListener() 
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
		@SuppressLint("SimpleDateFormat")
		@Override
		protected Void doInBackground(Void... params)  
		{
			Censos cen = new Censos();
			
			AccesoWebService acc = new AccesoWebService();
			
			time = System.currentTimeMillis();
			
			sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
			
			String idempresa= String.valueOf(sp.getInt("idempresa", 1));
			String idvendedor= String.valueOf(sp.getInt("idvendedor", 0));
			
			sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);
			
			String idactivacion= sp.getString("idactivacion", "0");
			String ctacte=sp.getString("idctacte", "NO DATA");

			db = dadi.getWritableDatabase();
			
			Cursor c = db.rawQuery("SELECT * FROM Censos WHERE idactivacion='"+idactivacion+"' and estadocenso='P' AND ctacte='"+ctacte+"' ",null);

			c.moveToFirst();

			Log.d("auxiliar1", aux+"");
			Log.d("isonline", isOnline()+"");
			Log.d("isonline", c.getCount()+"");
			
			if(isOnline())
			{
		    	for(int i=0;i<c.getCount(); i++)
		    	{		
					cen.setIdactivacion(String.valueOf(c.getInt(0)));
					cen.setCtacte(c.getString(1));
					cen.setFecha(convertTime(time));
					cen.setCalibre(String.valueOf(c.getInt(2)));
					cen.setItem(String.valueOf(c.getInt(3)));
					cen.setMarca(String.valueOf(c.getInt(4)));
					cen.setRespuesta(c.getString(5));	
					cen.setMotivo(c.getString(6));
					Log.d("auxiliarfor", aux+"");
					try 
					{
						Log.d("Resultado", cen.getIdactivacion()+"-"+cen.getCtacte()+"-"+cen.getFecha()+"-"+cen.getCalibre()+"-"+cen.getItem()+"-"+cen.getMarca()+"-"+cen.getRespuesta());
						
						result=acc.insertarCensos(cen,idempresa,idvendedor);
	
						if(result==true)
						{
							db.execSQL("UPDATE Censos SET estadocenso='Y' Where idactivacion="+c.getInt(0)+" and ctacte='"+c.getString(1)+
								   "' and calibre="+c.getInt(2)+" and item="+c.getInt(3)+" and marca="+c.getInt(4)+"");
							Log.d("auxiliarif", aux+"");	
						}
						else
						{
							aux=aux+1;
							Log.d("auxiliarelse", aux+"");
						}
						
					} 
					catch (Exception e) 
					{
						aux=aux+1;
						e.printStackTrace();
						Log.d("auxiliarcatch", aux+"");
					}
					
					c.moveToNext();
		    	}
			}
			else
			{
				aux=aux+1;
				Log.d("auxiliarelse", aux+"");
			}
			
			Log.d("auxiliar", aux+"");
			
			return null;
			
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			db=dadi.getWritableDatabase();
			AlertDialog.Builder alerta = new AlertDialog.Builder(Finalizar_Censo.this);
	        alerta.setTitle("Informacion Censos");	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{	
					Intent intent= new Intent(Finalizar_Censo.this,Principal.class);
					dialog.cancel();	
					startActivity(intent);
					finish();			
				}
			});		
	        
			pdialog.dismiss();

			if(aux==0)
			{				
				alerta.setMessage("Censo Ingresado Correctamente");
				alerta.show();
			}
			else
			{
				alerta.setMessage("Error al enviar el Censo");
				alerta.show();
			}
			
			db.close();
		}

		@Override
		protected void onPreExecute() 
		{
			pdialog.setTitle("Enviando Censos");
			pdialog.setMessage("Preparando Datos a Enviar");
			pdialog.show();
			super.onPreExecute();
		}
	}
	
	@Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Finalizar_Censo.this,Inicio_Censo.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }	
	
	public boolean isOnline() 
	{
	    ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	    if (netInfo != null && netInfo.isConnected()) 
	    {
	        return true;
	    }
	    
	    return false;
	}
	
	public String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");		
		return format.format(date).toString();
	}
}
