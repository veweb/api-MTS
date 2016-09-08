package com.authorwjf.quickgpsdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



public class taximetro extends Activity  {
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
	private Context context = this;
	public TextView kilometros;
	public TextView metros;
	public String distanciaKm ;
	public String distanciaMtr;
	private float tarifas = 0;
	private float tarifaTotal = 0;
	private float total = 0;
	private float mtrTotal = 0;
	public List<NameValuePair> lista1 = new ArrayList<NameValuePair>();

	
	
	
	
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.taximetro);
		super.onCreate(savedInstanceState);
		final Button btn_iniciar = (Button) findViewById(R.id.start_button);
		final Button btn_regresar = (Button) findViewById(R.id.regresar);
		
		
		
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		 final LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 final LocationListener milocListener = new MiLocationListener();
			
         

		// prepare the alert box                   
       final  AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
   
       final CharSequence[] tarifa = {"Con trafico","Sin trafico","Trafico normal"};

       AlertDialog.Builder alert = new AlertDialog.Builder(this);

       alert.setTitle("Seleccione tipo de tarifa:");

       alert.setSingleChoiceItems(tarifa,-1, new 

       DialogInterface.OnClickListener()

       {

           @Override
           public void onClick(DialogInterface dialog, int which) 
           {
               if(tarifa[which]=="Con trafico")

               {

                   tarifas=(float) 6.5;
                   dialog.cancel();
                   dialog.dismiss();
               }

               else if (tarifa[which]=="Sin trafico")

               {

                   tarifas=(float) 4.8;
                   dialog.cancel();
                   dialog.dismiss();

               }
               else if (tarifa[which]=="Trafico normal")

               {

                   tarifas=6;
                   dialog.cancel();
                   dialog.dismiss();

               }
              
           }
           
       });
       alert.show();	
       
		
	 	final Button btn_iniciar1 = (Button) findViewById(R.id.start_button);
		btn_iniciar1.setOnClickListener(new Button.OnClickListener(){   
	        
	    	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	    	 chronometer.setBase(SystemClock.elapsedRealtime());
	    	 chronometer.start();
	   	     milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 5, milocListener);

	           
	    	}
		});
		
		final Button btn_parar = (Button) findViewById(R.id.stop_button);
		btn_parar.setOnClickListener(new Button.OnClickListener(){ 
			
	        
	    	@Override
			public void onClick(View v) {
	    		

			      final TextView metros = (TextView) findViewById(R.id.txt_metros);

			      final TextView kilometros = (TextView) findViewById(R.id.txt_kilometros);
	    		
	    		alertbox.setTitle("�Desea terminar servicio?");  
	            // set the message to display
	    		if(distanciaKm == null)
	    			distanciaKm = "0";
	    		float m = (mtrTotal * 100) / 1000;
	    		
	    		float mm =  Float.parseFloat(distanciaKm) + m;
	    		
		
	    		//Toast.makeText( getApplicationContext(),"Total: "+ Double.toString(m) +","+total,Toast.LENGTH_SHORT ).show();
				   
	    		tarifaTotal = mm * tarifas;
	    		if(tarifaTotal < 1){ tarifaTotal = 0; }
	    		//Toast.makeText( getApplicationContext(),"Total: "+ Double.toString(m) +","+Double.toString(total),Toast.LENGTH_SHORT ).show();
				   
	            alertbox.setMessage(Html.fromHtml("<font size=\"100px\"><big>"+"<b>Distancia:</b> " + distanciaKm + " km con " + String.format("%.0f",mtrTotal)  + "00 mts. <br>" +
	            		            "<b>Tiempo:</b>" + chronometer.getText() + "<br>" +
	            		             "<b>Total a Pagar:  </b><br> <h1> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Q. " + String.format("%.2f",tarifaTotal) +"</h1></big></font>") );
	            
	                     
	            // set a positive/yes button and create a listener                    
	            alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
	                 
	                // do something when the button is clicked
	                public void onClick(DialogInterface arg0, int arg1) {
	                	
	                	// TODO Auto-generated method stub
	    	    	    milocManager.removeUpdates(milocListener);
	    	    		chronometer.stop();
	    		    	chronometer.setBase(SystemClock.elapsedRealtime());
	    		    	 HttpClient httpclient = new DefaultHttpClient();  
	    		  		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/updateservicio.php");
	    		  		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    		  		        
	    		  		  
	    		  		    //Encapsulamos
	    		  		    try {
								httppost.setEntity(new UrlEncodedFormEntity(lista1));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}          
	    		  		    //Lanzamos la petici�n
	    		  		   try {
							HttpResponse respuesta = httpclient.execute(httppost);
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    		  	     	
	    		    	
	    		    	distanciaMtsTotal = 0;
	    		    	distanciaKmTotal = 0;
	    		    	 kilometros.setText("0");
	    		    	 metros.setText("0");
	    		    	 mtrTotal = 0;
	    		    	
	    	    		
	    	    		//chronometer = null;
	    	    		
	                }
	            });
	             
	            // set a negative/no button and create a listener 
	            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
	 
	                // do something when the button is clicked                
	                public void onClick(DialogInterface arg0, int arg1) {
	                    //Toast.makeText(getApplicationContext(), "'No' button clicked", Toast.LENGTH_SHORT).show();
	                }
	            });
	             
	            // display box
	            alertbox.show();
	           
	    		
	    		
	    		
				
	    	}
	});	
		
		
		
