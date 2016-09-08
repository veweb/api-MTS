package com.authorwjf.quickgpsdemo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MiLocationListener implements LocationListener {
	private static final long MIN_TIME = 1 * 5 * 1000; //10 segundos para la actualizacion de ubicacion de la terminal
	private double latActual  = 0;
    private double longActual = 0;
    private double ultimaPoslat = 0;
    private double ultimaPoslng = 0;
    private double latIni = 0;
    private double longIni= 0;
    private double latFin =  0.0;
    private double longfin = 0.0;
    private float distance = 0;
    private float distanciaMtsTotal = 0;
    private float distanciaKmTotal = 0;
	private Chronometer chronometer;
	private MiLocationListener context = this;
	public TextView kilometros;
	public TextView metros;
	public String distanciaKm ;
	public String distanciaMtr;
	private float tarifas = 0;
	private float tarifaTotal = 0;
	private float total = 0;
	private float mtrTotal = 0;
	String provider;
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		 if(location != null)
		  {		
	     //Capturamos la posicion de la terminal	
			latActual =    location.getLatitude();
		    longActual =   location.getLongitude(); 
	      if(latIni == 0 && longIni == 0){
	    	  latIni =    location.getLatitude();
		      longIni =   location.getLongitude(); 
		      ultimaPoslat =    location.getLatitude();
			  ultimaPoslng =   location.getLongitude();
		     //Toast.makeText( getApplicationContext(),"POSICION INICIAL: "+ Double.toString(latIni) +","+Double.toString(longIni),Toast.LENGTH_SHORT ).show();
			   
	      }
	 
	    
	     	         Location locationA = new Location(ultimaPoslat+","+ultimaPoslng);
				      locationA.setLatitude(ultimaPoslat);
				      locationA.setLongitude(ultimaPoslng);
				      Location locationB = new Location(latActual+","+longActual);
				      locationB.setLatitude(latActual);
				      locationB.setLongitude(longActual);
				      
				      
				     //Toast.makeText( getApplicationContext(), "distancia: "+ distancia+" distancia:"+distance ,Toast.LENGTH_SHORT ).show();
					    
	    		 
			     
			    // Toast.makeText( getApplicationContext(), " UltimaLatitude: "+ ultimaPoslat + "ActualLat: " + latActual ,Toast.LENGTH_SHORT ).show();
				    
			      //String sSelectivityRate = String.valueOf(distance);
			      //kilometraje.setText("9.8"); 
			      //int distance = getDistance(latIni,longIni,latActual,longActual);
			      //if(distance > 1){
			    	 
			    	  distance = ((float) locationA.distanceTo(locationB));
			    	  
			    	  //if(distance >= 3) // parametro de error de distancia en un punto muerto
			    	  
			    	     distanciaMtsTotal = distanciaMtsTotal + distance;
			    	     
			    	  if( mtrTotal >= 9){
			    		  distanciaKmTotal = distanciaKmTotal + 1;
			    		  mtrTotal = 0;
			    		  distanciaKm = String.format("%.0f", distanciaKmTotal); 
					      final TextView kilometros = (TextView) findViewById(R.id.txt_kilometros);
				  	      kilometros.setText(distanciaKm);
				  	     // metros.setText("0");
			    		  
			    	  }
			    	  else{
			    		
			    		  if(distanciaMtsTotal >= 99){
			    			  mtrTotal = mtrTotal + 1;// lleva 10 mtrs mas
			    			  distanciaMtsTotal = 0;
			    			  distanciaMtr = String.format("%.0f",distanciaMtsTotal); 
						      
					  	      
					  	    	 
			    		  }
			    		  final TextView metros = (TextView) findViewById(R.id.txt_metros);
			    		  metros.setText(" "+ String.format("%.0f",mtrTotal));
			    		 Toast.makeText( getApplicationContext(),"DISTANCIA: "+ Double.toString(distanciaMtsTotal),Toast.LENGTH_SHORT ).show();
							
			    		  
			    		  
			    	  }
			    	
				      
			  	   // if(ultimaPoslat != latActual){
		    		     ultimaPoslat =   latActual;
				         ultimaPoslng =   longActual;
		      //}	         
			
			      
	    	  
	    //  }
	     
	     
	      //String aLatitude = Double.toString(latitude); 
		  //String aLongitude =Double.toString(longitude);
		 
		  }// fin de condicion
	        
	    }

		private Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

		private TextView findViewById(int txtKilometros) {
		// TODO Auto-generated method stub
		return null;
	}

		  


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
