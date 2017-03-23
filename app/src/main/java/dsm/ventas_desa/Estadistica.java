package dsm.ventas_desa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

@SuppressLint("SimpleDateFormat")
public class Estadistica extends Activity 
{
	Button atras;
	SharedPreferences sp;

	DatosAplicacion dadb, dadb2;
	SQLiteDatabase db, db2;
	Cursor c, c2;
	
	Spinner spcen;
	
	TextView txt01, txt02, txt03, txt04, txt05, txt06, txt07, txt08, txt09;
	
	String[] ncenso; 
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estadistica_censo);
		
		txt01 = (TextView)findViewById(R.id.ecTextView01);
		spcen = (Spinner)findViewById(R.id.ecspCenso);
		txt02 = (TextView)findViewById(R.id.ecTextView02);
		txt03 = (TextView)findViewById(R.id.ecTextView03);
		txt04 = (TextView)findViewById(R.id.ecTextView04);
		txt05 = (TextView)findViewById(R.id.ecTextView05);	
		txt06 = (TextView)findViewById(R.id.ecTextView06);
		txt07 = (TextView)findViewById(R.id.ecTextView07);
		txt08 = (TextView)findViewById(R.id.ecTextView08);
		txt09 = (TextView)findViewById(R.id.ecTextView09);
		
		Log.d("Estadistica: ","Carga de TextVIEW");
		
		atras = (Button)findViewById(R.id.ecbtncAtras);
	    
		dadb = new DatosAplicacion(Estadistica.this,"PrincipalDB", null, 1);
	    db = dadb.getWritableDatabase(); 
	    
		dadb2 = new DatosAplicacion(Estadistica.this,"InsertarDB", null, 1);
	    db2 = dadb2.getWritableDatabase();

	    c = db.rawQuery("SELECT distinct idactivacion, nombre FROM CENSO_DETALLE ",null);
	    c.moveToFirst();
	    
	    Log.d("Spiner: ",c.getCount()+"");
		
	    ncenso = new String[c.getCount()];  
    	
    	for(int i=0;i<ncenso.length;i++)
    	{
    		ncenso[i] = c.getInt(0)+".- "+c.getString(1);
    		c.moveToNext();
    	}
    	
    	ArrayAdapter<String> adaptador;
    	
    	adaptador = new ArrayAdapter<String>(Estadistica.this,android.R.layout.simple_spinner_item,ncenso);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
    	
    	spcen.setAdapter(adaptador);
    	
    	db.close();
    	
    	Log.d("Estadistica: ","Termino de Spinner");
    	
    	spcen.setOnItemSelectedListener(new OnItemSelectedListener() 
    	{
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) 
			{
				Log.d("Estadistica: ","Inicio Cambio de Spinner");
				
				String aux1=spcen.getSelectedItem().toString();
				
				String[] aux2 = aux1.split(".- ");
				String idcensoactivacion =aux2[0].trim().toString();
				
				db = dadb.getWritableDatabase(); 

			    c = db.rawQuery(" SELECT Count(*) FROM CENSO_CTACTE AS T0 " +
			    				" INNER JOIN CLIENTES T1 ON T0.idctacte=T1.idctacte "+
			    				" Where idactivacion='"+idcensoactivacion+"'  ",null);
			    c.moveToFirst();
			    
			    txt03.setText(String.valueOf(c.getInt(0)));
			    
			    db2 = dadb2.getWritableDatabase();
			    
			    c2 = db2.rawQuery("SELECT COUNT(*) FROM ( SELECT distinct idactivacion, ctacte FROM CENSOS Where idactivacion='"+idcensoactivacion+"' ) ",null);
			    c2.moveToFirst();
			    
			    txt05.setText(String.valueOf(c2.getInt(0)));
			    
			    c2 = db2.rawQuery("SELECT COUNT(*) FROM ( SELECT distinct idactivacion, ctacte FROM CENSOS Where idactivacion='"+idcensoactivacion+"' AND estadocenso='Y' ) ",null);
			    c2.moveToFirst();
			    
			    txt09.setText(String.valueOf(c2.getInt(0)));
			    
			    Log.d("Estadistica: ",c2.getInt(0)+"/"+c.getInt(0)+"*"+100+"%");
			    
			    txt07.setText(String.format("%.2f",((float)c2.getInt(0))/((float)c.getInt(0))*100)+"%");
			    
			    db.close();
			    db2.close();
			    
			    Log.d("Estadistica: ","Termino de Spinner");
				
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}
		});
    	
	    		
	    atras.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				onBackPressed();
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

	@Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Estadistica.this,MenuCenso.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }	
}
