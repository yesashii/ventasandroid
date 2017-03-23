package dsm.ventas_desa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import mdls.ventas_desa.Banco;
import mdls.ventas_desa.Bodega;
import mdls.ventas_desa.CANPROMDESC;
import mdls.ventas_desa.CENSO_CALIBRE;
import mdls.ventas_desa.CENSO_CANAL;
import mdls.ventas_desa.CENSO_CTACTE;
import mdls.ventas_desa.CENSO_DETALLE;
import mdls.ventas_desa.CENSO_ITEM;
import mdls.ventas_desa.CENSO_MARCA;
import mdls.ventas_desa.CENSO_MOTIVO;
import mdls.ventas_desa.CENSO_MOT_REL;
import mdls.ventas_desa.CENSO_VALIDACION;
import mdls.ventas_desa.CENSO_VAL_REL;
import mdls.ventas_desa.CLIPROMDESC;
import mdls.ventas_desa.Canal;
import mdls.ventas_desa.Ciudad;
import mdls.ventas_desa.CliDet;
import mdls.ventas_desa.Cliente;
import mdls.ventas_desa.Comuna;
import mdls.ventas_desa.CondicionPago;
import mdls.ventas_desa.ESCDESC;
import mdls.ventas_desa.Familia;
import mdls.ventas_desa.Flete;
import mdls.ventas_desa.Fletespag;
import mdls.ventas_desa.GRUPROMDESC;
import mdls.ventas_desa.GVTPROMDESC;
import mdls.ventas_desa.MGVTPROMDESC;
import mdls.ventas_desa.ListaPrecio;
import mdls.ventas_desa.MAXMCANDESC;
import mdls.ventas_desa.MAXPAGDESC;
import mdls.ventas_desa.MAXSKUDESC;
import mdls.ventas_desa.MEGPROMDESC;
import mdls.ventas_desa.Marca;
import mdls.ventas_desa.SUCPROMDESC;
import mdls.ventas_desa.PAGPROMDESC;
import mdls.ventas_desa.Producto;
import mdls.ventas_desa.Region;
import mdls.ventas_desa.Ruta;
import mdls.ventas_desa.SKUCanal;
import mdls.ventas_desa.SubFamilia;
import mdls.ventas_desa.VENPROMDESC;
import mdls.ventas_desa.SOConsumo;
import org.ksoap2.serialization.SoapObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.AccesoServicioWeb._ventas_desa.AccesoWebService;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;

public class Sincronizacion extends Activity 
{	
	private ImageView btnSincronizar;
	private TextView lblActualizacion;
	SimpleDateFormat sf;
	Calendar calendario;
	private Canal[] listadeCanales;
	private Cliente[] listadeClientes;	
	private Producto[] listadeProductos;
	private Banco[] listadeBancos;
	private Ciudad[] listadeCiudades;
	private Comuna[] listadeComunas;
	private Region[] listadeRegiones;
	private Ruta[] listadeRutas;
	private Familia[] listadeFamilias;
	private SubFamilia[] listadeSubFamilias;
	private ListaPrecio[] listadeprecios;
	private CLIPROMDESC[] listaCLIPROMDESC;
	private SUCPROMDESC[] listaSUCPROMDESC;
	private PAGPROMDESC[] listaPAGPROMDESC;
	private GRUPROMDESC[] listaGRUPROMDESC;
	private CANPROMDESC[] listaCANPROMDESC;	
	private Flete[] listaFlete;
	private ESCDESC[] listaESCDESC;
	private GVTPROMDESC[] listaGVTPROMDESC;
	private MGVTPROMDESC[] listaMGVTPROMDESC;
	private MEGPROMDESC[] listaMEGPROMDESC;
	private VENPROMDESC[] listaVENPROMDESC;
	private MAXPAGDESC[] listaMAXPAGDESC;
	private MAXSKUDESC[] listaMAXSKUDESC;
	private MAXMCANDESC[] listaMAXMCANDESC;
	private Marca[] listadeMarcas;
	private CondicionPago[] listadeCondicionesdePago;
	private CliDet[] listaCliDet;
	private Bodega[] listaBodega;
	private Fletespag[] listaFletespag;
	private SKUCanal[] listadeSKUCanales;
	private SOConsumo[] listadeSOConsumo;
	private CENSO_CALIBRE[] listaCensoCalibre;
	private CENSO_MARCA[] listaCensoMarca;
	private CENSO_ITEM[] listaCensoItem;
	private CENSO_MOTIVO[] listaCensoMotivo;
	private CENSO_VALIDACION[] listaCensoValidacion;
	private CENSO_CTACTE[] listaCensoCtacte;
	private CENSO_DETALLE[] listaCensoDetalle;
	private CENSO_MOT_REL[] listaCensoMotivoRel;
	private CENSO_VAL_REL[] listaCensoValRel;
	
	boolean cancelado = false;
	
	String error;
		
	Thread thread;
	Toast toast;
	SoapObject ic;	
	Cliente clt;
	public ProgressBar pbar;
	ProgressDialog pdialog;
	TextView lblSincro;
	SoapObject resultado = null;
	DatosAplicacion dadb;
	SQLiteDatabase db;
	Handler mhandler;	
	AccesoWebService access;
	SharedPreferences sp;
	SharedPreferences lastSincro;
	int idvendedor;
	int idempresa;
	int idgrupoventa;
	int idmgrupoventa;
	
	String stringidempresa;
	String stringidvendedor;
	String stringNombrevendedor;
	ImageView btnReset;
	TableLayout tabla;
	TextView tvsClientes,tvsProductos,tvsCanales,tvsComunas,tvsBancos,tvsRegiones,tvsCiudades,tvsCdepago,
	tvsRutas,tvsFamilia,tvsSubFamilia,tvsMarcas,tvsListaPrecio,tvsCLIPROMDESC,tvsPAGPROMDESC,tvsSUCPROMDESC,tvsGRUPROMDESC,
	tvsCANPROMDESC,tvsFlete,tvsESCDESC,tvsGVTPROMDESC,tvsMGVTPROMDESC,tvsMEGPROMDESC,tvsVPD,tvsMAXPAGDESC,
	tvsMAXSKUDESC,tvsMAXMCANDESC, tvsCliDet,tvsBodega,tvsFletespag, tvsSKUCanal, tvsSOConsumo, tvsCenso_Calibre, tvsCenso_Marca,
	tvsCenso_Item, tvsCenso_Motivo, tvsCenso_Validacion, tvsCenso_Ctacte, tvsCenso_Detalle, tvsCenso_MotRel, tvsCenso_ValRel;
	TextView resumen,click;
	Intent intent;
	AlertDialog.Builder finalizado;
	AlertDialog.Builder alerta;
	Boolean pressed = false;
	private String stringGrupoVenta, stringMGrupoVenta;
	int tipoventa=0;
	
	public Sincronizacion()
	{
	}	

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       
        lblSincro = (TextView)findViewById(R.id.lblSincroBar);
        btnSincronizar = (ImageView)findViewById(R.id.btnSincronizar);
        lblActualizacion = (TextView)findViewById(R.id.lblActualizacion);
        btnReset = (ImageView)findViewById(R.id.btnReset);
        
        tvsClientes = (TextView)findViewById(R.id.tvsCliente);
        tvsProductos = (TextView)findViewById(R.id.tvsProductos);
        tvsCanales = (TextView)findViewById(R.id.tvsCanales);
        tvsComunas =(TextView)findViewById(R.id.tvsComunas);
        tvsBancos = (TextView)findViewById(R.id.tvsBancos);
        tvsRegiones = (TextView)findViewById(R.id.tvsRegiones);
        tvsCiudades = (TextView)findViewById(R.id.tvsCiudades);
        tvsCdepago = (TextView)findViewById(R.id.tvsCdePago);
        tvsRutas = (TextView)findViewById(R.id.tvsRutas);
        tvsFamilia = (TextView)findViewById(R.id.tvsFamilia);
        tvsSubFamilia = (TextView)findViewById(R.id.tvsSubFamilia);
        tvsMarcas = (TextView)findViewById(R.id.tvsMarcas);
        tvsListaPrecio = (TextView)findViewById(R.id.tvsListaPrecio);
        tvsCLIPROMDESC = (TextView)findViewById(R.id.tvsCLIPROMDESC);
        tvsSUCPROMDESC = (TextView)findViewById(R.id.tvsSUCPROMDESC);
        tvsPAGPROMDESC = (TextView)findViewById(R.id.tvsPAGPROMDESC);
        tvsGRUPROMDESC = (TextView)findViewById(R.id.tvsGRUPROMDESC);
        tvsCANPROMDESC= (TextView)findViewById(R.id.tvsCANPROMDESC);
        tvsFlete = (TextView)findViewById(R.id.tvsFlete);
        tvsESCDESC = (TextView)findViewById(R.id.tvsESCDESC);
        tvsGVTPROMDESC = (TextView)findViewById(R.id.tvsGVTPROMDESC);
        tvsMGVTPROMDESC=(TextView)findViewById(R.id.tvsMGVT);
        tvsMEGPROMDESC = (TextView)findViewById(R.id.tvsMEGPROMDESC);
        tvsVPD =(TextView)findViewById(R.id.tvsVPD);
        tvsMAXPAGDESC=(TextView)findViewById(R.id.tvsMAXPAGDESC);
        tvsMAXSKUDESC=(TextView)findViewById(R.id.tvsMAXSKUDESC);
        tvsMAXMCANDESC=(TextView)findViewById(R.id.tvsMAXMCANDESC);
        tvsCliDet=(TextView)findViewById(R.id.tvsCliDet);
        tvsBodega=(TextView)findViewById(R.id.tvsBodega);
        tvsFletespag=(TextView)findViewById(R.id.tvsFletespag);
        tvsSKUCanal=(TextView)findViewById(R.id.tvsSKUCanal);
        tvsSOConsumo=(TextView)findViewById(R.id.tvsSOConsumo);
        tvsCenso_Calibre=(TextView)findViewById(R.id.tvsCENSO_CALIBRE);
        tvsCenso_Marca=(TextView)findViewById(R.id.tvsCENSO_MARCA);
        tvsCenso_Item=(TextView)findViewById(R.id.tvsCENSO_ITEMS);
        tvsCenso_Motivo=(TextView)findViewById(R.id.tvsCENSO_MOTIVO);
        tvsCenso_Validacion=(TextView)findViewById(R.id.tvsCENSO_VALIDACION);
        tvsCenso_Ctacte=(TextView)findViewById(R.id.tvsCENSO_CTACTE);
        tvsCenso_Detalle=(TextView)findViewById(R.id.tvsCENSO_DETALLE);
        tvsCenso_MotRel=(TextView)findViewById(R.id.tvsCENSO_REL_MOTIVO);
        tvsCenso_ValRel=(TextView)findViewById(R.id.tvsCENSO_REL_VAL);
        resumen= (TextView)findViewById(R.id.tvSincResumen);
        click = (TextView)findViewById(R.id.tvBtnSincVerResumen);
        
        tabla = (TableLayout)findViewById(R.id.tblSincro);
        
