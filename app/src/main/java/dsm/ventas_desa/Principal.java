package dsm.ventas_desa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import db.ventas_desa.DatosInsercion;

@SuppressLint("SimpleDateFormat")
public class Principal extends Activity 
{	
	private Intent intentLogin;
	private Intent intentSincro;
	private Intent intentCliente;
	private Intent intentVenta;
	private Intent intentCenso;
	private Intent intentPrincipal;
	private Button btnDesconectar;
	private Button btnSincronizar;
	private Button btnPedidos;	
	private Button btnClientes;
	private Button btnVentas;
	private Button btnCenso;
	private SimpleDateFormat sf;
	private Calendar calendario;
	AccesoWebService access;
	SoapObject resultado;
	SoapPrimitive resultado2;
	Toast toast;
	Toast toast2;
	ProgressDialog pd;
	Handler handler;
	String fecha;
	SharedPreferences sp,lastSincro;	

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
   
        setContentView(R.layout.activity_principal);        
        access = new AccesoWebService();       
       
        sf= new SimpleDateFormat("dd/MM/yyyy");
        calendario = Calendar.getInstance();
        fecha = sf.format(calendario.getTime());
        
        sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);        
        
        btnVentas = (Button)findViewById(R.id.btnVenta);
        btnCenso = (Button)findViewById(R.id.btnCenso);
        btnDesconectar = (Button)findViewById(R.id.btnDesconectar);
        btnSincronizar = (Button)findViewById(R.id.btnSincronizar);       
        btnClientes = (Button)findViewById(R.id.btnClientes); 
        btnPedidos = (Button)findViewById(R.id.btnPedidos);   
        
        pd=new ProgressDialog(this);
        intentLogin = new Intent(this,Login.class);
        intentSincro= new Intent(this,Sincronizacion.class); 
        intentCliente= new Intent(this,Clientes.class);
        intentVenta = new Intent(this,Venta.class);
        intentCenso = new Intent(this,MenuCenso.class);
        intentPrincipal = new Intent(this,Principal.class);
        handler = new Handler();
        
        final AlertDialog.Builder mantencion = new AlertDialog.Builder(Principal.this);
        mantencion.setTitle("Mantención");
        mantencion.setMessage("Mantención Finalizada");
        
        mantencion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which)
			{	
				dialog.cancel();
				startActivity(intentPrincipal);
			}
		});
        
        final AlertDialog.Builder alerta = new AlertDialog.Builder(Principal.this);
        alerta.setTitle("Sistema Desactualizado");
        
        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});

        alerta.setMessage("Por favor Sincronice la Aplicación");
        
        if((!fecha.equals(sp.getString("UltimaActualizacion","No DATA")))&&(!fecha.equals(sp.getString("PressedDay","No DATA"))))
        {
        	btnClientes.setEnabled(false);
        	btnVentas.setEnabled(false);
        	btnCenso.setEnabled(false);
        	lastSincro = getSharedPreferences("DatosSincronizacion",Context.MODE_PRIVATE);
        	SharedPreferences.Editor sincroEditor = lastSincro.edit();
        	
        	if(sp.getBoolean("FTIME",false))
        	{       	
				sincroEditor.putBoolean("Clientes", false);
				sincroEditor.putBoolean("Productos", false);
				sincroEditor.putBoolean("Canales", true);
				sincroEditor.putBoolean("Comunas", true);
				sincroEditor.putBoolean("Bancos", true);
				sincroEditor.putBoolean("Regiones", true);
				sincroEditor.putBoolean("Ciudades",true);
				sincroEditor.putBoolean("CdePago", true);
				sincroEditor.putBoolean("Rutas", false);
				sincroEditor.putBoolean("Familias",true);
				sincroEditor.putBoolean("SubFamilias",true);
				sincroEditor.putBoolean("Marcas",false);
				sincroEditor.putBoolean("ListadePrecio",false);
				sincroEditor.putBoolean("CLIPROMDESC",false);
				sincroEditor.putBoolean("SUCPROMDESC",false);
				sincroEditor.putBoolean("PAGPROMDESC",false);
				sincroEditor.putBoolean("GRUPROMDESC",false);
				sincroEditor.putBoolean("CANPROMDESC",false);
				sincroEditor.putBoolean("Flete",false);
				sincroEditor.putBoolean("ESCDESC",false);
				sincroEditor.putBoolean("GVTPROMDESC",false);
				sincroEditor.putBoolean("MGVTPROMDESC",false);
				sincroEditor.putBoolean("MEGPROMDESC",false);
				sincroEditor.putBoolean("VENPROMDESC",false);
				sincroEditor.putBoolean("MAXPAGDESC",false);
				sincroEditor.putBoolean("MAXSKUDESC",false);
				sincroEditor.putBoolean("MAXMCANDESC",false);
				sincroEditor.putBoolean("CliDet",false);
				sincroEditor.putBoolean("Bodega",false);
				sincroEditor.putBoolean("Bodega",false);
				sincroEditor.putBoolean("Fletespag", false);
				sincroEditor.putBoolean("SKUCanal", false);
				sincroEditor.putBoolean("SOCONSUMO", false);
				sincroEditor.putBoolean("CENSO_CALIBRE", false);
				sincroEditor.putBoolean("CENSO_MARCA", false);
				sincroEditor.putBoolean("CENSO_ITEM", false);
				sincroEditor.putBoolean("CENSO_MOTIVO", false);
				sincroEditor.putBoolean("CENSO_VALIDACION", false);
				sincroEditor.putBoolean("CENSO_CTACTE", false);
				sincroEditor.putBoolean("CENSO_DETALLE", false);
				sincroEditor.putBoolean("CENSO_MOT_REL", false);
				sincroEditor.putBoolean("CENSO_VAL_REL", false);
				sincroEditor.commit(); 
			
				alerta.show();
        	}
        	else
        	{
        		sincroEditor.putBoolean("Clientes", false);
    			sincroEditor.putBoolean("Productos", false);
    			sincroEditor.putBoolean("Canales", false);
    			sincroEditor.putBoolean("Comunas", false);
    			sincroEditor.putBoolean("Bancos", false);
    			sincroEditor.putBoolean("Regiones", false);
    			sincroEditor.putBoolean("Ciudades", false);
    			sincroEditor.putBoolean("CdePago", false);
    			sincroEditor.putBoolean("Rutas", false);
    			sincroEditor.putBoolean("Familias",false);
    			sincroEditor.putBoolean("SubFamilias",false);
    			sincroEditor.putBoolean("Marcas",false);
    			sincroEditor.putBoolean("ListadePrecio",false);
    			sincroEditor.putBoolean("CLIPROMDESC",false);
    			sincroEditor.putBoolean("SUCPROMDESC",false);
    			sincroEditor.putBoolean("PAGPROMDESC",false);
    			sincroEditor.putBoolean("GRUPROMDESC",false);
    			sincroEditor.putBoolean("CANPROMDESC",false);
    			sincroEditor.putBoolean("Flete",false);
    			sincroEditor.putBoolean("ESCDESC",false);
    			sincroEditor.putBoolean("GVTPROMDESC",false);
    			sincroEditor.putBoolean("MGVTPROMDESC",false);
    			sincroEditor.putBoolean("MEGPROMDESC",false);
    			sincroEditor.putBoolean("VENPROMDESC",false);
    			sincroEditor.putBoolean("MAXPAGDESC",false);
    			sincroEditor.putBoolean("MAXSKUDESC",false);
    			sincroEditor.putBoolean("MAXMCANDESC",false);
    			sincroEditor.putBoolean("CliDet",false);
    			sincroEditor.putBoolean("Bodega",false);
    			sincroEditor.putBoolean("Fletespag", false);
				sincroEditor.putBoolean("CENSO_CALIBRE", false);
				sincroEditor.putBoolean("CENSO_MARCA", false);
				sincroEditor.putBoolean("CENSO_ITEM", false);
				sincroEditor.putBoolean("CENSO_MOTIVO", false);
				sincroEditor.putBoolean("CENSO_VALIDACION", false);
				sincroEditor.putBoolean("CENSO_CTACTE", false);
				sincroEditor.putBoolean("CENSO_DETALLE", false);
				sincroEditor.putBoolean("CENSO_MOT_REL", false);
				sincroEditor.putBoolean("CENSO_VAL_REL", false);
    			sincroEditor.commit(); 
    			
    			alerta.show();
        	}
        }
        else
        {
        	btnClientes.setEnabled(true);
        	btnVentas.setEnabled(true);
        	btnCenso.setEnabled(true);
        }
        
        if((!fecha.equals(sp.getString("UltimaActualizacion","No DATA")))&&(fecha.equals(sp.getString("PressedDay","No DATA"))))
        {
        	alerta.show();
        	btnClientes.setEnabled(false);
            btnVentas.setEnabled(false);
            btnCenso.setEnabled(false);
        }
        
        btnClientes.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				startActivity(intentCliente);
				finish();
			}
		});
        
        btnDesconectar.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				SharedPreferences datosSistema = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = datosSistema.edit();
				editor.putBoolean("sesion", false);
				editor.putInt("idvendedor", -1);
				editor.commit();				
				startActivity(intentLogin);
				finish();
			}
		});
        
        btnSincronizar.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				final AlertDialog.Builder pend = new AlertDialog.Builder(Principal.this);
		        pend.setTitle("Pedidos Pendientes");
		        pend.setMessage("Usted tiene Pedidos pendientes porfavor vaya a la sección Pedidos para enviarlos o cancelarlos");
		        
		        pend.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
		        {
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
				});
				
				SQLiteDatabase data;				
				DatosInsercion dadi = new DatosInsercion(Principal.this, "InsertarDB", null,1);
				data=dadi.getWritableDatabase();
				
				Cursor c=data.rawQuery("SELECT id FROM Pedidos WHERE estadopedido='NO'", null);				
				
				if(c.getCount()>0)
				{
					pend.show();
				}
				else
				{					
					startActivity(intentSincro);
					finish();
				}
				
				data.close();
			}
		});
        
        btnVentas.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				startActivity(intentVenta);
				finish();
			}
		});
        
        btnCenso.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				startActivity(intentCenso);
				finish();
			}
		});
        
        btnPedidos.setOnClickListener(new OnClickListener() 
        {	
			public void onClick(View v) 
			{
				Intent intent = new Intent(Principal.this,Pedidos.class);
				startActivity(intent);
				finish();
			}
		});        
	} 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        return true;
    }

	@Override
	public void onBackPressed() 
	{
		final AlertDialog.Builder salir = new AlertDialog.Builder(Principal.this);
        salir.setTitle("¿Salir?");
        salir.setMessage("¿Esta seguro que desea salir?");
        
        salir.setNegativeButton("Salir", new DialogInterface.OnClickListener() 
        {			
			public void onClick(DialogInterface dialog, int which) 
			{
				finish();
				dialog.cancel();
				
			}
		});		
        
        salir.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
        
        salir.show();
	}
	
}
