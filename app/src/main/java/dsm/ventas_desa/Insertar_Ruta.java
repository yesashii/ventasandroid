package dsm.ventas_desa;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class Insertar_Ruta extends Activity 
{
	CheckBox lunes;
	CheckBox martes;
	CheckBox miercoles;
	CheckBox jueves;
	CheckBox viernes;
	CheckBox sabado;
	
	CheckBox semana1;
	CheckBox semana2;
	CheckBox semana3;
	CheckBox semana4;
	
	Button continuar;
	
	SharedPreferences sp;
	
	Intent intent;
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar__ruta);
        
        lunes = (CheckBox)findViewById(R.id.cbLuncob);
        martes = (CheckBox)findViewById(R.id.cbMarcob);
        miercoles = (CheckBox)findViewById(R.id.cbMiecob);
        jueves = (CheckBox)findViewById(R.id.cbJuecob);
        viernes = (CheckBox)findViewById(R.id.cbViecob);
        sabado = (CheckBox)findViewById(R.id.cbSabcob);
        
        semana1 = (CheckBox)findViewById(R.id.cbsemana1);
        semana2 = (CheckBox)findViewById(R.id.cbsemana2);
        semana3 = (CheckBox)findViewById(R.id.cbsemana3);
        semana4 = (CheckBox)findViewById(R.id.cbsemana4);
        
        continuar = (Button)findViewById(R.id.btnIRcontinuar);
        
        intent = new Intent(this,Resumen.class);
        
        sp = getSharedPreferences("DatosCliente",Context.MODE_PRIVATE);
    	final SharedPreferences.Editor editor = sp.edit();
    	
    	continuar.setOnClickListener(new OnClickListener() 
    	{
			public void onClick(View v) 
			{
				if(lunes.isChecked())
				{
					editor.putInt("LunCob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("LunCob",0);
					editor.commit();
				}
				
				if(martes.isChecked())
				{
					editor.putInt("MarCob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("MarCob",0);
					editor.commit();
				}
				
				if(miercoles.isChecked())
				{
					editor.putInt("MieCob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("MieCob",0);
					editor.commit();
				}
				
				if(jueves.isChecked())
				{
					editor.putInt("JueCob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("JueCob",0);
					editor.commit();
				}
				
				if(viernes.isChecked())
				{
					editor.putInt("VieCob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("VieCob",0);
					editor.commit();
				}
				
				if(sabado.isChecked())
				{
					editor.putInt("SabCob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("SabCob",0);
					editor.commit();
				}
				
				if(semana1.isChecked())
				{
					editor.putInt("Semana1Cob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("Semana1Cob",0);
					editor.commit();
				}
				
				if(semana2.isChecked())
				{
					editor.putInt("Semana2Cob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("Semana2Cob",0);
					editor.commit();
				}
				
				if(semana3.isChecked())
				{
					editor.putInt("Semana3Cob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("Semana3Cob",0);
					editor.commit();
				}
				
				if(semana4.isChecked())
				{
					editor.putInt("Semana4Cob",1);
					editor.commit();
				}
				else
				{
					editor.putInt("Semana4Cob",0);
					editor.commit();
				}
				
				startActivity(intent);
				finish();
			
				Log.d("PRUEBAS", ""+sp.getInt("LunCob", -1));
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_insertar__ruta, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {    	
    	finish();
    	super.onBackPressed();
    }
    
}
