package dsm.ventas_desa;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import mdls.ventas_desa.Titulos;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import db.ventas_desa.DatosAplicacion;
import db.ventas_desa.DatosInsercion;

public class Venta_En_Ruta extends Activity 
{
	private static final int VENDER = Menu.FIRST+1;
	private static final int NO_VENTA = Menu.FIRST+2;
	private static final int VER_ESTADO =Menu.FIRST;
	
	private Titulos[] datos;
	private Titulos[] datos2;
	private Titulos[] datos3;
	Date d;
	Intent intent;	
	int dia;
	int semana;
	DatosInsercion dadi;
	String diaSemana;	
	DatosAplicacion dadb;
	SQLiteDatabase db,db1;
	ListView lstClientesRuta,lstPedidos,lstNoventa;
	String[] nombreCliente;
	ArrayAdapter<String> adap,adap1,adap2;
	String[] arrayCLiente,arrayPedidos,arrayNoVENTA,arrayCliente2,arrayPedidos2,arrayNoVENTA2;
	Intent verCliente,informaNOVENTA,inicioVenta;
	private int tab =1;
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
	       
	    menu.add(0, VER_ESTADO, 0, "Ver datos Cliente");
	    menu.add(0, VENDER, 0, "Iniciar Venta");
	    menu.add(0,NO_VENTA,0,"Informar NO VENTA");        
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		String idctacte=null;
		String nombreCliente=null;
		
		if(tab==1)
		{
			Titulos tit = new Titulos();
			tit= (Titulos)lstClientesRuta.getAdapter().getItem(info.position);
			idctacte=tit.getTitulo();
			nombreCliente=tit.getSubtitulo();		
		}
		else
		{
			if(tab==2)
			{
				Titulos tit = new Titulos();
				tit = (Titulos)lstPedidos.getAdapter().getItem(info.position);
				idctacte=tit.getTitulo();
				nombreCliente=tit.getSubtitulo();
			}
			else
			{
				if(tab==3)
				{
					Titulos tit = new Titulos();
					tit=(Titulos)lstNoventa.getAdapter().getItem(info.position);
					idctacte=tit.getTitulo();
					nombreCliente=tit.getSubtitulo();
				}
			}
		}

        SharedPreferences sp = getSharedPreferences("InsercionProductos",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();		

		switch(item.getItemId())
		{
			case VER_ESTADO:
							verCliente = new Intent(Venta_En_Ruta.this,Ver_Cliente.class);	
							editor.putString("idctacte",idctacte);
							editor.commit();        
							startActivity(verCliente);
							finish();
							break;
			case NO_VENTA:	
							informaNOVENTA = new Intent(Venta_En_Ruta.this,Informar_NO_VENTA.class);
							nombreCliente=nombreCliente.split("\n")[0].trim();
							editor.putString("idctacte",idctacte);
							editor.putString("NombreCliente",nombreCliente);
							editor.commit();
							startActivity(informaNOVENTA);
							finish();
							break;
			case VENDER:
							nombreCliente=nombreCliente.split("\n")[0].trim();
							dadi= new DatosInsercion(this,"InsertarDB",null, 1);
							dadb=new DatosAplicacion(this, "PrincipalDB",null,1);
							db1= dadi.getWritableDatabase();
							inicioVenta = new Intent(Venta_En_Ruta.this,Inicio_Venta.class);		
					        Integer idcanal;
					        String idpagador;			
							db=dadb.getWritableDatabase();			
							db1.execSQL("DELETE FROM Insercion_Productos");
							Cursor c= db.rawQuery("SELECT idflete,idcanal,codlegal FROM Clientes WHERE idctacte = '"+idctacte+"' ;", null);
							c.moveToFirst();
							String idtipocliente = c.getString(0);
							idcanal = Integer.parseInt(c.getString(1).toString());
							idpagador = c.getString(2);
							c.close();
							db.close();
							db1.close();
							editor.putInt("Contador", 0);
							editor.putString("idpagador", idpagador);
							editor.putInt("idcanal",idcanal);
							editor.putString("NombreCliente",nombreCliente);
							editor.putString("idflete",idtipocliente);	
							editor.putString("idctacte",idctacte);
							editor.commit();
							startActivity(inicioVenta);
							finish();
		}
		
		return false;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta__en__ruta);
        
