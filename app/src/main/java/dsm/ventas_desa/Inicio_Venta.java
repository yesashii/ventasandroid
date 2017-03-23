package dsm.ventas_desa;

import java.text.DecimalFormat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import db.ventas_desa.DatosInsercion;
import dsm.ventas_desa.Finalizar_Venta.OnBack;

public class Inicio_Venta extends Activity 
{
	Button agregar;
	Button guardar;
	int contador;
	SharedPreferences sp, sp2;
	SharedPreferences sp1;
	DecimalFormat formato;
	SharedPreferences.Editor edit;
	
	TextView	numero1;
	TextView	numero2;
	TextView	numero3;
	TextView	numero4;
	TextView	numero5;
	TextView	numero6;
	TextView	numero7;
	TextView	numero8;
	TextView	numero9;
	TextView	numero10;
	TextView	numero11;
	TextView	numero12;
	
	TextView	tvv1;
	TextView	tvv2;
	TextView	tvv3;
	TextView	tvv4;
	TextView	tvv5;
	TextView	tvv6;
	TextView	tvv7;
	TextView	tvv8;
	TextView	tvv9;
	TextView	tvv10;
	TextView	tvv11;
	TextView	tvv12;
	
	TableLayout tabla;
	TextView	tvvSKU1;
	TextView	tvvSKU2;
	TextView	tvvSKU3;
	TextView	tvvSKU4;
	TextView	tvvSKU5;
	TextView	tvvSKU6;
	TextView	tvvSKU7;
	TextView	tvvSKU8;
	TextView	tvvSKU9;
	TextView	tvvSKU10;
	TextView	tvvSKU11;
	TextView	tvvSKU12;
	TextView	tvvP1;
	TextView	tvvP2;
	TextView	tvvP3;
	TextView	tvvP4;
	TextView	tvvP5;
	TextView	tvvP6;
	TextView	tvvP7;
	TextView	tvvP8;
	TextView	tvvP9;
	TextView	tvvP10;
	TextView	tvvP11;
	TextView	tvvP12	;
	TextView	tvvN1	;
	TextView	tvvN2	;
	TextView	tvvN3	;
	TextView	tvvN4	;
	TextView	tvvN5	;
	TextView	tvvN6	;
	TextView	tvvN7	;
	TextView	tvvN8	;
	TextView	tvvN9	;
	TextView	tvvN10	;
	TextView	tvvN11	;
	TextView	tvvN12	;
	TextView	tvbtnDel1	;
	TextView	tvbtnDel2	;
	TextView	tvbtnDel3	;
	TextView	tvbtnDel4	;
	TextView	tvbtnDel5	;
	TextView	tvbtnDel6	;
	TextView	tvbtnDel7	;
	TextView	tvbtnDel8	;
	TextView	tvbtnDel9	;
	TextView	tvbtnDel10	;
	TextView	tvbtnDel11	;
	TextView	tvbtnDel12	;
	TextView	totalfinal;
	
	TextView[]  listanumero;
	TextView[]  listaSku;
	TextView[]  listaProducto;
	TextView[]  listaCantidad;
	TextView[]  listaValor;
	TextView[]	listaDel;
	