        toast = new Toast(Sincronizacion.this); 
        sf= new SimpleDateFormat("dd/MM/yyyy");
        calendario = Calendar.getInstance();        
        mhandler = new Handler();        
        access = new AccesoWebService();
        sp = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE);
        lastSincro = getSharedPreferences("DatosSincronizacion",Context.MODE_PRIVATE);        
        idvendedor = sp.getInt("idvendedor", -1);
        idempresa = sp.getInt("idempresa", -1);
        idgrupoventa=sp.getInt("idgrupodeventa", -1);
        idmgrupoventa=sp.getInt("idmgrupodeventa", -1);
        
        stringNombrevendedor=sp.getString("nombreVendedor", "null");
        
        stringidvendedor = ""+idvendedor;
        stringidempresa = ""+idempresa;
        stringGrupoVenta = ""+idgrupoventa;
        stringMGrupoVenta=""+idmgrupoventa;
        
        Log.d("stringMGrupoVenta: ",stringMGrupoVenta+"StringGrupoventa="+stringGrupoVenta);
        
        if(stringMGrupoVenta.equals("25"))
        {
        	tipoventa=2;
        }
        else
        {
        	tipoventa=1;
        }
        
        Log.d("tipoventa: ",""+tipoventa);
        
        lblActualizacion.setText(sp.getString("UltimaActualizacion","No DATA"));             
        dadb = new DatosAplicacion(Sincronizacion.this,"PrincipalDB", null, 1);
        intent = new Intent(Sincronizacion.this,Principal.class);
        
        //*********OCULTANDO RESUMEN DE SINCRONIZACION******************//
        
        resumen.setVisibility(View.GONE);
        tabla.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);        
        
        
        //********************FIN************************************//    	
        
        click.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				if(pressed)
				{
					resumen.setVisibility(View.GONE);
			        tabla.setVisibility(View.GONE);
			        btnReset.setVisibility(View.GONE);
			        pressed = false;
				}
				else
				{
					resumen.setVisibility(View.VISIBLE);
			        tabla.setVisibility(View.VISIBLE);
			        btnReset.setVisibility(View.VISIBLE);
			        pressed=true;
				}
			}
		});
        
        pdialog = new ProgressDialog(Sincronizacion.this);
        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdialog.setCancelable(false);        
        
        alerta = new AlertDialog.Builder(Sincronizacion.this);
        alerta.setTitle("Error de Sincronizacion");
        
        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
        
        finalizado = new AlertDialog.Builder(Sincronizacion.this);
        finalizado.setTitle("Sincronización");
        finalizado.setMessage("Sincronización finalizada con exito.");
        
        finalizado.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {			
			public void onClick(DialogInterface dialog, int which) 
			{
				startActivity(intent);
				dialog.cancel();
				finish();
			}
		});	

       setearUI();
        
        btnSincronizar.setOnClickListener(new OnClickListener() 
        {			
			public void onClick(View v) 
			{
				setearUI();
				SharedPreferences.Editor editor = sp.edit();
				String fecha = sf.format(calendario.getTime());				
				editor.putString("PressedDay",fecha);
				editor.commit();				
				
				new EjecutarSincronizacion().execute();				
								
				lblActualizacion.setText(sp.getString("UltimaActualizacion","No DATA"));				
			}
		});
        
        btnReset.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				final AlertDialog.Builder alerta = new AlertDialog.Builder(Sincronizacion.this);
		        alerta.setTitle("Reset de Sincronización");
		        alerta.setMessage("Si presiona seguir reseteara la sincronización Global");
		        
		        alerta.setPositiveButton("Seguir", new DialogInterface.OnClickListener() 
		        {			
					public void onClick(DialogInterface dialog, int which) 
					{				
						dialog.cancel();	
						
						SharedPreferences.Editor sincroEditor = lastSincro.edit();
						SharedPreferences.Editor editor = sp.edit();
						
						sincroEditor.putBoolean("Clientes", false);
						sincroEditor.putBoolean("Productos", false);
						sincroEditor.putBoolean("Canales", false);
						sincroEditor.putBoolean("Comunas", false);
						sincroEditor.putBoolean("Bancos", false);
						sincroEditor.putBoolean("Regiones", false);
						sincroEditor.putBoolean("Ciudades", false);
						sincroEditor.putBoolean("CdePago", false);
						sincroEditor.putBoolean("Rutas", false);
						sincroEditor.putBoolean("Familias",false);
						sincroEditor.putBoolean("SubFamilias",false);
						sincroEditor.putBoolean("Marcas",false);
						sincroEditor.putBoolean("ListadePrecio",false);
						sincroEditor.putBoolean("CLIPROMDESC",false);
						sincroEditor.putBoolean("SUCPROMDESC",false);
						sincroEditor.putBoolean("PAGPROMDESC",false);
						sincroEditor.putBoolean("GRUPROMDESC",false);
						sincroEditor.putBoolean("CANPROMDESC",false);
						sincroEditor.putBoolean("Flete",false);				
						sincroEditor.putBoolean("ESCDESC",false);
						sincroEditor.putBoolean("GVTPROMDESC",false);
						sincroEditor.putBoolean("MGVTPROMDESC",false);
						sincroEditor.putBoolean("MEGPROMDESC",false);
						sincroEditor.putBoolean("VENPROMDESC",false);
						sincroEditor.putBoolean("MAXPAGDESC",false);
						sincroEditor.putBoolean("MAXSKUDESC",false);
						sincroEditor.putBoolean("MAXMCANDESC",false);
						sincroEditor.putBoolean("CliDet", false);
						sincroEditor.putBoolean("Bodega", false);
						sincroEditor.putBoolean("Fletespag", false);
						sincroEditor.putBoolean("SKUCanal", false);
						sincroEditor.putBoolean("SOCONSUMO", false);
						editor.putBoolean("FTIME",false);
						sincroEditor.putBoolean("CENSO_CALIBRE", false);
						sincroEditor.putBoolean("CENSO_MARCA", false);
						sincroEditor.putBoolean("CENSO_ITEM", false);
						sincroEditor.putBoolean("CENSO_MOTIVO", false);
						sincroEditor.putBoolean("CENSO_VALIDACION", false);
						sincroEditor.putBoolean("CENSO_CTACTE", false);
						sincroEditor.putBoolean("CENSO_DETALLE", false);
						sincroEditor.putBoolean("CENSO_MOT_REL", false);
						sincroEditor.putBoolean("CENSO_VAL_REL", false);
						editor.putString("UltimaActualizacion","NODATA");
						editor.commit();
						sincroEditor.commit();
						
						setearUI();
					}
				});	
		        
		        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
		        {
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();	
					}
				});
		        
		        alerta.show();
			}
		});        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_sincronizacion, menu);
        return true;
    }    

    public class EjecutarSincronizacion extends AsyncTask<Void, Integer, Void>
    {
		@Override
		protected Void doInBackground(Void... params) 
		{
			cancelado = false;
	
//******************************************BORRAR ARCHIVOS ANTIGUOS DB*****************************************************************
			
			SQLiteDatabase data;
			DatosInsercion dadi = new DatosInsercion(Sincronizacion.this,"InsertarDB", null, 1);
			data=dadi.getWritableDatabase();
			data.execSQL("DELETE FROM PEDIDOS");
			data.close();			
			SharedPreferences.Editor sincroEditor = lastSincro.edit();
			db = dadb.getWritableDatabase();

//******************************************FIN BORRAR ARCHIVOS ANTIGUOS DB*************************************************************
			
			if(isOnline())
			{

//******************************************INICIO DE CARGA TABLA CLIENTE***************************************************************
				
				if(lastSincro.getBoolean("Clientes", false)==false&cancelado==false)
				{
					publishProgress(0,-1,-1);
				
					try
					{
						resultado = null;
						resultado = access.usarWebService_2P_idven_idemp("obtenerClientes", stringidvendedor, stringidempresa);
					}
					catch(Exception e)
					{
						error="SYNC.CL1";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					try
					{
						publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
						listadeClientes = new Cliente[resultado.getPropertyCount()];
						Log.d("CONTADOR",""+resultado.getPropertyCount());
					}
					catch(Exception e)
					{					 
						error="SYNC.CL2";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
			 
					try
					{		
						for(int i=0;i<listadeClientes.length;i++)
						{							
							ic =(SoapObject)resultado.getProperty(i);	
						
							clt = new Cliente();
						
							clt.setIdempresa(Integer.parseInt(ic.getProperty(0).toString()));
							clt.setIdctacte(ic.getProperty(1).toString());
							clt.setCodlegal(ic.getProperty(2).toString());
							clt.setNombre(ic.getProperty(3).toString());
							clt.setSigla(ic.getProperty(4).toString());
							clt.setIdejecutivo(Integer.parseInt(ic.getProperty(5).toString()));
							clt.setIdcondpago(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(6).toString())));
							clt.setLimitecredito(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(7).toString())));
							clt.setTelefono(ic.getProperty(8).toString());
							clt.setCorreo(ic.getProperty(9).toString());
							clt.setNombreencargado(ic.getProperty(10).toString());
							clt.setDireccion(ic.getProperty(11).toString());
							clt.setIdlisprecio(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(12).toString())));
							clt.setIdcanal(ic.getProperty(13).toString());
							clt.setDescuento_base(ic.getProperty(14).toString());
							clt.setMonto_vencido(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(15).toString())));
							clt.setMonto_avencer(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(16).toString())));
							clt.setMonto_chcartera(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(17).toString())));
							clt.setTipo_factor(ic.getProperty(18).toString());
							clt.setIdflete(ic.getProperty(19).toString());
							clt.setIdvendedor(Integer.parseInt(ic.getProperty(20).toString()));	
							clt.setSoconsumo(ic.getProperty(21).toString());
							
							publishProgress(-1,-1,i+1);
						
							listadeClientes[i]=clt;			
						}	
					}
					catch(Exception e)
					{	
						 error="SYNC.CL3";
						 cancelado=true;
						 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
		
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM Clientes");
					}
					catch(Exception e)
					{
						 error="SYNC.CL4";
						 cancelado=true;
						 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					if (!cancelado) 
					{
						try 
						{	
							for (int i = 0; i < listadeClientes.length; i++)
							{
								db.execSQL("INSERT INTO Clientes (idempresa,idctacte,codlegal,nombre,sigla,"
										+ "idejecutivo,idcondpago,limitecredito,telefono,correo,nombreencargado,"
										+ "direccion,idlisprecio,idcanal,descuento_base,monto_vencido,monto_avencer,monto_chcartera,"
										+ "tipo_factor,idflete,idvendedor, soconsumo) "
										+ "VALUES ("
										+ listadeClientes[i].getIdempresa()
										+ ",'"
										+ listadeClientes[i].getIdctacte()
										+ "','"
										+ listadeClientes[i].getCodlegal()
										+ "'"
										+ ",'"
										+ listadeClientes[i].getNombre()
										+ "','"
										+ listadeClientes[i].getSigla()
										+ "','"
										+ listadeClientes[i].getIdejecutivo()
										+ "',"
										+ listadeClientes[i].getIdcondpago()
										+ ","
										+ listadeClientes[i].getLimitecredito()
										+ ",'"
										+ listadeClientes[i].getTelefono()
										+ "','"
										+ listadeClientes[i].getCorreo()
										+ "','"
										+ listadeClientes[i].getNombreencargado()
										+ "','"
										+ listadeClientes[i].getDireccion()
										+ "',"
										+ listadeClientes[i].getIdlisprecio()
										+ ",'"
										+ listadeClientes[i].getIdcanal()
										+ "','"
										+ listadeClientes[i].getDescuento_base()
										+ "',"
										+ listadeClientes[i].getMonto_vencido()
										+ ","
										+ listadeClientes[i].getMonto_avencer()
										+ ","
										+ listadeClientes[i].getMonto_chcartera()
										+ ",'"
										+ listadeClientes[i].getTipo_factor()
										+ "','"
										+ listadeClientes[i].getIdflete()
										+ "',"
										+ listadeClientes[i].getIdvendedor()
										+ ",NULL);");
								
								publishProgress(-1,-1,i+1);
							}
						}
						catch (Exception e)
						{
							error = "SYNC.CL5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				
					listadeClientes =null;
					db.close();
					System.gc();
					
					if(!cancelado)
					{						
						sincroEditor.putBoolean("Clientes",true);
						sincroEditor.commit();
					}
				}
			
//******************************************FIN DE CARGA TABLA CLIENTE***************************************************************
				
//******************************************INCIO CARGA TABLA PRODUCTOS**************************************************************
				
			if(lastSincro.getBoolean("Productos", false)==false&cancelado==false)
			{
				publishProgress(1,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idven_idemp("obtenerProducto",stringidvendedor,stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.PR1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());	
				}
				
				try
				{
					listadeProductos = new Producto[resultado.getPropertyCount()];
					publishProgress(-1,resultado.getPropertyCount(),-1);
				}
				catch(Exception e)
				{
					error="SYNC.PR2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
							
				try
				{				
					for(int i=0;i<listadeProductos.length;i++)						
					{
						Producto prod = new Producto();
					
						ic= (SoapObject)resultado.getProperty(i);
					
						prod.setIdempresa(Integer.parseInt(ic.getProperty(0).toString()));
						prod.setSku(ic.getProperty(1).toString());
						prod.setNombre(ic.getProperty(2).toString());
						prod.setIdfamilia(Integer.parseInt(ic.getProperty(3).toString()));
						prod.setIdsubfamilia(Integer.parseInt(ic.getProperty(4).toString()));
						prod.setIdmarca(Integer.parseInt(ic.getProperty(5).toString()));
						prod.setFactoralt(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(6).toString())));
						prod.setFlete(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(7).toString())));
						prod.setIla(ic.getProperty(8).toString());					
						publishProgress(-1,-1,i+1);
						listadeProductos[i]=prod;
					}
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.PR3";
					cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Productos");
				}
				catch(Exception e)
				{
					error="SYNC.PR4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeProductos.length; i++) 
						{
							db.execSQL("INSERT INTO Productos (idempresa , sku , nombre , idfamilia , idsubfamilia ,"
									+ " idmarca , factoralt , flete , ila) "
									+ "VALUES ("
									+ listadeProductos[i].getIdempresa()
									+ " , '"
									+ listadeProductos[i].getSku()
									+ "' , '"
									+ listadeProductos[i].getNombre()
									+ "' , "
									+ listadeProductos[i].getIdfamilia()
									+ " , "
									+ listadeProductos[i].getIdsubfamilia()
									+ " , "
									+ listadeProductos[i].getIdmarca()
									+ " , "
									+ listadeProductos[i].getFactoralt()
									+ " , "
									+ listadeProductos[i].getFlete()
									+ " , '"
									+ listadeProductos[i].getIla()									
									+ "' );");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.PR5";
						cancelado = true;
					}
				}
				
				listadeProductos = null;
				db.close();				
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("Productos",true);
					sincroEditor.commit();
				}
				
			}
			
//******************************************FIN CARGA TABLA PRODUCTOS****************************************************************
			
//******************************************INICIO CARGA TABLA CANALES***************************************************************				
			
			if(lastSincro.getBoolean("Canales",false)==false&cancelado==false)
			{
				publishProgress(2,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCanal");	
				}
				catch(Exception e)
				{
					error="SYNC.CN1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeCanales = new Canal[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.CN2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listadeCanales.length;i++)
					{
						Canal canal = new Canal();						
						ic=(SoapObject)resultado.getProperty(i);

						canal.setIdcanal(Integer.parseInt(ic.getProperty(0).toString()));
						canal.setIdmegacanal(Integer.parseInt(ic.getProperty(1).toString()));
						canal.setNombre(ic.getProperty(2).toString());
						canal.setSigla(ic.getProperty(3).toString());
						canal.setDescuento(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(4).toString())));
						publishProgress(-1,-1,i+1);
						listadeCanales[i]=canal;
					}
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.CN3";
					cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Canales");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.CN4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeCanales.length; i++) 
						{
							db.execSQL("INSERT INTO Canales(idcanal ,idmegacanal, nombre,sigla,descuento) "
									+ "VALUES ("
									+ listadeCanales[i].getIdcanal()
									+ " , "
									+listadeCanales[i].getIdmegacanal()
									+ ",'"
									+ listadeCanales[i].getNombre()
									+ "' ,"
									+ "'"
									+ listadeCanales[i].getSigla()
									+ "' ,"
									+ listadeCanales[i].getDescuento() + ");");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.CN5";
						cancelado = true;
					}
				}
				
				listadeCanales=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{
					sincroEditor.putBoolean("Canales",true);
					sincroEditor.commit();
				}
				
			}
				
