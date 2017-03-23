package dsm.ventas_desa;

import java.math.BigDecimal;
import java.text.DecimalFormat;
//import com.google.android.maps.MyLocationOverlay;
//import android.R.integer;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;

public class Seleccionar_Cantidad extends Activity 
{
	SharedPreferences sp,sp2;
	TextView sku;	
	TextView prod;
	TextView info;
	TextView unidades;
	TextView pu;
	TextView pt;
	TextView desctop;
	TableLayout tabla;
	EditText cantidad;
	EditText desc;
	Button calcular;
	Button guardar;
	DatosAplicacion dadb;
	SQLiteDatabase db;	
	ProgressDialog pd;
	Integer cant;
	float descuento;
	float descuento_prom;
	
	String strSKU;
	float idlisprecio;
	BigDecimal factoralt ;
	BigDecimal flete ;
	BigDecimal ila;
	BigDecimal valor;
	String idflete;
	DecimalFormat formato;
	float descuentoFinal;
	
	String idcliente;
	String idpagador;
	
	DatosInsercion dadi;
	Intent intent;
	
	double total;
	
	public final Integer iva = 19;
	private String strNombre;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar__cantidad);
        
        sku = (TextView)findViewById(R.id.tvSelPSKU);
        unidades = (TextView)findViewById(R.id.tvSelPunit);
        prod = (TextView)findViewById(R.id.tvSelPProd);
        pu = (TextView)findViewById(R.id.tvSelPpu);
        pt = (TextView)findViewById(R.id.tvSelPpt);
        info = (TextView)findViewById(R.id.tvSelPinfo);
        
        cantidad = (EditText)findViewById(R.id.txtSelPCantidad);
        desc = (EditText)findViewById(R.id.txtSelPdesc);
        desctop=(TextView)findViewById(R.id.textView13);
        
        tabla = (TableLayout)findViewById(R.id.tablaFinal);
        
        calcular = (Button)findViewById(R.id.btnSelPCalc);
        guardar =(Button)findViewById(R.id.btnSelPguardar);
        
        pd = new ProgressDialog(Seleccionar_Cantidad.this);
        pd.setTitle("Cargando...");
        pd.setMessage("Espere un Momento");      
        
        formato = new DecimalFormat("'$'###,###,###");        
        dadb = new DatosAplicacion(this,"PrincipalDB", null, 1);
        dadi = new DatosInsercion(this, "InsertarDB", null, 1);        
        
        sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
        
        @SuppressWarnings("unused")
		SharedPreferences.Editor editor = sp.edit();  

        strSKU 			= sp.getString("sku","NO DATA").trim();
        strNombre  		= sp.getString("nombre", "NO DATA");
        idlisprecio 	= sp.getFloat("idlisprecio",-99);
        idflete 		= sp.getString("idflete", "-1");
        idcliente 		= sp.getString("idctacte","NO DATA");
        idpagador 		= sp.getString("idpagador","NO DATA");
        
        desc.setEnabled(false);
        desc.setText("");
        cantidad.setText("");        

        tabla.setVisibility(View.GONE);
        guardar.setVisibility(View.GONE);
        
        new Onback().execute();        

        calcular.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{			
				descuento_prom = obtenerDescuento();
				
				new Onback().onPostExecute(null);

				Log.e("Descuento", ""+descuento_prom);
				
				if(sp.getString("MAX", "V").equals("V"))
				{
					if(descuento_prom>=((desc.getText().toString().equals(""))?0:Float.parseFloat(desc.getText().toString())))
					{
						descuentoFinal = (desc.getText().toString().equals(""))?0:Float.parseFloat(desc.getText().toString());						
						cant=(cantidad.getText().toString().equals(""))?1:Integer.parseInt(cantidad.getText().toString());
						double neto = valor.doubleValue();
						double ilap = ila.doubleValue();
						double fletec = flete.doubleValue();
						
						//Calculo el valor Neto con descuento
						double NETODesc = neto -((neto*descuentoFinal)/100);
						//Calculo ILA
						double NETOILA = NETODesc*ilap/100;
						//Calculo IVA
						double NETOIVA = (NETODesc+fletec)*iva/100;
						
						double unitario = NETODesc+NETOILA+NETOIVA+fletec;
						
						total = unitario*cant;				

						unidades.setText(""+cant);
						pu.setText(""+formato.format(unitario));
						pt.setText(""+formato.format(total));

						@SuppressWarnings("unused")
						DecimalFormat f = new DecimalFormat("#.#");				
						
						tabla.setVisibility(View.VISIBLE);
				        guardar.setVisibility(View.VISIBLE);	
					}
					else
					{
						final AlertDialog.Builder alerta = new AlertDialog.Builder(Seleccionar_Cantidad.this);
				        alerta.setTitle("Descuento");
				        alerta.setMessage("Descuento excede el Maximo");
				        
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
				else
				{
					descuentoFinal = (desc.getText().toString().equals(""))?0:Float.parseFloat(desc.getText().toString());
					cant=(cantidad.getText().toString().equals(""))?1:Integer.parseInt(cantidad.getText().toString());
					double neto = valor.doubleValue();
					double ilap = ila.doubleValue();
					double fletec = flete.doubleValue();
					Log.e("CALC", ""+descuentoFinal);
					Log.e("CALC", ""+cant);
					Log.e("CALC", ""+neto);
					Log.e("CALC", ""+ilap);
					Log.e("CALC", ""+fletec);					
					
					//Calculo el valor Neto con descuento
					double NETODesc = neto -((neto*descuentoFinal)/100);
					//Calculo ILA
					double NETOILA = NETODesc*ilap/100;
					//Calculo IVA
					double NETOIVA = (NETODesc+fletec)*iva/100;
					
					double unitario = NETODesc+NETOILA+NETOIVA+fletec;
					
					total = unitario*cant;				

					unidades.setText(""+cant);
					pu.setText(""+formato.format(unitario));
					pt.setText(""+formato.format(total));

					@SuppressWarnings("unused")
					DecimalFormat f = new DecimalFormat("#.#");				
					
					tabla.setVisibility(View.VISIBLE);
			        guardar.setVisibility(View.VISIBLE);	
				}
			}
		});
        
        guardar.setOnClickListener(new OnClickListener() 
        {	
			public void onClick(View v) 
			{
				db = dadb.getWritableDatabase();
				Cursor consulta = db.rawQuery("SELECT T1.cerrada FROM Productos AS T0 LEFT JOIN Familias AS T1 ON T0.idfamilia=T1.idfamilia  WHERE T0.sku = '"+strSKU+"';" ,null);
				consulta.moveToFirst();
				
				String cerrada = (consulta.getString(0));				
				consulta.close();
				
				sp2 = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
		        @SuppressWarnings("unused")
		        
		        int minimo = sp2.getInt("minimo",0);
		        
		        Log.d("SQL","SELECT T1.cerrada FROM Productos AS T0 LEFT JOIN Familias AS T1 ON T0.idfamilia=T1.idfamilia  WHERE T0.sku = '"+strSKU+"';+"+cerrada+"cant%fact="+cant%(factoralt.intValue())+"minimo: "+minimo);
				
				if(minimo!=0 && cerrada.equals("S") && cant%(factoralt.intValue())!=0 )
				{
					final AlertDialog.Builder alerta = new AlertDialog.Builder(Seleccionar_Cantidad.this);
			        alerta.setTitle("Cantidad");
			        alerta.setMessage("La cantidad debe ser multiplo de: "+factoralt);
			        
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
					if(descuento_prom>=((desc.getText().toString().equals(""))?0:Float.parseFloat(desc.getText().toString())))
					{
						calcular.performClick();
								
						int contador = sp.getInt("Contador", 0);
								
						db = dadi.getWritableDatabase();				
						ContentValues reg = new ContentValues();
								
						reg.put("codigo",contador+1);
						reg.put("sku", strSKU);
						reg.put("nombre", strNombre);
						reg.put("cantidad",cant);
						reg.put("descuento",""+descuentoFinal);				
						reg.put("valor", Math.round(total));
								
						db.insert("Insercion_Productos",null,reg );
								
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("Contador", contador+1);
						editor.commit();
							
						db.execSQL("DELETE FROM Seleccionados WHERE sku ='"+strSKU+"'");
						Cursor c = db.rawQuery("SELECT * FROM Seleccionados",null);
						
						if(c.getCount()>0)
						{
							c.moveToFirst();
							String skuk = c.getString(0);
							String nombrek=c.getString(1);
							SharedPreferences sp1;
							sp1 = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
							SharedPreferences.Editor editor1 =sp1.edit();			
							editor1.putString("sku",skuk);
							editor1.putString("nombre",nombrek);
							editor1.commit();						
							intent = new Intent(Seleccionar_Cantidad.this,Seleccionar_Cantidad.class);						
							startActivity(intent);				
							db.close();
							finish();
						}
						else
						{
							intent = new Intent(Seleccionar_Cantidad.this,Inicio_Venta.class);						
							startActivity(intent);	
							finish();	
						}
					}
					else
					{
						final AlertDialog.Builder alerta = new AlertDialog.Builder(Seleccionar_Cantidad.this);
						alerta.setTitle("Descuento");
						alerta.setMessage("Descuento excede el Maximo");
						
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

				db.close();
			}
		});
    }
    
    public class Onback extends AsyncTask<Void, Void, Void>
    {
		@Override
		protected Void doInBackground(Void... params) 
		{
			sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		    SharedPreferences.Editor editor = sp.edit();  
		     
		    editor.putString("MAX", "V");
		    editor.commit();
			
			descuento_prom = obtenerDescuento();
			 
			db = dadb.getWritableDatabase();
			Cursor c = db.rawQuery("SELECT factoralt,ila FROM Productos WHERE sku = '"+strSKU+"';" ,null);
			c.moveToFirst();
			
			factoralt = BigDecimal.valueOf(c.getDouble(0));			
			ila = BigDecimal.valueOf(c.getDouble(1));		
			c.close();
			
			Log.d("Factor ALTL;ila",""+factoralt+";"+ila);
			
			Cursor fp = db.rawQuery("SELECT valorFlete, valor FROM Fletespag WHERE idcliente = '"+idpagador+"' AND idproducto = '"+strSKU+"'", null);
			
			if(fp.moveToFirst())
			{
				valor = BigDecimal.valueOf(fp.getDouble(1));
				flete = BigDecimal.valueOf(fp.getDouble(0));
				
				Log.d("VALORES","VALOR FLETE:"+valor+"-VALOR:"+flete);
			}
			else
			{
				Cursor d = db.rawQuery("SELECT valor FROM Lista_De_Precios WHERE idsku = '"+strSKU+"' AND idlisprecio = "+idlisprecio, null);
				d.moveToFirst();			
				valor = BigDecimal.valueOf(d.getDouble(0));			
				d.close();			
				
				Cursor e = db.rawQuery("SELECT valorFlete FROM Flete WHERE idtipocliente = '"+idflete+"' AND idproducto='"+strSKU+"';",null);
				e.moveToFirst();
				flete = BigDecimal.valueOf(e.getDouble(0));
				e.close();
				
				Log.d("VALORES","VALOR FLETE:"+valor+"-VALOR:"+flete);
			}
			
			fp.close();
			
			db.close();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{		
			if(sp.getString("MAX","V").equals("V"))
			{
				desc.setTextColor(Color.WHITE);
				desc.setEnabled(true);
				desctop.setText(""+descuento_prom);
				desctop.setTextColor(Color.GRAY);
			}
			else
			{
				desc.setText(""+descuento_prom);
				desc.setTextColor(Color.RED);
				desc.setEnabled(false);
				desctop.setText(""+descuento_prom);
				desctop.setTextColor(Color.GRAY);
			}
			
			Log.d("POSTEXECUTE","FIJO:"+sp.getString("MAX","V"));
			
			sku.setText(strSKU);
	        prod.setText(strNombre);
	        info.setText("(Empaque) "+factoralt+" por Caja");	        
	        
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{	
			super.onPreExecute();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	getMenuInflater().inflate(R.menu.activity_seleccionar__cantidad, menu);
        return true;
    }
    
    public float obtenerDescuento()
    {
    	SharedPreferences sp;
    	sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString("MAX","V");
    	editor.commit();
    	
    	String idcliente 	= sp.getString("idctacte","NO DATA");
    	String strSKU 		= sp.getString("sku","NO DATA").trim();
    	Integer idcanal 	= sp.getInt("idcanal",-1);
    	Log.d("IDCANAL","idcanal: "+idcanal);
    	String idpagador 	= sp.getString("idpagador","NO DATA");
    	
    	sp=getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
    	
    	float descuento_prom =0;
    	String fijo_prom="V";
    	DatosAplicacion dadb;
    	SQLiteDatabase db;	
    	dadb = new DatosAplicacion(Seleccionar_Cantidad.this,"PrincipalDB", null, 1);
    	db=dadb.getWritableDatabase();
    	
    	Cursor c = db.rawQuery("SELECT idmegacanal FROM Canales WHERE idcanal = "+idcanal, null);
    	c.moveToFirst();
    	Integer idmegacanal=c.getInt(0);
    	
    	//Primero se Busca el descuento BASE
    	c=db.rawQuery("SELECT descuento_base FROM Clientes WHERE idctacte = '"+idcliente+"'", null);
    	c.moveToFirst();
    	float descuento_prom_base=Float.parseFloat(c.getString(0));
    	String fijo_base="V";
    	Log.d("DESCUENTO","DESCUENTO BASE");
    	
    	//***********************
	    //Descuento X Cliente
    	//***********************
    	
	    float descuento_prom_cli =0;
	    String fijo_cli="V";
	    	
	    c=db.rawQuery("SELECT descuento, sw_trato FROM CLIPROMDESC WHERE idproducto = '"+strSKU+"' AND idcliente = '"+idcliente+"'",null);
			
		if(c.getCount()>0)
		{
			c.moveToFirst();    		
	    	descuento_prom_cli= c.getFloat(0);
	    	fijo_cli=c.getString(1).toUpperCase();
	    	Log.d("DESCUENTO","CLIPROMDESC,"+fijo_cli+":"+descuento_prom_cli);
		}
		else
		{
		   	c=db.rawQuery("SELECT descuento, sw_trato FROM MAXPAGDESC WHERE idcliente='"+idpagador+"' AND idproducto='"+strSKU+"'", null);
		    	
		   	if(c.getCount()>0)
		   	{
		   		c.moveToFirst();
		    	descuento_prom_cli=c.getFloat(0);
		    	fijo_cli=c.getString(1).toUpperCase();
		    	Log.d("DESCUENTO","MAXPAGDESC,"+fijo_cli+":"+descuento_prom_cli);
		    }
		    else
		    {		
		    	c=db.rawQuery("SELECT descuento, sw_trato FROM PAGPROMDESC WHERE idproducto = '"+strSKU+"' AND idcliente = '"+idpagador+"'",null);
					
				if(c.getCount()>0)
				{
					c.moveToFirst();    		
	            	descuento_prom_cli= c.getFloat(0);
	            	fijo_cli=c.getString(1).toUpperCase();
	            	Log.d("DESCUENTO","PAGPROMDESC,"+fijo_cli+":"+descuento_prom_cli);
				}
				else
				{
					c=db.rawQuery("SELECT descuento,sw_trato FROM GRUPROMDESC WHERE idproducto = '"+strSKU+"' AND idcliente = '"+idpagador+"'",null);
						
					if(c.getCount()>0)
	    			{
						c.moveToFirst();    		
	                	descuento_prom_cli= c.getFloat(0);
	                	fijo_cli=c.getString(1).toUpperCase();
	                	Log.d("DESCUENTO","GRUPROMDESC,"+fijo_cli+":"+descuento_prom_cli);
	    			}
				}
		    }
		}
			
		//**********************
		//Descuento X Segmento
		//**********************
			
		float descuento_prom_seg =0;
		String fijo_seg="V";
			
		c=db.rawQuery("SELECT descuento, sw_trato FROM SUCPROMDESC WHERE idproducto = '"+strSKU+"' AND idcanal ="+idcanal,null);
			
		if(c.getCount()>0)
		{
			c.moveToFirst();    		
	    	descuento_prom_seg= c.getFloat(0);
	    	fijo_seg=c.getString(1).toUpperCase();
	    	Log.d("DESCUENTO","SUCPROMDESC,"+fijo_seg+":"+descuento_prom_seg);
		}
		else
		{		
			c=db.rawQuery("SELECT descuento, sw_trato FROM CANPROMDESC WHERE idproducto = '"+strSKU+"' AND idcanal ="+idcanal,null);
				
			if(c.getCount()>0)
			{
				c.moveToFirst();    		
		    	descuento_prom_seg= c.getFloat(0);
		    	fijo_seg=c.getString(1).toUpperCase();
		    	Log.d("DESCUENTO","CANPROMDESC,"+fijo_seg+":"+descuento_prom_seg);
			}
			else
			{
				c=db.rawQuery("SELECT descuento, sw_trato FROM MAXMCANDESC WHERE idproducto = '"+strSKU+"' AND idmegacanal="+idmegacanal+"", null);
			    	
			   	if(c.getCount()>0)
			   	{
			   		c.moveToFirst();
			   		descuento_prom_seg=c.getFloat(0);
			   		fijo_seg=c.getString(1).toUpperCase();
			       	Log.d("DESCUENTO","MAXMCANDESC,"+fijo_seg+":"+descuento_prom_seg);
			   	}
			   	else
			   	{
			   		c = db.rawQuery("SELECT descuento, sw_trato FROM MEGPROMDESC WHERE idmegacanal = "+idmegacanal+" AND idproducto = '"+strSKU+"'", null);
			    		
			    	if(c.getCount()>0)
			    	{
			    		c.moveToFirst(); 
			    		descuento_prom_seg=c.getFloat(0);
			    		fijo_seg=c.getString(1).toUpperCase();
			    		Log.d("DESCUENTO","MEGPROMDESC,"+fijo_seg+descuento_prom_seg);
			    	}   		
			    }	
			}
		}
		
		//***Compite Descuento por Cliente y Descuento por Segmento***
			
		if(descuento_prom_cli>=descuento_prom_seg)
		{
			descuento_prom=descuento_prom_cli;
			fijo_prom=fijo_cli;
		}
		else
		{
			descuento_prom=descuento_prom_seg;
			fijo_prom=fijo_seg;
		}
			
		//***********************
		//Descuento X Vendedor
		//***********************
			
		float descuento_prom_ven =0;
		String fijo_ven="V";
		
		c = db.rawQuery("SELECT descuento, sw_trato FROM VENPROMDESC WHERE idproducto = '"+strSKU+"'",null);
		    	
		if(c.getCount()>0)
		{
			c.moveToFirst();    		
		    descuento_prom_ven= c.getFloat(0);
		    fijo_ven=c.getString(1).toUpperCase();
		    Log.d("DESCUENTO","VENPROMDESC,"+fijo_ven+":"+descuento_prom_ven);
		}
		else
		{
		   	c=db.rawQuery("SELECT descuento, sw_trato FROM GVTPROMDESC WHERE idproducto = '"+strSKU+"'", null);
	    		
	    	if(c.getCount()>0)
	    	{
	    		c.moveToFirst();    		
	    		descuento_prom_ven= c.getFloat(0);
	    		fijo_ven=c.getString(1).toUpperCase();
	       		Log.d("DESCUENTO","GVTPROMDESC,"+fijo_ven+":"+descuento_prom_ven);
	    	}	
	    	else
	    	{		
	    		c=db.rawQuery("SELECT descuento, sw_trato FROM MGVTPROMDESC WHERE idproducto = '"+strSKU+"'", null);
		    		
		    	if(c.getCount()>0)
		    	{
		    		c.moveToFirst();    		
		    		descuento_prom_ven= c.getFloat(0);
		    		fijo_ven=c.getString(1).toUpperCase();
		       		Log.d("DESCUENTO","MGVTPROMDESC,"+fijo_ven+":"+descuento_prom_ven);
		    	}
	    	}
		}
		    
		//***Compite el descuento acarreado anteriormente con el descuento de vendedor***
		    
		if(descuento_prom_ven>=descuento_prom)
		{
			descuento_prom=descuento_prom_ven;
		    fijo_prom=fijo_ven;
		}
		    
		//***********************
		//Descuento por SKU
		//***********************
		    
		float descuento_prom_sku =0;
		String fijo_sku="V";
		   
		c=db.rawQuery("SELECT descuento, sw_trato FROM MAXSKUDESC WHERE idproducto = '"+strSKU+"'", null);
	    	
	    if(c.getCount()>0)
	    {
	    	c.moveToFirst();
	    	descuento_prom_sku=c.getFloat(0);
	    	fijo_sku=c.getString(1).toUpperCase();
	    	Log.d("DESCUENTO","MAXSKUDESC,"+fijo_sku+":"+descuento_prom_sku);
	    }
	    	
	    //***Compite el descuento acarreado con el descuento por SKU***
	    	
	    if(descuento_prom_sku>=descuento_prom)
	    {
	    	descuento_prom=descuento_prom_sku;
	    	fijo_prom=fijo_sku;
	    }
	    	
	    //***********************
	    //Descuento por Volumen
	    //***********************
	    
	    float descuento_prom_vol =0;
	    String fijo_vol="V";
	    	
	    int canti = (cantidad.getText().toString().equals(""))?1:Integer.parseInt(cantidad.getText().toString());
	    	
	    db = dadb.getWritableDatabase();
	    Cursor esc=db.rawQuery("SELECT descuento, sw_trato FROM ESCDESC WHERE idcanal ="+idcanal+" " +
	    			"AND idproducto ='"+strSKU+"' AND cantidad_desde <="+canti+
	    			" AND cantidad_hasta >= "+canti, null);
	    
	    Log.d("PASO","PASO: "+idcanal+strSKU+canti);
	    	
	    if(esc.getCount()>0)
	    {
	    	esc.moveToFirst();
	    	descuento_prom_vol = esc.getFloat(0);
	    	fijo_vol=esc.getString(1).toUpperCase();
			Log.d("DESCUENTO","ESCDESC,"+fijo_vol+":"+descuento_prom_vol);
	    }
	    	
	    db.close();
	    	
	    //***Compite el descuento acarreado con el descuento por volumen***	
	    if(descuento_prom_vol>=descuento_prom)
	    {
	    	descuento_prom=descuento_prom_vol;
	    	fijo_prom=fijo_vol;
	    }
	    	
	    //***En caso de que no encuentre descuento anteriormente, se queda con el D. Base***
	    if(descuento_prom==0)
	    {
		   	if(descuento_prom_base>=descuento_prom)
		   	{
		   		descuento_prom=descuento_prom_base;
		   		fijo_prom=fijo_base;
		   	}
	    }
    	
    	editor.putString("MAX",fijo_prom);;
    	editor.commit();
    	
    	Log.d("DESCUENTO FINAL",""+descuento_prom);
    	Log.d("FIJO FINAL",""+fijo_prom);
	    		
    	db.close();    	
		return descuento_prom;
    }
    
    public void onBackPressed()
    {
    	Intent intent=new Intent(Seleccionar_Cantidad.this,Seleccionar_Producto.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
}
