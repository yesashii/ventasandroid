package dsm.ventas_desa;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.array;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;

public class Censo extends Activity 
{
	DatosAplicacion dadb, dadb1;
	DatosInsercion dadb2, dadb21;
	SQLiteDatabase db,db1, db2, db21;
	Cursor c, c1,c2, c21;
	
	AlertDialog.Builder alerta;
	
	String[] nombrescalibres, nombresmotivo, nombresitems;
	
	TextView tvVCRut;
	Spinner spcalibres, spindicador, spmotivo;
	Button finalizar, guardar;

	TableLayout tl0, tl; 
	TableRow.LayoutParams layoutFila;
	
	boolean cmp;
	
	String answ="", mensaje="";
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censo);

        spcalibres = (Spinner)findViewById(R.id.spCalibre);
        spindicador = (Spinner)findViewById(R.id.spIndicador); 
        spmotivo=(Spinner)findViewById(R.id.spMotivonoCenso);
        
        tl0 = (TableLayout)findViewById(R.id.tableLayout1);
        tl = (TableLayout)findViewById(R.id.tableLayout2);
        
        guardar=(Button)findViewById(R.id.btncGuardar);
        finalizar=(Button)findViewById(R.id.btncFinalizar);
        
        layoutFila = new TableRow.LayoutParams(300,TableRow.LayoutParams.MATCH_PARENT);
        
	    dadb = new DatosAplicacion(Censo.this,"PrincipalDB", null, 1);
	    dadb1 = new DatosAplicacion(Censo.this,"PrincipalDB", null, 1);
	    dadb2 = new DatosInsercion(Censo.this,"InsertarDB", null, 1);
	    dadb21 = new DatosInsercion(Censo.this,"InsertarDB", null, 1);
        
	    alerta = new AlertDialog.Builder(Censo.this);
	    alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
	    {
		public void onClick(DialogInterface dialog, int which) 
		{
			dialog.cancel();
		}
		});

        SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
        String idactactivaicon =sp.getString("idactivacion","NO DATA");
        String idctacte =sp.getString("idctacte","NO DATA");

        db = dadb.getWritableDatabase(); 
        
        //*********************CARGA DE SPINNER MOTIVO NO CENSO***************
    	c = db.rawQuery(" SELECT DISTINCT T2.motivo " +
							   " FROM CENSO_DETALLE AS T0 " + 
							   " INNER JOIN CENSO_MOT_REL AS T1 ON T0.idcensogeneral=T1.idcensogeneral " +
							   " INNER JOIN CENSO_MOTIVO AS T2 ON T1.idmotivo=T2.idmotivo " +
							   " WHERE T0.idactivacion='"+ idactactivaicon +"'",null);
    	c.moveToFirst();   	
    	
    	nombresmotivo = new String[c.getCount()+1];
    	
    	nombresmotivo[0] ="SIN MOTIVO";
    	
    	for(int i=1;i<nombresmotivo.length;i++)
    	{
    		nombresmotivo[i] = c.getString(0);
    		c.moveToNext();
    	}
    	
    	ArrayAdapter<String> adaptador;
    	
    	adaptador = new ArrayAdapter<String>(Censo.this,android.R.layout.simple_spinner_item,nombresmotivo);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
    	
    	spmotivo.setAdapter(adaptador);
    	spmotivo.setSelection(0);
        
		//********************************************************************                 
		//**********************CARGA DE SPINNER CALIBRE**********************
       
    	c = db.rawQuery("SELECT DISTINCT T1.CALIBRE " +
    			   " FROM CENSO_DETALLE AS T0 INNER JOIN CENSO_CALIBRE AS T1 " + 
    			   " ON T0.calibre=T1.idcalibre WHERE T0.idactivacion='"+ idactactivaicon +"'",null);
    	c.moveToFirst();   	
    	
    	nombrescalibres = new String[c.getCount()];
    	
    	for(int i=0;i<nombrescalibres.length;i++)
    	{
    		nombrescalibres[i] = c.getString(0);
    		c.moveToNext();
    	}
    	
    	ArrayAdapter<String> adaptador2;
    	
    	adaptador2 = new ArrayAdapter<String>(Censo.this,android.R.layout.simple_spinner_item,nombrescalibres);
    	adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
    	
    	spcalibres.setAdapter(adaptador2);
    	
		//********************************************************************                 
		//**********************CARGA DE SPINNER MOTIVO***********************
    	
    	String calibreselected = spcalibres.getSelectedItem().toString();
    	
    	c = db.rawQuery(" SELECT DISTINCT T2.ITEM, T2.tipodato " +
    					" FROM CENSO_DETALLE AS T0 " +
						" INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
						" INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " +
						" WHERE T0.idactivacion='"+idactactivaicon+"' and T1.calibre='"+calibreselected+"' ",null);
	 	c.moveToFirst();   	
	 	
	 	nombresitems = new String[c.getCount()];
	 	
	 	for(int i=0;i<nombresitems.length;i++)
	 	{
	 		nombresitems[i] = c.getString(0);
	 		c.moveToNext();
	 	}
	 	
	 	ArrayAdapter<String> adaptador3;
	 	
	 	adaptador3 = new ArrayAdapter<String>(Censo.this,android.R.layout.simple_spinner_item,nombresitems);
	 	adaptador3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
	 	
	 	spindicador.setAdapter(adaptador3);
	 	
	 	db.close();
	 	
	 	//**************************TERMINO DE SPINNER**************************

        spcalibres.setOnItemSelectedListener(new OnItemSelectedListener() 
    	{
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) 
			{
				SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
			    String idactactivaicon =sp.getString("idactivacion","NO DATA");   
			    
			    db = dadb.getWritableDatabase(); 
			        
			    //**************************CARGA DE SPINNER INDICADOR**************************
		    	
		    	String calibreselected = spcalibres.getSelectedItem().toString();
		    	
			    c = db.rawQuery(" SELECT DISTINCT T2.ITEM, T2.tipodato " +
			    					   " FROM CENSO_DETALLE AS T0 " +
			    					   " INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
			    					   " INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " +
			    					   " WHERE T0.idactivacion='"+idactactivaicon+"' and T1.calibre='"+calibreselected+"' ",null);
			 	c.moveToFirst();   	
			 	
			 	String tipodatos=c.getString(1);
			 	
			 	nombresitems = new String[c.getCount()];
			 	
			 	for(int i=0;i<nombresitems.length;i++)
			 	{
			 		nombresitems[i] = c.getString(0);
			 		c.moveToNext();
			 	}
			 	
			 	db.close();
			 	
			 	ArrayAdapter<String> adaptador2;
			 	
			 	adaptador2 = new ArrayAdapter<String>(Censo.this,android.R.layout.simple_spinner_item,nombresitems);
			 	adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
			 	
			 	spindicador.setAdapter(adaptador2);
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}
		});
        
        spindicador.setOnItemSelectedListener(new OnItemSelectedListener() 
    	{
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) 
			{
				SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
			    String idactactivaicon =sp.getString("idactivacion","NO DATA");
			    String ctacte =sp.getString("idctacte","NO DATA"); 

			    db = dadb.getWritableDatabase(); 
			    db2 = dadb2.getWritableDatabase(); 

			    //**************************CARGA DE MARCAS**************************
			    String calibreselected = spcalibres.getSelectedItem().toString();
			 	String itemselected = spindicador.getSelectedItem().toString();
		    	
			 	c = db.rawQuery(" SELECT DISTINCT T3.marca, T2.tipodato, T0.calibre, T0.item, T0.marca " +
		    					" FROM CENSO_DETALLE AS T0 " + 
								" INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
								" INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " +
								" INNER JOIN CENSO_MARCA AS T3 ON T0.marca=T3.idmarca " +
								" WHERE T0.idactivacion='" + idactactivaicon + "' " +
								" AND T1.Calibre='" + calibreselected + "' AND T2.Item='" + itemselected + "'" ,null);
			 	c.moveToFirst();   	
			 	
			 	Log.d("CONSULTA MARCAS"," SELECT DISTINCT T3.marca, T2.tipodato, T0.calibre, T0.item, T0.marca " +
    					" FROM CENSO_DETALLE AS T0 " + 
						" INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
						" INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " +
						" INNER JOIN CENSO_MARCA AS T3 ON T0.marca=T3.idmarca " +
						" WHERE T0.idactivacion='" + idactactivaicon + "' " +
						" AND T1.Calibre='" + calibreselected + "' AND T2.Item='" + itemselected + "'" );
			 	
			 	String tipodatos=c.getString(1);

		 		TableLayout tl = (TableLayout)findViewById(R.id.tableLayout2);
			 	
			 	tl.removeAllViewsInLayout();

		 		TableRow row2 = new TableRow(Censo.this);
		 		
		 		TextView tv00 = new TextView(Censo.this);
	 	        tv00.setText("Marca");
	 	        tv00.setTextSize(16);
	 	        tv00.setTypeface(null, Typeface.BOLD);
	 	        tv00.setLayoutParams(layoutFila);
	 	        row2.addView(tv00);
	 	        
	 	 		TextView tv11 = new TextView(Censo.this);
	 	        tv11.setText("Respuesta");
	 	        tv11.setTextSize(16);
	 	        tv11.setTypeface(null, Typeface.BOLD);
	 	        tv11.setLayoutParams(layoutFila);
	 	        row2.addView(tv11);
	 	        
	 	        tl.addView(row2);
		 	
		        
	 	        for(int i=0;i<c.getCount();i++)
	 	        {
	 	        	c2 = db2.rawQuery(" SELECT Respuesta, idmotivo " +
	    					" FROM CENSOS " +
							" WHERE idactivacion='" + idactactivaicon + "' AND ctacte='"+ctacte+"' " +
							" AND calibre='" + c.getInt(2) + "' AND item='" + c.getInt(3) + "' " +
							" AND marca="+c.getInt(4)+"" ,null);
	 	        	c2.moveToFirst();  
	 	        	
	 	        	TableRow row = new TableRow(Censo.this);
	 	        	TextView tv0 = new TextView(Censo.this);
	 	
			        tv0.setText(c.getString(0));
			        tv0.setLayoutParams(layoutFila);
			        row.addView(tv0);
			        
			 		if(tipodatos.equals("N"))
			 		{
			 			EditText tv1 = new EditText(Censo.this);
			 			tv1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
			 			
		 	        	if(c2.getCount()>0)
		 	        	{
		 	        		tv1.setText(c2.getString(0));
		 	        	}
		 	        	else
		 	        	{
		 	        		 tv1.setText("0");
		 	        	}
		 	        	
				        tv1.setLayoutParams(layoutFila);
				        row.addView(tv1);
			 		}
			 		else
			 		{
			 			CheckBox tv1 = new CheckBox(Censo.this);
			 			
		 	        	if(c2.getCount()>0)
		 	        	{
		 	        		tv1.setChecked(Boolean.valueOf(c2.getString(0)));
		 	        	}
		 	        	else
		 	        	{
		 	        		 tv1.setChecked(false);
		 	        	}
		 	        	
			 			tv1.setLayoutParams(layoutFila);
				        row.addView(tv1);
			 		}
			        
			 		tl.addView(row);
			 		c.moveToNext();
			 	}
	 	        
 	        	spmotivo.setSelection(0);
 	        	
	 	        if(c2.getCount()>0)
	 	        {
	 	        	Log.d("SELECCION: ","c2.getCount()>0");
	 	        	
	 	        	if(c2.getInt(1)>0)
	 	        	{
	 	        		Log.d("SELECCION: ","c2.getInt(1)>0");
		 	        	c = db.rawQuery(" SELECT motivo " +
		    					" FROM CENSO_MOTIVO AS T0 " +
								" WHERE idmotivo=" + c2.getInt(1) ,null);
		 	        	c.moveToFirst();
	
		 	        	Log.d("Spiner motivo: "," SELECT motivo " +
		    					" FROM CENSO_MOTIVO AS T0 " +
								" WHERE idmotivo=" + c2.getInt(1));
		 	        	
		 	        	ArrayAdapter myAdap = (ArrayAdapter) spmotivo.getAdapter(); //cast to an ArrayAdapter
	
		 	        	int spinnerPosition = myAdap.getPosition(c.getString(0));
	
		 	        	Log.d("Spiner motivo: ",""+spinnerPosition);

		 	        	spmotivo.setSelection(spinnerPosition);
	 	        	}
	 	        }
	 	        	
		        db.close();	 
		        db2.close();	 
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}
		});
        
        guardar.setOnClickListener(new OnClickListener() 
        {
 			public void onClick(View v) 
 			{
 	 		    db = dadb.getWritableDatabase(); 
	 	 	 	db2 = dadb2.getWritableDatabase(); 
	 	 	 	boolean aux=false;
 		        
 				SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
 		        String idactactivaicon =sp.getString("idactivacion","NO DATA"); 
 		        String ctacte=sp.getString("idctacte","NO DATA"); 
 				String calibreselected = spcalibres.getSelectedItem().toString();
 				String itemselected = spindicador.getSelectedItem().toString();	
 				String motivo=spmotivo.getSelectedItem().toString();

 				Cursor c = db.rawQuery(" SELECT DISTINCT T0.idcalibre " +
						   " FROM CENSO_CALIBRE AS T0 " + 
						   " WHERE T0.calibre='"+ calibreselected +"'",null);
 				
 				c.moveToFirst();   	
	
 				int idcalibreselected=c.getInt(0);

 				c = db.rawQuery(" SELECT DISTINCT T0.iditem " +
						   " FROM CENSO_ITEM AS T0 " + 
						   " WHERE T0.item='"+ itemselected +"'",null);
				
				c.moveToFirst();   	

				int iditemselected=c.getInt(0);
				
			 	c = db.rawQuery(" SELECT DISTINCT T3.marca, T2.tipodato " +
    					" FROM CENSO_DETALLE AS T0 " + 
						" INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
						" INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " +
						" INNER JOIN CENSO_MARCA AS T3 ON T0.marca=T3.idmarca " +
						" WHERE T0.idactivacion='" + idactactivaicon + "' " +
						" AND T1.Calibre='" + calibreselected + "' AND T2.Item='" + itemselected + "'" ,null);
			 	c.moveToFirst();   	
	 	
	 			String tipodatos=c.getString(1);

 				if(motivo.equals("SIN MOTIVO"))
 				{
 					if(tipodatos.equals("N"))
	 		        {
 						aux=valida_datos();
 						
 						for (int i=1;i<tl.getChildCount();i++) 
 	 	 		        {
 	 	 		        	TableRow row = (TableRow) tl.getChildAt(i);
 	 	 		        	TextView marca=(TextView) row.getChildAt(0);
 	 	 		        	EditText respuesta=(EditText) row.getChildAt(1);
 	 	 		        		
 	 	 	 		        if(respuesta.getText().toString().matches(""))
 	 	 	 		        {
 	 	 	 		        	answ="0";
 	 	 	 		        }
 	 	 	 		        else
 	 	 	 		        {
 	 	 	 		        	answ=respuesta.getText().toString();
 	 	 	 		        }
 	 	 	 		        
 	 	 		        	c = db.rawQuery(" SELECT DISTINCT T0.idmarca " +
 	 							   " FROM CENSO_MARCA AS T0 " + 
 	 							   " WHERE T0.marca='"+ marca.getText() +"'",null);
 	 	 				
 	 	 		        	c.moveToFirst();  
 	 	 				
 	 	 		        	Log.d("TEXTO EN...",""+marca.getText()+"-"+answ);

 	 						db2.execSQL(" DELETE FROM CENSOS WHERE idactivacion='"+idactactivaicon+"' AND ctacte='"+ctacte+"' AND Calibre="+idcalibreselected+
 							   " AND Item in ("+iditemselected+") AND Marca="+c.getInt(0)+" and estadocenso IN ('N','P') ");
 				
 							db2.execSQL("INSERT INTO CENSOS (idactivacion, ctacte, calibre, item, marca, respuesta, idmotivo, estadocenso) "
 									+ "VALUES (" + idactactivaicon + ",'" + ctacte + "'," + idcalibreselected + ","	+ iditemselected + ","
 									+ c.getInt(0) + ",'" + answ	+ "',0,'N'"	+ ");");
 	 	 		        }
 					}
 					else
 					{
 						if(tipodatos.equals("B"))
 		 		        {
	 						for (int i=1;i<tl.getChildCount();i++) 
	 	 	 		        {
	 	 	 		        	TableRow row = (TableRow) tl.getChildAt(i);
	 	 	 		        	TextView marca=(TextView) row.getChildAt(0);
	 	 	 		        	CheckBox respuesta=(CheckBox) row.getChildAt(1);
	 	 		        		
	 	 	 		        	if(respuesta.isChecked())
	 	 	 		        	{
	 	 	 		        		answ="true";
	 	 	 		        	}
	 	 	 		        	else
	 	 	 		        	{
	 	 	 		        		answ="false";
	 	 	 		        	}
	 	 	 	 		        
	 	 	 		        	c = db.rawQuery(" SELECT DISTINCT T0.idmarca " +
	 	 							   " FROM CENSO_MARCA AS T0 " + 
	 	 							   " WHERE T0.marca='"+ marca.getText() +"'",null);
	 	 	 				
	 	 	 		        	c.moveToFirst();  
	 	 	 				
	 	 	 		        	Log.d("TEXTO EN...",""+marca.getText()+"-"+answ);
	
	 	 						db2.execSQL(" DELETE FROM CENSOS WHERE idactivacion='"+idactactivaicon+"' AND ctacte='"+ctacte+"' AND Calibre="+idcalibreselected+
	 							   " AND Item in ("+iditemselected+") AND Marca="+c.getInt(0)+" and estadocenso IN ('N','P')");
	 				
	 							db2.execSQL("INSERT INTO CENSOS (idactivacion, ctacte, calibre, item, marca, respuesta, idmotivo, estadocenso) "
	 									+ "VALUES (" + idactactivaicon + ",'" + ctacte + "'," + idcalibreselected + ","	+ iditemselected + ","
	 									+ c.getInt(0) + ",'" + answ	+ "',0,'N'"	+ ");");
	 	 	 		        }
	 					}
 					}
 				}
 				else
 				{	 	 		
 	 	 	 		c = db.rawQuery(" SELECT DISTINCT T0.idmotivo " +
						   " FROM CENSO_MOTIVO AS T0 " + 
						   " WHERE T0.motivo='"+ motivo +"'",null);
				
 	 	 	 		c.moveToFirst();   	

					int idmotivo=c.getInt(0);
					
					Log.d("1.- motivo: ",motivo+".-"+c.getInt(0));

 	 		        for (int i=1;i<tl.getChildCount();i++) 
 	 		        {
 	 		        	TableRow row = (TableRow) tl.getChildAt(i);
 	 		        	TextView marca=(TextView) row.getChildAt(0);

 	 		        	c = db.rawQuery(" SELECT DISTINCT T0.idmarca " +
 							   " FROM CENSO_MARCA AS T0 " + 
 							   " WHERE T0.marca='"+ marca.getText() +"'",null);
 	 				
 	 		        	c.moveToFirst();  
 	 		        	
 						db2.execSQL(" DELETE FROM CENSOS WHERE idactivacion='"+idactactivaicon+"' AND ctacte='"+ctacte+"' AND Calibre="+idcalibreselected+
						   " AND Item in ("+iditemselected+") AND Marca="+c.getInt(0)+" and estadocenso IN ('N','P') ");
 						
 						Log.d("1a.- motivo: "," DELETE FROM CENSOS WHERE idactivacion='"+idactactivaicon+"' AND ctacte='"+ctacte+"' AND Calibre="+idcalibreselected+
 							   " AND Item in ("+iditemselected+") AND Marca="+c.getInt(0)+" and estadocenso='N'");
			
						db2.execSQL("INSERT INTO CENSOS (idactivacion, ctacte, calibre, item, marca, respuesta, idmotivo, estadocenso) "
								+ "VALUES (" + idactactivaicon + ",'"+ ctacte+ "',"	+ idcalibreselected	+ "," + iditemselected	+ ","
								+ c.getInt(0) + ",0," + idmotivo	+ ",'N');");
						
						Log.d("1b.- motivo: ","INSERT INTO CENSOS (idactivacion, ctacte, calibre, item, marca, respuesta, idmotivo, estadocenso) "
								+ "VALUES (" + idactactivaicon + ",'"+ ctacte+ "',"	+ idcalibreselected	+ "," + iditemselected	+ ","
								+ c.getInt(0) + ",0," + idmotivo	+ ",'N');");
 	 		        }
 				}
 				
				db.close();
				db2.close();

				if(!aux)
				{
 				    alerta.setTitle("Guardado");
 			        alerta.setMessage("Censo guardado en la Base del Telefono");
				}
				
			    alerta.show();	
 			}
 		});
       
        finalizar.setOnClickListener(new OnClickListener() 
        {
 			public void onClick(View v) 
 			{
	 	 		    db = dadb.getWritableDatabase(); 
		 	 		db2 = dadb2.getWritableDatabase(); 
	 		        
	 				SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
	 		        String idactactivaicon =sp.getString("idactivacion","NO DATA"); 
	 		        String ctacte=sp.getString("idctacte","NO DATA"); 
	 		        
	 		        int faltacalibre= 0;
	 		        int faltaitem= 0;
	 				
	 				Cursor c = db.rawQuery(" Select distinct T0.idactivacion, T1.idctacte, T0.calibre, T0.item " +
	 									   " FROM CENSO_DETALLE AS T0 INNER JOIN CENSO_CTACTE AS T1 ON T0.idactivacion=T1.idactivacion " + 
	 									   " where T1.idctacte='"+ctacte+"' and T0.idactivacion='"+idactactivaicon+"'",null);
	 				c.moveToFirst();   	
	 				
	 				for(int i=0; i<c.getCount(); i++)
	 				{
	 					c2 = db2.rawQuery(" Select count (*) FROM CENSOS AS T0 " + 
								   		  " WHERE idactivacion="+c.getInt(0)+" and ctacte='"+c.getString(1)+"' and "+
								   		  " calibre="+c.getInt(2)+" and item="+c.getInt(3)+" ",null);
	 					c2.moveToFirst();
	 					
	 					if(c2.getInt(0)==0)
	 					{
	 						faltacalibre=c.getInt(2);
	 						faltaitem=c.getInt(3);
	 					}
	 					
	 					c.moveToNext();
	 				}
	 				
	 				db2.close();
	 				
	 				if(faltacalibre!=0 && faltaitem!=0)
	 				{
	 					c = db.rawQuery(" Select calibre FROM CENSO_CALIBRE where idcalibre="+faltacalibre,null);
						c.moveToFirst();
						String nombrecalibre=c.getString(0);
						
	 					c = db.rawQuery(" Select item FROM CENSO_ITEM where iditem="+faltaitem,null);
	 					c.moveToFirst();
	 					String nombreitem=c.getString(0);
	
	  			        alerta.setTitle("Faltan datos por completar");
	  			        alerta.setMessage("Favor ingrese "+nombrecalibre+" - "+nombreitem+""); 
	  			        alerta.show();
	 				}
	 				else
	 				{
	 					if(!valida_rel())
	 	 				{
							
							Log.d("Actualiza: "," UPDATE CENSOS SET estadocenso='P' "+ 
									   " where ctacte='"+ctacte+"' and idactivacion='"+idactactivaicon+"'");
							
							db2 = dadb2.getWritableDatabase(); 
	 						db2.execSQL(" UPDATE CENSOS SET estadocenso='P' "+ 
									   " where ctacte='"+ctacte+"' and idactivacion='"+idactactivaicon+"'");
	 						
			 				Intent intent=new Intent(Censo.this,Finalizar_Censo.class);
			 		    	startActivity(intent);
			 		    	finish();
	 		 			}
	 	 				else
	 	 				{
	 	 					alerta.show();
	 	 				}
	 				}
	 				
	 				db.close();
 			}
 		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_censo, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Censo.this,Listado_Censo.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
	public String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");		
		return format.format(date).toString();
	}
	
	public boolean valida_datos()
	{   
		SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
	    String idactactivaicon =sp.getString("idactivacion","NO DATA"); 
		String calibreselected = spcalibres.getSelectedItem().toString();
		String itemselected = spindicador.getSelectedItem().toString();	
				
		cmp=false;
		
		for (int i=1;i<tl.getChildCount();i++) 
	    {
			TableRow row = (TableRow) tl.getChildAt(i);
	        TextView marca = (TextView) row.getChildAt(0);
	        EditText respuesta = (EditText) row.getChildAt(1);

		    if(respuesta.getText().toString().matches(""))
		    {
		    	answ="0";
		    }
		    else
		    {
		       	answ=respuesta.getText().toString();
		    }

			if(!answ.equals("0"))
			{
			    c = db.rawQuery(" Select T0.desde, T0.hasta FROM CENSO_DETALLE AS T0 " + 
			    				" INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
			    				" INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " + 
			    				" INNER JOIN CENSO_MARCA AS T3 ON T0.marca=T3.idmarca " + 
			    				" WHERE T0.idactivacion='"+idactactivaicon+"' " + 
			    				" AND T1.Calibre='"+calibreselected+"' AND T2.Item='"+itemselected+"' " +
			    				" AND T3.marca='"+marca.getText().toString()+"' and T0.desde<="+answ+
			    				" AND T0.hasta>="+answ+"",null);
				c.moveToFirst();
	
				if(c.getCount()==0)
				{
	
				    c = db.rawQuery(" Select T0.desde, T0.hasta FROM CENSO_DETALLE AS T0 " + 
		    				" INNER JOIN CENSO_CALIBRE AS T1 ON T0.calibre=T1.idcalibre " + 
		    				" INNER JOIN CENSO_ITEM AS T2 ON T0.item=T2.iditem " + 
		    				" INNER JOIN CENSO_MARCA AS T3 ON T0.marca=T3.idmarca " + 
		    				" WHERE T0.idactivacion='"+idactactivaicon+"' " + 
		    				" AND T1.Calibre='"+calibreselected+"' AND T2.Item='"+itemselected+"' " +
		    				" AND T3.marca='"+marca.getText().toString()+"'",null);
				    
				    c.moveToFirst();
			
					cmp=true;
					
					alerta.setTitle("ADVERTENCIA...");
				    alerta.setMessage("LA RESPUESTA "+marca.getText().toString()+" DEBIESE ESTAR ENTRE ["+c.getInt(0)+"-"+c.getInt(1)+"]");
				
				}
			}
	    }
		
		return cmp;
	}
	
	public boolean valida_rel()
	{
		try
		{
			db = dadb.getWritableDatabase();
			db1 = dadb1.getWritableDatabase();
		 	db2 = dadb2.getWritableDatabase(); 
			db21 = dadb21.getWritableDatabase();
		        
			SharedPreferences sp = getSharedPreferences("DatosCenso",Context.MODE_PRIVATE);              
		    String idactactivaicon =sp.getString("idactivacion","NO DATA"); 
		    String ctacte=sp.getString("idctacte","NO DATA"); 
			
			cmp=false;
			
			c = db.rawQuery(" SELECT distinct T2.item1, T3.item,T3.tipodato,T2.operador, T2.item2, T4.item, T4.tipodato " + 
							" FROM CENSO_DETALLE AS T0 " + 
							" INNER JOIN CENSO_VAL_REL AS T1 ON T0.idcensogeneral=T1.idcensogeneral " +
							" INNER JOIN CENSO_VALIDACION AS T2 ON T1.idvalidacion=T2.idvalidacion " + 
							" INNER JOIN CENSO_ITEM AS T3 ON T2.item1=T3.iditem " +
							" INNER JOIN CENSO_ITEM AS T4 ON T2.item2=T4.iditem " +
							" WHERE T0.idactivacion='"+idactactivaicon+"' AND T3.tipodato='N' and T4.tipodato='N'",null);
			
			c.moveToFirst();
	
			if(c.getCount()>0)
			{	
				for(int i=0; i<c.getCount();i++)
				{
					c2 = db2.rawQuery(" SELECT idactivacion, ctacte, calibre, item, marca, respuesta, idmotivo, estadocenso " +
							" FROM CENSOS " +
							" WHERE idactivacion='"+idactactivaicon+"' and ctacte='"+ctacte+"' AND Item="+c.getInt(0)+" AND idmotivo=0 ",null);
			
					c2.moveToFirst();
					
					for(int j=0; j<c2.getCount(); j++)
					{
						c21 = db21.rawQuery(" SELECT idactivacion, ctacte, calibre, item, marca, respuesta, idmotivo, estadocenso " +
								" FROM CENSOS " +
								" WHERE idactivacion='"+idactactivaicon+"' and ctacte='"+ctacte+"' AND Item="+c.getInt(4)+
								" and calibre="+c2.getInt(2)+" and marca="+c2.getInt(4)+" AND idmotivo=0 ",null);
						
						c21.moveToFirst();
						
						if(!valida_ecuacion(c2.getString(5), c.getString(3), c21.getString(5)))
						{
							alerta.setTitle("VALIDACION INCORRECTA");
							
							mensaje="";
							
							c1 = db.rawQuery(" SELECT marca FROM CENSO_MARCA WHERE idmarca="+c21.getInt(4),null);
							
							c1.moveToFirst();
							
							mensaje="No cumple con \""+c.getString(1)+" "+ c.getString(3) + " " + c.getString(5)+"\" favor revisar marca: "+c1.getString(0);
							
							c1 = db.rawQuery(" SELECT calibre FROM CENSO_Calibre WHERE idcalibre="+c21.getInt(2),null);
							
							c1.moveToFirst();
							
							mensaje=mensaje+" en calibre: "+c1.getString(0);
							
							alerta.setMessage(mensaje);
							
							cmp=true;
						}
						
						c2.moveToNext();
					}
					
					c.moveToNext();
				}
			}

			db.close();
			db1.close();
			db2.close();
			db21.close();
		}
		catch(Exception e)
		{
			cmp=true;
			alerta.setTitle("VALIDACION INCORRECTA");
			alerta.setMessage("ERROR EN LOS DATOS CONTACTARSE CON INFORMÁTICA");
		}
		
		return cmp;
	}
	
	public boolean valida_ecuacion(String it1, String op, String it2)
	{
		boolean retorno=false;
		
		if(op.equals("="))
		{
			if(it1.equals(it2))
			{
				retorno=true;
			}
		}
		
		if(op.equals("<"))
		{
			if(Integer.parseInt(it1)<Integer.parseInt(it2))
			{
				retorno=true;
			}
		}
		
		if(op.equals(">"))
		{
			if(Integer.parseInt(it1)>Integer.parseInt(it2))
			{
				retorno=true;
			}
		}
		
		if(op.equals("<="))
		{
			if(Integer.parseInt(it1)<=Integer.parseInt(it2))
			{
				retorno=true;
			}
		}
		
		if(op.equals(">="))
		{
			if(Integer.parseInt(it1)>=Integer.parseInt(it2))
			{
				retorno=true;
			}
		}
		
		return retorno;
	}
}
