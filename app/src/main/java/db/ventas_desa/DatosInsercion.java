package db.ventas_desa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatosInsercion extends SQLiteOpenHelper
{
	public DatosInsercion(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}	
	
	public final String sqlCreateClientesInsercion ="CREATE TABLE Insercion_Clientes(codigo INTEGER PRIMARY KEY AUTOINCREMENT , " +
													" idempresa INTEGER , idctacte TEXT , codlegal TEXT , nombre TEXT , sigla TEXT , "+
													 "idcondpago NUMERIC , limitecredito NUMERIC , telefono TEXT , "+
													 "idvendedor INTEGER , banco TEXT , rutcuenta TEXT , numeroCuenta TEXT , canal TEXT , " +
													 "nombreEncargado TEXT , email TEXT , titularCuenta TEXT , sucursal TEXT , serieCI TEXT , " +
													 "direccion TEXT , estado TEXT );";
	
	public final String sqlCreateProductosInsercion="CREATE TABLE Insercion_Productos(codigo INTEGER , "+
													 "sku TEXT ,nombre TEXT , cantidad NUMERIC , descuento NUMERIC, valor NUMERIC );";
	
	public final String sqlCreatePedidos = "CREATE TABLE Pedidos(id INTEGER, " +
										   "vendedor TEXT, fecha TEXT,hora TEXT,cliente TEXT,nota NUMERIC,oc TEXT," +
										   "producto01	TEXT,	cantidad01 	NUMERIC,	descuento01	NUMERIC ,"+
										   "producto02	TEXT,	cantidad02	NUMERIC,	descuento02	NUMERIC ,"+
										   "producto03	TEXT,	cantidad03	NUMERIC,	descuento03	NUMERIC ,"+
										   "producto04	TEXT,	cantidad04	NUMERIC,	descuento04	NUMERIC ,"+
										   "producto05	TEXT,	cantidad05	NUMERIC,	descuento05	NUMERIC ,"+
										   "producto06	TEXT,	cantidad06	NUMERIC,	descuento06	NUMERIC ,"+
										   "producto07	TEXT,	cantidad07	NUMERIC,	descuento07	NUMERIC ,"+
										   "producto08	TEXT,	cantidad08	NUMERIC,	descuento08	NUMERIC ,"+
										   "producto09	TEXT,	cantidad09	NUMERIC,	descuento09	NUMERIC ,"+
										   "producto10	TEXT,	cantidad10	NUMERIC,	descuento10	NUMERIC ,"+
										   "producto11	TEXT,	cantidad11	NUMERIC,	descuento11	NUMERIC ,"+
										   "producto12	TEXT,	cantidad12	NUMERIC,	descuento12	NUMERIC ,"+
										   "OBS TEXT,estado TEXT,opcion TEXT,fechaentrega TEXT,OBS2 TEXT,empresa TEXT,estadopedido TEXT,gps_latitud TEXT," +
										   "gps_longitud TEXT,gps_tiempo TEXT, idbodega TEXT);";
	
	public final String sqlCreateCensos = "CREATE TABLE Censos(idactivacion INTEGER, ctacte TEXT, " +
			   "calibre int, item int, marca int, respuesta TEXT, idmotivo int, estadocenso TEXT);";
	
	public final String sqlCreateSeleccionados="CREATE TABLE Seleccionados(sku TEXT,nombre TEXT)";

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(sqlCreateClientesInsercion);
		db.execSQL(sqlCreateProductosInsercion);
		db.execSQL(sqlCreatePedidos);
		db.execSQL(sqlCreateSeleccionados);
		db.execSQL(sqlCreateCensos);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	
	}

}