btn_regresar.setOnClickListener(new Button.OnClickListener(){ 
			
	        
	    	@Override
			public void onClick(View v) {
	    		
	    		Intent i = new Intent(context, Main.class );
				 startActivity(i);
				 finish();
	    	}
	});		
		
	}
 public class MiLocationListener implements LocationListener
  { 
	 
	public void onLocationChanged(Location loc)
    {
      if(loc != null)
	  {		
	    //Capturamos la posicion de la terminal	
	    latActual =    loc.getLatitude();
		longActual =   loc.getLongitude(); 
	    if(latIni == 0 && longIni == 0){
	       latIni =    loc.getLatitude();
		   longIni =   loc.getLongitude(); 
		   ultimaPoslat =    loc.getLatitude();
		   ultimaPoslng =   loc.getLongitude();
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
			   
			    	 
			    	  distance = ((float) locationA.distanceTo(locationB));
			    	
			    	  
			    	 // if(distance >= 10 ){ //parametro de error de distancia en un punto muerto
			    		  distanciaMtsTotal = distanciaMtsTotal + distance;
			    		  
			    	  // }
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
			    			  lista1.add(new BasicNameValuePair("posicion[]", latActual+","+longActual));
			    			  
					  	      
					  	    	 
			    		  }
			    		  final TextView metros = (TextView) findViewById(R.id.txt_metros);
			    		  metros.setText(" "+ String.format("%.0f",mtrTotal));
			    		// Toast.makeText( getApplicationContext(),"DISTANCIA: "+ Double.toString(distanciaMtsTotal),Toast.LENGTH_SHORT ).show();
							
			    		  
			    		  
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

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		  
		  
	         //EnviarDatos2(aLatitude,aLongitude);
	        // testLocation(14.66688115,-90.48853434,latitude,longitude);
	    
	    
	    
	    }
	 
	
		
	   
	    public void onProviderDisabled(String provider)
	    {
	    Toast.makeText( getApplicationContext(),"Gps Desactivado",Toast.LENGTH_SHORT ).show();
	    }
	    public void onProviderEnabled(String provider)
	    {
	    Toast.makeText( getApplicationContext(),"Gps Activo",Toast.LENGTH_SHORT ).show();
	    }
	    public void onStatusChanged(String provider, int status, Bundle extras){}
	    
	 

     
     
        
	private String StreamToString(InputStream is) {
			// TODO Auto-generated method stub
			return null;
		}



	public static int getDistance(int lat_a,int lng_a, int lat_b, int lon_b){
		  int Radius = 6371000; //Radio de la tierra
		  double lat1 = lat_a / 1E6;
		  double lat2 = lat_b / 1E6;
		  double lon1 = lng_a / 1E6;
		  double lon2 = lon_b / 1E6;
		  Log.i ("PUNTOS: ",lat1+" - "+lat2);
		  double dLat = Math.toRadians(lat2-lat1);
		  double dLon = Math.toRadians(lon2-lon1);
		  double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
		  double c = 2 * Math.asin(Math.sqrt(a));
		  return (int) (Radius * c);  

		 }
	
	public static String Distance2(float distance){  
		  if(distance >= 1000){
		   float k = distance / 1000;
		   float m = distance - (k*1000);
		   m = m / 100;
		   DecimalFormat df = new DecimalFormat("#.#");
		   return String.valueOf(df.format(k)); //+ (m>0?("."+String.valueOf(m)):"") + " Km" +(k>1?"s":"");
		  } else {
			  DecimalFormat df = new DecimalFormat("#.#");
			  distance = distance;
		   return String.valueOf(df.format(distance)); //+ " metro"+(distance==1?"":"s");
		  }  
		 }
	


	
//	@Override
//	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		//switch(v.getId()) {
		//case R.id.start_button:
	//		chronometer.getBase();
	//		chronometer.start();
	//		 LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	//	     LocationListener milocListener = new MiLocationListener();
	//	     milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);
		 
		//	break;
		// case R.id.stop_button:
		//	chronometer.stop();
	//		break;
		//case R.id.regresar:
		//	 Intent i = new Intent(context, Main.class );
		//	 startActivity(i);
		//	 break;
	//	}
		
		
	//}
	
}
