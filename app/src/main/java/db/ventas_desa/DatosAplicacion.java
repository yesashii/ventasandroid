package db.ventas_desa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatosAplicacion extends SQLiteOpenHelper
{
	public DatosAplicacion(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}	
	
	public final String sqlCreateClientes = "CREATE TABLE Clientes (" +
			                    			" idempresa INTEGER , idctacte TEXT , codlegal TEXT , nombre TEXT , sigla TEXT , "+
			                    			"idejecutivo INTEGER , idcondpago NUMERIC , limitecredito NUMERIC , telefono TEXT , "+
			                    			"correo TEXT , nombreencargado TEXT , direccion TEXT , idlisprecio NUMERIC , idcanal TEXT , " +
			                    			"descuento_base TEXT , monto_vencido NUMERIC , monto_avencer NUMERIC , monto_chcartera NUMERIC , " +
			                    			"tipo_factor TEXT , idflete TEXT , idvendedor INTEGER, soconsumo TEXT);";
	
	public final String sqlCreateProducto = "CREATE TABLE Productos("+
			                    			"idempresa INTEGER , sku TEXT , nombre TEXT , idfamilia INTEGER , "+
			                    			"idsubfamilia INTEGER , idmarca INTEGER , factoralt NUMERIC , "+
			                    			"flete NUMERIC , ila TEXT )";
	
	public final String sqlCreateRuta = "CREATE TABLE Rutas("+
										"idempresa INTEGER , idcliente TEXT , luncob INTEGER , marcob  INTEGER , " +
										"miecob INTEGER , juecob INTEGER , viecob INTEGER , sabcob INTEGER , semana1cob INTEGER , " +
										"semana2cob INTEGER , semana3cob INTEGER , semana4cob INTEGER , idcallcenter INTEGER , " +
										"luncall INTEGER , marcall INTEGER , miecall INTEGER , juecall INTEGER , viecall INTEGER , " +
										"sabcall INTEGER , semana1call INTEGER , semana2call INTEGER , semana3call INTEGER , semana4call INTEGER , " +
										"idvendedor INTEGER );";
	
	public final String sqlCreateCanal = "CREATE TABLE Canales("+
										 "idcanal INTEGER,idmegacanal INTEGER , nombre TEXT , sigla TEXT , descuento NUMERIC);";
	
	public final String sqlCreateComuna = "CREATE TABLE Comunas("+
											"idcomuna INTEGER , nombre TEXT , idciudad INTEGER , idregion INTEGER);";
	
	public final String sqlCreateBanco = "CREATE TABLE Bancos("+
											"idbanco INTEGER , nombre TEXT);";
	
	public final String sqlCreateRegiones = "CREATE TABLE Regiones("+
											"idregion INTEGER , nombre TEXT);";
	
	public final String sqlCreateCiudades = "CREATE TABLE Ciudades("+
											"idciudad  INTEGER , nombre TEXT);";
	
	public final String sqlCreateCondPago = "CREATE TABLE Condiciones_De_Pago("+
											"idcondpago  INTEGER , nombre TEXT);";
	
	public final String sqlCreateFamilias= "CREATE TABLE Familias("+
											"idfamilia  INTEGER , nombre TEXT, cerrada TEXT);";
	
	public final String sqlCreateSubFamilias = "CREATE TABLE SubFamilias("+
			"idsubfamilia INTEGER , idfamilia  INTEGER , nombre TEXT);";
						
	public final String sqlCreateMarcas = "CREATE TABLE Marcas("+
			"idmarca INTEGER , nombre TEXT);";
	
	public final String sqlCreateListaPrecio = "CREATE TABLE Lista_De_Precios("+
			"idempresa INTEGER , idlisprecio NUMERIC , idsku TEXT , valor NUMERIC);";
	
	public final String sqlCreateCANPROMDESC = "CREATE TABLE CANPROMDESC("+
	"idcanal INTEGER , idproducto TEXT , descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateCLIPROMDESC = "CREATE TABLE CLIPROMDESC("+
			"idcliente TEXT , idproducto TEXT , descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateGRUPROMDESC = "CREATE TABLE GRUPROMDESC("+
			"idcliente TEXT , idproducto TEXT , descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreatePAGPROMDESC = "CREATE TABLE PAGPROMDESC("+
			"idcliente TEXT , idproducto TEXT , descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateSUCPROMDESC = "CREATE TABLE SUCPROMDESC("+
			"idcanal INTEGER, idproducto TEXT , descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateFlete ="CREATE TABLE Flete("+
			"idtipocliente TEXT,idproducto TEXT,valorFlete NUMERIC);";
	
	public final String sqlCreateESCDESC ="CREATE TABLE ESCDESC("+
			"idcanal INTEGER,idproducto TEXT,cantidad_desde INTEGER,cantidad_hasta INTEGER,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateGVTPROMDESC ="CREATE TABLE GVTPROMDESC("+
			"idproducto TEXT,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateMGVTPROMDESC ="CREATE TABLE MGVTPROMDESC("+
			"idproducto TEXT,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateMEGPROMDESC ="CREATE TABLE MEGPROMDESC("+
			"idmegacanal INTEGER,idproducto TEXT,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateVENPROMDESC = "CREATE TABLE VENPROMDESC("+
			"idproducto TEXT,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateMAXPAGDESC = "CREATE TABLE MAXPAGDESC("+
			"idcliente TEXT,idproducto TEXT,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateMAXSKUDESC = "CREATE TABLE MAXSKUDESC("+
			"idproducto TEXT,descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateMAXMCANDESC = "CREATE TABLE MAXMCANDESC("+
			"idmegacanal NUMERIC, idproducto TEXT, descuento NUMERIC, sw_trato TEXT);";
	
	public final String sqlCreateCliDet = "CREATE TABLE CliDet("+
			"empresa TEXT,numero TEXT, fechavcto TEXT, saldo INTEGER, rut TEXT, cliente TEXT);";
	
	public final String sqlCreateBodega = "CREATE TABLE Bodega("+
			"idbodega INTEGER,nombre TEXT);";
	
	public final String sqlCreateFletespag ="CREATE TABLE Fletespag("+
			"idcliente TEXT,idproducto TEXT,valorFlete NUMERIC, valor NUMERIC);";
	
	public final String sqlCreateSKUCanal ="CREATE TABLE SKUCanal("+
			"idcanal NUMERIC, idsku TEXT, idsucursal NUMERIC);";
	
	public final String sqlCreateSOConsumo ="CREATE TABLE SOConsumo("+
			"soconsumo TEXT);";
	
	public final String sqlCreateCENSO_CALIBRE ="CREATE TABLE CENSO_CALIBRE("+
			"idcalibre int, calibre TEXT);";
	
	public final String sqlCreateCENSO_MARCA ="CREATE TABLE CENSO_MARCA("+
			"idmarca int, marca TEXT);";
	
	public final String sqlCreateCENSO_ITEM ="CREATE TABLE CENSO_ITEM("+
			"iditem int, item TEXT, tipodato TEXT, excluyente TEXT, nulo TEXT);";
	
	public final String sqlCreateCENSO_MOTIVO ="CREATE TABLE CENSO_MOTIVO("+
			"idmotivo int, motivo TEXT);";
	
	public final String sqlCreateCENSO_VALIDACION ="CREATE TABLE CENSO_VALIDACION("+
			"idvalidacion int, descripcion TEXT, item1 int ,operador TEXT , item2 int);";
	
	public final String sqlCreateCENSO_CTACTE ="CREATE TABLE CENSO_CTACTE("+
			"idactivacion int, idcensolocal int , idgeneralact int , nombre TEXT , idcensogeneral int , idctacte TEXT, hasta TEXT, comentario TEXT);";
	
	public final String sqlCreateCENSO_DETALLE ="CREATE TABLE CENSO_DETALLE("+
			"idactivacion int , idcensolocal int , idgeneralact int , nombre TEXT , idcensogeneral int , calibre int ,item int , marca int , desde NUMERIC , hasta NUMERIC );";
	
	public final String sqlCreateCENSO_MOT_REL="CREATE TABLE CENSO_MOT_REL("+
			"idcensogeneral int , idmotivo int);";
	
	public final String sqlCreateCENSO_VAL_REL="CREATE TABLE CENSO_VAL_REL("+
			"idcensogeneral int , idvalidacion int);";
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(sqlCreateClientes);
		db.execSQL(sqlCreateProducto);
		db.execSQL(sqlCreateRuta);
		db.execSQL(sqlCreateCanal);
		db.execSQL(sqlCreateCiudades);
		db.execSQL(sqlCreateRegiones);
		db.execSQL(sqlCreateBanco);
		db.execSQL(sqlCreateComuna);
		db.execSQL(sqlCreateCondPago);
		db.execSQL(sqlCreateFamilias);
		db.execSQL(sqlCreateSubFamilias);
		db.execSQL(sqlCreateMarcas);
		db.execSQL(sqlCreateListaPrecio);
		db.execSQL(sqlCreateCANPROMDESC);
		db.execSQL(sqlCreateCLIPROMDESC);
		db.execSQL(sqlCreateGRUPROMDESC);
		db.execSQL(sqlCreatePAGPROMDESC);
		db.execSQL(sqlCreateSUCPROMDESC);
		db.execSQL(sqlCreateFlete);
		db.execSQL(sqlCreateESCDESC);
		db.execSQL(sqlCreateGVTPROMDESC);
		db.execSQL(sqlCreateMGVTPROMDESC);
		db.execSQL(sqlCreateMEGPROMDESC);
		db.execSQL(sqlCreateVENPROMDESC);
		db.execSQL(sqlCreateMAXPAGDESC);
		db.execSQL(sqlCreateMAXSKUDESC);
		db.execSQL(sqlCreateMAXMCANDESC);
		db.execSQL(sqlCreateCliDet);
		db.execSQL(sqlCreateBodega);
		db.execSQL(sqlCreateFletespag);
		db.execSQL(sqlCreateSKUCanal);
		db.execSQL(sqlCreateSOConsumo);
		db.execSQL(sqlCreateCENSO_CALIBRE);
		db.execSQL(sqlCreateCENSO_MARCA);
		db.execSQL(sqlCreateCENSO_ITEM);
		db.execSQL(sqlCreateCENSO_MOTIVO);
		db.execSQL(sqlCreateCENSO_VALIDACION);
		db.execSQL(sqlCreateCENSO_CTACTE);
		db.execSQL(sqlCreateCENSO_DETALLE);
		db.execSQL(sqlCreateCENSO_MOT_REL);
		db.execSQL(sqlCreateCENSO_VAL_REL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
	}

}
