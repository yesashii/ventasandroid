package dsm.ventas_desa;

import java.math.BigDecimal;
import mdls.ventas_desa.Titulos;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class Seleccionar_Producto extends Activity 
{
	private static final int SELECCIONAR =Menu.FIRST;
	
	Titulos[] datos;	
	Titulos[] datosselecteds;
	
	Spinner spfamilia;
	Spinner spMarca;
	EditText txtbuscar;
	ListView lstProductos,lstSeleccionados;
	DatosAplicacion dadb;
	DatosInsercion dadi;
	SQLiteDatabase db;	
	TextView tvBuscar;
	String cliente;
	AdaptadorTitulares adapter;	
	AdaptadorTitulares adapselec;
	Button continuar;
	SharedPreferences sp1;
	int contador;
	
	Integer[] idfamilia;
	Integer[] idmarca;
	String[] nombreProducto;
	BigDecimal idlisprecio;
	
	Intent nextstep;
	int productosCarro;
	
	int selectedidFamilia;
	int selectedidMarca;
	int idcanal;
	int idlocal;
	
	ArrayAdapter<String> adaptadorLista;
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
	       super.onCreateContextMenu(menu, v, menuInfo);
	       menu.add(0, SELECCIONAR, 0, "Seleccionar");	       
	       Log.d("1 vista","Creando menu");
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		String select = (String)lstProductos.getAdapter().getItem(info.position);		
		switch(item.getItemId())
		{
			case SELECCIONAR:
							String[] datos = select.split("\n");
							String sku = datos[0].trim();
							String nombre = datos[1].trim();
							nextstep = new Intent(Seleccionar_Producto.this,Seleccionar_Cantidad.class);
							SharedPreferences sp;
							sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
							idcanal  = sp.getInt("idcanal", 0);
							
							Log.d("idcanal",""+idcanal);
							
							SharedPreferences.Editor editor =sp.edit();			
							editor.putString("sku", sku);
							editor.putString("nombre", nombre);	
							editor.commit();
							startActivity(nextstep);	
							finish();
							
							Log.d("1 vista","Creando itemselected");
		}
		
		return false;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar__producto);   
        
        SharedPreferences sp3;
		sp3 = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		idcanal  = sp3.getInt("idcanal", 0);
        
        SharedPreferences sp2;
        sp2 = getSharedPreferences("DatosSistema",Context.MODE_PRIVATE); 
        idlocal = sp2.getInt("idlocal", -1);
        
        SharedPreferences sp;
		sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor =sp.edit();
               
        cliente =sp.getString("idctacte", "NO DATA");
        spfamilia = (Spinner)findViewById(R.id.spSPFamilia);
        spMarca = (Spinner)findViewById(R.id.spSPMarca);
        txtbuscar = (EditText)findViewById(R.id.txtSPBuscar);
        txtbuscar.addTextChangedListener(filterTextWatcher);
        lstProductos = (ListView)findViewById(R.id.lstSPProductos);
        tvBuscar = (TextView)findViewById(R.id.tvSPbuscar);
        lstSeleccionados = (ListView)findViewById(R.id.lstSeleccionados);
        continuar=(Button)findViewById(R.id.btnSeguir);
        
        Log.d("1 vista","cargando variablesu");
        
        dadb = new DatosAplicacion(this,"PrincipalDB", null, 1);       
    	db = dadb.getWritableDatabase(); 
    	
    	DatosInsercion dd =new DatosInsercion(this,"InsertarDB",null,1);
    	SQLiteDatabase dbx = dd.getWritableDatabase();
    	Cursor x=dbx.rawQuery("SELECT * FROM Seleccionados",null);
    	
    	Log.d("1 vista","cargando seleccionados");
    	
    	if(x.getCount()>0)
    	{
    		dbx.execSQL("DELETE FROM Seleccionados");
    	}
    	
    	Log.d("1 vista","Se borraron");
    	
    	dbx.close();
    	
    	adaptadorLista = new ArrayAdapter<String>(Seleccionar_Producto.this, android.R.layout.simple_list_item_1);
    	
    	spMarca.setVisibility(View.INVISIBLE);
    	txtbuscar.setVisibility(View.INVISIBLE);
    	lstProductos.setVisibility(View.INVISIBLE);
    	tvBuscar.setVisibility(View.INVISIBLE);    	
    	
    	datosselecteds = new Titulos[0];
    	
		adapselec = new AdaptadorTitulares(Seleccionar_Producto.this,datosselecteds);	

    	//TABHOSTS    	
    	TabHost tabs = (TabHost)findViewById(R.id.tabsshost);
    	tabs.setup();
    	
    	TabHost.TabSpec spec = tabs.newTabSpec("tab1");
    	spec.setContent(R.id.tabx1);
        spec.setIndicator("Producto");
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("tab2");
        spec.setContent(R.id.tabx2);
        spec.setIndicator("Listado");
        tabs.addTab(spec);
    	
        tabs.setCurrentTab(0);
    	
        Log.d("1 vista","antes de lista de precio");
        
    	Cursor d = db.rawQuery("SELECT idlisprecio FROM Clientes WHERE idctacte='"+cliente+"';", null);
    	d.moveToFirst();    	
    	idlisprecio=BigDecimal.valueOf(d.getDouble(0));    	
		
    	Log.d("1 vista","despues lista precio");
    	
		editor.putFloat("idlisprecio", idlisprecio.floatValue());
		editor.commit();
    	
		Log.d("1 vista","despues ingreso lp");
    	
    	Cursor c = db.rawQuery("SELECT idfamilia FROM PRODUCTOS GROUP BY idfamilia",null);
    	c.moveToFirst();
    	
    	idfamilia = new Integer[c.getCount()];
    	
    	Log.d("1 vista","despues ingreso familia"+idfamilia);
    	
    	for(int i=0;i<idfamilia.length;i++)
    	{
    		idfamilia[i] = c.getInt(0);
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	Log.d("1 vista","despues nombre familia"+idfamilia);
    	
    	String[] nombrefamilia = new String[idfamilia.length];
    	
    	for(int i=0;i<idfamilia.length;i++)
    	{
    		Log.d("1 vista","nombres de familia"+idfamilia[i]);
    		Cursor cur = db.rawQuery("SELECT nombre FROM Familias WHERE idfamilia ="+idfamilia[i]+";", null);
    		cur.moveToFirst();    		
    		nombrefamilia[i]= cur.getString(0);
    		Log.d("1 vista","nombres de familia"+nombrefamilia[i]);
    		cur.close();
    	}
    	
    	Log.d("1 vista","despues ingreso nombre familia");
    	
    	db.close();
    	ArrayAdapter<String> adaptador;
    	Log.d("1 vista","array");
    	
    	adaptador = new ArrayAdapter<String>(Seleccionar_Producto.this,android.R.layout.simple_spinner_item,nombrefamilia);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
    	Log.d("1 vista","adapter");
    	
    	spfamilia.setAdapter(adaptador);
    	//spfamilia.setSelection(0);
    	//registerForContextMenu(lstProductos);

    	Log.d("1 vista","spfamilia");
    	
    	spfamilia.setOnItemSelectedListener(new OnItemSelectedListener() 
    	{
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) 
			{
				
				db = dadb.getWritableDatabase();
				selectedidFamilia = idfamilia[position];
				
				Log.d("1 vista","antes de cargar spfamilia");
				
				Cursor c = db.rawQuery("SELECT idmarca FROM Productos WHERE idfamilia ="+selectedidFamilia+ " GROUP BY idmarca;", null);
				c.moveToFirst();
				idmarca = new Integer[c.getCount()];
				
				Log.d("1 vista","despues de cargar spfamilia idmarca");
				
				for(int i=0;i<idmarca.length;i++)
		    	{
		    		idmarca[i] = c.getInt(0);
		    		c.moveToNext();
		    	}
				
				c.close();
				
				String[] nombremarca = new String[idmarca.length];
		    	
				Log.d("1 vista","antes de cargar spfamilia nombre");
				
		    	for(int i=0;i<idmarca.length;i++)
		    	{
		    		Cursor cur = db.rawQuery("SELECT nombre FROM Marcas WHERE idmarca ="+idmarca[i]+";", null);
		    		cur.moveToFirst();    		
		    		nombremarca[i]= cur.getString(0);
		    		cur.close();
		    	}
				
		    	Log.d("1 vista","despues de cargar spfamilia nombre");
		    	
		    	spMarca.setVisibility(View.VISIBLE);
		    	ArrayAdapter<String> adaptador;
		    	adaptador = new ArrayAdapter<String>(Seleccionar_Producto.this,android.R.layout.simple_spinner_item,nombremarca);
		    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    	
		    	spMarca.setAdapter(adaptador);
		    	
		    	db.close();
		    	
		    	Log.d("1 vista","sp selected listener");
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}
		});
    	
    	lstProductos.setOnItemClickListener(new OnItemClickListener() 
    	{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				Titulos tit = new Titulos();
				tit = (Titulos)lstProductos.getAdapter().getItem(arg2);				
				String sku = tit.getTitulo();
				String nombre = tit.getSubtitulo();
				DatosInsercion dadi = new DatosInsercion(Seleccionar_Producto.this,"InsertarDB",null,1);
				SQLiteDatabase database = dadi.getWritableDatabase();
				Cursor z = database.rawQuery("SELECT sku FROM Seleccionados",null);
				
				if((z.getCount()+productosCarro)<12)
				{
					ContentValues cv=new ContentValues();
					cv.put("sku",sku);
					cv.put("nombre",nombre);					
					database.insert("Seleccionados",null,cv);					
					recargarUI();
				}
				else
				{
					final AlertDialog.Builder alerta = new AlertDialog.Builder(Seleccionar_Producto.this);
			        alerta.setTitle("Excede Maximo");
			        alerta.setMessage("Excede la maxima cantidad de productos por pedido");
			        
			        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
			        {			
						public void onClick(DialogInterface dialog, int which) 
						{				
							dialog.cancel();				
						}
					});		
			        
			        alerta.show();
				}
				
				database.close();
			}
		});
    	
    	lstSeleccionados.setOnItemClickListener(new OnItemClickListener() 
    	{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				Titulos tit = new Titulos();
				tit = (Titulos)lstSeleccionados.getAdapter().getItem(arg2);				
				String sku = tit.getTitulo();
				DatosInsercion dadi = new DatosInsercion(Seleccionar_Producto.this,"InsertarDB",null,1);
				SQLiteDatabase database = dadi.getWritableDatabase();				
				database.execSQL("DELETE FROM Seleccionados WHERE sku='"+sku+"'");		
				recargarUI();				
				database.close();
			}
		});

		spMarca.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
				public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) 
				{
					dadi =new DatosInsercion(Seleccionar_Producto.this, "InsertarDB", null, 1);
					SQLiteDatabase db2 = dadi.getWritableDatabase();
					
					Cursor d = db2.rawQuery("SELECT sku FROM Insercion_Productos",null);
					Cursor x= db2.rawQuery("SELECT sku FROM Seleccionados",null);
					
					productosCarro=d.getCount();
					
					String sql = "";
					String[] skus = new String[d.getCount()+x.getCount()];
					
					d.moveToFirst();
					x.moveToFirst();
					
					if(d.getCount()>0)
					{
						for(int i=0;i<d.getCount();i++)
						{
							skus[i]=d.getString(0);
							sql =sql+"?,";
							d.moveToNext();
						}
					}
					
					if(x.getCount()>0)
					{
						for(int i=d.getCount();i<(d.getCount()+x.getCount());i++)
						{
							skus[i]=x.getString(0);
							sql =sql+"?,";
							x.moveToNext();
						}
					}
					
					if(x.getCount()>0||d.getCount()>0)
					{					
						sql=sql.substring(0,sql.length()-1);
					}
					
					db = dadb.getWritableDatabase();
					selectedidMarca = idmarca[position];
							
					Cursor c = db.rawQuery("SELECT nombre,sku FROM Productos WHERE idmarca = "+selectedidMarca+" AND sku NOT IN("+sql+") AND " +
							" sku NOT IN(SELECT idsku FROM SKUCanal WHERE idcanal='"+idcanal+"' AND idsucursal="+idlocal+" )", skus);
					
					Log.d("SQL1","SELECT nombre,sku FROM Productos WHERE idmarca = "+selectedidMarca+" AND sku NOT IN("+sql+") AND " +
							" sku NOT IN(SELECT idsku FROM SKUCanal WHERE idcanal='"+idcanal+"' AND idsucursal="+idlocal);
					
					if(c.getCount()>0)
					{
						nombreProducto =new String[c.getCount()];
						String[] nombreP = new String[c.getCount()];
						String[] skuP = new String[c.getCount()];
						
						c.moveToFirst();	
						
						for(int i =0; i<c.getCount();i++)
						{
							nombreP[i]=c.getString(0);
							skuP[i]=c.getString(1);
							c.moveToNext();
						}
						
						for (int i=0;i<skuP.length;i++)
						{
							nombreProducto[i] = skuP[i]+" \n "+nombreP[i];
						}
						
						datos = new Titulos[skuP.length];
						
						for(int i=0;i<skuP.length;i++)
						{
							datos[i]=new Titulos(skuP[i], nombreP[i]);
						}						
						
						adapter = new AdaptadorTitulares(Seleccionar_Producto.this,datos);						
						
						lstProductos.setAdapter(adapter);
						
						tvBuscar.setVisibility(View.VISIBLE);
						txtbuscar.setVisibility(View.VISIBLE);
						lstProductos.setVisibility(View.VISIBLE);
						
						db.close();
						db2.close();
						
						Log.d("1 vista","lista marca");
					}
				}

				public void onNothingSelected(AdapterView<?> arg0) 
				{
					// TODO Auto-generated method stub
				}
			});	
			
			continuar.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					//Sentencia para saber cuantos ya agrego a la lista
					sp1 = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);     
			        contador = sp1.getInt("Contador",0);
			        Log.d("CONTADOR",""+contador);
			        
					//Sentencia para saber cuantos esta agregando a la lista
					DatosInsercion dadi = new DatosInsercion(Seleccionar_Producto.this,"InsertarDB",null,1);
					SQLiteDatabase datab = dadi.getWritableDatabase();
					Cursor cu = datab.rawQuery("SELECT * FROM Seleccionados",null);
					Log.d("Cursor",""+cu.getCount()); 
							
					Log.d("Cursor",""+(cu.getCount()+contador));
					
					//resta de Ambos
					if(cu.getCount()+contador<=12)
					{
						if(cu.getCount()>0)
						{
							cu.moveToFirst();
							String sku = cu.getString(0);
							String nombre=cu.getString(1);
							SharedPreferences sp;
							sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
							SharedPreferences.Editor editor =sp.edit();			
							editor.putString("sku",sku);
							editor.putString("nombre", nombre);
							editor.commit();						
							datab.execSQL("DELETE FROM Seleccionados WHERE sku='"+sku+"'");
							nextstep = new Intent(Seleccionar_Producto.this,Seleccionar_Cantidad.class);
							startActivity(nextstep);
							finish();
						}
						else
						{
							final AlertDialog.Builder alerta = new AlertDialog.Builder(Seleccionar_Producto.this);
						    alerta.setTitle("Advertencia");
						    alerta.setMessage("No a seleccionado ningun producto.");
						        
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
						final AlertDialog.Builder alerta = new AlertDialog.Builder(Seleccionar_Producto.this);
				        alerta.setTitle("Advertencia");
				        alerta.setMessage("Tiene "+(cu.getCount()+contador)+" productos, favor elimine al menos "+Math.abs((12-cu.getCount()-contador)));
				        Log.d("Cursor",""+(cu.getCount()-12-contador));
				        
				        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
				        {			
							public void onClick(DialogInterface dialog, int which) 
							{				
								dialog.cancel();				
							}
						});
				        
				        alerta.show();
					}
						
					datab.close();
				}
			});
    	}
    
    	private TextWatcher filterTextWatcher = new TextWatcher() 
    	{
    		public void onTextChanged(CharSequence s, int start, int before, int count) 
    		{
    			// TODO Auto-generated method stub
    		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,int after) 
		{
			// TODO Auto-generated method stub	
		}
		
		public void afterTextChanged(Editable s) 
		{
			adapter.getFilter().filter(s);	
		}
	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_seleccionar__producto, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Seleccionar_Producto.this,Inicio_Venta.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
    class AdaptadorTitulares extends ArrayAdapter<Titulos>
    {	
    	Activity context;
    	private LayoutInflater vi;	
    	private static final int mResource = R.layout.listitem_titulos_small;

        public	AdaptadorTitulares(Activity context,Titulos[] datos) 
        {
        	super(context, mResource,datos);
        	this.context = context;		
        	vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } 
        
        public  class ViewHolder
        {
        	private TextView textViewTitulo;
        	private TextView textViewSubTitulo;
        		
        	public ViewHolder(View v)
        	{
        		this.textViewTitulo =(TextView) v.findViewById(R.id.LblTitulo);
        		this.textViewSubTitulo=(TextView) v.findViewById(R.id.LblSubTitulo);
        	}
        }       	

		public View getView(int position, View convertView, ViewGroup parent) 
		{		
			ViewHolder holder;   		
        	
			if (convertView == null) 
			{		
				convertView = vi.inflate(mResource, null);    			
        		holder = new ViewHolder(convertView);    			
        		convertView.setTag(holder);
        			
        	} 
			else 
			{    			
        		holder = (ViewHolder) convertView.getTag();    			
        	}
        		
        	final Titulos item = getItem(position);
    			
        	if(item!=null)
        	{
        		holder.textViewSubTitulo.setText(Html.fromHtml(item.getSubtitulo()));
        		holder.textViewTitulo.setText(Html.fromHtml(item.getTitulo()));		
        	}
        		
        	return convertView;	
    	}    	
    }
    
    public void recargarUI()
    {
    	SQLiteDatabase db,db1;
    	DatosAplicacion dadi = new DatosAplicacion(Seleccionar_Producto.this,"InsertarDB",null,1);
    	DatosAplicacion dadb = new DatosAplicacion(Seleccionar_Producto.this,"PrincipalDB",null,1);
    	
    	db=dadb.getWritableDatabase();
    	db1=dadi.getWritableDatabase();
    	
    	String marca = spMarca.getSelectedItem().toString();
    	
    	Cursor c = db1.rawQuery("SELECT sku FROM Insercion_Productos",null);
    	Cursor d= db1.rawQuery("SELECT sku,nombre FROM Seleccionados",null);
    	
    	String sql="";
    	
    	d.moveToFirst();
    	c.moveToFirst();
    	
    	String[] skus=new String[c.getCount()+d.getCount()];
    	
    	if(c.getCount()>0)
    	{
    		for(int i=0;i<c.getCount();i++)
    		{
    			skus[i]=c.getString(0);
    			sql=sql+"?,";
    			c.moveToNext();
    		}		
    	}
    	
    	if(d.getCount()>0)
    	{
    		for(int i=(c.getCount());i<(c.getCount()+d.getCount());i++)
    		{
    			skus[i]=d.getString(0);
    			sql=sql+"?,";
    			d.moveToNext();
    		}
    	}

    	if(c.getCount()>0||d.getCount()>0)
    	{
    		sql=sql.substring(0,sql.length()-1);
    	}

    	Cursor p = db.rawQuery("SELECT nombre,sku FROM Productos WHERE idmarca = "+selectedidMarca+" AND sku NOT IN("+sql+") AND "  +
				" sku NOT IN(SELECT idsku FROM SKUCanal WHERE idcanal='"+idcanal+"' AND idsucursal="+idlocal+")", skus);
    	
    	p.moveToFirst();
    	Titulos[] titulos = new Titulos[p.getCount()];
    	
    	if(p.getCount()>0)
    	{
    		for(int i=0;i<p.getCount();i++)
    		{
    			titulos[i]=new Titulos(p.getString(1),p.getString(0));
    			p.moveToNext();
    		}
    	}
    	
    	AdaptadorTitulares adap = new AdaptadorTitulares(Seleccionar_Producto.this,titulos);
		lstProductos.setAdapter(adap); 
    	
    	Titulos[] titulosSelected=new Titulos[d.getCount()];
    	
    	if(d.getCount()>0)
    	{
    		d.moveToFirst();    		
    		
    		for(int i=0;i<d.getCount();i++)
    		{
    			titulosSelected[i]=new Titulos(d.getString(0),d.getString(1));
    			d.moveToNext();
    		}    		
    	}
    	
    	AdaptadorTitulares aadap = new AdaptadorTitulares(Seleccionar_Producto.this, titulosSelected);
		lstSeleccionados.setAdapter(aadap);

    	db.close();
    	db1.close();
    }
    
}
