package db.ventas_desa;

import mdls.ventas_desa.Vendedor;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestionDatosVendedor extends SQLiteOpenHelper 
{
	private Vendedor[] listadeVendedores;
	
	private static final String crearTablaVendedores = " CREATE TABLE IF NOT EXISTS Vendedor (codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
													   " idvendedor INTEGER , idempresa INTEGER , nombre TEXT , idruta INTEGER , psw_pedidos TEXT, " +
													   " psw_avance TEXT ,usuario TEXT, idlocal NUMERIC, minimo NUMERIC, version TEXT);";			

	public GestionDatosVendedor(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		Boolean fallo=true;
		db.execSQL("DROP TABLE IF EXISTS Vendedor");
		db.execSQL(crearTablaVendedores);	
		
		final String NAMESPACE = "WS.DESA.cl";
		final String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		final String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";
		final String METHOD_NAME = "obtenerVendedores";
		final String SOAP_ACTION = "WS.DESA.cl/obtenerVendedores29";			
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);				
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);				
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		while(fallo==true)
		{
			try 
			{
				HttpTransportSE transporte = new HttpTransportSE(URL1);
				transporte.call(SOAP_ACTION, envelope);
				fallo=false;
			} 
			catch (Exception e) 
			{				
				Log.d("TRANSPORTE", (e.getMessage()==null)?"Mensaje Nulo1":e.getMessage());
				
				try
				{
					HttpTransportSE transporte = new HttpTransportSE(URL2);
					transporte.call(SOAP_ACTION, envelope);
					fallo=false;
				}
				catch (Exception ex)
				{
					Log.d("TRANSPORTE", (ex.getMessage()==null)?"Mensaje Nulo2":ex.getMessage());
					fallo=true;
				}
			}
	
			try
			{
				SoapObject resultado_xml = (SoapObject)envelope.getResponse();
				listadeVendedores = new Vendedor[resultado_xml.getPropertyCount()];
					
				for(int i=0; i<listadeVendedores.length;i++)
				{
					SoapObject ic = (SoapObject)resultado_xml.getProperty(i);
						
					Vendedor vnd = new Vendedor();
					
					vnd.setIdvendedor(Integer.parseInt(ic.getProperty(0).toString()));
					vnd.setIdempresa(Integer.parseInt(ic.getProperty(1).toString()));
					vnd.setNombre(ic.getProperty(2).toString());
					vnd.setIdruta(Integer.parseInt(ic.getProperty(3).toString()));
					vnd.setPsw_pedidos(ic.getProperty(4).toString());
					vnd.setPsw_avance(ic.getProperty(5).toString());
					vnd.setUsuario(ic.getProperty(6).toString());	
					vnd.setIdlocal(Integer.parseInt(ic.getProperty(7).toString()));
					vnd.setMinimo(Integer.parseInt(ic.getProperty(8).toString()));
					vnd.setVersion(ic.getProperty(9).toString());
					listadeVendedores[i]= vnd;	
				}				
						
				fallo=false;
					
			}
			catch (Exception e)
			{				
				Log.d("PARSING",(e.getMessage()==null)?"Mensaje Nulo3":e.getMessage());
				fallo=true;
			}
				
			try
			{
				for(int i=0;i<=listadeVendedores.length-1;i++)
				{	
					db.execSQL("INSERT INTO Vendedor (idvendedor,idempresa,nombre,idruta,psw_pedidos,psw_avance,usuario,idlocal, minimo, version) "+
					"VALUES ("+listadeVendedores[i].getIdvendedor()+","+listadeVendedores[i].getIdempresa()+" , '"+listadeVendedores[i].getNombre()+"' , "+
					listadeVendedores[i].getIdruta()+" , '"+listadeVendedores[i].getPsw_pedidos()+"' , '"+listadeVendedores[i].getPsw_avance()+"' , '"+
					listadeVendedores[i].getUsuario()+"',"+listadeVendedores[i].getIdlocal()+","+listadeVendedores[i].getMinimo()+",'"+listadeVendedores[i].getVersion()+"')");
				}
				
				fallo=false;
			}
			catch(Exception e)
			{
				Log.d("DATABASE",(e.getMessage()==null)?"Mensaje Nulo4":e.getCause().toString());
				fallo=true;
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS Vendedor");
		db.execSQL(crearTablaVendedores);
	}

}
