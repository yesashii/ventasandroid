package dsm.ventas_desa;

import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import db.ventas_desa.DatosAplicacion;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Resumen extends Activity 
{
	TextView tvRut,tvSigla,tvRazonSocial,tvCanal,tvNombreEncargado,tvTelefono,tvEmail,tvCondiciondePago,tvMontoDeCredito,
	tvBanco,tvNumeroDeCuenta,tvTitular,tvRutTitular,tvSucursal,tvSerieCI,tvDireccion,tvRegion,tvComuna,tvCiudad,tvCobDias,tvCobSemanas, tvsSoconsumo;
	ProgressDialog pd;
	Button enviar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        
        tvCobDias=(TextView)findViewById(R.id.tvCobDias);
        tvCobSemanas=(TextView)findViewById(R.id.tvCobSemana);
        
        tvRut = (TextView)findViewById(R.id.tvres_rut);
        tvSigla = (TextView)findViewById(R.id.tvres_sigla);
        tvRazonSocial = (TextView)findViewById(R.id.tvres_Razon_Social);
        tvCanal = (TextView)findViewById(R.id.tvres_Canal);
        tvsSoconsumo = (TextView)findViewById(R.id.tvres_Soconsumo);
        tvNombreEncargado = (TextView)findViewById(R.id.tvres_Nombre_Encargado);
        tvTelefono = (TextView)findViewById(R.id.tvres_Telefono);
        tvEmail = (TextView)findViewById(R.id.tvres_Email);
        tvCondiciondePago = (TextView)findViewById(R.id.tvres_Condicion_De_Pago);
        tvMontoDeCredito = (TextView)findViewById(R.id.tvres_Monto_De_Credito);
        tvBanco = (TextView)findViewById(R.id.tvres_Banco);
        tvNumeroDeCuenta = (TextView)findViewById(R.id.tvres_Numero_De_Cuenta);
        tvTitular = (TextView)findViewById(R.id.tvres_Titular_de_Cuenta);
        tvRutTitular = (TextView)findViewById(R.id.tvres_Rut_Titular_De_Cuenta);
        tvSucursal = (TextView)findViewById(R.id.tvres_Sucursal);
        tvSerieCI = (TextView)findViewById(R.id.tvres_SerieCI);
        tvDireccion = (TextView)findViewById(R.id.tvres_Direccion);
        tvRegion = (TextView)findViewById(R.id.tvres_Region);        
        tvCiudad = (TextView)findViewById(R.id.tvres_Ciudad);
        enviar =(Button)findViewById(R.id.btnGuardar);
        
        final SharedPreferences sp = getSharedPreferences("DatosCliente",Context.MODE_PRIVATE);
       
        tvRut.setText(tvRut.getText().toString()+sp.getString("Rut", "NO DATA").toString());
        tvSigla.setText(tvSigla.getText().toString()+sp.getString("Sigla", "NO DATA").toString());
        tvRazonSocial.setText(tvRazonSocial.getText().toString()+sp.getString("Razon Social", "NO DATA").toString());
        tvCanal.setText(tvCanal.getText().toString()+sp.getString("Canal", "NO DATA").toString());
        tvsSoconsumo.setText(tvsSoconsumo.getText().toString()+sp.getString("Soconsumo", "NO DATA").toString());
        tvNombreEncargado.setText(tvNombreEncargado.getText().toString()+sp.getString("Nombre Encargado", "NO DATA").toString());
        tvTelefono.setText(tvTelefono.getText().toString()+sp.getString("Telefono", "NO DATA").toString());
        tvEmail.setText(tvEmail.getText().toString()+sp.getString("Email", "NO DATA").toString());
        tvCondiciondePago.setText(tvCondiciondePago.getText().toString()+sp.getString("Condicion de Pago", "NO DATA").toString());
        tvMontoDeCredito.setText(tvMontoDeCredito.getText().toString()+"$"+sp.getString("Monto de Credito", "NO DATA").toString());
        tvBanco.setText(tvBanco.getText().toString()+sp.getString("Banco", "").toString());
        tvNumeroDeCuenta.setText(tvNumeroDeCuenta.getText().toString()+sp.getString("Numero de Cuenta", "NO DATA").toString());
        tvTitular.setText(tvTitular.getText().toString()+sp.getString("Titular", "NO DATA").toString());
        tvRutTitular.setText(tvRutTitular.getText().toString()+sp.getString("Rut Titular", "NO DATA").toString());
        tvSucursal.setText(tvSucursal.getText().toString()+sp.getString("Sucursal", "NO DATA").toString());
        tvSerieCI.setText(tvSerieCI.getText().toString()+sp.getString("Serie CI", "NO DATA").toString());
        tvDireccion.setText(sp.getString("Calle", "")+" "+sp.getString("Nro", "")+" "+sp.getString("Oficina", "NO DATA").toString());
        tvRegion.setText(tvRegion.getText().toString()+sp.getString("Region", "NO DATA").toString());
        tvCiudad.setText(tvCiudad.getText().toString()+sp.getString("Ciudad", "NO DATA").toString());       
       
        String dias="";
       
        if(sp.getInt("LunCob",0)==1)
        {
        	dias = dias+" Lunes ";
        }
       
        if(sp.getInt("MarCob",0)==1)
        {
        	dias = dias+" Martes ";;
        }
       
        if(sp.getInt("MieCob",0)==1)
        {
        	dias = dias+" Miercoles ";;
        }
       
        if(sp.getInt("JueCob",0)==1)
        {
        	dias = dias+" Jueves ";;
        }
        
        if(sp.getInt("VieCob",0)==1)
        {
        	dias = dias+" Viernes ";;
        }
        
        if(sp.getInt("sabCob",0)==1)
        {
        	dias = dias+" Sabado ";;
        }
        
        String semanas="";
        
        if(sp.getInt("Semana1Cob",0)==1)
        {
        	semanas = semanas+" Semana 1 ";
        }
        
        if(sp.getInt("Semana2Cob",0)==1)
        {
        	semanas = semanas+" Semana 2 ";
        }
        
        if(sp.getInt("Semana3Cob",0)==1)
        {
        	semanas = semanas+" Semana 3 ";
        }
       
        if(sp.getInt("Semana4Cob",0)==1)
        {
        	semanas = semanas+" Semana 4 ";
        }       
       
        tvCobDias.setText(tvCobDias.getText().toString()+dias);
        tvCobSemanas.setText(tvCobSemanas.getText().toString()+semanas);
       
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
        getMenuInflater().inflate(R.menu.activity_resumen, menu);
        return true;
    }
    
    public class OnBack extends AsyncTask<Void, Void,Void>
    {
    	Boolean valido;
    	SharedPreferences sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
    	int idvendedor = sp.getInt("idvendedor", -1);
    	int idempresa=sp.getInt("idempresa",-1);
    	String nombreEmpresa=sp.getString("nombreEmpresa","NO DATA");
    	String nombreVendedor=sp.getString("nombreVendedor","NO DATA");
    	   	
		@Override
		protected Void doInBackground(Void... params) 
		{
			sp=getSharedPreferences("DatosCliente",Context.MODE_PRIVATE);
			String direccion = sp.getString("Calle", "NO DATA").toString()+" "+sp.getString("Nro", "NO DATA").toString()+" "+sp.getString("Oficina", "NO DATA").toString();
			
			if(direccion.length()>100)
			{
				direccion=direccion.substring(0,99);
			}
			
			AccesoWebService acc = new AccesoWebService();
			valido=true;
			
			try 
			{
				valido=acc.insertarCliente(nombreEmpresa,sp.getString("Rut", "NO DATA").toString(),sp.getString("Razon Social", "NO DATA").toString(),
										sp.getString("Sigla", "NO DATA").toString(), sp.getString("Canal", "NO DATA").toString(),nombreVendedor,
										sp.getString("Condicion de Pago", "NO DATA").toString(),sp.getString("Region", "NO DATA").toString(),
										sp.getString("Ciudad", "NO DATA").toString(),sp.getString("Telefono", "NO DATA").toString(),sp.getString("Email", "NO DATA").toString(),
										sp.getString("Nombre Encargado", "NO DATA").toString(),direccion,sp.getString("Monto de Credito", "NO DATA").toString(),
										sp.getString("Banco", "NO DATA").toString(),""+idempresa,""+idvendedor, ""+sp.getInt("LunCob",0),""+sp.getInt("MarCob",0),
										""+sp.getInt("MieCob",0), ""+sp.getInt("JueCob",0), ""+sp.getInt("VieCob",0),""+sp.getInt("SabCob",0),
										""+sp.getInt("Semana1Cob",0),""+sp.getInt("Semana2Cob",0),""+sp.getInt("Semana3Cob",0),""+sp.getInt("Semana4Cob",0),
										sp.getString("Soconsumo", "NO DATA").toString());
			} 
			catch (Exception e) 
			{
				valido=false;
				e.printStackTrace();
			} 	
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			final AlertDialog.Builder alerta = new AlertDialog.Builder(Resumen.this);
	        
			alerta.setTitle("Cliente");
	        alerta.setMessage("Cliente Ingresado Correctamente");
	        
	        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        {			
				public void onClick(DialogInterface dialog, int which) 
				{				
					dialog.cancel();
					Intent intent = new Intent(Resumen.this,Nuevo_Cliente.class);
					startActivity(intent);
					finish();
				}
			});
	        
	        if(valido)
	        {
	        	alerta.show();
	        }
	        else
	        {
	        	alerta.setTitle("Error");
	        	alerta.setMessage("Error en la creación de Cliente");
	        	
	        	alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	        	{			
					public void onClick(DialogInterface dialog, int which) 
					{				
						dialog.cancel();
					}
				});
	        	
	        	alerta.show();
	        }
	        
			pd.dismiss();
			
			if(valido)
			{
				
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pd = new ProgressDialog(Resumen.this);
			pd.setTitle("Enviando");
			pd.setMessage("Enviando nuevo cliente");
			pd.show();
			super.onPreExecute();
		}
    }
    
    @Override
    public void onBackPressed()
    {    	
    	finish();
    	super.onBackPressed();
    }
    
}
