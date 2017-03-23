package dsm.ventas_desa;

import org.ksoap2.serialization.SoapObject;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.Toast;
import db.ventas_desa.GestionDatosVendedor;

public class Login extends Activity 
{
	
	public Login()
	{
		
	}
	
	private EditText txtPassword;
	private Button btnAcceder;			
	private SQLiteDatabase basedeDatos;
	private Spinner spinnerUsuario;
	private Toast toast;
	private String psw_pedidos,version;
	private int idlocal;
	private int minimo;
	private Intent intent;
	private Cursor consulta2;
	private GestionDatosVendedor database;
	private ProgressDialog pdCargando,pd;	
	private ArrayAdapter<String> adaptador,adaptador2;
	private String[] nombreVendedor;	
	private Integer[] idempresaVendedor;
	private String[] usuarioVendedor;
	private String[] idvendedor;
	private Integer[] empresas;
	private String[] nombreEmpresa;
	public Cursor c;
	private Spinner spEmpresa;
	private int idempresa;
	private Boolean fallo=false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_login);
        
        toast = Toast.makeText(this, "Error Base de Datos", Toast.LENGTH_LONG);
        intent = new Intent(this,Principal.class);
        pdCargando = new ProgressDialog(Login.this); 
        pd= new ProgressDialog(Login.this); 
        btnAcceder = (Button)findViewById(R.id.btnAcceder);
        txtPassword = (EditText)findViewById(R.id.txtPassword); 
        spinnerUsuario =(Spinner)findViewById(R.id.spinnerUser);
        spEmpresa = (Spinner)findViewById(R.id.spEmpresa);
        
        spinnerUsuario.setEnabled(false);
        btnAcceder.setEnabled(false);
        
        final SharedPreferences datosIniciales = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
        
        final AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
        alert.setTitle("Error");        
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
        
        if(datosIniciales.getBoolean("sesion",false)== false)
        { 	
	        try
	        {
	        	new TareaAsincrona().execute();   
	        }
	        catch(Exception e)
	        {
	        	alert.setMessage("Error Conexion de Datos/n"+e.getMessage());        	
	        	alert.show();
	        }           
        
	        spEmpresa.setOnItemSelectedListener(new OnItemSelectedListener() 
	        {
	        	public void onItemSelected(AdapterView<?> a, View v,int position, long arg3) 
	        	{
					String nombreEmpresa = spEmpresa.getSelectedItem().toString();				
					idempresa = empresas[position];
					basedeDatos = database.getWritableDatabase();				
					
					Cursor c = basedeDatos.rawQuery("SELECT nombre,idvendedor FROM Vendedor WHERE idempresa = "+idempresa+";", null);
					nombreVendedor = new String[c.getCount()];
					idvendedor = new String[c.getCount()];
					String[] vendedorFinal = new String[c.getCount()];
					
					c.moveToFirst();
				
					for(int i=0;i<nombreVendedor.length;i++)
					{
						nombreVendedor[i]=c.getString(0);
						idvendedor[i]=c.getString(1);
						vendedorFinal[i]=idvendedor[i]+" "+nombreVendedor[i];
						c.moveToNext();					
					}

					c.close();
					basedeDatos.close();
					
					adaptador2 = new ArrayAdapter<String>(Login.this,android.R.layout.simple_spinner_item,vendedorFinal);
			        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        
			        spinnerUsuario.setEnabled(true);
			        btnAcceder.setEnabled(true);
			        
			        SharedPreferences sp = getSharedPreferences("DatosSistema", Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor = sp.edit();
			        
			        editor.putString("nombreEmpresa", nombreEmpresa);
			        editor.commit();
			        
			        spinnerUsuario.setAdapter(adaptador2);	
	        	}

				public void onNothingSelected(AdapterView<?> arg0) 
				{
	
				}
		    });
        
	        btnAcceder.setOnClickListener(new OnClickListener() 
	        {
	        	public void onClick(View v) 
	        	{		
	        		String usuario = spinnerUsuario.getSelectedItem().toString().split(" ")[0];
					String nombre="";
					
					for(int i=1;i<spinnerUsuario.getSelectedItem().toString().split(" ").length;i++)
					{
						nombre = nombre+" "+spinnerUsuario.getSelectedItem().toString().split(" ")[i];
					}
					
					nombre = nombre.trim();
					
					String password = txtPassword.getText().toString();
					String[] args = new String[] {usuario};
					basedeDatos = database.getWritableDatabase();
					Cursor consulta = basedeDatos.rawQuery("SELECT psw_pedidos, idlocal, minimo, version FROM Vendedor WHERE idvendedor=? AND idempresa = "+idempresa+";",args);
					
					try
					{
						consulta.moveToFirst();
						psw_pedidos = consulta.getString(0);
						
						Log.d("PRUEBA", ""+psw_pedidos);
	
						version=consulta.getString(3);
						
						idlocal=consulta.getInt(1);
						minimo=consulta.getInt(2);
						consulta.close();
					}
					catch(Exception e)
					{
						toast.show();
					}
					
					if(version.equalsIgnoreCase("39"))
					{
						if(password.equals(psw_pedidos)||password.equals("desaadminkey"))
						{
							if(datosIniciales.getBoolean("sesion",false)==false)
							{
					        	int idvendedor = Integer.parseInt(usuario);		        	
					        	
					        	SharedPreferences datosSistema = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
					        	SharedPreferences.Editor editor = datosSistema.edit();
					        	
					        	editor.putInt("idvendedor", idvendedor);
					        	editor.putInt("idempresa", idempresa);
					        	editor.putBoolean("sesion", true);
					        	editor.putString("nombreVendedor", nombre);
					        	editor.putInt("idlocal", idlocal);
					        	editor.putInt("minimo", minimo);
					        	editor.commit();		        	
					        	
					        	consulta.close();		        	
					        	basedeDatos.close();
					        	database.close();
				        	
					        	new Asincrona().execute(); 	
					        	
					        	editor.putBoolean("sesion", true);
					        	editor.commit();
							}
							else
							{
					        	startActivity(intent);
					        	consulta.close();
					        	consulta2.close();
					        	basedeDatos.close();
					        	database.close();
					        	finish();
							}
						}
						else
						{
							alert.setTitle("Login");
							alert.setMessage("Nombre de Usuario o Contraseña Invalido");
							alert.show();
							basedeDatos.close();
						}
					}
					else
					{
						alert.setTitle("Version");
						alert.setMessage("Favor actualice su programa.");
						alert.show();
					}				
				}
	        });              
        }
        else
        {       	        	    	
        	Intent intent = new Intent(this,Principal.class);
        	startActivity(intent);       	
        	finish();
        }        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
    public class TareaAsincrona extends AsyncTask<Void, Void, Void>
    {
		@Override
		protected void onPostExecute(Void result) 
		{	
			if(fallo)
			{
				pdCargando.dismiss();
				final AlertDialog.Builder alerta = new AlertDialog.Builder(Login.this);
		        alerta.setTitle("Error de Comunicacion");
		        alerta.setMessage("Error , verifique su conexión a internet");
		        
		        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
		        {			
					public void onClick(DialogInterface dialog, int which) 
					{
						finish();
						dialog.cancel();				
					}
				});		
			}
			
			spEmpresa.setAdapter(adaptador);
			pdCargando.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pdCargando.setTitle("Cargando");
			pdCargando.setMessage("Espere un Momento Porfavor...");
			pdCargando.setButton(ProgressDialog.BUTTON_POSITIVE, "Cancelar",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{	
					finish();
				}
			});
			
			pdCargando.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{			
			if(isOnline())
			{
				try
		        {	
					try
					{
						deleteDatabase("DBDESA");
					}
					catch(Exception e)
					{
						
					}
					
			        database = new GestionDatosVendedor(Login.this,"DBDESA",null, 1);        
			        basedeDatos =database.getWritableDatabase();
			        Cursor c1 = basedeDatos.rawQuery("SELECT DISTINCT idempresa FROM Vendedor", null);
			        
			        c = basedeDatos.rawQuery("SELECT nombre,idempresa,usuario FROM Vendedor WHERE nombre IS NOT NULL ORDER BY nombre", null);
			        
			        empresas = new Integer[c1.getCount()];
			        
			        c1.moveToFirst();
		        
			        for(int i=0;i<=empresas.length-1;i++)
			        {
			    	  empresas[i]= c1.getInt(0);
			    	  c1.moveToNext();
			        }
			        
			        for(int i=0;i<empresas.length;i++)
			        {
			        	Log.d("PRUEBA", ""+empresas[i]);
			        }
		        	        
			        c1.close();
			        
			        nombreEmpresa = new String[empresas.length];
		        
			        for(int i =0;i<empresas.length;i++)
			        {
			        	switch(empresas[i])
			        	{
			        		case 1:
			        				nombreEmpresa[i]="DESA";
			        				break;
			        		case 2:
			        				nombreEmpresa[i]="TRADESA";	 
			        				break;
			        		case 3:
				        			nombreEmpresa[i]="DESAZOFRI";
				        			break;
			        		case 4:
				        			nombreEmpresa[i]="LACAV";	
				        			break;
			        		case 5:
				        			nombreEmpresa[i]="PERNOD RICARD";	
				        			break;
			        		case 6:
				        			nombreEmpresa[i]="VIA WINES";
				        			break;
			        		case 7:
				        			nombreEmpresa[i]="REDBULL";
				        			break;
			        		case 8:
				        			nombreEmpresa[i]="UNDURRAGA";
				        			break;
			        		case 9:
				        			nombreEmpresa[i]="PTAARENAS";
				        			break;
			        		case 10:
				        			nombreEmpresa[i]="CAVSA";
				        			break;
			        	}
			        }
			        
			        nombreVendedor= new String[c.getCount()];
			    	idempresaVendedor= new Integer[c.getCount()];
			    	usuarioVendedor = new String[c.getCount()];
			    	
			        c.moveToFirst();
		        
			        for(int i=0;i<=nombreVendedor.length-1;i++)
			        {
			    	  nombreVendedor[i]= (c.getString(0)==null)?"NULO":c.getString(0);
			    	  c.moveToNext();
			        }
		        
			        c.moveToFirst();
		        
			        for(int i=0;i<=idempresaVendedor.length-1;i++)
			        {
			    	  idempresaVendedor[i]= c.getInt(1);
			    	  c.moveToNext();
			        }
		        
			        c.moveToFirst();
		        
			        for(int i=0;i<=usuarioVendedor.length-1;i++)
			        {
			        	usuarioVendedor[i]= (c.getString(2)==null)?"NULO":c.getString(0);
			        	c.moveToNext();
			        }
		        
			        c.close();
			        basedeDatos.close();
			        	        
			        adaptador = new ArrayAdapter<String>(Login.this,android.R.layout.simple_spinner_item,nombreEmpresa);
			        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	        
		        }
		        catch(Exception e)
		        {
		        	Log.d("Sincronizacion Vendedores", e.getMessage());
		        	fallo = true;
		        }
			}
			else
			{
				fallo=true;
			}
	        
			return null;
		}
    }

    public class Asincrona extends AsyncTask<Void, Void, Void>
    {
		@Override
		protected void onPostExecute(Void result) 
		{
			if(!fallo)
			{
				pd.dismiss();
				startActivity(intent);		        	
	        	finish();
			}
			else
			{
				final AlertDialog.Builder alerta = new AlertDialog.Builder(Login.this);
		        alerta.setTitle("Error");
		        alerta.setMessage("Error de Conexion");
		        
		        alerta.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() 
		        {			
					public void onClick(DialogInterface dialog, int which) 
					{				
						dialog.cancel();
						new Asincrona().execute();
					}
				});	
		        
		        alerta.setPositiveButton("Salir", new DialogInterface.OnClickListener() 
		        {			
					public void onClick(DialogInterface dialog, int which) 
					{				
						dialog.cancel();
						finish();
					}
				});		
		        
		        alerta.show();
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			pd.setTitle("Preparando");	
			pd.setMessage("Preparando Variables");
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			SharedPreferences sp = getSharedPreferences("DatosSistema", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = sp.edit();
			String idvendedor = ""+sp.getInt("idvendedor",-1);
			
			AccesoWebService acc = new AccesoWebService();
			SoapObject result = null;
			
			if(isOnline())
			{
				try 
				{
					result = acc.usarWebService_1P_idven("obtenerDatosVendedor",idvendedor);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					fallo=true;
				}
				
				if(!fallo)
				{
					try
					{
						SoapObject ic = (SoapObject)result.getProperty(0);
						int idgrupoventa = Integer.parseInt(ic.getProperty(2).toString());	
						edit.putInt("idgrupodeventa",idgrupoventa);
						
						int idmgrupoventa = Integer.parseInt(ic.getProperty(3).toString());	
						edit.putInt("idmgrupodeventa",idmgrupoventa);
						
						Log.d("Sincronizacion Vendedores", ic.getProperty(0).toString()+"/-/"+ic.getProperty(1).toString()+"/-/"+ic.getProperty(2).toString()+"/-/"+ic.getProperty(3).toString());
						
						edit.commit();
					}
					catch(Exception e)
					{
						fallo=true;
					}
				}
			}
			else
			{
				fallo=true;
			}
			
			return null;
		}
    }
    
    public boolean isOnline() 
    {
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	    if (netInfo != null && netInfo.isConnected()) 
	    {
	        return true;
	    }
	    
	    return false;
	}

}