	TableRow	table1	;
	TableRow	table2	;
	TableRow	table3	;
	TableRow	table4	;
	TableRow	table5	;
	TableRow	table6	;
	TableRow	table7	;
	TableRow	table8	;
	TableRow	table9	;
	TableRow	table10	;
	TableRow	table11	;
	TableRow	table12	;
	TableRow    header;
	Intent seleccionarProducto;
	Intent guardarIntent;
	DatosInsercion dadi;
	SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio__venta);
        
        listanumero = new TextView[12]; 
        listaSku = new TextView[12];
        listaProducto = new TextView[12];
        listaCantidad = new TextView[12];
        listaValor = new TextView[12];
        listaDel = new TextView[12];

        formato = new DecimalFormat("'$'###,###,###");

        agregar = (Button)findViewById(R.id.btnIVAgregar);
        guardar = (Button)findViewById(R.id.btnIVGuardar);
        
        //*****CODIGO GENERADO EXCEL*******//
        
        header = (TableRow)findViewById(R.id.TablaPrincipal);
        tabla = (TableLayout)findViewById(R.id.tablaCuerpo); 
       
        listanumero[0]=(TextView)findViewById(R.id.numeroSKU1);
        listanumero[1]=(TextView)findViewById(R.id.numeroSKU2);
        listanumero[2]=(TextView)findViewById(R.id.numeroSKU3);
        listanumero[3]=(TextView)findViewById(R.id.numeroSKU4);
        listanumero[4]=(TextView)findViewById(R.id.numeroSKU5);
        listanumero[5]=(TextView)findViewById(R.id.numeroSKU6);
        listanumero[6]=(TextView)findViewById(R.id.numeroSKU7);
        listanumero[7]=(TextView)findViewById(R.id.numeroSKU8);
        listanumero[8]=(TextView)findViewById(R.id.numeroSKU9);
        listanumero[9]=(TextView)findViewById(R.id.numeroSKU10);
        listanumero[10]=(TextView)findViewById(R.id.numeroSKU11);
        listanumero[11]=(TextView)findViewById(R.id.numeroSKU12);
        
        listaDel[0	]	=	(TextView)findViewById(R.id.	tvbtnDel1	);
        listaDel[1	]	=	(TextView)findViewById(R.id.	tvbtnDel2	);
        listaDel[2	]	=	(TextView)findViewById(R.id.	tvbtnDel3	);
        listaDel[3	]	=	(TextView)findViewById(R.id.	tvbtnDel4	);
        listaDel[4	]	=	(TextView)findViewById(R.id.	tvbtnDel5	);
        listaDel[5	]	=	(TextView)findViewById(R.id.	tvbtnDel6	);
        listaDel[6	]	=	(TextView)findViewById(R.id.	tvbtnDel7	);
        listaDel[7	]	=	(TextView)findViewById(R.id.	tvbtnDel8	);
        listaDel[8	]	=	(TextView)findViewById(R.id.	tvbtnDel9	);
        listaDel[9	]	=	(TextView)findViewById(R.id.	tvbtnDel10	);
        listaDel[10	]	=	(TextView)findViewById(R.id.	tvbtnDel11	);
        listaDel[11	]	=	(TextView)findViewById(R.id.	tvbtnDel12	);

        listaSku[0]	=	(TextView)findViewById(R.id.tvvSKU1);
        listaSku[1]	=	(TextView)findViewById(R.id.tvvSKU2);
        listaSku[2]	=	(TextView)findViewById(R.id.tvvSKU3);
        listaSku[3]	=	(TextView)findViewById(R.id.tvvSKU4);
        listaSku[4]	=	(TextView)findViewById(R.id.tvvSKU5);
        listaSku[5]	=	(TextView)findViewById(R.id.tvvSKU6);
        listaSku[6]	=	(TextView)findViewById(R.id.tvvSKU7);
        listaSku[7]	=	(TextView)findViewById(R.id.tvvSKU8);
        listaSku[8]	=	(TextView)findViewById(R.id.tvvSKU9);
        listaSku[9]	=	(TextView)findViewById(R.id.tvvSKU10);
        listaSku[10]	=	(TextView)findViewById(R.id.tvvSKU11);
        listaSku[11]	=	(TextView)findViewById(R.id.tvvSKU12);
        
        listaProducto[0]	=	(TextView)findViewById(R.id.tvvP1);
        listaProducto[1]	=	(TextView)findViewById(R.id.tvvP2);
        listaProducto[2]	=	(TextView)findViewById(R.id.tvvP3);
        listaProducto[3]	=	(TextView)findViewById(R.id.tvvP4);
        listaProducto[4]	=	(TextView)findViewById(R.id.tvvP5);
        listaProducto[5]	=	(TextView)findViewById(R.id.tvvP6);
        listaProducto[6]	=	(TextView)findViewById(R.id.tvvP7);
        listaProducto[7]	=	(TextView)findViewById(R.id.tvvP8);
        listaProducto[8]	=	(TextView)findViewById(R.id.tvvP9);
        listaProducto[9]	=	(TextView)findViewById(R.id.tvvP10);
        listaProducto[10]	=	(TextView)findViewById(R.id.tvvP11);
        listaProducto[11]	=	(TextView)findViewById(R.id.tvvP12);
        
        listaCantidad[0]	=	(TextView)findViewById(R.id.tvvN1);
        listaCantidad[1]	=	(TextView)findViewById(R.id.tvvN2);
        listaCantidad[2]	=	(TextView)findViewById(R.id.tvvN3);
        listaCantidad[3]	=	(TextView)findViewById(R.id.tvvN4);
        listaCantidad[4]	=	(TextView)findViewById(R.id.tvvN5);
        listaCantidad[5]	=	(TextView)findViewById(R.id.tvvN6);
        listaCantidad[6]	=	(TextView)findViewById(R.id.tvvN7);
        listaCantidad[7]	=	(TextView)findViewById(R.id.tvvN8);
        listaCantidad[8]	=	(TextView)findViewById(R.id.tvvN9);
        listaCantidad[9]	=	(TextView)findViewById(R.id.tvvN10);
        listaCantidad[10]	=	(TextView)findViewById(R.id.tvvN11);
        listaCantidad[11]	=	(TextView)findViewById(R.id.tvvN12);
        
        listaValor[0]	=	(TextView)findViewById(R.id.tvvV1);
        listaValor[1]	=	(TextView)findViewById(R.id.tvvV2);
        listaValor[2]	=	(TextView)findViewById(R.id.tvvV3);
        listaValor[3]	=	(TextView)findViewById(R.id.tvvV4);
        listaValor[4]	=	(TextView)findViewById(R.id.tvvV5);
        listaValor[5]	=	(TextView)findViewById(R.id.tvvV6);
        listaValor[6]	=	(TextView)findViewById(R.id.tvvV7);
        listaValor[7]	=	(TextView)findViewById(R.id.tvvV8);
        listaValor[8]	=	(TextView)findViewById(R.id.tvvV9);
        listaValor[9]	=	(TextView)findViewById(R.id.tvvV10);
        listaValor[10]	=	(TextView)findViewById(R.id.tvvV11);
        listaValor[11]	=	(TextView)findViewById(R.id.tvvV12);
        
        tvbtnDel1=(TextView)findViewById(R.id.tvbtnDel1);
        tvbtnDel2=(TextView)findViewById(R.id.tvbtnDel2);
        tvbtnDel3=(TextView)findViewById(R.id.tvbtnDel3);
        tvbtnDel4=(TextView)findViewById(R.id.tvbtnDel4);
        tvbtnDel5=(TextView)findViewById(R.id.tvbtnDel5);
        tvbtnDel6=(TextView)findViewById(R.id.tvbtnDel6);
        tvbtnDel7=(TextView)findViewById(R.id.tvbtnDel7);
        tvbtnDel8=(TextView)findViewById(R.id.tvbtnDel8);
        tvbtnDel9=(TextView)findViewById(R.id.tvbtnDel9);
        tvbtnDel10=(TextView)findViewById(R.id.tvbtnDel10);
        tvbtnDel11=(TextView)findViewById(R.id.tvbtnDel11);
        tvbtnDel12=(TextView)findViewById(R.id.tvbtnDel12);
        totalfinal=(TextView)findViewById(R.id.totalfinal);
        
        dadi = new DatosInsercion(this, "InsertarDB", null, 1);        
        
        sp1 = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
        edit = sp1.edit();
        
        contador = sp1.getInt("Contador",0);
        
        tvbtnDel1.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				db=dadi.getWritableDatabase();
				
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[0].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);				
				finish();			
				
				db.close();
			}
		});
        
		 tvbtnDel2.setOnClickListener(new OnClickListener() 
		 {	
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
						
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[1].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
				
				db.close();
			}
		});
		 
		tvbtnDel3.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[2].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
		 
		tvbtnDel4.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{	
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[3].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
		 
		tvbtnDel5.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[4].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();	
			}
		});
		 
		tvbtnDel6.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[5].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
				
				db.close();
			}
		});
		 
		tvbtnDel7.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[6].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();	
				
				db.close();	
			}
		});
		  
		tvbtnDel8.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[7].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
		 
		tvbtnDel9.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[8].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
		 
		tvbtnDel10.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[9].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
		 
		tvbtnDel11.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[10].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
		 
		tvbtnDel12.setOnClickListener(new OnClickListener() 
		{		
			public void onClick(View v) 
			{		
				db=dadi.getWritableDatabase();
					
				db.execSQL("DELETE FROM Insercion_Productos WHERE sku='"+listaSku[11].getText().toString()+"'");				
				Intent intent = new Intent(Inicio_Venta.this,Inicio_Venta.class);
				edit.putInt("Contador",contador-1);
				edit.commit();
				startActivity(intent);
				finish();			
					
				db.close();
			}
		});
	
        for(int i=0;i<listaCantidad.length;i++)
        {
        	listanumero[i].setVisibility(View.GONE);
        	listaCantidad[i].setVisibility(View.GONE);
        	listaSku[i].setVisibility(View.GONE);
        	listaValor[i].setVisibility(View.GONE);
        	listaProducto[i].setVisibility(View.GONE);
        	listaDel[i].setVisibility(View.GONE);	
        }

        @SuppressWarnings("unused")
		SharedPreferences sp = getSharedPreferences("InsercionProductos", Context.MODE_PRIVATE);

        if((contador > 0)&&(contador<13))
        {
        	db = dadi.getWritableDatabase();        	
        	Cursor c = db.rawQuery("SELECT * FROM Insercion_Productos",null);
        	c.moveToFirst();
        	float valor_final=0;
        	
        	for(int i=1;i<=contador;i++)
        	{
        		listanumero[i-1].setText(""+i+".-");
        		listaSku[i-1].setText(c.getString(1).toString());
        		listaProducto[i-1].setText(c.getString(2).toString());
        		listaCantidad[i-1].setText(""+c.getInt(3));
        		listaValor[i-1].setText(""+formato.format(c.getFloat(5)));
        		
        		listanumero[i-1].setVisibility(View.VISIBLE);
        		listaCantidad[i-1].setVisibility(View.VISIBLE);
            	listaSku[i-1].setVisibility(View.VISIBLE);
            	listaValor[i-1].setVisibility(View.VISIBLE);
            	listaProducto[i-1].setVisibility(View.VISIBLE);
            	listaDel[i-1].setVisibility(View.VISIBLE);
            	
            	valor_final=valor_final+c.getFloat(5);
            	
            	c.moveToNext();
        	}
        	
        	totalfinal.setText("TOTAL: "+formato.format(valor_final));
        	totalfinal.setVisibility(View.VISIBLE);
        	db.close();
        }        
        
        agregar.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				if(contador<12)
				{
					seleccionarProducto = new Intent(Inicio_Venta.this,Seleccionar_Producto.class);				
					startActivity(seleccionarProducto);
					finish();
				}
				else
				{
					AlertDialog.Builder alerta = new AlertDialog.Builder(Inicio_Venta.this);
					alerta.setTitle("Error");
					alerta.setMessage("No puede agregar mas de 12 productos al Carro");
					
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
		});
        
        guardar.setOnClickListener(new OnClickListener() 
        {	
        	public void onClick(View v) 
        	{	
				sp2 = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
				
				int min = sp2.getInt("minimo",0);
				
				float fin=0;
				
			    if((contador > 0)&&(contador<13))
			    {
			    	db = dadi.getWritableDatabase();        	
			        Cursor c = db.rawQuery("SELECT sum(valor) FROM Insercion_Productos",null);
			        c.moveToFirst();
			        fin=c.getFloat(0);
			        db.close();
			    }
				
				if(min<fin)
				{	
						db = dadi.getWritableDatabase();        	
			        	Cursor c = db.rawQuery("SELECT * FROM Insercion_Productos",null);
			        	c.moveToFirst();
			        	
			        if(c.getCount()>0)
			        {
			        	guardarIntent = new Intent(Inicio_Venta.this,Finalizar_Venta.class);	        		
			        	startActivity(guardarIntent);
			        	finish();
			        }
			        else
			        {
			        	final AlertDialog.Builder alerta = new AlertDialog.Builder(Inicio_Venta.this);
			        	alerta.setTitle("Productos");
			            alerta.setMessage("No tiene ningun producto agregado");
			            
			            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
			            {			
			            	public void onClick(DialogInterface dialog, int which) 
			            	{				
			            		dialog.cancel();				
			        		}
			        	});
			                
			            alerta.show();                
			        		
			        }
			        	
						db.close();
				}
				else
				{
					final AlertDialog.Builder alerta = new AlertDialog.Builder(Inicio_Venta.this);
		            alerta.setTitle("Pedido Final");
		            alerta.setMessage("El pedido minimo debe ser de: "+min);
		            
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_inicio__venta, menu);
        return true;    
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Inicio_Venta.this,Venta.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
    }
    
}
