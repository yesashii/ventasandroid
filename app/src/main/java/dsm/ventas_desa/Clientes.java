package dsm.ventas_desa;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Clientes extends Activity 
{
	private Button btnNuevoCliente;
	private Button btnVerClientes;
	private Intent NuevoCliente;
	private Intent VerClientes;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        
        btnNuevoCliente = (Button)findViewById(R.id.btnNuevoCliente);
        btnVerClientes = (Button)findViewById(R.id.btnVerClientes);
        
        NuevoCliente = new Intent(this,Nuevo_Cliente.class);
        VerClientes = new Intent(this,Listado_Clientes.class);
        
        btnNuevoCliente.setOnClickListener(new OnClickListener() 
        {			
			public void onClick(View v) 
			{
				startActivity(NuevoCliente);
				finish();
			}
		});
        
        btnVerClientes.setOnClickListener(new OnClickListener() 
        {	
			public void onClick(View v) 
			{
				startActivity(VerClientes);
				finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_clientes, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	Intent intent=new Intent(Clientes.this,Principal.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
    }
    
}
