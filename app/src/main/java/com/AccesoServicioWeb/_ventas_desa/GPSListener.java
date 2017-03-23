package com.AccesoServicioWeb._ventas_desa;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSListener implements LocationListener 
{
	public static double latitude;
	public static double longitude;
	public static long time;

	public void onLocationChanged(Location loc)
	{
		loc.getLatitude();
		loc.getLongitude();
		loc.getTime();
		
		latitude=loc.getLatitude();
		longitude=loc.getLongitude();
		time=loc.getTime();
	}

	public void onProviderDisabled(String provider) 
	{
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub
	}

	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub
	}

}