//******************************************FIN CARGA TABLA CANALES******************************************************************
			
//******************************************INICIO CARGA TABLA COMUNAS***************************************************************
			
			if(lastSincro.getBoolean("Comunas", false)==false&cancelado==false)
			{
				publishProgress(3,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerComunas");
				}
				catch(Exception e)
				{	
					error = "SYNC.CM1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());	
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeComunas = new Comuna[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error = "SYNC.CM2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeComunas.length;i++)
					{
						Comuna cm = new Comuna();
						ic= (SoapObject)resultado.getProperty(i);
						
						cm.setIdcomuna(Integer.parseInt(ic.getProperty(0).toString()));
						cm.setNombre(ic.getProperty(1).toString());
						cm.setIdciudad(Integer.parseInt(ic.getProperty(2).toString()));
						cm.setIdregion(Integer.parseInt(ic.getProperty(3).toString()));
						
						listadeComunas[i]=cm;
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());	
					error = "SYNC.CM3";
					cancelado=true;
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Comunas");
				} 
				catch (SQLException e) 
				{	
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error = "SYNC.CM4";
					cancelado=true;
				}
					
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeComunas.length; i++) 
						{
							db.execSQL("INSERT INTO Comunas(idcomuna,nombre,idciudad,idregion) "
									+ "VALUES ("
									+ listadeComunas[i].getIdcomuna()
									+ " , "
									+ "'"
									+ listadeComunas[i].getNombre()
									+ "'"
									+ " , "
									+ listadeComunas[i].getIdciudad()
									+ " , "
									+ listadeComunas[i].getIdregion()
									+ ");");
							publishProgress(-1,-1,i+1);

						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.CM5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeComunas = null;
				db.close();				
				System.gc();			
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("Comunas",true);
					sincroEditor.commit();
				}
			}
			
//******************************************FIN CARGA TABLA COMUNAS******************************************************************
			
