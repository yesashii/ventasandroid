package dsm.ventas_desa;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Venta extends Activity 
{
	Intent intent;
	Button btnSinRuta;
	Button btnConRuta;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);        
        
        btnSinRuta = (Button)findViewById(R.id.btnClienteSinRuta);
        btnConRuta = (Button)findViewById(R.id.btnClienteConRuta);        

        btnSinRuta.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{				
				intent = new Intent(Venta.this,Venta_No_Ruta.class);
				startActivity(intent);
				finish();
			}
		});
        
        btnConRuta.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				intent = new Intent(Venta.this,Venta_En_Ruta.class);
				startActivity(intent);
				finish();
			}
		});   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_cliente__ruta_principal, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Venta.this,Principal.class);
    	startActivity(intent);    	
    	finish();
    	super.onBackPressed();
    }
}
