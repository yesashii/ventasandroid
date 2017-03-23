package dsm.ventas_desa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("SimpleDateFormat")
public class MenuCenso extends Activity 
{	
	private Intent intentCensar;
	private Intent intentEstadistica;
	private Intent intentEnvioCenso;
	private Button btnCensar;
	private Button btnEstadistica;
	private Button btnEnviodeCenso;
	private SimpleDateFormat sf;
	private Calendar calendario;
	ProgressDialog pd;
	Handler handler;
	String fecha;
	SharedPreferences sp,lastSincro;	

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
   
        setContentView(R.layout.activity_menucenso);          
       
        sf= new SimpleDateFormat("dd/MM/yyyy");
        calendario = Calendar.getInstance();
        fecha = sf.format(calendario.getTime());
        
        sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);        
        
        btnCensar = (Button)findViewById(R.id.btnCensar);
        btnEstadistica = (Button)findViewById(R.id.btnEstadistica);
        btnEnviodeCenso = (Button)findViewById(R.id.btncensospendientes);
        
        pd=new ProgressDialog(this);
        
        intentCensar = new Intent(this,Clientes_Censo.class);
        intentEstadistica = new Intent(this,Estadistica.class);
        intentEnvioCenso = new Intent(this,Envio_Censo.class);
        
        handler = new Handler();
        
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuCenso.this);
        alerta.setTitle("Sistema Desactualizado");
        
        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
				onBackPressed();
			}
		});

        alerta.setMessage("Por favor Sincronice la Aplicación");
        
        if((!fecha.equals(sp.getString("UltimaActualizacion","No DATA")))&&(!fecha.equals(sp.getString("PressedDay","No DATA"))))
        {
        	btnCensar.setEnabled(false);
        	btnEstadistica.setEnabled(false);
        	
    		alerta.show();
        	
        }
        else
        {
        	btnCensar.setEnabled(true);
        	btnEstadistica.setEnabled(true);
        }
        
        if((!fecha.equals(sp.getString("UltimaActualizacion","No DATA")))&&(fecha.equals(sp.getString("PressedDay","No DATA"))))
        {
        	alerta.show();
        	btnCensar.setEnabled(false);
        	btnEstadistica.setEnabled(false);
        }
        
        btnCensar.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				startActivity(intentCensar);
				finish();
			}
		});
        
        btnEstadistica.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				startActivity(intentEstadistica);
				finish();
			}
		});       
        
        btnEnviodeCenso.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				startActivity(intentEnvioCenso);
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
    	Intent intent=new Intent(MenuCenso.this,Principal.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
	
}
