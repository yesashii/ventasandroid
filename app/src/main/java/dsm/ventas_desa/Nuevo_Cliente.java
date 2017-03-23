package dsm.ventas_desa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import db.ventas_desa.DatosAplicacion;

public class Nuevo_Cliente extends Activity 
{
	private TextView tvRut,tvSigla,tvRazonSocial,tvNombreEncargado,tvTelefono,tvSocon;
	private TextView tvNumerodeCuenta,tvTitular,tvRutCuenta,tvSucursal,tvSerieCI,tvCalle,tvNro;
	private EditText txtRut,txtSigla,txtRazonSocial,txtNombreEncargado,txtTelefono,txtEmail,txtNumerodeCuenta,txtTitular;
	private EditText txtRutCuenta,txtSucursal,txtSerieCI,txtCalle,txtNro,txtOf,txtMontoCredito;	
	
	@SuppressWarnings("unused")
	private Spinner spCanal,spCondiciondePago,spBanco,spRegion,spCiudad,spComuna,spSoconsumo;
	private Button btnContinuar;
	private DatosAplicacion dadb;
	private SQLiteDatabase db;
	
	@SuppressWarnings("unused")
	private Cursor cursorBanco,cursorCanal,cursorCondPag,cursorComunas,cursorCiudades,cursorRegiones;
	private Intent nextStep;
	SharedPreferences sp;
	Integer[] idregiones;
	String[] datosCiudades;
	String[] nombresoconsumo;
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo__cliente);
        
        nextStep = new Intent(this,Insertar_Ruta.class);
        sp = getSharedPreferences("DatosCliente",Context.MODE_PRIVATE);
    	final SharedPreferences.Editor editor = sp.edit();
        
        String[] datosBancos;
        String[] datosCondPago;
        String[] datosCanal;
        @SuppressWarnings("unused")
		String[] datosComunas;        
        String[] datosRegiones;
       
        tvRut = (TextView)findViewById(R.id.tvRut);
        tvSigla=(TextView)findViewById(R.id.tvSigla);
        tvRazonSocial =(TextView)findViewById(R.id.tvRazonSocial);
        tvSocon =(TextView)findViewById(R.id.tvssoconsumonc);
        tvNombreEncargado=(TextView)findViewById(R.id.tvNombreEncargado);
        tvTelefono = (TextView)findViewById(R.id.tvTelefono);        
        tvNumerodeCuenta=(TextView)findViewById(R.id.tvNumeroCuenta);
        tvTitular=(TextView)findViewById(R.id.tvTitular);
        tvRutCuenta =(TextView)findViewById(R.id.tvRutCuenta);
        tvSucursal =(TextView)findViewById(R.id.tvSucursal);
        tvSerieCI=(TextView)findViewById(R.id.tvSerieCI);
        tvCalle =(TextView)findViewById(R.id.tvCalle);
        tvNro =(TextView)findViewById(R.id.tvNro);       
        
        txtRut = (EditText)findViewById(R.id.txtRut);
        txtSigla=(EditText)findViewById(R.id.txtSigla);
        txtRazonSocial = (EditText)findViewById(R.id.txtRazonSocial);
        txtNombreEncargado = (EditText)findViewById(R.id.txtNombreEncargado);
        txtTelefono = (EditText)findViewById(R.id.txtTelefono);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtNumerodeCuenta = (EditText)findViewById(R.id.txtNumeroCuenta);
        txtTitular = (EditText)findViewById(R.id.txtTitular);
        txtRutCuenta =(EditText)findViewById(R.id.txtRutCuenta);
        txtSucursal=(EditText)findViewById(R.id.txtSucursal);
        txtSerieCI = (EditText)findViewById(R.id.txtSerieCI);
        txtCalle = (EditText)findViewById(R.id.txtCalle);
        txtNro = (EditText)findViewById(R.id.txtNro);
        txtOf = (EditText)findViewById(R.id.txtOf);
        txtMontoCredito=(EditText)findViewById(R.id.txtMontoCredito);
        
        btnContinuar = (Button)findViewById(R.id.btnContinuar);
        
        spCanal = (Spinner)findViewById(R.id.spcanal);
        spSoconsumo= (Spinner)findViewById(R.id.spsoconsumonc);
        spCondiciondePago = (Spinner)findViewById(R.id.spCondicionPago);
        spBanco = (Spinner)findViewById(R.id.spBanco);        
        spRegion = (Spinner)findViewById(R.id.spRegion);
        spCiudad = (Spinner)findViewById(R.id.spCiudad);        
   
        dadb = new DatosAplicacion(this, "PrincipalDB", null, 1);
        db = dadb.getWritableDatabase();
        txtMontoCredito.setEnabled(false);
        
        txtTitular.setEnabled(false);
        txtRutCuenta.setEnabled(false);
        txtSucursal.setEnabled(false);
        txtSerieCI.setEnabled(false);
        
      //**********INICIO INSTANCIA SPINNER SOCONSUMO**************//
        
      	Cursor c = db.rawQuery("SELECT * FROM SOConsumo",null);
      	c.moveToFirst();   	
      	
      	nombresoconsumo = new String[c.getCount()];
      	
      	for(int i=0;i<nombresoconsumo.length;i++)
      	{
      		nombresoconsumo[i] = c.getString(0);
      		c.moveToNext();
      	}

      	c.close();
      	
      	ArrayAdapter<String> adaptadorsoconsumo;
      	
      	adaptadorsoconsumo = new ArrayAdapter<String>(Nuevo_Cliente.this,android.R.layout.simple_spinner_item,nombresoconsumo);
      	adaptadorsoconsumo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
      	
      	spSoconsumo.setAdapter(adaptadorsoconsumo);
	    int spinnerPosition = adaptadorsoconsumo.getPosition("SELECCIONE...");
	    spSoconsumo.setSelection(spinnerPosition); ; 
      	
      	//**********FIN INSTANCIA SPINNER SOCONSUMO**************//
      	
        //**********INICIO INSTANCIA SPINNER BANCOS**************//
        cursorBanco = db.rawQuery("SELECT nombre FROM Bancos",null);
        datosBancos = new String[cursorBanco.getCount()];
        cursorBanco.moveToFirst();
        
        for(int i=0;i<datosBancos.length;i++)
        {
        	datosBancos[i] = cursorBanco.getString(0);
        	cursorBanco.moveToNext();
        }
        
        ArrayAdapter<String> adaptadorBancos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,datosBancos);
        adaptadorBancos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBanco.setAdapter(adaptadorBancos);
        
        cursorBanco.close();
        
        //**********FIN INSTANCIA SPINNER BANCOS**************//
        
        //**********INICIO INSTANCIA SPINNER CANALES**************//    
        
        cursorCanal = db.rawQuery("SELECT nombre FROM Canales",null);
        datosCanal = new String[cursorCanal.getCount()];
        cursorCanal.moveToFirst();
        
        for(int i=0;i<datosCanal.length;i++)
        {
        	datosCanal[i] = cursorCanal.getString(0);
        	cursorCanal.moveToNext();
        }
        
        ArrayAdapter<String> adaptadorCanal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,datosCanal);
        adaptadorCanal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCanal.setAdapter(adaptadorCanal);
        
        cursorCanal.close();
        
        //**************************FIN INSTANCIA SPINNER CANALES****************************//
        
        //************************INICIO INSTANCIA SPINNER CONDICIONES DE PAGO***********************//
        cursorCondPag = db.rawQuery("SELECT nombre FROM Condiciones_De_Pago",null);
        datosCondPago = new String[cursorCondPag.getCount()];
        cursorCondPag.moveToFirst();
        
        for(int i=0;i<datosCondPago.length;i++)
        {
        	datosCondPago[i] = cursorCondPag.getString(0);
        	cursorCondPag.moveToNext();
        }
        
        ArrayAdapter<String> adaptadorCondPag = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,datosCondPago);
        adaptadorCondPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCondiciondePago.setAdapter(adaptadorCondPag);
        spCondiciondePago.setSelection(7);
        
        cursorCondPag.close();
        
        //**********FIN INSTANCIA SPINNER CONDICIONES DE PAGO**************//
        
        //**********INICIO INSTANCIA SPINNER REGIONES**************//
        
        cursorRegiones = db.rawQuery("SELECT nombre,idregion FROM Regiones",null);
        idregiones = new Integer[cursorRegiones.getCount()];
        datosRegiones = new String[cursorRegiones.getCount()];
        cursorRegiones.moveToFirst();
        
        for(int i=0;i<datosRegiones.length;i++)
        {
        	datosRegiones[i] = cursorRegiones.getString(0);
        	idregiones[i]= cursorRegiones.getInt(1);        	
        	cursorRegiones.moveToNext();
        }
        
        ArrayAdapter<String> adaptadorRegiones = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,datosRegiones);
        adaptadorRegiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegion.setAdapter(adaptadorRegiones);
		
		cursorRegiones.close();
		
        //**********FIN INSTANCIA SPINNER REGIONES**************//
        
        //**********INICIO INSTANCIA SPINNER CIUDADES**************//
		
		spRegion.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			
			public void onItemSelected(AdapterView<?> arg0, View v,int pos, long arg3) 
			{
				db = dadb.getWritableDatabase();
				int idregion = idregiones[pos];
				
				cursorCiudades = db.rawQuery("SELECT nombre FROM Comunas WHERE idregion = "+idregion,null);
		        datosCiudades = new String[cursorCiudades.getCount()];
		        cursorCiudades.moveToFirst();
		        
		        for(int i=0;i<datosCiudades.length;i++)
		        {
		        	datosCiudades[i] = cursorCiudades.getString(0);
		        	cursorCiudades.moveToNext();
		        }
				
		        ArrayAdapter<String> adaptadorCiudades = new ArrayAdapter<String>(Nuevo_Cliente.this, android.R.layout.simple_spinner_item,datosCiudades);
		        adaptadorCiudades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        spCiudad.setAdapter(adaptadorCiudades);
				
				cursorCiudades.close();
				db.close();
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
			}
		});          

        //**********FIN INSTANCIA SPINNER CIUDADES**************//
        
        final AlertDialog.Builder builder = new AlertDialog.Builder(Nuevo_Cliente.this);
		builder.setTitle("Validación");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{					
			public void onClick(DialogInterface dialog, final int which) 
			{				
				dialog.cancel();				
			}
		});
		
		db.close();
		
		spCondiciondePago.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent, View v,int position, long id) 
			{
				if(spCondiciondePago.getSelectedItem().toString().equals("EFECTIVO CAMION"))
				{
					txtMontoCredito.setEnabled(false);
					txtMontoCredito.setText("500000");
				}
				else
				{
					txtMontoCredito.setEnabled(true);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				
			}
			
		});
		
        //*********INSTANCIAS VALIDADORAS PARA EXPERIENCIAS DEL USUARIO*********//
        
        btnContinuar.setOnClickListener(new OnClickListener() 
        {			
			public void onClick(View v) 
			{			
				String mensaje="Errores de Ingreso de Datos";
				
				//***********Variables de Validacion**************//
				
				boolean validador = true;
				boolean rutval = true;
				boolean siglaval=true;
				boolean rsval = true;
				boolean neval=true;
				boolean telval = true;
				boolean calleval = true;
				boolean nroval = true;
				
				String cn = "Campo Necesario";
				
				if(txtRut.getText().length()>0)
				{
					if(validarRut(txtRut.getText().toString().toUpperCase()))
					{
						
					}
					else
					{
						rutval = false;
						validador = false;
						tvRut.setText("RUT:No Valido (Ingrese RUT sin puntos)");
						tvRut.setTextColor(Color.RED);
					}
				}
				else
				{
					validador = false;
					rutval = false;
					tvRut.setText("RUT:Campo Necesario");
					tvRut.setTextColor(Color.RED);					
				}				
				
				if(rutval)
				{
					tvRut.setTextColor(Color.WHITE);
					tvRut.setText("RUT");
				}
				
				if(txtSigla.length()==0)
				{
					validador = false;
					siglaval=false;					
					tvSigla.setText("Sigla:Campo Necesario");
					tvSigla.setTextColor(Color.RED);
				}
				
				if(siglaval)
				{
					tvSigla.setTextColor(Color.WHITE);
					tvSigla.setText("Sigla");					
				}
				
				if(txtRazonSocial.length()==0)
				{
					validador=false;
					rsval=false;
					tvRazonSocial.setText("Razon Social:Campo Necesario");
					tvRazonSocial.setTextColor(Color.RED);
				}
				
				if(rsval)
				{
					tvRazonSocial.setTextColor(Color.WHITE);
					tvRazonSocial.setText("Razon Social");			
				}
				
				if(txtNombreEncargado.length()==0)
				{
					validador=false;
					neval=false;
					tvNombreEncargado.setText("Nombre Encargado:"+cn);
					tvNombreEncargado.setTextColor(Color.RED);
				}
				
				if(neval)
				{
					tvNombreEncargado.setTextColor(Color.WHITE);
					tvNombreEncargado.setText("Nombre Encargado");
				}
				
				if(txtTelefono.length()==0)
				{
					validador=false;
					telval=false;
					tvTelefono.setText("Telefono:"+cn);
					tvTelefono.setTextColor(Color.RED);
				}
				
				if(telval)
				{
					tvTelefono.setTextColor(Color.WHITE);
					tvTelefono.setText("Telefono");
				}
				
				if(txtCalle.length()==0)
				{
					validador=false;
					calleval=false;
					tvCalle.setText("Calle o Avenida:"+cn);
					tvCalle.setTextColor(Color.RED);
				}
				
				if(calleval)
				{
					tvCalle.setTextColor(Color.WHITE);
					tvCalle.setText("Calle o Avenida");
				}
				
				if(txtNro.length()==0)
				{
					validador=false;					
					tvNro.setText("Nro:"+cn);
					tvNro.setTextColor(Color.RED);
					nroval = false;					
				}
				
				if(nroval)
				{
					tvNro.setTextColor(Color.WHITE);
					tvNro.setText("Calle o Avenida");
				}
				
				if(spCondiciondePago.getSelectedItem().toString().equals("EFECTIVO CAMION"))
				{
					
				}
				else
				{
					validador=validardatosCuenta();
				}
				
				if(spSoconsumo.getSelectedItem().toString().equals("SELECCIONE..."))
				{
					validador = false;
					siglaval=false;					
					tvSocon.setText("SO Consumo:Campo Necesario");
					tvSocon.setTextColor(Color.RED);
				}
				
				db = dadb.getReadableDatabase();
				String[] arg = new String[] {txtRut.getText().toString().toUpperCase()};
				Cursor caux = db.rawQuery("SELECT codlegal FROM Clientes WHERE codlegal=? ", arg);
				
				if(caux.getCount()>0)
				{
					validador = false;
					mensaje = "El Usuario ya Existe";
				}				
				
				caux.close();
				db.close();			
				
				//*************ULTIMA INSTANCIA VALIDADORA**************************//
				
				if (validador)
				{					
					editor.putString("Rut", txtRut.getText().toString().toUpperCase());
					editor.putString("Sigla", txtSigla.getText().toString().toUpperCase());
					editor.putString("Razon Social", txtRazonSocial.getText().toString().toUpperCase());
					editor.putString("Canal", spCanal.getSelectedItem().toString());
					editor.putString("Nombre Encargado", txtNombreEncargado.getText().toString().toUpperCase());
					editor.putString("Telefono",txtTelefono.getText().toString());
					editor.putString("Email",txtEmail.getText().toString());
					editor.putString("Condicion de Pago",spCondiciondePago.getSelectedItem().toString());
					editor.putString("Monto de Credito", txtMontoCredito.getText().toString());
					editor.putString("Banco",spBanco.getSelectedItem().toString());
					editor.putString("Numero de Cuenta" ,txtNumerodeCuenta.getText().toString());
					editor.putString("Titular",txtTitular.getText().toString());
					editor.putString("Rut Titular", txtRutCuenta.getText().toString().toUpperCase());
					editor.putString("Sucursal",txtSucursal.getText().toString());
					editor.putString("Serie CI",txtSerieCI.getText().toString());
					editor.putString("Calle",txtCalle.getText().toString());
					editor.putString("Nro", txtNro.getText().toString());
					editor.putString("Oficina", txtOf.getText().toString());
					editor.putString("Region", spRegion.getSelectedItem().toString());
					editor.putString("Ciudad", spCiudad.getSelectedItem().toString());	
					editor.putString("Soconsumo", spSoconsumo.getSelectedItem().toString());
					editor.commit();
					
					startActivity(nextStep);						
				}
				else
				{					
					builder.setMessage(mensaje);
					builder.show();
				}
			}
		});
    }
    
    public boolean validarRut(String rut)
    {
    	if(rut.length()>0)
    	{
    		String[] rut_dv=rut.split("-"); 
    		
    		if(rut_dv.length ==2)
    		{
    			try
    			{
    				int rutNum = Integer.parseInt(rut_dv[0]);
    				char dv = rut_dv[1].charAt(0);
    				
    				if(this.validarRutNum(rutNum,dv))
    				{
    					return true;    					
    				}
    				else
    				{
    					return false;
    				}
    			}
    			catch(Exception e)
    			{
    				
    				Log.d("Error Validación",e.getMessage());
    				return false;
    			}
    		}
    	}
    	
		return false;
    }
    
    private boolean validardatosCuenta()
    {
    	boolean val=true;
    	
    	if(txtNumerodeCuenta.length()==0)
    	{
    		val=false;
    		tvNumerodeCuenta.setTextColor(Color.RED);
    	}
    	else
    	{
    		val=true;
    		tvNumerodeCuenta.setTextColor(Color.WHITE);
    	}
    	
    	if(txtTitular.length()==0)
    	{
    		val=false;
    		tvTitular.setTextColor(Color.RED);
    	}
    	else
    	{
    		val=true;
    		tvTitular.setTextColor(Color.WHITE);
    		
    	}
    	
    	if(txtSucursal.length()==0)
    	{
    		val=false;
    		tvSucursal.setTextColor(Color.RED);
    		
    	}
    	else
    	{
    		val=true;
    		tvSucursal.setTextColor(Color.WHITE);    		
    	}
    	
    	if(txtSerieCI.length()==0)
    	{
    		val=false;
    		tvSerieCI.setTextColor(Color.RED);
    	}
    	else
    	{
    		val=true;
    		tvSerieCI.setTextColor(Color.WHITE);
    	}
    	
    	if(txtRutCuenta.length()==0)
    	{
    		val=false;
    		tvRutCuenta.setTextColor(Color.RED);
    	}
    	else
    	{
    		val=true;
    		tvRutCuenta.setTextColor(Color.WHITE);
    	}
    	
		return val;
    }

    private boolean validarRutNum(int rutNum, char dv) 
    {
    	Character.toUpperCase(dv);
    	int m = 0;
    	int s = 1;  
    	
    	for(;rutNum !=0;rutNum/=10)
    	{
    		s=(s+rutNum % 10 * (9-m++ % 6)) % 11;
    	}
    	
		return dv == (char)(s != 0 ? s + 47 : 75);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.activity_nuevo__cliente, menu);
        return true;
    }
	
	@Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Nuevo_Cliente.this,Clientes.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
	
}
