package com.AccesoServicioWeb._ventas_desa;

import java.io.IOException;

import mdls.ventas_desa.Censos;
import mdls.ventas_desa.Pedido;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;

public class AccesoWebService 
{
	public SoapObject usarWebService_SP(String METHOD_NAME) throws IOException, XmlPullParserException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		
		//envelope.bodyOut=request;
		envelope.dotNet = true;		
		envelope.setOutputSoapObject(request);	
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_SP", ex.getMessage());
			}
		}
		
		return (SoapObject)envelope.getResponse();
	}
	
	public SoapObject usarWebService_1P_idven(String METHOD_NAME,String parametro) throws IOException, XmlPullParserException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";	
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("idvendedor");
		pi.setValue(parametro);
		pi.setType(PropertyInfo.STRING_CLASS);
				
		request.addProperty(pi);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idven", ex.getMessage());
			}
		}
										
		return (SoapObject)envelope.getResponse();			
	}
	
	public SoapObject usarWebService_1P_idemp(String METHOD_NAME,String parametro) throws IOException, XmlPullParserException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";			
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("idempresa");
		pi.setValue(parametro);
		pi.setType(PropertyInfo.STRING_CLASS);
				
		request.addProperty(pi);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}
										
		return (SoapObject)envelope.getResponse();			
	}
	
	public SoapObject usarWebService_2P_idemp_idtvta(String METHOD_NAME,String parametro, int parametro2) throws IOException, XmlPullParserException
	{		
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";		
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("idempresa");
		pi.setValue(parametro);
		pi.setType(PropertyInfo.STRING_CLASS);
		
		PropertyInfo ptpv = new PropertyInfo();
		
		ptpv.setName("idtipoventa");
		ptpv.setValue(parametro2);
		ptpv.setType(PropertyInfo.INTEGER_CLASS);
		
		request.addProperty(pi);
		request.addProperty(ptpv);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}
										
		return (SoapObject)envelope.getResponse();	
	}
	
	public SoapObject usarWebService_2P_idven_idemp(String METHOD_NAME,String parametro,String parametro2) throws IOException, XmlPullParserException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";	
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo idempresaProp = new PropertyInfo();
		PropertyInfo idvendedorProp = new PropertyInfo();
		
		idvendedorProp.setName("idvendedor");
		idvendedorProp.setValue(parametro);
		idvendedorProp.setType(PropertyInfo.STRING_CLASS);
		
		idempresaProp.setName("idempresa");
		idempresaProp.setValue(parametro2);
		idempresaProp.setType(PropertyInfo.STRING_CLASS);
				
		request.addProperty(idvendedorProp);
		request.addProperty(idempresaProp);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}			
									
		return (SoapObject)envelope.getResponse();	
	}

	public SoapObject usarWebService_2p_emp_idven(String METHOD_NAME,String parametro, int parametro2) throws IOException, XmlPullParserException
	{		
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";			
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("empresa");
		pi.setValue(parametro);
		pi.setType(PropertyInfo.STRING_CLASS);
		
		PropertyInfo ptpv = new PropertyInfo();
		
		ptpv.setName("idvendedor");
		ptpv.setValue(parametro2);
		ptpv.setType(PropertyInfo.INTEGER_CLASS);
		
		request.addProperty(pi);
		request.addProperty(ptpv);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}
												
		return (SoapObject)envelope.getResponse();			
	}
	
	public SoapObject usarWebService_3P_idemp_idgpvta_idtvta(String METHOD_NAME,String parametro,String parametro2, int parametro3) throws XmlPullParserException, IOException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";				
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo idgrupoventaProp= new PropertyInfo();
		PropertyInfo idempresaProp = new PropertyInfo();
		PropertyInfo tpvt = new PropertyInfo();
		
		idempresaProp.setName("idempresa");
		idempresaProp.setValue(parametro);
		idempresaProp.setType(PropertyInfo.STRING_CLASS);
		
		idgrupoventaProp.setName("idgrupoventa");
		idgrupoventaProp.setValue(parametro2);
		idgrupoventaProp.setType(PropertyInfo.STRING_CLASS);

		tpvt.setName("idtipoventa");
		tpvt.setValue(parametro3);
		tpvt.setType(PropertyInfo.INTEGER_CLASS);
				
		request.addProperty(idempresaProp);
		request.addProperty(idgrupoventaProp);
		request.addProperty(tpvt);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}
							
		return (SoapObject)envelope.getResponse();			
	}
	
	public SoapObject usarWebService_3P_idemp_idmgvta_idtvta(String METHOD_NAME,String parametro,String parametro2, int parametro3) throws XmlPullParserException, IOException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";		
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo idgrupoventaProp= new PropertyInfo();
		PropertyInfo idempresaProp = new PropertyInfo();
		PropertyInfo tpvt = new PropertyInfo();
		
		idempresaProp.setName("idempresa");
		idempresaProp.setValue(parametro);
		idempresaProp.setType(PropertyInfo.STRING_CLASS);
		
		idgrupoventaProp.setName("idmgrupoventa");
		idgrupoventaProp.setValue(parametro2);
		idgrupoventaProp.setType(PropertyInfo.STRING_CLASS);
		
		tpvt.setName("idtipoventa");
		tpvt.setValue(parametro3);
		tpvt.setType(PropertyInfo.INTEGER_CLASS);
				
		request.addProperty(idempresaProp);
		request.addProperty(idgrupoventaProp);
		request.addProperty(tpvt);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}
							
		return (SoapObject)envelope.getResponse();			
	}
	
	public SoapObject usarWebService_3P_idven_idemp_idtvta(String METHOD_NAME,String parametro,String parametro2, int parametro3) throws IOException, XmlPullParserException
	{
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";		
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo idempresaProp = new PropertyInfo();
		PropertyInfo idvendedorProp = new PropertyInfo();
		PropertyInfo tpvt = new PropertyInfo();
		
		idvendedorProp.setName("idvendedor");
		idvendedorProp.setValue(parametro);
		idvendedorProp.setType(PropertyInfo.STRING_CLASS);
		
		idempresaProp.setName("idempresa");
		idempresaProp.setValue(parametro2);
		idempresaProp.setType(PropertyInfo.STRING_CLASS);
		
		tpvt.setName("idtipoventa");
		tpvt.setValue(parametro3);
		tpvt.setType(PropertyInfo.INTEGER_CLASS);
				
		request.addProperty(idvendedorProp);
		request.addProperty(idempresaProp);
		request.addProperty(tpvt);
		
		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}			
								
		return (SoapObject)envelope.getResponse();		
	}
	
	public Boolean insertarCliente(	String Empresa, String CodLegal, String RazonSocial, String Sigla, String Canal, String Vendedor,
							        String CondPago, String Region, String Comuna, String Telefono, String eMail, String NombreEncargado,
							        String Direccion, String LimiteCredito, String Banco, String idempresa, String idvendedor, String luncob,
							        String marcob, String miecob, String juecob, String viecob, String sabcob, String semana1cob,
							        String semana2cob, String semana3cob, String semana4cob, String soconsumo) 
	throws IOException, XmlPullParserException
	{
		String METHOD_NAME="insertarCliente";
		String NAMESPACE ="WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";			
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		request.addProperty("Empresa",Empresa);
		request.addProperty("CodLegal",CodLegal);
		request.addProperty("RazonSocial",RazonSocial);
		request.addProperty("Sigla",Sigla);
		request.addProperty("Canal",Canal);
		request.addProperty("Vendedor",Vendedor);
		request.addProperty("CondPago",CondPago);
		request.addProperty("Region",Region);
		request.addProperty("Direccion",Direccion);
		request.addProperty("Comuna",Comuna);
		request.addProperty("Telefono",Telefono);
		request.addProperty("eMail",eMail);
		request.addProperty("NombreEncargado",NombreEncargado);
		request.addProperty("LimiteCredito",LimiteCredito);
		request.addProperty("Banco",Banco);
		request.addProperty("idempresa",idempresa);		
		request.addProperty("idvendedor",idvendedor);
		request.addProperty("luncob",luncob);
		request.addProperty("marcob",marcob);
		request.addProperty("miecob",miecob);
		request.addProperty("juecob",juecob);
		request.addProperty("viecob",viecob);
		request.addProperty("sabcob",sabcob);		
		request.addProperty("semana1cob",semana1cob);
		request.addProperty("semana2cob",semana2cob);
		request.addProperty("semana3cob",semana3cob);
		request.addProperty("semana4cob",semana4cob);
		request.addProperty("semana4cob",semana4cob);
		request.addProperty("Soconsumo",soconsumo);

		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);			
		envelope.dotNet = true;		
		envelope.setOutputSoapObject(request);		
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);
			transporte.debug=true;	
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
				transporte.debug=true;	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}	

		SoapPrimitive result;
		
		result = (SoapPrimitive)envelope.getResponse();			
			
		return Boolean.valueOf(result.toString());			
	}
	
	public Boolean insertarPedido(Pedido p,int idempresa,int idvendedor) throws XmlPullParserException, IOException
	{
		String NAMESPACE = "WS.DESA.cl";
		String METHOD_NAME ="insertarPedido29";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";		
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;	
		
		SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
		
		request.addProperty("vendedor",p.getVendedor().toString());
		request.addProperty("fecha",p.getFecha().toString());
		request.addProperty("hora",p.getHora().toString());
		request.addProperty("cliente",p.getCliente().toString());
		request.addProperty("nota",p.getNota().toString());
		request.addProperty("oc",p.getOc().toString());
		request.addProperty("producto01",p.getProducto01().toString());
		request.addProperty("cantidad01",p.getCantidad01().toString());
		request.addProperty("descuento01",p.getDescuento01().toString());
		request.addProperty("producto02",p.getProducto02().toString());
		request.addProperty("cantidad02",p.getCantidad02().toString());
		request.addProperty("descuento02",p.getDescuento02().toString());
		request.addProperty("producto03",p.getProducto03().toString());
		request.addProperty("cantidad03",p.getCantidad03().toString());
		request.addProperty("descuento03",p.getDescuento03().toString());
		request.addProperty("producto04",p.getProducto04().toString());
		request.addProperty("cantidad04",p.getCantidad04().toString());
		request.addProperty("descuento04",p.getDescuento04().toString());
		request.addProperty("producto05",p.getProducto05().toString());
		request.addProperty("cantidad05",p.getCantidad05().toString());
		request.addProperty("descuento05",p.getDescuento05().toString());
		request.addProperty("producto06",p.getProducto06().toString());
		request.addProperty("cantidad06",p.getCantidad06().toString());
		request.addProperty("descuento06",p.getDescuento06().toString());
		request.addProperty("producto07",p.getProducto07().toString());
		request.addProperty("cantidad07",p.getCantidad07().toString());
		request.addProperty("descuento07",p.getDescuento07().toString());
		request.addProperty("producto08",p.getProducto08().toString());
		request.addProperty("cantidad08",p.getCantidad08().toString());
		request.addProperty("descuento08",p.getDescuento08().toString());
		request.addProperty("producto09",p.getProducto09().toString());
		request.addProperty("cantidad09",p.getCantidad09().toString());
		request.addProperty("descuento09",p.getDescuento09().toString());
		request.addProperty("producto10",p.getProducto10().toString());
		request.addProperty("cantidad10",p.getCantidad10().toString());
		request.addProperty("descuento10",p.getDescuento10().toString());
		request.addProperty("producto11",p.getProducto11().toString());
		request.addProperty("cantidad11",p.getCantidad11().toString());
		request.addProperty("descuento11",p.getDescuento11().toString());
		request.addProperty("producto12",p.getProducto12().toString());
		request.addProperty("cantidad12",p.getCantidad12().toString());
		request.addProperty("descuento12",p.getDescuento12().toString());
		request.addProperty("OBS",p.getOBS1().toString());
		request.addProperty("estado",p.getEstado().toString());
		request.addProperty("opcion",p.getOpcion().toString());
		request.addProperty("fechaentrega",p.getFechaentrega().toString().toString());
		request.addProperty("OBS2",p.getOBS21().toString());
		request.addProperty("empresa",p.getEmpresa().toString());
		request.addProperty("notamovil",p.getNotamovil().toString());
		request.addProperty("gps_latitud",p.getGPS_latitud());
		request.addProperty("gps_longitud",p.getGPS_longitud());
		request.addProperty("gps_tiempo",p.getGPS_tiempo());
		request.addProperty("idempresa",idempresa);
		request.addProperty("idvendedor",idvendedor);
		request.addProperty("idbodega",p.getBodega().toString());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);			
		envelope.dotNet = true;		
		envelope.setOutputSoapObject(request);		
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);
			transporte.debug=true;	
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
				transporte.debug=true;	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}	
		
		SoapPrimitive result;
			
		result = (SoapPrimitive)envelope.getResponse();
			
		return Boolean.valueOf(result.toString());			
	}
	
	public SoapObject usarWebServiceClienteDet(String METHOD_NAME,String parametro) throws IOException, XmlPullParserException
	{		
		String NAMESPACE = "WS.DESA.cl";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";		
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("nombre");
		pi.setValue(parametro);
		pi.setType(PropertyInfo.STRING_CLASS);
				
		request.addProperty(pi);

		Log.d("COMPROBANDO VALOR","" );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//envelope.bodyOut=request;
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);			
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);						
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE usarWebService_1P_idemp", ex.getMessage());
			}
		}	
											
		return (SoapObject)envelope.getResponse();			
	}
	
	public Boolean insertarCensos(Censos c,String idempresa,String idvendedor) throws XmlPullParserException, IOException
	{
		String NAMESPACE = "WS.DESA.cl";
		String METHOD_NAME ="insertarCenso29";
		String URL1="http://201.238.217.22/webserviceVentaSAP/Service.asmx";
		String URL2="http://pda.desa.cl/webserviceVentaSAP/Service.asmx";		
		String SOAP_ACTION = "WS.DESA.cl/"+METHOD_NAME;	
		
		SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
		
		request.addProperty("idempresa",idempresa.toString());
		request.addProperty("idactivacion",c.getIdactivacion().toString());
		request.addProperty("idvendedor",idvendedor);
		request.addProperty("ctacte",c.getCtacte().toString());
		request.addProperty("fecha",c.getFecha().toString());
		request.addProperty("calibre",c.getCalibre().toString());
		request.addProperty("item",c.getItem().toString());
		request.addProperty("marca",c.getMarca().toString());
		request.addProperty("respuesta",c.getRespuesta().toString());
		request.addProperty("motivo",c.getMotivo().toString());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);			
		envelope.dotNet = true;		
		envelope.setOutputSoapObject(request);		
		
		try
		{
			HttpTransportSE transporte = new HttpTransportSE(URL1);
			transporte.call(SOAP_ACTION, envelope);
			transporte.debug=true;	
		}	
		catch (Exception e)
		{
			try
			{
				HttpTransportSE transporte = new HttpTransportSE(URL2);
				transporte.call(SOAP_ACTION, envelope);	
				transporte.debug=true;	
			}
			catch (Exception ex)
			{
				Log.d("ERROR WEB SERVICE insertarCensos", ex.getMessage());
			}
		}	
		
		SoapPrimitive result;
			
		result = (SoapPrimitive)envelope.getResponse();
			
		return Boolean.valueOf(result.toString());			
	}
}