        //***TAB*******//        
        TabHost tabs = (TabHost)findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tab1");        
        spec.setContent(R.id.Ruta);
        spec.setIndicator("Ruta");
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("tab2");
        spec.setContent(R.id.Venta);
        spec.setIndicator("Venta");
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("tab3");
        spec.setContent(R.id.NO);
        spec.setIndicator("No Venta");
        tabs.addTab(spec);
        
        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new OnTabChangeListener() 
        {
			public void onTabChanged(String tabId) 
			{				
				if(tabId.equals("tab1"))
				{
					Log.d("TAB",""+1);
					tab = 1;
				}
				else
				{
					if(tabId.equals("tab2"))
					{
						Log.d("TAB",""+2);						
						tab=2;
					}
					else
					{
						Log.d("TAB",""+3);		
						tab=3;
					}
				}
				
			}
		});

        lstClientesRuta = (ListView)findViewById(R.id.lstClienteRuta);
        lstClientesRuta.setBackgroundColor(color.darker_gray); 
        
        lstPedidos = (ListView)findViewById(R.id.lstPedidos);
        lstNoventa=(ListView)findViewById(R.id.lstNoventa);
        
        intent = new Intent(this,Venta.class);
        
        final AlertDialog.Builder alerta = new AlertDialog.Builder(Venta_En_Ruta.this);
        alerta.setTitle("Error");
        alerta.setMessage("No hay rutas disponibles los dias Domingos");
        
        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
				startActivity(intent);
			}
		});
        
        registerForContextMenu(lstClientesRuta);
        registerForContextMenu(lstPedidos);

        dadb = new DatosAplicacion(Venta_En_Ruta.this, "PrincipalDB", null, 1);			
		d= new Date();
        semana = (getWeekOfDate(d))%4;
        dia = getDayOfTheWeek(d);        
        
        if(semana==0)
        {
        	semana=4;
        }

        switch(dia)
        {
        	case 1:
		        	diaSemana ="Error";
		        	break;
	        case 2:
		        	diaSemana ="lun";
		        	break;
	        case 3:
		        	diaSemana ="mar";
		        	break;
	        case 4:
		        	diaSemana ="mie";
		        	break;
	        case 5:
		        	diaSemana ="jue";
		        	break;
	        case 6:
		        	diaSemana ="vie";
		        	break;
	        case 7:
		        	diaSemana ="sab";
		        	break;
        }
        
        if(diaSemana.equals("Error"))
        {
        	alerta.show();
        }
        else
        {
        	Log.d("PRUEBA", ""+diaSemana+" "+semana);
        	new Onback().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_venta__en__ruta, menu);
        return true;
    }

    public static int getDayOfTheWeek(Date d)
    {
    	GregorianCalendar cal = new GregorianCalendar();
    	cal.setTime(d);
    	return cal.get(Calendar.DAY_OF_WEEK);		
    }
    
    public static int getWeekOfDate(Date d)
    {
    	GregorianCalendar cal = new GregorianCalendar();
    	cal.setTime(d);
    	return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public class Onback extends AsyncTask<Void,Void,Void>
    {
		@Override
		protected Void doInBackground(Void... params) 
		{   
			SQLiteDatabase db2;
			SQLiteDatabase db3;
			DatosInsercion dadi = new DatosInsercion(Venta_En_Ruta.this, "InsertarDB", null, 1);
			db2=dadi.getWritableDatabase();
			db3=dadi.getWritableDatabase();
			
			Cursor f = db3.rawQuery("SELECT  cliente FROM Pedidos WHERE opcion<>'VENTA'", null);
			Cursor d = db2.rawQuery("SELECT  cliente FROM Pedidos WHERE  opcion='VENTA'", null);
			Cursor p = db2.rawQuery("SELECT cliente FROM Pedidos",null);
			
			String inoventa[]= new String[f.getCount()];
			String sqlinoventa="";
			f.moveToFirst();
			
			String noped[] = new String[p.getCount()];
			String sqp="";
			p.moveToFirst();
			
			if(p.getCount()>0)
			{
				for(int i=0;i<p.getCount();i++)
				{
					noped[i]=p.getString(0);
					sqp=sqp+"?,";
					p.moveToNext();
				}
			}
			else
			{
				sqp="?";
			}
			
			sqp=sqp.substring(0,sqp.length()-1);
			
			if(f.getCount()>0)
			{
				for(int i=0;i<f.getCount();i++)
				{
					inoventa[i]=f.getString(0);					
					sqlinoventa =sqlinoventa+"?,";
					f.moveToNext();
				}
			}
			else
			{
				sqlinoventa="?";
			}
			
			sqlinoventa = sqlinoventa.substring(0,sqlinoventa.length()-1);			
			db3=dadb.getWritableDatabase();
			
			Cursor g = db3.rawQuery("SELECT sigla,idctacte,nombre FROM Clientes WHERE idctacte IN("+sqlinoventa+")",inoventa);
			
			arrayNoVENTA = new String[f.getCount()];
			arrayNoVENTA2 = new String[f.getCount()];
			g.moveToFirst();

			if(g.getCount()>0)
			{
				for(int i=0;i<g.getCount();i++)
				{					
					arrayNoVENTA[i] = g.getString(2)+" \n "+g.getString(0);
					arrayNoVENTA2[i]=g.getString(1);
					g.moveToNext();
							
				}
			}		
			
			String pedidos[]= new String[d.getCount()];		
			
			d.moveToFirst();
			String sql="";
			
			if(d.getCount()>0)
			{
				for(int i = 0;i<d.getCount();i++)
				{
					pedidos[i]=d.getString(0);
					sql=sql+"?,";					
					d.moveToNext();
				}
			}			
			else
			{				
				sql="?";
			}		
			
			String[] noventa = new String[d.getCount()];
			String sql1 ="";
			d.moveToFirst();
			
			if(d.getCount()>0)
			{
				for(int i = 0;i<d.getCount();i++)
				{
					noventa[i]=d.getString(0);	
					sql1=sql1+"?,";
					d.moveToNext();
				}
			}
			else
			{
				sql1="?";
			}
			
			String sqlfinal = sql.substring(0,sql.length()-1);
			sql1 = sql1.substring(0,sql1.length()-1);
			
	        db = dadb.getWritableDatabase();
	        
	        Cursor d1 = db.rawQuery("SELECT sigla,idctacte,nombre FROM Clientes WHERE idctacte IN ("+sqlfinal+")",pedidos);
	        
	        String[] nombrePedidos = new String[d1.getCount()];
	        String[] idctactePed = new String[d1.getCount()];
	        d1.moveToFirst();
	        
	        if(d1.getCount()>0)
	        {
	        	for(int i=0;i<d1.getCount();i++)
	        	{
	        		nombrePedidos[i]=d1.getString(2)+" \n "+d1.getString(0);
	        		idctactePed[i]=d1.getString(1);
	        		d1.moveToNext();
	        	}
	        }
	        
	        arrayPedidos = new String[d1.getCount()];
	        arrayPedidos2 = new String[d1.getCount()];
	        
	        if(d1.getCount()>0)
	        {
		        for(int i=0;i<nombrePedidos.length;i++)
		        {
		        	arrayPedidos[i]=nombrePedidos[i];
		        	arrayPedidos2[i]=idctactePed[i];
		        }
	        }	        
	        
	        Log.d("TEST",diaSemana+semana);
	        
	        Cursor c =  db.rawQuery("SELECT idcliente FROM Rutas WHERE "+diaSemana+"cob = 1 AND semana"+semana+"cob = 1 AND idcliente NOT IN ("+sqp+");",noped);
	        String[] ids = new String[c.getCount()];        
	        String s="";
	        c.moveToFirst();	        
	       
	        if(c.getCount()>0)
	        {
		        for(int i=0;i<c.getCount();i++)
		        {
		        	ids[i] = c.getString(0);
		        	s= s+"?,";
		        	c.moveToNext();
		        }	        	        
	        	       
	        }
	        else
	        {
	        	s="?";
	        }
	        
	        s=s.substring(0,s.length()-1);	        
		    Cursor c1 = db.rawQuery("SELECT sigla,idctacte,nombre FROM Clientes WHERE idctacte IN ("+s+")",ids);	
		    nombreCliente = new String[c1.getCount()];
		    String idCliente[] = new String[c1.getCount()];
		    c1.moveToFirst();

		    for(int i=0;i<c1.getCount();i++)
		    {
		    	nombreCliente[i]=c1.getString(2)+" \n "+c1.getString(0);
		        idCliente[i]=c1.getString(1);
		        c1.moveToNext();		        		
		    }
	        	
		    arrayCLiente = new String[c1.getCount()];
		    arrayCliente2 = new String[c1.getCount()];
		        	
		    for(int i=0;i<c1.getCount();i++)
		    {
		    	arrayCLiente[i]= nombreCliente[i].toString();
		        arrayCliente2[i]=idCliente[i];
		    }

	        c.close();
	        db.close();	
	        db2.close();
	        db3.close();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if(arrayCLiente.length>0)
			{
				datos = new Titulos[arrayCLiente.length];
				
				for(int i=0;i<arrayCLiente.length;i++)
				{
					datos[i]=new Titulos(arrayCliente2[i],arrayCLiente[i]);
				}
				
				AdaptadorCliente adaptador = new AdaptadorCliente(Venta_En_Ruta.this);			
				lstClientesRuta.setAdapter(adaptador);			
			}
			
			if(arrayPedidos.length>0)
			{
				datos2 = new Titulos[arrayPedidos.length];
				
				for(int i=0;i<arrayPedidos.length;i++)
				{
					datos2[i]=new Titulos(arrayPedidos2[i],arrayPedidos[i]);
				}
				
				AdaptadorPedidos adaptador1 = new AdaptadorPedidos(Venta_En_Ruta.this);			
				lstPedidos.setAdapter(adaptador1);		
			}					
			
			if(arrayNoVENTA.length>0)
			{
				datos3 = new Titulos[arrayNoVENTA.length];
				
				for(int i=0;i<arrayNoVENTA.length;i++)
				{
					datos3[i]=new Titulos(arrayNoVENTA2[i],arrayNoVENTA[i]);
				}
				
				AdaptadorNOVENTA adaptador2 = new AdaptadorNOVENTA(Venta_En_Ruta.this);			
				lstNoventa.setAdapter(adaptador2);			
			}

			super.onPostExecute(result);
		}
    }
		
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Venta_En_Ruta.this,Venta.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
    class AdaptadorCliente extends ArrayAdapter 
    {
    	Activity context;
    	
    	AdaptadorCliente(Activity context)
    	{
    		super(context, R.layout.listitem_titulos,datos);
    		this.context = context;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) 
    	{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_titulos, null);
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
			lblTitulo.setText(datos[position].getTitulo());
			
			TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText(datos[position].getSubtitulo());
			
			return(item);
		}
    }
    
    class AdaptadorPedidos extends ArrayAdapter 
    {
    	Activity context;
	
    	AdaptadorPedidos(Activity context) 
    	{
    		super(context, R.layout.listitem_titulos,datos2);
    		this.context = context;
    	}
	
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_titulos, null);
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
			lblTitulo.setText(datos2[position].getTitulo());
			
			TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText(datos2[position].getSubtitulo());
			
			return(item);
		}
    }
    
    class AdaptadorNOVENTA extends ArrayAdapter 
    {
	
    	Activity context;
	
    	AdaptadorNOVENTA(Activity context) 
		{
			super(context, R.layout.listitem_titulos,datos3);
			this.context = context;
		}
	
    	public View getView(int position, View convertView, ViewGroup parent) 
    	{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_titulos, null);
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
			lblTitulo.setText(datos3[position].getTitulo());
			
			TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText(datos3[position].getSubtitulo());
			
			return(item);
    	}
    }
	  
}