//******************************************INICIO CARGA TABLA BANCOS****************************************************************
			
			if(lastSincro.getBoolean("Bancos", false)==false&cancelado==false)
			{
				publishProgress(4,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerBancos");
				}
				catch(Exception e)							
				{					
					error = "SYNC.BC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeBancos = new Banco[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());					
					error = "SYNC.BC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listadeBancos.length;i++)
					{
						Banco bank = new Banco();
						ic=(SoapObject)resultado.getProperty(i);
						
						bank.setIdbanco(Integer.parseInt(ic.getProperty(0).toString()));
						bank.setNombre(ic.getProperty(1).toString());
						publishProgress(-1,-1,i+1);
						
						listadeBancos[i]=bank;
					}
				}
				catch(Exception e)				
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());	
					error = "SYNC.BC3";
					cancelado=true;
				}				
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Bancos");
				} 
				catch (SQLException e) 
				{
					error = "SYNC.BC4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeBancos.length; i++) 
						{
							db.execSQL("INSERT INTO Bancos(idbanco,nombre) "
									+ "VALUES ("
									+ listadeBancos[i].getIdbanco() + " , "
									+ "'" + listadeBancos[i].getNombre()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.BC5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeBancos = null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("Bancos",true);
					sincroEditor.commit();
				}
			}

//******************************************FIN CARGA TABLA BANCOS*******************************************************************
			
//******************************************INICIO CARGA TABLA REGIONES**************************************************************				
			
			if(lastSincro.getBoolean("Regiones", false)==false&cancelado==false)
			{
				publishProgress(5,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerRegiones");
				}				
				catch(Exception e)
				{			
					error="SYNC.RG1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeRegiones = new Region[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.RG2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0; i<listadeRegiones.length;i++)
					{
						Region r = new Region();						
						ic=(SoapObject)resultado.getProperty(i);
						
						r.setIdregion(Integer.parseInt(ic.getProperty(0).toString()));
						r.setNombre(ic.getProperty(1).toString());
						publishProgress(-1,-1,i+1);						
						listadeRegiones[i]=r;
					}
				}
				
				catch(Exception e)
				{
					error="SYNC.RG3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db=dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Regiones");
				} 
				catch (SQLException e1) 
				{
					error="SYNC.RG4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeRegiones.length; i++) 
						{
							db.execSQL("INSERT INTO Regiones(idregion,nombre) "
									+ "VALUES ("
									+ listadeRegiones[i].getIdregion() + " , "
									+ "'" + listadeRegiones[i].getNombre()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.RG5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeRegiones = null;
				db.close();
				System.gc();		
				
				if(!cancelado)
				{	
					sincroEditor.putBoolean("Regiones",true);
					sincroEditor.commit();
				}
			}
//******************************************FIN CARGA TABLA REGIONES*****************************************************************
				
//******************************************INICIO CARGA TABLA CIUDADES**************************************************************	

			if(lastSincro.getBoolean("Ciudades", false)==false&cancelado==false)
			{
				publishProgress(6,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCiudades");
				}
				catch(Exception e)
				{
					error="SYNC.CD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeCiudades=new Ciudad[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.CD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeCiudades.length;i++)						
					{
						Ciudad cd = new Ciudad();
						ic = (SoapObject)resultado.getProperty(i);
						
						cd.setIdciudad(Integer.parseInt(ic.getProperty(0).toString()));
						cd.setNombre(ic.getProperty(1).toString());
						publishProgress(-1,-1,i+1);
						listadeCiudades[i]=cd;
					}
				}
				catch(Exception e)
				{
					error="SYNC.CD3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());				
				}
				
				try 
				{
					db=dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Ciudades");
				} 
				catch (SQLException e) 
				{	
					error="SYNC.CD4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeCiudades.length; i++) 
						{
							db.execSQL("INSERT INTO Ciudades(idciudad,nombre) "
									+ "VALUES ("
									+ listadeCiudades[i].getIdciudad() + " , "
									+ "'" + listadeCiudades[i].getNombre()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.CD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
			
				listadeCiudades = null;
				db.close();
				System.gc();	
				
				if(!cancelado)
				{		
					sincroEditor.putBoolean("Ciudades",true);
					sincroEditor.commit();
				}
			}
			
//******************************************FIN CARGA TABLA CIUDADES*****************************************************************
			
//***********************************INICIO CARGA TABLA CONDICIONES DE PAGO**********************************************************				
				
			if(lastSincro.getBoolean("CdePago", false)==false&cancelado==false)
			{
				publishProgress(7,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCondiciondePago");
				}
				catch(Exception e)
				{	
					error="SYNC.CP1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO2":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeCondicionesdePago = new CondicionPago[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.CP2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeCondicionesdePago.length;i++)
					{
						CondicionPago cp = new CondicionPago();
						ic = (SoapObject)resultado.getProperty(i);
						
						cp.setIdcondpago(Integer.parseInt(ic.getProperty(0).toString()));
						cp.setNombre(ic.getProperty(1).toString());
						publishProgress(-1,-1,i+1);
						listadeCondicionesdePago[i]=cp;
					}
				}
				catch(Exception e)
				{
					error="SYNC.CP3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());				
				}
				
				try 
				{
					db=dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Condiciones_De_Pago");
					
				} 
				catch (SQLException e) 
				{	
					error="SYNC.CP4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeCondicionesdePago.length; i++) 
						{
							db.execSQL("INSERT INTO Condiciones_De_Pago(idcondpago,nombre) "
									+ "VALUES ("
									+ listadeCondicionesdePago[i]
											.getIdcondpago()
									+ " , "
									+ "'"
									+ listadeCondicionesdePago[i].getNombre()
									+ "');");
							publishProgress(-1,-1,i+1);

						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.CP5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeCondicionesdePago = null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("CdePago",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN CARGA TABLA CONDICIONES DE PAGO*************************************************************
			
//***********************************INICIO CARGA TABLA DE RUTAS*********************************************************************	
			
			if(lastSincro.getBoolean("Rutas", false)==false&cancelado==false)
			{
				publishProgress(8,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idven_idemp("obtenerRuta", stringidvendedor, stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.RT1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO2":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeRutas = new Ruta[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.RT2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeRutas.length;i++)
					{
						Ruta ruta = new Ruta();				
						ic=(SoapObject)resultado.getProperty(i);
						
						ruta.setIdempresa(Integer.parseInt(ic.getProperty(0).toString()));
						ruta.setIdcliente(ic.getProperty(1).toString());
						ruta.setLuncob(Integer.parseInt(ic.getProperty(2).toString()));
						ruta.setMarcob(Integer.parseInt(ic.getProperty(3).toString()));
						ruta.setMiecob(Integer.parseInt(ic.getProperty(4).toString()));
						ruta.setJuecob(Integer.parseInt(ic.getProperty(5).toString()));
						ruta.setViecob(Integer.parseInt(ic.getProperty(6).toString()));
						ruta.setSabcob(Integer.parseInt(ic.getProperty(7).toString()));
						ruta.setSemana1cob(Integer.parseInt(ic.getProperty(8).toString()));
						ruta.setSemana2cob(Integer.parseInt(ic.getProperty(9).toString()));
						ruta.setSemana3cob(Integer.parseInt(ic.getProperty(10).toString()));
						ruta.setSemana4cob(Integer.parseInt(ic.getProperty(11).toString()));
						ruta.setIdcallcenter(Integer.parseInt(ic.getProperty(12).toString()));
						ruta.setLuncall(Integer.parseInt(ic.getProperty(13).toString()));
						ruta.setMarcall(Integer.parseInt(ic.getProperty(14).toString()));
						ruta.setMiecall(Integer.parseInt(ic.getProperty(15).toString()));
						ruta.setJuecall(Integer.parseInt(ic.getProperty(16).toString()));
						ruta.setViecall(Integer.parseInt(ic.getProperty(17).toString()));
						ruta.setSabcall(Integer.parseInt(ic.getProperty(18).toString()));
						ruta.setSemana1call(Integer.parseInt(ic.getProperty(19).toString()));
						ruta.setSemana2call(Integer.parseInt(ic.getProperty(20).toString()));
						ruta.setSemana3call(Integer.parseInt(ic.getProperty(21).toString()));
						ruta.setSemana4call(Integer.parseInt(ic.getProperty(22).toString()));
						ruta.setIdvendedor(Integer.parseInt(ic.getProperty(23).toString()));
						
						publishProgress(-1,-1,i+1);
						
						listadeRutas[i]=ruta;
					}
				}
				catch(Exception e)
				{
					error="SYNC.RT3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db=dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Rutas");
				} 
				catch (SQLException e) 
				{
					error="SYNC.RT4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeRutas.length; i++) 
						{
							db.execSQL("INSERT INTO Rutas(idempresa , idcliente , luncob , marcob , miecob , juecob , viecob , sabcob , semana1cob ,"
									+ "semana2cob , semana3cob , semana4cob , idcallcenter , luncall , marcall , miecall , juecall , viecall ,"
									+ " sabcall , semana1call , semana2call , semana3call , semana4call , idvendedor) "
									+

									"VALUES ("
									+ listadeRutas[i].getIdempresa()
									+ " , "
									+ "'"
									+ listadeRutas[i].getIdcliente()
									+ "',"
									+ listadeRutas[i].getLuncob()
									+ " , "
									+ listadeRutas[i].getMarcob()
									+ " , "
									+ listadeRutas[i].getMiecob()
									+ " , "
									+ listadeRutas[i].getJuecob()
									+ " , "
									+ listadeRutas[i].getViecob()
									+ " , "
									+ listadeRutas[i].getSabcob()
									+ " , "
									+ listadeRutas[i].getSemana1cob()
									+ " , "
									+ listadeRutas[i].getSemana2cob()
									+ " , "
									+ listadeRutas[i].getSemana3cob()
									+ " , "
									+ listadeRutas[i].getSemana4cob()
									+ " , "
									+ listadeRutas[i].getIdcallcenter()
									+ " , "
									+ listadeRutas[i].getLuncall()
									+ " , "
									+ listadeRutas[i].getMarcall()
									+ " , "
									+ listadeRutas[i].getMiecall()
									+ " , "
									+ listadeRutas[i].getJuecall()
									+ " , "
									+ listadeRutas[i].getViecall()
									+ " , "
									+ listadeRutas[i].getSabcall()
									+ " , "
									+ listadeRutas[i].getSemana1call()
									+ " , "
									+ listadeRutas[i].getSemana2call()
									+ " , "
									+ listadeRutas[i].getSemana3call()
									+ " , "
									+ listadeRutas[i].getSemana4call()
									+ " , "
									+ listadeRutas[i].getIdvendedor()
									+ ")");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.RT5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeRutas = null;
				db.close();
				System.gc();		
				
				if(!cancelado)
				{	
					sincroEditor.putBoolean("Rutas",true);
					sincroEditor.commit();
				}
			}
				
//***********************************FIN CARGA TABLA DE RUTAS************************************************************************
			
//***********************************INICIO CARGA TABLA FAMILIAS*********************************************************************	

			if(lastSincro.getBoolean("Familias", false)==false&cancelado==false)
			{
				publishProgress(9,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerFamilias");					
				}
				catch(Exception e)
				{
					error="SYNC.FM1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeFamilias = new Familia[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.FM2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeFamilias.length;i++)
					{
						Familia fam = new Familia();
						ic=(SoapObject)resultado.getProperty(i);
						
						fam.setIdfamilia(Integer.parseInt(ic.getProperty(0).toString()));
						fam.setNombre(ic.getProperty(1).toString());
						fam.setCerrada(ic.getProperty(2).toString());
						
						listadeFamilias[i]=fam;
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					error="SYNC.FM3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Familias");
				} 
				catch (SQLException e) 
				{	
					error="SYNC.FM4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeFamilias.length; i++) 
						{
							db.execSQL("INSERT INTO Familias(idfamilia , nombre, cerrada) "
									+ "VALUES ("
									+ listadeFamilias[i].getIdfamilia()
									+ " , "
									+ "'"
									+ listadeFamilias[i].getNombre()
									+ "'" 
									+ " , "
									+ "'"
									+ listadeFamilias[i].getCerrada()
									+ "');");
							
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.FM5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeFamilias=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("Familias",true);
					sincroEditor.commit();	
				}
			}

//***********************************FIN CARGA TABLA FAMILIAS************************************************************************
			
//***********************************INICIO CARGA TABLA SUBFAMILIAS******************************************************************	

			if(lastSincro.getBoolean("SubFamilias", false)==false&cancelado==false)
			{
				publishProgress(10,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerSubFamilias");					
				}
				catch(Exception e)
				{
					error="SYNC.SF1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeSubFamilias = new SubFamilia[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.SF2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeSubFamilias.length;i++)
					{
						SubFamilia sfam = new SubFamilia();
						ic=(SoapObject)resultado.getProperty(i);
						
						sfam.setIdSubfamilia(Integer.parseInt(ic.getProperty(0).toString()));
						sfam.setIdFamilia(Integer.parseInt(ic.getProperty(1).toString()));
						sfam.setNombre(ic.getProperty(2).toString());
						
						listadeSubFamilias[i]=sfam;
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					error="SYNC.SF3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM SubFamilias");
				} 
				catch (SQLException e) 
				{
					error="SYNC.SF4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeSubFamilias.length; i++) 
						{
							db.execSQL("INSERT INTO SubFamilias(idsubfamilia , idfamilia , nombre) "
									+ "VALUES ("
									+ listadeSubFamilias[i].getIdSubfamilia()
									+ " , "
									+ listadeSubFamilias[i].getIdFamilia()
									+ " , "
									+ "'"
									+ listadeSubFamilias[i].getNombre()
									+ "'"
									+ ");");
							
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.SF5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeSubFamilias=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("SubFamilias",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION TABLA SUBFAMILIAS************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA MARCAS**************************************************************

			if(lastSincro.getBoolean("Marcas", false)==false&cancelado==false)
			{
				publishProgress(11,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerMarcas");
				}
				catch(Exception e)							
				{		
					error="SYNC.MC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO2":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeMarcas = new Marca[resultado.getPropertyCount()];
				} 
				catch (Exception e1) 
				{	
					error="SYNC.MC2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e1.getMessage()==null)?"MENSAJE NULO":e1.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeMarcas.length;i++)
					{
						Marca mc= new Marca();
						ic=(SoapObject)resultado.getProperty(i);
						
						mc.setIdmarca(Integer.parseInt(ic.getProperty(0).toString()));
						mc.setNombre(ic.getProperty(1).toString());
						publishProgress(-1,-1,i+1);
						
						listadeMarcas[i]=mc;
					}
				}
				catch(Exception e)				
				{
					error="SYNC.MC3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());				
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Marcas");
				} 
				catch (SQLException e) 
				{
					error="SYNC.MC4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}				
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeMarcas.length; i++) 
						{
							db.execSQL("INSERT INTO Marcas(idmarca,nombre) "
									+ "VALUES ("
									+ listadeMarcas[i].getIdmarca() + " , "
									+ "'" + listadeMarcas[i].getNombre()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.MC5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeMarcas = null;
				db.close();
				System.gc();	
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("Marcas",true);
					sincroEditor.commit();
				}
			}
			
//***********************************FIN SINCRONIZACION TABLA MARCAS*****************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA LISTA DE PRECIOS****************************************************			

			if(lastSincro.getBoolean("ListadePrecio", false)==false&cancelado==false)
			{
				publishProgress(12,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_1P_idemp("obtenerListadePrecios", stringidempresa);					
				}
				catch(Exception e)
				{
					error="SYNC.LP1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO2":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeprecios = new ListaPrecio[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					error="SYNC.LP2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listadeprecios.length;i++)
					{
						ListaPrecio ls = new ListaPrecio();
						ic=(SoapObject)resultado.getProperty(i);
						
						ls.setIdempresa(Integer.parseInt(ic.getProperty(0).toString()));
						ls.setIdlisprecio(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(1).toString())));
						ls.setIdsku(ic.getProperty(2).toString());
						ls.setValor(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(3).toString())));
						publishProgress(-1,-1,i+1);
						
						listadeprecios[i]=ls;
					}
				}
				catch(Exception e)
				{
					error="SYNC.LP3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Lista_De_Precios");
				} 
				catch (SQLException e) 
				{
					error="SYNC.LP4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeprecios.length; i++) 
						{
							db.execSQL("INSERT INTO Lista_De_Precios(idempresa , idlisprecio , idsku , valor) "
									+ "VALUES ("
									+ listadeprecios[i].getIdempresa()
									+ " , "
									+ listadeprecios[i].getIdlisprecio()
									+ " , '"
									+ listadeprecios[i].getIdsku()
									+ "', "
									+ listadeprecios[i].getValor()
									+ "); ");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.LP5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listadeprecios=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("ListadePrecio",true);
					sincroEditor.commit();	
				}
			}

//***********************************FIN SINCRONIZACION LISTA DE PRECIOS*************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA CANPROMDESC*********************************************************	
			
			if(lastSincro.getBoolean("CANPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(13,-1,-1);
			
				try
				{
					resultado =null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerCANPROMDESC",stringidempresa,tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.CPD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCANPROMDESC = new CANPROMDESC[resultado.getPropertyCount()];
				} 
				catch (Exception e) 
				{
					error="SYNC.CPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaCANPROMDESC.length;i++)
					{
						CANPROMDESC cp = new CANPROMDESC();
						ic = (SoapObject)resultado.getProperty(i);
						
						cp.setIdcanal(Integer.parseInt(ic.getProperty(0).toString()));
						cp.setIdproducto(ic.getProperty(1).toString());
						cp.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
						cp.setSw_trato(ic.getProperty(3).toString());
						
						listaCANPROMDESC[i]=cp;
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					error="SYNC.CPD3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CANPROMDESC");
				} 
				catch (SQLException e) 
				{
					error="SYNC.CPD4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCANPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO CANPROMDESC(idcanal , idproducto , descuento, sw_trato)"
									+ " VALUES ("
									+ listaCANPROMDESC[i].getIdcanal()
									+ " , '"
									+ listaCANPROMDESC[i].getIdproducto()
									+ "', "
									+ listaCANPROMDESC[i].getDescuento()
									+ ", '"
									+ listaCANPROMDESC[i].getSw_trato()
									+ "');");
							publishProgress(-1,-1,i+1);

						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.CPD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaCANPROMDESC=null;
				db.close();
				System.gc();	
				
				if(!cancelado)
				{										
					sincroEditor.putBoolean("CANPROMDESC",true);
					sincroEditor.commit();
				}
			}
			
//***********************************IN SINCRONIZACION TABLA CANPROMDESC*************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA CLIPROMDESC*********************************************************	

			if(lastSincro.getBoolean("CLIPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(14,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerCLIPROMDESC",stringidempresa, tipoventa);	
				}
				catch(Exception e)
				{
					error="SYNC.CLPD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO2":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCLIPROMDESC = new CLIPROMDESC[resultado.getPropertyCount()];
					
				} 
				catch (Exception e) 
				{	
					error="SYNC.CLPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaCLIPROMDESC.length;i++)
					{
						CLIPROMDESC cp = new CLIPROMDESC();
						ic = (SoapObject)resultado.getProperty(i);
						
						cp.setIdcliente((ic.getProperty(0).toString()));
						cp.setIdproducto(ic.getProperty(1).toString());
						cp.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
						cp.setSw_trato(ic.getProperty(3).toString());
						
						listaCLIPROMDESC[i]=cp;
						
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					error="SYNC.CLPD3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{	
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CLIPROMDESC");
				} 
				catch (SQLException e) 
				{
					error="SYNC.CLPD4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCLIPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO CLIPROMDESC(idcliente , idproducto , descuento, sw_trato)"
									+ " VALUES ('"
									+ listaCLIPROMDESC[i].getIdcliente()
									+ "' , '"
									+ listaCLIPROMDESC[i].getIdproducto()
									+ "', "
									+ listaCLIPROMDESC[i].getDescuento()
									+ ", '"
									+ listaCLIPROMDESC[i].getSw_trato()									
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.CLPD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaCLIPROMDESC=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{										
					sincroEditor.putBoolean("CLIPROMDESC",true);
					sincroEditor.commit();	
				}
			}

//***********************************FIN SINCRONIZACION TABLA CLIPROMDESC************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA GRUPROMDESC*********************************************************	

			if(lastSincro.getBoolean("GRUPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(15,-1,-1);
				try
				{
					resultado = null;
					resultado = access.usarWebService_3P_idven_idemp_idtvta("obtenerGRUPROMDESC", stringidvendedor, stringidempresa, tipoventa);	
				}
				catch(Exception e)
				{
					error="SYNC.GPD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO2":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaGRUPROMDESC = new GRUPROMDESC[resultado.getPropertyCount()];
				} 
				catch (Exception e) 
				{	
					error="SYNC.GPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaGRUPROMDESC.length;i++)
					{
						GRUPROMDESC cp = new GRUPROMDESC();
						
						ic = (SoapObject)resultado.getProperty(i);
						
						cp.setIdcliente((ic.getProperty(0).toString()));
						cp.setIdproducto(ic.getProperty(1).toString());
						cp.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
						cp.setSw_trato(ic.getProperty(3).toString());
						
						publishProgress(-1,-1,i+1);
						
						listaGRUPROMDESC[i]=cp;
					}
				}
				catch(Exception e)
				{
					error="SYNC.GPD3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM GRUPROMDESC");
				} 
				catch (SQLException e) 
				{	
					error="SYNC.GPD4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaGRUPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO GRUPROMDESC(idcliente , idproducto , descuento,sw_trato)"
									+ " VALUES ('"
									+ listaGRUPROMDESC[i].getIdcliente()
									+ "' , '"
									+ listaGRUPROMDESC[i].getIdproducto()
									+ "', "
									+ listaGRUPROMDESC[i].getDescuento()
									+ ", '"
									+ listaGRUPROMDESC[i].getSw_trato()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.GPD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaGRUPROMDESC=null;
				db.close();
				System.gc();		
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("GRUPROMDESC",true);
					sincroEditor.commit();
				}
			}
			
//***********************************FIN SINCRONIZACION TABLA GRUPROMDESC************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA PAGPROMDESC*********************************************************	

			if(lastSincro.getBoolean("PAGPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(16,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_3P_idven_idemp_idtvta("obtenerPAGPROMDESC", stringidvendedor, stringidempresa, tipoventa);	
				}
				catch(Exception e)
				{
					error="SYNC.PPD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaPAGPROMDESC = new PAGPROMDESC[resultado.getPropertyCount()];
				} 
				catch (Exception e) 
				{	
					error="SYNC.PPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaPAGPROMDESC.length;i++)
					{
						PAGPROMDESC cp = new PAGPROMDESC();						
						ic = (SoapObject)resultado.getProperty(i);
						
						cp.setIdcliente((ic.getProperty(0).toString()));
						cp.setIdproducto(ic.getProperty(1).toString());
						cp.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
						cp.setSw_trato(ic.getProperty(3).toString());
						
						listaPAGPROMDESC[i]=cp;
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					error="SYNC.PPD3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM PAGPROMDESC");
				} 
				catch (SQLException e) 
				{	
					error="SYNC.PPD4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaPAGPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO PAGPROMDESC(idcliente , idproducto , descuento, sw_trato)"
									+ " VALUES ('"
									+ listaPAGPROMDESC[i].getIdcliente()
									+ "' , '"
									+ listaPAGPROMDESC[i].getIdproducto()
									+ "', "
									+ listaPAGPROMDESC[i].getDescuento()
									+ ", '"
									+ listaPAGPROMDESC[i].getSw_trato()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.PPD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaPAGPROMDESC=null;
				db.close();
				System.gc();	
				
				if(!cancelado)
				{									
					sincroEditor.putBoolean("PAGPROMDESC",true);
					sincroEditor.commit();
				}	
			}

//***********************************FIN SINCRONIZACION TABLA PAGPROMDESC************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA SUCPROMDESC*********************************************************	

			if(lastSincro.getBoolean("SUCPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(30,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_3P_idven_idemp_idtvta("obtenerSUCPROMDESC", stringidvendedor, stringidempresa, tipoventa);	
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SUCPD1";
					cancelado=true;
				}
				
				try 
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaSUCPROMDESC = new SUCPROMDESC[resultado.getPropertyCount()];
				} 
				catch (Exception e) 
				{	
					error="SYNC.SUCPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaSUCPROMDESC.length;i++)
					{
						SUCPROMDESC sucp = new SUCPROMDESC();						
						ic = (SoapObject)resultado.getProperty(i);
						
						sucp.setIdcanal(Integer.parseInt(ic.getProperty(0).toString()));
						sucp.setIdproducto(ic.getProperty(1).toString());
						sucp.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
						sucp.setSw_trato(ic.getProperty(3).toString());
						
						listaSUCPROMDESC[i]=sucp;
						publishProgress(-1,-1,i+1);
					}
				}
				catch(Exception e)
				{
					error="SYNC.SUCPD3";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try 
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM SUCPROMDESC");
				} 
				catch (SQLException e) 
				{
					error="SYNC.SUCPD4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaSUCPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO SUCPROMDESC(idcanal , idproducto , descuento, sw_trato)"
									+ " VALUES ('"
									+ listaSUCPROMDESC[i].getIdcanal()
									+ "' , '"
									+ listaSUCPROMDESC[i].getIdproducto()
									+ "', "
									+ listaSUCPROMDESC[i].getDescuento()
									+ ", '"
									+ listaSUCPROMDESC[i].getSw_trato()
									+ "');");
							publishProgress(-1,-1,i+1);
						}
					} 
					catch (SQLException e) 
					{
						error = "SYNC.SUCPD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaSUCPROMDESC=null;
				db.close();
				System.gc();	
				
				if(!cancelado)
				{									
					sincroEditor.putBoolean("SUCPROMDESC",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION TABLA SUCPROMDESC************************************************************
			
//***********************************INICIO SINCRONIZACION TABLA FLETES**************************************************************	
			
			if(lastSincro.getBoolean("Flete", false)==false&cancelado==false)
			{
				publishProgress(17,0,0);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerFlete");
				}
				catch(Exception e)
				{
					error="SYNC.FL1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaFlete = new Flete[resultado.getPropertyCount()];					 
				}
				catch(Exception e)
				{	
					error="SYNC.FL2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				 
				try
				{
					for(int i = 0;i<listaFlete.length;i++)
					{
						ic =(SoapObject)resultado.getProperty(i);
						Flete fl = new Flete();
						 
						fl.setTipoCliente(ic.getProperty(0).toString());
						fl.setIdProducto(ic.getProperty(1).toString());
						fl.setValorFlete(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(2).toString())));
						 
						publishProgress(-1,-1,i+1);
						listaFlete[i]=fl;
					}
				}
				catch(Exception e)
				{
						error="SYNC.FL3";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
						
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM Flete");
				}
				catch(Exception e)
				{
					error="SYNC.FL4";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(!cancelado)
				{
					try
					{
						for(int i=0; i<listaFlete.length;i++)
						{
							db.execSQL("INSERT INTO Flete(idtipocliente,idproducto,valorFlete) "+
									 "VALUES('"+listaFlete[i].getTipoCliente()+"' , '"+listaFlete[i].getIdProducto()+"' , "+
									 listaFlete[i].getValorFlete()+");");
							publishProgress(-1,-1,i+1);
						}
					}
				 	catch (Exception e) 
				 	{
				 		error = "SYNC.FL5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
				 	}
					
					listaFlete =null;
					db.close();
					System.gc();
					
					if(!cancelado)
					{					
						sincroEditor.putBoolean("Flete",true);
						sincroEditor.commit();
					} 
				 }
			}
			
//***********************************FIN SINCRONIZACION TABLA FLETES*****************************************************************

//***********************************INICIO CARGA ESCDESC****************************************************************************	

			if(lastSincro.getBoolean("ESCDESC", false)==false&cancelado==false)
			{
				publishProgress(18,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerESCDESC",stringidempresa,tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.ECD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaESCDESC = new ESCDESC[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{		 
					error="SYNC.ECD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaESCDESC.length;i++)
					{
						ic =(SoapObject)resultado.getProperty(i);
						ESCDESC esc = new ESCDESC();
						
						esc.setIdcanal(Integer.parseInt(ic.getProperty(0).toString()));
						esc.setIdproducto(ic.getProperty(1).toString());
						esc.setCantidad_desde(Integer.parseInt(ic.getProperty(2).toString()));
						esc.setCantidad_hasta(Integer.parseInt(ic.getProperty(3).toString()));
						esc.setDescuento(Float.parseFloat(ic.getProperty(4).toString()));
						esc.setSw_trato(ic.getProperty(5).toString());
						
						Log.d("ERROR APLICACION",Integer.parseInt(ic.getProperty(0).toString())+";"+ic.getProperty(1).toString()+";"+ Integer.parseInt(ic.getProperty(2).toString())+";"+Integer.parseInt(ic.getProperty(3).toString())+";"+Float.parseFloat(ic.getProperty(4).toString())+";"+ic.getProperty(5).toString());
						
						publishProgress(-1,-1,i+1);
						
						listaESCDESC[i]=esc;		
					}
				}
				catch(Exception e)
				{
					 error="SYNC.ECD3";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM ESCDESC");
				}
				catch(Exception e)
				{
					 error="SYNC.ECD4";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaESCDESC.length; i++) 
						{
							db.execSQL("INSERT INTO ESCDESC(idcanal,idproducto,cantidad_desde,cantidad_hasta,descuento, sw_trato) "+
									"VALUES("+listaESCDESC[i].getIdcanal()+" , '"+listaESCDESC[i].getIdproducto()+"' ,"+
									listaESCDESC[i].getCantidad_desde()+" , "+listaESCDESC[i].getCantidad_hasta()+" , "+
									listaESCDESC[i].getDescuento()+",'"+listaESCDESC[i].getSw_trato()+"');");
							
							publishProgress(-1,-1,i+1);
						}
					}
					catch (Exception e) 
					{
						error = "SYNC.ECD5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
			
				listaESCDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{				
					sincroEditor.putBoolean("ESCDESC",true);
					sincroEditor.commit();
				}
			}
			
//***********************************FIN SINCRONIZACION ESCDESC**********************************************************************

//***********************************INICIO CARGA GVTPROMDESC************************************************************************	

			if(lastSincro.getBoolean("GVTPROMDESC",false)==false&cancelado==false)
			{	
				publishProgress(19,-1,-1);
				
				try
				{		
					resultado = null;
					resultado = access.usarWebService_3P_idemp_idgpvta_idtvta("obtenerGVTPROMDESC", stringidempresa, stringGrupoVenta, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.GVT1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{				
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaGVTPROMDESC = new GVTPROMDESC[resultado.getPropertyCount()];					 
				}
				catch(Exception e)
				{		 
					error="SYNC.GVT2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaGVTPROMDESC.length;i++)
					{
						ic =(SoapObject)resultado.getProperty(i);
						
						GVTPROMDESC esc = new GVTPROMDESC();
						
						esc.setIdproducto(ic.getProperty(0).toString());						
						esc.setDescuento(Float.parseFloat(ic.getProperty(1).toString()));
						esc.setSw_trato(ic.getProperty(2).toString());	
						
						publishProgress(-1,-1,i+1);
						
						listaGVTPROMDESC[i]=esc;		
					}
				}
				catch(Exception e)
				{
					 error="SYNC.GVT3";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM GVTPROMDESC");
				}
				catch(Exception e)
				{
					 error="SYNC.GVT4";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaGVTPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO GVTPROMDESC(idproducto,descuento, sw_trato) "+
									"VALUES('"+listaGVTPROMDESC[i].getIdproducto()+"' , "
									+listaGVTPROMDESC[i].getDescuento()+",'"+listaGVTPROMDESC[i].getSw_trato()+"');");
							
							publishProgress(-1,-1,i+1);
						}
					}
					catch (Exception e) 
					{
						error = "SYNC.GVT5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaGVTPROMDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{	
					sincroEditor.putBoolean("GVTPROMDESC",true);
					sincroEditor.commit();
				}
			}
			
//***********************************FIN SINCRONIZACION GVTPROMDESC******************************************************************

//***********************************INICIO CARGA MGVTPROMDESC***********************************************************************				
			
			if(lastSincro.getBoolean("MGVTPROMDESC",false)==false&cancelado==false)
			{	
				publishProgress(26,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_3P_idemp_idmgvta_idtvta("obtenerMGVTPROMDESC", stringidempresa, stringMGrupoVenta, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.MGVT1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				Log.d("ERROR APLICACION","Cancelado: " + cancelado);
				
				try
				{				
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaMGVTPROMDESC = new MGVTPROMDESC[resultado.getPropertyCount()];					 
					Log.d("ERROR APLICACION","Cantidad: " + resultado.getPropertyCount());
				}
				catch(Exception e)
				{		 
					error="SYNC.MGVT2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaMGVTPROMDESC.length;i++)
					{
						ic =(SoapObject)resultado.getProperty(i);
						
						MGVTPROMDESC esc = new MGVTPROMDESC();
									
						esc.setIdproducto(ic.getProperty(0).toString());						
						esc.setDescuento(Float.parseFloat(ic.getProperty(1).toString()));
						esc.setSw_trato(ic.getProperty(2).toString());
						
						publishProgress(-1,-1,i+1);
						
						listaMGVTPROMDESC[i]=esc;		
					}
				}
				catch(Exception e)
				{
					 error="SYNC.MGVT3";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM MGVTPROMDESC");
				}
				catch(Exception e)
				{
					 error="SYNC.MGVT4";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaMGVTPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO MGVTPROMDESC(idproducto,descuento, sw_trato) "+
									"VALUES('"+listaMGVTPROMDESC[i].getIdproducto()+"' , "
									+listaMGVTPROMDESC[i].getDescuento()+",'"+listaMGVTPROMDESC[i].getSw_trato()+"');");
							
							publishProgress(-1,-1,i+1);
							
						}
								
					}
					catch (Exception e) 
					{
						error = "SYNC.MGVT5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}	
				}				
				
				listaMGVTPROMDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{		
					sincroEditor.putBoolean("MGVTPROMDESC",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION MGVTPROMDESC*****************************************************************

//***********************************INICIO CARGA MEGPROMDESC************************************************************************		

			if(lastSincro.getBoolean("MEGPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(20,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerMEGPROMDESC", stringidempresa, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.MEG1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaMEGPROMDESC = new MEGPROMDESC[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{		 
					error="SYNC.MEG2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					for(int i=0;i<listaMEGPROMDESC.length;i++)
					{
						ic =(SoapObject)resultado.getProperty(i);
						MEGPROMDESC esc = new MEGPROMDESC();						
						
						esc.setIdmegacanal(Integer.parseInt(ic.getProperty(0).toString()));
						esc.setIdproducto(ic.getProperty(1).toString());						
						esc.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
						esc.setSw_trato(ic.getProperty(3).toString());	
						
						publishProgress(-1,-1,i+1);
						
						listaMEGPROMDESC[i]=esc;		
					}
				}
				catch(Exception e)
				{
					 error="SYNC.MEG3";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM MEGPROMDESC");
				}
				catch(Exception e)
				{
					 error="SYNC.MEG4";
					 cancelado=true;
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaMEGPROMDESC.length; i++) 
						{
							db.execSQL("INSERT INTO MEGPROMDESC(idmegacanal,idproducto,descuento, sw_trato) "+
									"VALUES("+listaMEGPROMDESC[i].getIdmegacanal()+" , '"+listaMEGPROMDESC[i].getIdproducto()+"' , "
									+listaMEGPROMDESC[i].getDescuento()+",'"+listaMEGPROMDESC[i].getSw_trato()+"');");
							
							publishProgress(-1,-1,i+1);	
						}
					}
					catch (Exception e) 
					{
						error = "SYNC.MEG5";
						cancelado = true;
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
					}
				}
				
				listaMEGPROMDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{									
					sincroEditor.putBoolean("MEGPROMDESC",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION MEGPROMDESC******************************************************************

//***********************************INICIO CARGA VENPROMDESC************************************************************************		

			if(lastSincro.getBoolean("VENPROMDESC", false)==false&cancelado==false)
			{
				publishProgress(21,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_3P_idven_idemp_idtvta("obtenerVENPROMDESC", stringidvendedor, stringidempresa, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.VPD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaVENPROMDESC = new VENPROMDESC[resultado.getPropertyCount()];					 
				}
				catch(Exception e)
				{	 
					error="SYNC.VPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaVENPROMDESC.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							VENPROMDESC esc = new VENPROMDESC();
						
							esc.setIdproducto(ic.getProperty(0).toString());						
							esc.setDescuento(Float.parseFloat(ic.getProperty(1).toString()));
							esc.setSw_trato(ic.getProperty(2).toString());
						
							Log.d("Prueba",ic.getProperty(0).toString()+"//"+ic.getProperty(1).toString());
						
							publishProgress(-1,-1,i+1);
						
							listaVENPROMDESC[i]=esc;		
						}
					}
					catch(Exception e)
					{
						error="SYNC.VPD3";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM VENPROMDESC");
					}
					catch(Exception e)
					{
						error="SYNC.VPD4";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaVENPROMDESC.length; i++) 
							{
								db.execSQL("INSERT INTO VENPROMDESC(idproducto,descuento, sw_trato) "+
										"VALUES('"+listaVENPROMDESC[i].getIdproducto()+"' , "
										+listaVENPROMDESC[i].getDescuento()+",'"+listaVENPROMDESC[i].getSw_trato()+"');");
								
								Log.d("Prueba",listaVENPROMDESC[i].getIdproducto()+"//"+listaVENPROMDESC[i].getDescuento());
								publishProgress(-1,-1,i+1);
							}
						}
						catch (Exception e) 
						{
							error = "SYNC.VPD5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
			
				listaVENPROMDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("VENPROMDESC",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION VENPROMDESC******************************************************************

//***********************************INICIO CARGA MAXPAGDESC*************************************************************************

			if(lastSincro.getBoolean("MAXPAGDESC", false)==false&cancelado==false)
			{
				publishProgress(22,-1,-1);	
			
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerMAXPAGDESC", stringidempresa, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.MPD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaMAXPAGDESC = new MAXPAGDESC[resultado.getPropertyCount()];					 
				}
				catch(Exception e)
				{	 
					error="SYNC.MPD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaMAXPAGDESC.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							MAXPAGDESC esc = new MAXPAGDESC();
						
							esc.setIdpagador(ic.getProperty(0).toString());
							esc.setIdproducto(ic.getProperty(1).toString());						
							esc.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
							esc.setSw_trato(ic.getProperty(3).toString());
						
							publishProgress(-1,-1,i+1);
						
							listaMAXPAGDESC[i]=esc;		
						}
					}
					catch(Exception e)
					{
						error="SYNC.MPD3";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM MAXPAGDESC");
					}
					catch(Exception e)
					{
						error="SYNC.MPD4";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaMAXPAGDESC.length; i++) 
							{
								db.execSQL("INSERT INTO MAXPAGDESC(idcliente,idproducto,descuento, sw_trato) "+
										"VALUES('"+listaMAXPAGDESC[i].getIdpagador()+"','"+listaMAXPAGDESC[i].getIdproducto()+"' , "
										+listaMAXPAGDESC[i].getDescuento()+",'"+listaMAXPAGDESC[i].getSw_trato()+"');");
								
								publishProgress(-1,-1,i+1);
							}
						}
						catch (Exception e) 
						{
							error = "SYNC.MPD5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
				
				listaMAXPAGDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("MAXPAGDESC",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION MAXPAGDESC*******************************************************************

//***********************************INICIO CARGA MAXSKUDESC*************************************************************************

			if(lastSincro.getBoolean("MAXSKUDESC", false)==false&cancelado==false)
			{
				publishProgress(23,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerMAXSKUDESC", stringidempresa, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.MXD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaMAXSKUDESC = new MAXSKUDESC[resultado.getPropertyCount()];					 
				}
				catch(Exception e)
				{	 
					error="SYNC.MXD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaMAXSKUDESC.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							MAXSKUDESC esc = new MAXSKUDESC();
							
							esc.setIdproducto(ic.getProperty(0).toString());						
							esc.setDescuento(Float.parseFloat(ic.getProperty(1).toString()));
							esc.setSw_trato(ic.getProperty(2).toString());	
							
							publishProgress(-1,-1,i+1);
							
							listaMAXSKUDESC[i]=esc;		
						}
					}
					catch(Exception e)
					{
						 error="SYNC.MXD3";
						 cancelado=true;
						 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM MAXSKUDESC");
					}
					catch(Exception e)
					{
						 error="SYNC.MXD4";
						 cancelado=true;
						 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaMAXSKUDESC.length; i++) 
							{
								db.execSQL("INSERT INTO MAXSKUDESC(idproducto,descuento, sw_trato) "+
										"VALUES('"+listaMAXSKUDESC[i].getIdproducto()+" ', "
										+listaMAXSKUDESC[i].getDescuento()+",'"+listaMAXSKUDESC[i].getSw_trato()+"');");
								
								publishProgress(-1,-1,i+1);
							}
						}
						catch (Exception e) 
						{
							error = "SYNC.MXD5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
				
				listaMAXSKUDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("MAXSKUDESC",true);
					sincroEditor.commit();
				}
			}	

//***********************************FIN SINCRONIZACION MAXSKUDESC*******************************************************************

//***********************************INICIO CARGA MAXMCANDESC************************************************************************
			
			if(lastSincro.getBoolean("MAXMCANDESC", false)==false&cancelado==false)
			{
				publishProgress(24,-1,-1);	
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idemp_idtvta("obtenerMAXMCANDESC", stringidempresa, tipoventa);
				}
				catch(Exception e)
				{
					error="SYNC.MMD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaMAXMCANDESC = new MAXMCANDESC[resultado.getPropertyCount()];					 	
				}
				catch(Exception e)
				{	 
					error="SYNC.MMD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaMAXMCANDESC.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							MAXMCANDESC esc = new MAXMCANDESC();
							
							esc.setIdmcanal(Integer.parseInt(ic.getProperty(0).toString()));
							esc.setIdproducto(ic.getProperty(1).toString());						
							esc.setDescuento(Float.parseFloat(ic.getProperty(2).toString()));
							esc.setSw_trato(ic.getProperty(3).toString());
							
							publishProgress(-1,-1,i+1);
							
							listaMAXMCANDESC[i]=esc;		
						}
					}
					catch(Exception e)
					{
						 error="SYNC.MMD3";
						 cancelado=true;
						 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM MAXMCANDESC");
					}
					catch(Exception e)
					{
						 error="SYNC.MMD4";
						 cancelado=true;
						 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaMAXMCANDESC.length; i++) 
							{
								db.execSQL("INSERT INTO MAXMCANDESC(idmegacanal,idproducto,descuento, sw_trato) "+
										"VALUES("+listaMAXMCANDESC[i].getIdmegacanal()+",'"
										+listaMAXMCANDESC[i].getIdproducto()+"',"+listaMAXMCANDESC[i].getDescuento()+",'"+listaMAXMCANDESC[i].getSw_trato()+"');");
								
								publishProgress(-1,-1,i+1);	
							}
						}
						catch (Exception e) 
						{
							error = "SYNC.MMD5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
				
				listaMAXMCANDESC =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("MAXMCANDESC",true);
					sincroEditor.commit();
				}
			}	

//***********************************FIN SINCRONIZACION MAXMCANDESC******************************************************************

//***********************************INICIO CARGA CLIDET*****************************************************************************

			if(lastSincro.getBoolean("CliDet", false)==false&cancelado==false)
			{
				publishProgress(25,-1,-1);	
			
				try
				{
					resultado = null;
					resultado = access.usarWebServiceClienteDet("obtenerClientedetalle",stringNombrevendedor);
				}
				catch(Exception e)
				{
					error="SYNC.CLD1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaCliDet = new CliDet[resultado.getPropertyCount()];					 
				}
				catch(Exception e)
				{
					error="SYNC.CLD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaCliDet.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							CliDet cl = new CliDet();
							
							cl.setEmpresa(ic.getProperty(0).toString());
							cl.setNumero(ic.getProperty(1).toString());						
							cl.setFechavcto(ic.getProperty(2).toString());
							cl.setSaldo(Integer.parseInt(ic.getProperty(3).toString()));
							cl.setRut(ic.getProperty(4).toString());
							cl.setCliente(ic.getProperty(5).toString());
							
							publishProgress(-1,-1,i+1);
							
							listaCliDet[i]=cl;		
						}
					}
					catch(Exception e)
					{
						error="SYNC.CLD3";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}

					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM CliDet");
					}
					catch(Exception e)
					{
						error="SYNC.CLD4";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaCliDet.length; i++) 
							{
								db.execSQL("INSERT INTO CliDet(empresa,numero,fechavcto,saldo, rut, cliente) "+
										"VALUES('"+listaCliDet[i].getEmpresa()+"','"+listaCliDet[i].getNumero()+"' , '"
										+listaCliDet[i].getFechavcto()+"',"+listaCliDet[i].getSaldo()+",'"+listaCliDet[i].getRut()+"','"+listaCliDet[i].getCliente()+"');");
							
								publishProgress(-1,-1,i+1);
							}	
						}	
						catch (Exception e) 
						{
							error = "SYNC.CLD5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
				
				listaCliDet =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("CliDet",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION CLIDET******************************************************************

//***********************************INICIO CARGA BODEGA************************************************************************

			if(lastSincro.getBoolean("Bodega", false)==false&cancelado==false)
			{
				publishProgress(27,-1,-1);	
			
				try
				{
					resultado = null;
					resultado = access.usarWebService_2p_emp_idven("obtenerBodega",stringidempresa,idvendedor);
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.BD1";
					cancelado=true;
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaBodega = new Bodega[resultado.getPropertyCount()];					 
					Log.d("ERROR APLICACION",resultado.getPropertyCount()+"/");
				}
				catch(Exception e)
				{	 
					error="SYNC.BD2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				 }
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaBodega.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							Bodega bd = new Bodega();
						
							bd.setIdBodega(Integer.parseInt(ic.getProperty(0).toString()));
							bd.setNombre(ic.getProperty(1).toString());	
						
							publishProgress(-1,-1,i+1);
						
							listaBodega[i]=bd;		
						}
					}
					catch(Exception e)
					{
						error="SYNC.BD3";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM Bodega");
					}
					catch(Exception e)
					{
						error="SYNC.BD4";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaBodega.length; i++) 
							{
								db.execSQL("INSERT INTO Bodega(idbodega,nombre) "+
										"VALUES("+listaBodega[i].getIdbodega()+",'"+listaBodega[i].getNombre()+"');");
								
								publishProgress(-1,-1,i+1);
							}
						}
						catch (Exception e) 
						{
							error = "SYNC.BD5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
				
				listaBodega =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("Bodega",true);
					sincroEditor.commit();
				}
			}
			
//***********************************FIN SINCRONIZACION BODEGA******************************************************************

//***********************************INICIO CARGA FLETESPAG*********************************************************************

			if(lastSincro.getBoolean("Fletespag", false)==false&cancelado==false)
			{
				publishProgress(28,-1,-1);	
			
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerFletespag");
				}
				catch(Exception e)
				{
					error="SYNC.FP1";
					cancelado=true;		
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);	    	 
					listaFletespag = new Fletespag[resultado.getPropertyCount()];					 
					Log.d("ERROR APLICACION",resultado.getPropertyCount()+"/");
				}
				catch(Exception e)
				{	 
					error="SYNC.FP2";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				if(resultado.getPropertyCount()>0)
				{
					try
					{
						for(int i=0;i<listaFletespag.length;i++)
						{
							ic =(SoapObject)resultado.getProperty(i);
							Fletespag fp = new Fletespag();
						
							fp.setIdCliente(ic.getProperty(0).toString());
							fp.setIdProducto(ic.getProperty(1).toString());	
							fp.setValorFlete(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(2).toString())));	
							fp.setValor(BigDecimal.valueOf(Double.parseDouble(ic.getProperty(3).toString())));
						
							publishProgress(-1,-1,i+1);
						
							listaFletespag[i]=fp;		
						}
					}
					catch(Exception e)
					{
						error="SYNC.FP3";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
				
					try
					{
						db = dadb.getWritableDatabase();
						db.execSQL("DELETE FROM Fletespag");
					}
					catch(Exception e)
					{
						error="SYNC.FP4";
						cancelado=true;
						Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					}
					
					if (!cancelado) 
					{
						try 
						{
							for (int i = 0; i < listaFletespag.length; i++) 
							{
								db.execSQL("INSERT INTO Fletespag(idcliente, idproducto, valorFlete, valor) "+
										"VALUES('"+listaFletespag[i].getIdCliente()+"','"+listaFletespag[i].getIdProducto()+"',"+listaFletespag[i].getValorFlete()+","+listaFletespag[i].getValor()+");");
								
								publishProgress(-1,-1,i+1);
							}		
						}
						catch (Exception e) 
						{
							error = "SYNC.FP5";
							cancelado = true;
							Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						}
					}
				}
				
				listaFletespag =null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{							
					sincroEditor.putBoolean("Fletespag",true);
					sincroEditor.commit();
				}
			}

//***********************************FIN SINCRONIZACION FLETESPAG***************************************************************

//***********************************INICIO CARGA TABLA SKUCANALES**************************************************************
			
			if(lastSincro.getBoolean("SKUCanal",false)==false&cancelado==false)
			{
				publishProgress(29,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_1P_idemp("obtenerSKUCanal",stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeSKUCanales = new SKUCanal[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listadeSKUCanales.length;i++)
					{
						SKUCanal skucanal = new SKUCanal();						
						ic=(SoapObject)resultado.getProperty(i);
						
						skucanal.setIdcanal(Integer.parseInt(ic.getProperty(0).toString()));
						skucanal.setIdsku(ic.getProperty(1).toString());
						skucanal.setIdsucursal(Integer.parseInt(ic.getProperty(2).toString()));
				
						publishProgress(-1,-1,i+1);
						
						listadeSKUCanales[i]=skucanal;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM SKUCanal");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeSKUCanales.length; i++) 
						{
							db.execSQL("INSERT INTO SKUCanal(idcanal ,idsku, idsucursal) "
									+ "VALUES ("
									+ listadeSKUCanales[i].getIdcanal()
									+ " ,'"
									+ listadeSKUCanales[i].getIdsku()
									+ "', " 
									+ listadeSKUCanales[i].getIdsucursal()
									+ ");");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listadeSKUCanales=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("SKUCanal",true);
					sincroEditor.commit();
				}
			}
				
			//***********************************FIN CARGA TABLA SKUCANALES***************************************************************
			
			//***********************************INICIO CARGA TABLA SOCONSUMO*************************************************************
			
			if(lastSincro.getBoolean("SOConsumo",false)==false&cancelado==false)
			{
				publishProgress(31,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_1P_idemp("obtenerSOConsumo",stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listadeSOConsumo = new SOConsumo[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listadeSOConsumo.length;i++)
					{
						SOConsumo soco = new SOConsumo();						
						ic=(SoapObject)resultado.getProperty(i);
						
						soco.setSOConsumo(ic.getProperty(0).toString());
				
						publishProgress(-1,-1,i+1);
						
						listadeSOConsumo[i]=soco;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listadeSOConsumo.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM SOCONSUMO");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listadeSOConsumo.length; i++) 
						{
							db.execSQL("INSERT INTO SOCONSUMO(soconsumo) "
									+ "VALUES ('"
									+ listadeSOConsumo[i].getSOConsumo()
									+ "');");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listadeSOConsumo=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("SOCONSUMO",true);
					sincroEditor.commit();
				}
			}
				
			//***********************************FIN CARGA TABLA SOConsumo***************************************************************
			//***********************************INICIO CARGA TABLA CENSO_CALIBRE********************************************************
			if(lastSincro.getBoolean("CENSO_CALIBRE",false)==false&cancelado==false)
			{
				publishProgress(32,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCensoCalibre");
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoCalibre = new CENSO_CALIBRE[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoCalibre.length;i++)
					{
						CENSO_CALIBRE cencal = new CENSO_CALIBRE();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cencal.setIdcalibre(Integer.parseInt(ic.getProperty(0).toString()));
						cencal.setCalibre(ic.getProperty(1).toString());
				
						publishProgress(-1,-1,i+1);
						
						listaCensoCalibre[i]=cencal;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoCalibre.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_CALIBRE");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoCalibre.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_CALIBRE(idcalibre, calibre) "
									+ "VALUES ("
									+ listaCensoCalibre[i].getIdcalibre()
									+ ",'"
									+ listaCensoCalibre[i].getCalibre()
									+ "');");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoCalibre=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_CALIBRE",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_CALIBRE***********************************************************			
			//***********************************INICIO CARGA TABLA CENSO_MARCA********************************************************
			if(lastSincro.getBoolean("CENSO_MARCA",false)==false&cancelado==false)
			{
				publishProgress(33,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCensoMarca");
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoMarca = new CENSO_MARCA[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoMarca.length;i++)
					{
						CENSO_MARCA cenmar = new CENSO_MARCA();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenmar.setIdmarca(Integer.parseInt(ic.getProperty(0).toString()));
						cenmar.setMarca(ic.getProperty(1).toString());
				
						publishProgress(-1,-1,i+1);
						
						listaCensoMarca[i]=cenmar;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoMarca.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_MARCA");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoMarca.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_MARCA(idmarca, marca) "
									+ "VALUES ("
									+ listaCensoMarca[i].getIdmarca()
									+ ",'"
									+ listaCensoMarca[i].getMarca()
									+ "');");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoMarca=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_MARCA",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_MARCA***********************************************************	
			//***********************************INICIO CARGA TABLA CENSO_ITEM*********************************************************
			if(lastSincro.getBoolean("CENSO_ITEM",false)==false&cancelado==false)
			{
				publishProgress(34,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCensoItem");
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoItem = new CENSO_ITEM[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoItem.length;i++)
					{
						CENSO_ITEM cenitem = new CENSO_ITEM();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenitem.setIditem(Integer.parseInt(ic.getProperty(0).toString()));
						cenitem.setItem(ic.getProperty(1).toString());
						cenitem.setTipodato(ic.getProperty(2).toString());
						cenitem.setExcluyente(ic.getProperty(3).toString());
						cenitem.setNulo(ic.getProperty(4).toString());
				
						publishProgress(-1,-1,i+1);
						
						listaCensoItem[i]=cenitem;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoItem.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_ITEM");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoItem.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_ITEM(iditem, item, tipodato, excluyente, nulo) "
									+ "VALUES ("
									+ listaCensoItem[i].getIditem()
									+ ",'"
									+ listaCensoItem[i].getItem()
									+ "','"
									+ listaCensoItem[i].getTipodato()
									+ "','"
									+ listaCensoItem[i].getExcluyente()
									+ "','"
									+ listaCensoItem[i].getNulo()
									+ "');");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoItem=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_ITEM",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_MARCA***********************************************************	
			//***********************************INICIO CARGA TABLA CENSO_MOTIVO*******************************************************
			if(lastSincro.getBoolean("CENSO_MOTIVO",false)==false&cancelado==false)
			{
				publishProgress(35,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCensoMotivo");
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoMotivo = new CENSO_MOTIVO[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoMotivo.length;i++)
					{
						CENSO_MOTIVO cenmot = new CENSO_MOTIVO();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenmot.setIdmotivo(Integer.parseInt(ic.getProperty(0).toString()));
						cenmot.setMotivo(ic.getProperty(1).toString());
				
						publishProgress(-1,-1,i+1);
						
						listaCensoMotivo[i]=cenmot;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoMotivo.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_MOTIVO");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoMotivo.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_MOTIVO(idmotivo, motivo) "
									+ "VALUES ("
									+ listaCensoMotivo[i].getIdmotivo()
									+ ",'"
									+ listaCensoMotivo[i].getMotivo()
									+ "');");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoMotivo=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_MOTIVO",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_MOTIVO**********************************************************	
			//***********************************INICIO CARGA TABLA CENSO_VALIDACION***************************************************
			if(lastSincro.getBoolean("CENSO_VALIDACION",false)==false&cancelado==false)
			{
				publishProgress(36,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_SP("obtenerCensoValidacion");
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoValidacion = new CENSO_VALIDACION[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoValidacion.length;i++)
					{
						CENSO_VALIDACION cenval = new CENSO_VALIDACION();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenval.setIdvalidacion(Integer.parseInt(ic.getProperty(0).toString()));
						cenval.setValidacion(ic.getProperty(1).toString());
						cenval.setItem1(Integer.parseInt(ic.getProperty(2).toString()));
						cenval.setOperador(ic.getProperty(3).toString());
						cenval.setItem2(Integer.parseInt(ic.getProperty(4).toString()));
				
						publishProgress(-1,-1,i+1);
						
						listaCensoValidacion[i]=cenval;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoValidacion.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_VALIDACION");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoValidacion.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_VALIDACION(idvalidacion, descripcion, item1, operador, item2  ) "
									+ "VALUES ("
									+ listaCensoValidacion[i].getIdvalidacion()
									+ ",'"
									+ listaCensoValidacion[i].getValidacion()
									+ "',"
									+ listaCensoValidacion[i].getItem1()
									+ ",'"
									+ listaCensoValidacion[i].getOperador()
									+ "',"
									+ listaCensoValidacion[i].getItem2()
									+ ");");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoValidacion=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_VALIDACION",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_VALIDACION******************************************************
			//***********************************INICIO CARGA TABLA CENSO_CTACTE*******************************************************
			if(lastSincro.getBoolean("CENSO_CTACTE",false)==false&cancelado==false)
			{
				publishProgress(38,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idven_idemp("obtenerCensoCtacte", stringidvendedor, stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoCtacte= new CENSO_CTACTE[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoCtacte.length;i++)
					{
						CENSO_CTACTE cenctacte = new CENSO_CTACTE();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenctacte.setIdactivacion(Integer.parseInt(ic.getProperty(0).toString()));
						cenctacte.setIdcensolocal(Integer.parseInt(ic.getProperty(1).toString()));
						cenctacte.setHasta(ic.getProperty(2).toString());
						cenctacte.setIdgeneralact(Integer.parseInt(ic.getProperty(3).toString()));
						cenctacte.setNombre(ic.getProperty(4).toString());
						cenctacte.setIdcensogeneral(Integer.parseInt(ic.getProperty(5).toString()));
						cenctacte.setComentario(ic.getProperty(6).toString());
						cenctacte.setIdctacte(ic.getProperty(7).toString());
				
						publishProgress(-1,-1,i+1);
						
						listaCensoCtacte[i]=cenctacte;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION CENSO CTACTE",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoCtacte.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_CTACTE");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoCtacte.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_CTACTE(idactivacion, idcensolocal, idgeneralact, nombre, idcensogeneral, idctacte, hasta, comentario) "
									+ "VALUES ("
									+ listaCensoCtacte[i].getIdactivacion()
									+ ","
									+ listaCensoCtacte[i].getIdcensolocal()
									+ ","
									+ listaCensoCtacte[i].getIdgeneralact()
									+ ",'"
									+ listaCensoCtacte[i].getNombre()
									+ "',"
									+ listaCensoCtacte[i].getIdcensogeneral()
									+ ",'"
									+ listaCensoCtacte[i].getIdctacte()
									+ "','"
									+ listaCensoCtacte[i].getHasta()
									+ "','"
									+ listaCensoCtacte[i].getComentario()
									+ "');");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoCtacte=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_CTACTE",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_CTACTE**********************************************************
			//***********************************INICIO CARGA TABLA CENSO_DETALLE******************************************************
			if(lastSincro.getBoolean("CENSO_DETALLE",false)==false&cancelado==false)
			{
				publishProgress(39,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idven_idemp("obtenerCensoDetalle", stringidvendedor, stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoDetalle= new CENSO_DETALLE[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoDetalle.length;i++)
					{
						CENSO_DETALLE cendetalle = new CENSO_DETALLE();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cendetalle.setIdactivacion(Integer.parseInt(ic.getProperty(0).toString()));
						cendetalle.setIdcensolocal(Integer.parseInt(ic.getProperty(1).toString()));
						cendetalle.setIdgeneralact(Integer.parseInt(ic.getProperty(2).toString()));
						cendetalle.setNombre(ic.getProperty(3).toString());
						cendetalle.setIdcensogeneral(Integer.parseInt(ic.getProperty(4).toString()));
						cendetalle.setCalibres(Integer.parseInt(ic.getProperty(5).toString()));
						cendetalle.setItem(Integer.parseInt(ic.getProperty(6).toString()));
						cendetalle.setMarca(Integer.parseInt(ic.getProperty(7).toString()));
						cendetalle.setDesde(Float.parseFloat(ic.getProperty(8).toString()));
						cendetalle.setHasta(Float.parseFloat(ic.getProperty(9).toString()));
				
						publishProgress(-1,-1,i+1);
						
						listaCensoDetalle[i]=cendetalle;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoDetalle.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_DETALLE");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoDetalle.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_DETALLE(idactivacion, idcensolocal, idgeneralact, nombre, idcensogeneral, calibre, item, marca, desde, hasta) "
									+ "VALUES ("
									+ listaCensoDetalle[i].getIdactivacion()
									+ ","
									+ listaCensoDetalle[i].getIdcensolocal()
									+ ","
									+ listaCensoDetalle[i].getIdgeneralact()
									+ ",'"
									+ listaCensoDetalle[i].getNombre()
									+ "',"
									+ listaCensoDetalle[i].getIdcensogeneral()
									+ ","
									+ listaCensoDetalle[i].getCalibres()
									+ ","
									+ listaCensoDetalle[i].getItem()
									+ ","
									+ listaCensoDetalle[i].getMarca()
									+ ","
									+ listaCensoDetalle[i].getDesde()
									+ ","
									+ listaCensoDetalle[i].getHasta()
									+ ");");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoDetalle=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_DETALLE",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_DETALLE******************************************************
			
			//***********************************INICIO CARGA TABLA CENSO_MOT_REL***************************************************
			if(lastSincro.getBoolean("CENSO_MOT_REL",false)==false&cancelado==false)
			{
				publishProgress(40,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idven_idemp("obtenerCensoMotivoGeneral", stringidvendedor, stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoMotivoRel= new CENSO_MOT_REL[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoMotivoRel.length;i++)
					{
						CENSO_MOT_REL cenmotrel = new CENSO_MOT_REL();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenmotrel.setIdcensogeneral(Integer.parseInt(ic.getProperty(0).toString()));
						cenmotrel.setIdmotivo(Integer.parseInt(ic.getProperty(1).toString()));
				
						publishProgress(-1,-1,i+1);
						
						listaCensoMotivoRel[i]=cenmotrel;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoMotivoRel.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_MOT_REL");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoMotivoRel.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_MOT_REL(idcensogeneral, idmotivo) "
									+ "VALUES ("
									+ listaCensoMotivoRel[i].geIdcensogeneral()
									+ ","
									+ listaCensoMotivoRel[i].getIdmotivo()
									+ ");");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoMotivoRel=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			
					sincroEditor.putBoolean("CENSO_MOT_REL",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_MOT_REL******************************************************
			//***********************************INICIO CARGA TABLA CENSO_VAL_REL***************************************************
			if(lastSincro.getBoolean("CENSO_VAL_REL",false)==false&cancelado==false)
			{
				publishProgress(41,-1,-1);
				
				try
				{
					resultado = null;
					resultado = access.usarWebService_2P_idven_idemp("obtenerCensoValRel", stringidvendedor, stringidempresa);
				}
				catch(Exception e)
				{
					error="SYNC.SC1";
					cancelado=true;
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
				}
				
				try
				{
					publishProgress(-1,resultado.getPropertyCount(),-1);
					listaCensoValRel= new CENSO_VAL_REL[resultado.getPropertyCount()];
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC2";
					cancelado=true;
				}
				
				try
				{
					for(int i=0;i<listaCensoValRel.length;i++)
					{
						CENSO_VAL_REL cenvalrel = new CENSO_VAL_REL();						
						ic=(SoapObject)resultado.getProperty(i);
						
						cenvalrel.setIdcensogeneral(Integer.parseInt(ic.getProperty(0).toString()));
						cenvalrel.setIdvalidacion(Integer.parseInt(ic.getProperty(1).toString()));
				
						publishProgress(-1,-1,i+1);
						
						listaCensoValRel[i]=cenvalrel;
					}
				}
				catch(Exception e)
				{
					 Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage()+listaCensoValRel.length);
					 error="SYNC.SC3";
					 cancelado=true;
				}
				
				try
				{
					db = dadb.getWritableDatabase();
					db.execSQL("DELETE FROM CENSO_VAL_REL");
				}
				catch(Exception e)
				{
					Log.d("ERROR APLICACION",(e.getMessage()==null)?"MENSAJE NULO":e.getMessage());
					error="SYNC.SC4";
					cancelado=true;
				}
				
				if (!cancelado) 
				{
					try 
					{
						for (int i = 0; i < listaCensoValRel.length; i++) 
						{
							db.execSQL("INSERT INTO CENSO_VAL_REL(idcensogeneral, idvalidacion) "
									+ "VALUES ("
									+ listaCensoValRel[i].geIdcensogeneral()
									+ ","
									+ listaCensoValRel[i].getIdvalidacion()
									+ ");");
							publishProgress(-1,-1,i+1);
						}

					} catch (Exception e) 
					{
						Log.d("ERROR APLICACION",(e.getMessage() == null) ? "MENSAJE NULO" : e.getMessage());
						error = "SYNC.SC5";
						cancelado = true;
					}
				}
				
				listaCensoValRel=null;
				db.close();
				System.gc();
				
				if(!cancelado)
				{			 
					sincroEditor.putBoolean("CENSO_VAL_REL",true);
					sincroEditor.commit();
				}
			}
			//***********************************FIN CARGA TABLA CENSO_VAL_REL******************************************************
			
			
			if(cancelado==false)
			{
				//**************FINALIZAR SINCRONIZACION***************************//
				SharedPreferences.Editor editor = sp.edit();
				String fecha = sf.format(calendario.getTime());				
				editor.putString("UltimaActualizacion",fecha);
				editor.putBoolean("FTIME",true);
				editor.commit();			
				//********************FIN SINCRONIZACION DE TABLAS**********************//
			}
		}
		else
		{
			cancelado = true;
		}
		
			return null;				
		}

		@Override
		protected void onCancelled() 
		{														
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) 
		{			
			if(cancelado==false)
			{
				pdialog.dismiss();
				lblSincro.setText("Sincronización Finalizada");
				lblSincro.setTextColor(Color.GREEN);			
				lblActualizacion.setText(sp.getString("UltimaActualizacion","No DATA"));
				finalizado.show();
				setearUI();
			}
			else
			{
				lblSincro.setText("Error de Sincronizacion");
				lblSincro.setTextColor(Color.RED);	
				alerta.setMessage("Error de Sincronización\n Codido de Error : \n"+error);
				alerta.show();
				pdialog.dismiss();
				setearUI();
				
				super.onPostExecute(result);
			}
		}

		@Override
		protected void onPreExecute() 
		{
			pdialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Cancelar",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{	
					error="Cancelado";	
					cancelado = true;	
				}
			});
			
			lblSincro.setText("Iniciando Sincronización...");
			lblSincro.setTextColor(Color.YELLOW);
			pdialog.setTitle("Sincronizando");
			pdialog.setMessage("Espere un momento...\nIniciando Carga");			
			pdialog.show();
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) 
		{
			int flag = values[0].intValue();			
			int maxProgress= values[1].intValue();
			int currentProgress= values[2].intValue();
			
			if(maxProgress>0)
			{
				pdialog.setMax(maxProgress);
			
			}
			
			if(currentProgress>0)
			{
				pdialog.setProgress(currentProgress);
			}
			
			if(flag>-1)
			{
				switch(flag)
				{		
					case 0 :
							pdialog.setMessage("Espere un momento...\nCargando Clientes");
							break;
					case 1 :				
							pdialog.setMessage("Espere un momento...\nCargando Productos");
							break;	
					case 2 :
							pdialog.setMessage("Espere un momento...\nCargando Canales");
							break;
					case 3:				
							pdialog.setMessage("Espere un momento...\nCargando Comunas");
							break;
					case 4 :				
							pdialog.setMessage("Espere un momento...\nCargando Bancos");
							break;
					case 5 :				
							pdialog.setMessage("Espere un momento...\nCargando Regiones");
							break;
					case 6 :				
							pdialog.setMessage("Espere un momento...\nCargando Ciudades");
							break;
					case 7 :				
							pdialog.setMessage("Espere un momento...\nCargando CDP");
							break;
					case 8 :				
							pdialog.setMessage("Espere un momento...\nCargando Rutas");
							break;				
					case 9 :				
							pdialog.setMessage("Espere un momento...\nCargando Familias");
							break;				
					case 10:				
							pdialog.setMessage("Espere un momento...\nCargando SF");
							break;
					case 11:				
							pdialog.setMessage("Espere un momento...\nCargando Marcas");
							break;				
					case 12:				
							pdialog.setMessage("Espere un momento...\nCargando Lista de Precios");
							break;				
					case 13:				
							pdialog.setMessage("Espere un momento...\nCargando CANPD");
							break;				
					case 14:				
							pdialog.setMessage("Espere un momento...\nCargando CLIPD");
							break;				
					case 15:				
							pdialog.setMessage("Espere un momento...\nCargando GRUPD");
							break;				
					case 16:				
							pdialog.setMessage("Espere un momento...\nCargando PAGPD");
							break;				
					case 17:			
							pdialog.setMessage("Espere un momento...\nCargando Flete");
							break;				
					case 18:				
							pdialog.setMessage("Espere un momento...\nCargando ESCD");
							break;				
					case 19:				
							pdialog.setMessage("Espere un momento...\nCargando GVT");
							break;				
					case 20:				
							pdialog.setMessage("Espere un momento...\nCargando MEG");
							break;				
					case 21:
							pdialog.setMessage("Espere un momento...\nCargando VENPD");
							break;				
					case 22:
							pdialog.setMessage("Espere un momento...\nCargando MAXPD");
							break;				
					case 23:
							pdialog.setMessage("Espere un momento...\nCargando MAXSKUD");
							break;				
					case 24:
							pdialog.setMessage("Espere un momento...\nCargando MAXMCAND");
							break;				
					case 25:
							pdialog.setMessage("Espere un momento...\nCargando CliDet");
							break;				
					case 26:
							pdialog.setMessage("Espere un momento...\nCargando MGVT");
							break;				
					case 27:
							pdialog.setMessage("Espere un momento...\nCargando Bodegas");
							break;				
					case 28:
							pdialog.setMessage("Espere un momento...\nCargando Fletes Pagador");
							break;
					case 29:
							pdialog.setMessage("Espere un momento...\nCargando SKUCanales");
							break;				
					case 30:
							pdialog.setMessage("Espere un momento...\nCargando SUCPROMDESC");
							break;		
					case 31:
							pdialog.setMessage("Espere un momento...\nCargando SOConsumo");
							break;	
					case 32:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_CALIBRE");
						break;		
					case 33:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_MARCA");
						break;		
					case 34:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_ITEM");
						break;	
					case 35:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_MOTIVO");
						break;
					case 36:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_VALIDACION");
						break;
					case 38:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_CTACTE");
						break;
					case 39:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_DETALLE");
						break;
					case 40:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_MOT_REL");
						break;
					case 41:
						pdialog.setMessage("Espere un momento...\nCargando CENSO_VAL_REL");
						break;
				}
			}
			
			super.onProgressUpdate(values);
		}		
    }
    
    public void setearUI()
    {
    	 if(lastSincro.getBoolean("Clientes", false))
         {
    		tvsClientes.setText("OK");
         	tvsClientes.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsClientes.setText("NO");
          	tvsClientes.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Productos", false))
         {
    		tvsProductos.setText("OK");
         	tvsProductos.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsProductos.setText("NO");
          	tvsProductos.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Canales", false))
         {
    		tvsCanales.setText("OK");
         	tvsCanales.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsCanales.setText("NO");
          	tvsCanales.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Comunas", false))
         {
    		tvsComunas.setText("OK");
         	tvsComunas.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsComunas.setText("NO");
          	tvsComunas.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Bancos", false))
         {
    		tvsBancos.setText("OK");
         	tvsBancos.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsBancos.setText("NO");
          	tvsBancos.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Regiones", false))
         {
    		tvsRegiones.setText("OK");
         	tvsRegiones.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsRegiones.setText("NO");
          	tvsRegiones.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Ciudades", false))
         {
    		tvsCiudades.setText("OK");
         	tvsCiudades.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsCiudades.setText("NO");
          	tvsCiudades.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("CdePago", false))
         {
    		tvsCdepago.setText("OK");
         	tvsCdepago.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsCdepago.setText("NO");
          	tvsCdepago.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Rutas", false))
         {
    		tvsRutas.setText("OK");
         	tvsRutas.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsRutas.setText("NO");
          	tvsRutas.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Familias", false))
         {
    		tvsFamilia.setText("OK");
         	tvsFamilia.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsFamilia.setText("NO");
          	tvsFamilia.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("SubFamilias", false))
         {
    		tvsSubFamilia.setText("OK");
         	tvsSubFamilia.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsSubFamilia.setText("NO");
          	tvsSubFamilia.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Marcas", false))
         {
    		tvsMarcas.setText("OK");
         	tvsMarcas.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsMarcas.setText("NO");
          	tvsMarcas.setTextColor(Color.RED);    		 
    	 }
    	 if(lastSincro.getBoolean("ListadePrecio", false))
         {
    		tvsListaPrecio.setText("OK");
         	tvsListaPrecio.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsListaPrecio.setText("NO");
          	tvsListaPrecio.setTextColor(Color.RED);    		 
    	 }
    	 if(lastSincro.getBoolean("CANPROMDESC", false))
         {
    		tvsCANPROMDESC.setText("OK");
         	tvsCANPROMDESC.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsCANPROMDESC.setText("NO");
          	tvsCANPROMDESC.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("CLIPROMDESC", false))
         {
    		tvsCLIPROMDESC.setText("OK");
         	tvsCLIPROMDESC.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsCLIPROMDESC.setText("NO");
          	tvsCLIPROMDESC.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("GRUPROMDESC", false))
         {
    		tvsGRUPROMDESC.setText("OK");
         	tvsGRUPROMDESC.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsGRUPROMDESC.setText("NO");
          	tvsGRUPROMDESC.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("PAGPROMDESC", false))
         {
    		tvsPAGPROMDESC.setText("OK");
         	tvsPAGPROMDESC.setTextColor(Color.GREEN);
         }
    	 else
    	 {
    		tvsPAGPROMDESC.setText("NO");
          	tvsPAGPROMDESC.setTextColor(Color.RED);    		 
    	 }
    	 
    	 if(lastSincro.getBoolean("Flete", false))
    	 {
    		tvsFlete.setText("OK");
          	tvsFlete.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		tvsFlete.setText("NO");
           	tvsFlete.setTextColor(Color.RED); 
    	 }
    	 if(lastSincro.getBoolean("ESCDESC", false))
    	 {
    		tvsESCDESC.setText("OK");
          	tvsESCDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		tvsESCDESC.setText("NO");
           	tvsESCDESC.setTextColor(Color.RED); 
    	 }
    	 if(lastSincro.getBoolean("GVTPROMDESC", false))
    	 {
    		tvsGVTPROMDESC.setText("OK");
          	tvsGVTPROMDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		tvsGVTPROMDESC.setText("NO");
           	tvsGVTPROMDESC.setTextColor(Color.RED); 
    	 }
    	 if(lastSincro.getBoolean("MEGPROMDESC", false))
    	 {
    		tvsMEGPROMDESC.setText("OK");
          	tvsMEGPROMDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		tvsMEGPROMDESC.setText("NO");
           	tvsMEGPROMDESC.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("VENPROMDESC", false))
    	 {
    		tvsVPD.setText("OK");
    		tvsVPD.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsVPD.setText("NO");
    		 tvsVPD.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("MAXPAGDESC", false))
    	 {
    		tvsMAXPAGDESC.setText("OK");
    		tvsMAXPAGDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsMAXPAGDESC.setText("NO");
    		 tvsMAXPAGDESC.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("MAXSKUDESC", false))
    	 {
    		tvsMAXSKUDESC.setText("OK");
    		tvsMAXSKUDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsMAXSKUDESC.setText("NO");
    		 tvsMAXSKUDESC.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("MAXMCANDESC", false))
    	 {
    		tvsMAXMCANDESC.setText("OK");
    		tvsMAXMCANDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsMAXMCANDESC.setText("NO");
    		 tvsMAXMCANDESC.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CliDet", false))
    	 {
    		tvsCliDet.setText("OK");
    		tvsCliDet.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCliDet.setText("NO");
    		 tvsCliDet.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("MGVTPROMDESC", false))
    	 {
    		tvsMGVTPROMDESC.setText("OK");
    		tvsMGVTPROMDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsMGVTPROMDESC.setText("NO");
    		 tvsMGVTPROMDESC.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("Bodega", false))
    	 {
    		tvsBodega.setText("OK");
    		tvsBodega.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsBodega.setText("NO");
    		 tvsBodega.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("Fletespag", false))
    	 {
    		tvsFletespag.setText("OK");
    		tvsFletespag.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsFletespag.setText("NO");
    		 tvsFletespag.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("SKUCanal", false))
    	 {
    		tvsSKUCanal.setText("OK");
    		tvsSKUCanal.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsSKUCanal.setText("NO");
    		 tvsSKUCanal.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("SUCPROMDESC", false))
    	 {
    		tvsSUCPROMDESC.setText("OK");
    		tvsSUCPROMDESC.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsSUCPROMDESC.setText("NO");
    		 tvsSUCPROMDESC.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("SOCONSUMO", false))
    	 {
    		tvsSOConsumo.setText("OK");
    		tvsSOConsumo.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsSOConsumo.setText("NO");
    		 tvsSOConsumo.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_CALIBRE", false))
    	 {
    		tvsCenso_Calibre.setText("OK");
    		tvsCenso_Calibre.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Calibre.setText("NO");
    		 tvsCenso_Calibre.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_MARCA", false))
    	 {
    		tvsCenso_Marca.setText("OK");
    		tvsCenso_Marca.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Marca.setText("NO");
    		 tvsCenso_Marca.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_ITEM", false))
    	 {
    		tvsCenso_Item.setText("OK");
    		tvsCenso_Item.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Item.setText("NO");
    		 tvsCenso_Item.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_MOTIVO", false))
    	 {
    		tvsCenso_Motivo.setText("OK");
    		tvsCenso_Motivo.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Motivo.setText("NO");
    		 tvsCenso_Motivo.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_VALIDACION", false))
    	 {
    		tvsCenso_Validacion.setText("OK");
    		tvsCenso_Validacion.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Validacion.setText("NO");
    		 tvsCenso_Validacion.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_CTACTE", false))
    	 {
    		tvsCenso_Ctacte.setText("OK");
    		tvsCenso_Ctacte.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Ctacte.setText("NO");
    		 tvsCenso_Ctacte.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_DETALLE", false))
    	 {
    		tvsCenso_Detalle.setText("OK");
    		tvsCenso_Detalle.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_Detalle.setText("NO");
    		 tvsCenso_Detalle.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_MOT_REL", false))
    	 {
    		tvsCenso_MotRel.setText("OK");
    		tvsCenso_MotRel.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_MotRel.setText("NO");
    		 tvsCenso_MotRel.setTextColor(Color.RED); 
    	 }
    	 
    	 if(lastSincro.getBoolean("CENSO_VAL_REL", false))
    	 {
    		tvsCenso_ValRel.setText("OK");
    		tvsCenso_ValRel.setTextColor(Color.GREEN);
    	 }
    	 else
    	 {
    		 tvsCenso_ValRel.setText("NO");
    		 tvsCenso_ValRel.setTextColor(Color.RED); 
    	 }
    }
    
    @Override
    public void onBackPressed()
    {
    	startActivity(intent);
    	super.onBackPressed();
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
    





