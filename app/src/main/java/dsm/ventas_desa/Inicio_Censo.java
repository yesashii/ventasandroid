package dsm.ventas_desa;

import java.text.DecimalFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import db.ventas_desa.DatosAplicacion;
import dsm.ventas_desa.VerCliente2.OnBack;

public class Inicio_Censo extends Activity 
{
	String cliente;
	String clienteFinal;
	DatosAplicacion dadb, dadb2;
	SQLiteDatabase db,db2;
	TextView tvsICRut,tvsICRazonSocial,tvsICSigla, tvsICCensonombre, tvsICDescripcion, tvsICFechafin, tvsICComentario;
	String rut ;
	String nombre ;
	String sigla;
	ProgressDialog pd;
	DecimalFormat formato;
	private Button inicio;
	String error;
	Intent censo;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciocenso);
        
        tvsICRut = (TextView)findViewById(R.id.tvsICRut);
        tvsICRazonSocial = (TextView)findViewById(R.id.tvsICRazonSocial);
        tvsICSigla = (TextView)findViewById(R.id.tvsICSigla);
        tvsICCensonombre = (TextView)findViewById(R.id.tvsICCensonombre);
        tvsICFechafin = (TextView)findViewById(R.id.tvsICFechafin);  
        tvsICComentario= (TextView)findViewById(R.id.tvsICComentario);  

        inicio=(Button)findViewById(R.id.IniciarCenso);
        
        Log.d("Carga de Interfaz: ","OK");
        
        //***************CARGAR INFORMACION EN LOS TVS**************
    	SharedPreferences sp;
        sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);
        String idctacte=sp.getString("idctacte","SIN DATOS");
        String nombrecenso=sp.getString("nombrecenso","SIN DATOS");
        String idactivacion=sp.getString("idactivacion","0");
        
        dadb = new DatosAplicacion(Inicio_Censo.this,"PrincipalDB", null, 1);
        db = dadb.getWritableDatabase(); 

    	Cursor c = db.rawQuery("SELECT nombre, sigla, idcanal  FROM Clientes Where idctacte='"+idctacte+"'",null);
    	c.moveToFirst();   	

        tvsICRut.setText(idctacte);
        tvsICRazonSocial.setText(c.getString(0));
        tvsICSigla.setText(c.getString(1));
        tvsICCensonombre.setText(nombrecenso);

        c = db.rawQuery("SELECT distinct hasta, comentario FROM CENSO_CTACTE Where idctacte='"+idctacte+"' and idactivacion='"+idactivacion+"'",null);
        
        c.moveToFirst();   
        
        String fin=c.getString(0);
        
        String fechafin=fin.substring(6,8)+"/"+fin.substring(4,6)+"/"+fin.substring(0,4);
        
        tvsICFechafin.setText(fechafin);
        tvsICComentario.setText(c.getString(1));

        db.close();
        dadb.close();
        
        
        //***********************************************************

        inicio.setOnClickListener(new OnClickListener() 
        {
 			public void onClick(View v) 
 			{
 				censo=new Intent(Inicio_Censo.this,Censo.class);
				startActivity(censo);
				finish();		
 			}
 		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_iniciocenso, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Inicio_Censo.this,Listado_Censo.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
}

