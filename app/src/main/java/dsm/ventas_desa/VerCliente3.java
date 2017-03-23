package dsm.ventas_desa;

import java.text.DecimalFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import db.ventas_desa.DatosAplicacion;
import dsm.ventas_desa.Ver_Cliente.OnBack;
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
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class VerCliente3 extends Activity 
{	
	String cliente;
	String clienteFinal;
	DatosAplicacion dadb;
	SQLiteDatabase db;
	TextView tvVCRut,tvVCRazonSocial,tvVCSigla,tvVCCdePago,tvVCCdepago,tvVCLimiteCredito,tvVCTelefono,tvVCEmail,
	tvVCNombreEncargado,tvVCDireccion,tvVCMontoVencido,tvVCMontoaVencer,tvVCchCartera, tvVCCredDisponible;
	
	Spinner spsoconsumo;
	String rut ;
	String nombre ;
	String sigla;
	int idcondpago;
	double limitedeCredito;
	String telefono;
	String correo;
	String nombreEncargado;
	String direccion;
	String nombreCP;
	double montoVencido;
	double montoVencer;
	double chCartera;
	ProgressDialog pd;
	DecimalFormat formato;
	private Button detalle3,actualizar3;
	private Intent intentDetalle3;
	String[] nombresoconsumo;
	String error;
	Object resultado = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_cliente3);
		
		tvVCRut = (TextView)findViewById(R.id.tvVCRut3);
        tvVCRazonSocial = (TextView)findViewById(R.id.tvVCRazonSocial3);
        tvVCSigla = (TextView)findViewById(R.id.tvVCSigla3);
        tvVCCdepago = (TextView)findViewById(R.id.tvVCCdePago3);
        tvVCLimiteCredito = (TextView)findViewById(R.id.tvVCLimiteCredito3);
        tvVCTelefono = (TextView)findViewById(R.id.tvVCTelefono3);
        tvVCEmail = (TextView)findViewById(R.id.tvVCEmail3);
        tvVCNombreEncargado= (TextView)findViewById(R.id.tvVCNombreEncargado3);
        tvVCDireccion = (TextView)findViewById(R.id.tvVCDireccion3);
        tvVCMontoVencido = (TextView)findViewById(R.id.tvVCMontoVencido3);
        tvVCMontoaVencer = (TextView)findViewById(R.id.tvVCMontoaVencer3);
        tvVCchCartera = (TextView)findViewById(R.id.tvVCChCartera3);
        tvVCCredDisponible=(TextView)findViewById(R.id.tvVCCreddisp);
        detalle3=(Button)findViewById(R.id.VerDetalle3);
        spsoconsumo = (Spinner)findViewById(R.id.spSPSOCONSUMO3);
        actualizar3=(Button)findViewById(R.id.SOCActualizar3);
        intentDetalle3 = new Intent(this,VerDetalle3.class);
		detalle3.setEnabled(true);
		actualizar3.setEnabled(true);
        
        pd = new ProgressDialog(VerCliente3.this);
        
        dadb = new DatosAplicacion(VerCliente3.this,"PrincipalDB", null, 1);
        db = dadb.getWritableDatabase(); 
        
        SharedPreferences sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);              
        clienteFinal =sp.getString("idctacte","NO DATA");        
        formato = new DecimalFormat("'$'###,###,###.##");
        
        //**************************CARGA DE SPINNER**************************
        
    	Cursor c = db.rawQuery("SELECT * FROM SOConsumo",null);
    	c.moveToFirst();   	
    	
    	nombresoconsumo = new String[c.getCount()];
    	
    	for(int i=0;i<nombresoconsumo.length;i++)
    	{
    		nombresoconsumo[i] = c.getString(0);
    		c.moveToNext();
    	}

    	c.close();
    	
    	ArrayAdapter<String> adaptador;
    	
    	adaptador = new ArrayAdapter<String>(VerCliente3.this,android.R.layout.simple_spinner_item,nombresoconsumo);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
    	
    	spsoconsumo.setAdapter(adaptador);
    	
    	String[] args = new String[] {clienteFinal};		
		c = db.rawQuery("SELECT soconsumo FROM Clientes WHERE idctacte=?", args);
		c.moveToFirst();
    	
		if(!c.isNull(0))
		{	
			if(adaptador.getPosition(c.getString(0))>=0)
			{
				int spinnerPosition = adaptador.getPosition(c.getString(0));
				spsoconsumo.setSelection(spinnerPosition);
			}
			else
			{
			    int spinnerPosition = adaptador.getPosition("SELECCIONE...");
			    spsoconsumo.setSelection(spinnerPosition); 
			}
		}
		else
		{
		    int spinnerPosition = adaptador.getPosition("SELECCIONE...");
		    spsoconsumo.setSelection(spinnerPosition); 
		}

    	//**************************TERMINO DE SPINNER**************************
        
        detalle3.setOnClickListener(new OnClickListener() 
        {
 			public void onClick(View v) 
 			{
 				startActivity(intentDetalle3);
 				finish();	
 			}
 		});

        //**************************ACTUALIZAR SOCONSUMO**************************
        
        actualizar3.setOnClickListener(new OnClickListener() 
        {
 			public void onClick(View v) 
 			{
 				String aux=spsoconsumo.getSelectedItem().toString();
 				
 				if(aux.equals("SELECCIONE..."))
 				{
 					AlertDialog.Builder alerta = new AlertDialog.Builder(VerCliente3.this);
 			        alerta.setTitle("Error");	     
 			        alerta.setMessage("Seleccione un SO Consumo distinto");
 			        
 			        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
 			        {			
 						public void onClick(DialogInterface dialog, int which) 
 						{		
 						}
 					});	
 			        
 			        alerta.show();
 				}
 				else
 				{
 					new OnBack().execute();
 				}
 			}
 		});
        
        //**************************FIN ACTUALIZAR SOCONSUMO**************************
        
        new CargaDatos().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ver_cliente3, menu);
		return true;
	}
	
	public class CargaDatos extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) 
		{		
			db = dadb.getWritableDatabase();
				
			String[] args = new String[] {clienteFinal};		
			Cursor c = db.rawQuery("SELECT * FROM Clientes WHERE idctacte=?", args);
			c.moveToFirst();
				
			rut = c.getString(2);
			nombre = c.getString(3);
			sigla= c.getString(4);
			idcondpago = c.getInt(6);
			limitedeCredito = (Double)c.getDouble(7);
			telefono = c.getString(8);
			correo = c.getString(9);
			nombreEncargado = c.getString(10);
			direccion = c.getString(11);
			montoVencido =(Double)c.getDouble(15);
			montoVencer = (Double)c.getDouble(16);
			chCartera = (Double)c.getDouble(17);
								
			c = db.rawQuery("SELECT nombre FROM Condiciones_De_Pago WHERE idcondpago = "+idcondpago, null);
			c.moveToFirst();
				
			nombreCP = c.getString(0);			
									
			db.close();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) 
		{		
			tvVCRut.setText(rut);
			tvVCRazonSocial.setText(nombre);
			tvVCSigla.setText(sigla);
			Log.d("ERROR APLICACION",nombreCP+"");
				
			tvVCCdepago.setText(nombreCP);			
			tvVCLimiteCredito.setText(""+formato.format(limitedeCredito));
			tvVCTelefono.setText(telefono);				
			
			if(correo.equals("anyType{}"))
			{
				tvVCEmail.setText("No Registra Correo");
			}
			else
			{
				tvVCEmail.setText(correo);
			}
			
			tvVCNombreEncargado.setText(nombreEncargado);
			tvVCDireccion.setText(direccion);
			
			if(montoVencido==0)
			{
				tvVCMontoVencido.setTextColor(Color.GREEN);
				tvVCMontoVencido.setText(""+formato.format(montoVencido));
			}
			else
			{
				tvVCMontoVencido.setTextColor(Color.RED);
				tvVCMontoVencido.setText(""+formato.format(montoVencido));	
			}
				
			tvVCMontoaVencer.setText(""+formato.format(montoVencer));
			tvVCchCartera.setText(""+formato.format(chCartera));
				
			double creditodisponible=limitedeCredito-(montoVencer+montoVencido+chCartera);
				
			if(creditodisponible<0)
			{
				tvVCCredDisponible.setTextColor(Color.RED);
				tvVCCredDisponible.setText(""+formato.format(creditodisponible));
			}
			else
			{
				tvVCCredDisponible.setTextColor(Color.GREEN);
				tvVCCredDisponible.setText(""+formato.format(creditodisponible));
			}
				
			pd.dismiss();	
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pd.setTitle("Cargando");
			pd.setMessage("Espere un momento porfavor..");
			pd.show();
			super.onPreExecute();
		}	
	}
	 
	@Override
	public void onBackPressed()
	{
		Intent intent=new Intent(VerCliente3.this,Venta_No_Ruta.class);
	    startActivity(intent);
	    finish();
	    super.onBackPressed();
	}
	
	//*******************************ACTUALIZA SOCONSUMO WS*********************************
	
  	public class OnBack extends AsyncTask<Void,Void,Void>
  	{
  		private boolean validar=true;

  		@SuppressLint("SimpleDateFormat")
  		@Override
  		protected Void doInBackground(Void... params)  
  		{
  			String aux=spsoconsumo.getSelectedItem().toString();

  		    dadb = new DatosAplicacion(VerCliente3.this,"PrincipalDB", null, 1);
  		    db = dadb.getWritableDatabase();
  		        
  			String[] args = new String[] {clienteFinal};		
  			Cursor c = db.rawQuery("SELECT idempresa, idctacte soconsumo FROM Clientes WHERE idctacte=?", args);
  			c.moveToFirst();
  				
  			String stringidempresa=c.getInt(0)+"";
  			String idctacte=c.getString(1);
  				
  			c.close();
  				
  			error="ok";
  				
  			String NAMESPACE = "WS.DESA.cl";
  			String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
  			String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";	
  			String SOAP_ACTION = "WS.DESA.cl/"+"IngSOCManager";
  			
  			SoapObject request = new SoapObject(NAMESPACE, "IngSOCManager");		
  			
  			PropertyInfo idempresaProp = new PropertyInfo();
  			PropertyInfo clienteProp = new PropertyInfo();
  			PropertyInfo soconProp = new PropertyInfo();
  				
  			idempresaProp.setName("idempresa");
  			idempresaProp.setValue(stringidempresa);
  			idempresaProp.setType(PropertyInfo.STRING_CLASS);
  				
  			clienteProp.setName("cliente");
  			clienteProp.setValue(idctacte);
  			clienteProp.setType(PropertyInfo.STRING_CLASS);
  				
  			soconProp.setName("socon");
  			soconProp.setValue(aux);
  			soconProp.setType(PropertyInfo.STRING_CLASS);
  						
  			request.addProperty(idempresaProp);
  			request.addProperty(clienteProp);
  			request.addProperty(soconProp);
  			
  			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
  			envelope.dotNet = true;
  			
  			envelope.setOutputSoapObject(request);			
  				
  			try
  			{
  				HttpTransportSE transporte = new HttpTransportSE(URL1);
  				transporte.call(SOAP_ACTION, envelope);		
  			}	
  			catch (Exception e)
  			{
  				try
  				{
  					HttpTransportSE transporte = new HttpTransportSE(URL2);
  					transporte.call(SOAP_ACTION, envelope);	
  				}
  				catch (Exception ex)
  				{
  					Log.d("ERROR WEB SERVICE", "ERROR AL EJECUTAR");
  				}
  			}			
  											
  			try
  			{		
  				resultado = null;
  				resultado = envelope.getResponse();
  			}
  			catch(Exception e)
  			{
  				error="ERROR";
  			}
  			
  			if(!error.equals("ERROR"))
  			{								
  				ContentValues cv = new ContentValues();
  				cv.put("soconsumo", aux);
  				
  				String[] selectionArgs = { String.valueOf(idctacte) };
  				
  				int filafec=db.update("Clientes", cv, "idctacte=?",selectionArgs);		
  				Log.d("COMPROBANDO VALOR","Filas Afectadas" + filafec );
  			}
  			
  			db.close();
  			
  			return null;
  		}

  		@Override
  		protected void onPostExecute(Void result) 
  		{
  			pd.dismiss();
  				
  			if(!error.equals("ERROR"))
  			{				
  				AlertDialog.Builder alerta = new AlertDialog.Builder(VerCliente3.this);
  			    alerta.setTitle("Actualización Realizada");
  			    alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 	
  			    {			
  			    	public void onClick(DialogInterface dialog, int which) 
  			    	{	
  			    		dialog.cancel();	
  			    	}
  			    });		
  			        
  			    alerta.show();
  			}
  			else
  			{
  				AlertDialog.Builder alerta = new AlertDialog.Builder(VerCliente3.this);
  			    alerta.setTitle("Error al Actualizar");	        
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

  		@Override
  		protected void onPreExecute() 
  		{
  			pd.setTitle("Actualizando SO Consumo");
  			pd.setMessage("Espere un momento porfavor..");
  			pd.show();
  			super.onPreExecute();
  		}
  	}
  	
  	//*******************************ACTUALIZA SOCONSUMO WS*********************************
  	
}
