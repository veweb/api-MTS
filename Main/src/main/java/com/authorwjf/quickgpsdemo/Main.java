package com.authorwjf.quickgpsdemo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.*;
import android.annotation.TargetApi;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
//import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.RingtoneManager;



public class Main extends Activity {
	
	private static final long MIN_TIME = 1 * 10 * 1000; //10 segundos para la actualizacion de ubicacion de la terminal
	private int msg ;
	final Context context = this;
	int alerta = 0;
	int enruta = 0;
	int mNotificationId = 001;
	int contadorTiempoImagenes=0;
	
	ImageView imagen;
	ArrayList<String> nameImages = new ArrayList<String>();
	int contImages;
	Thread thread=null;
	boolean messageUp=false;
	boolean verificandoTiempo=false;
	RelativeLayout anuncios;
	long actualtime=0;
	ImageAdd anunciosAsync;

	

    
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	 setContentView(R.layout.main);
         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
         StrictMode.setThreadPolicy(policy);
         msg =0;
         TelephonyManager mTelephonyManager;
		   mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		  Toast.makeText( getApplicationContext(),"telefono: "+ mTelephonyManager.getLine1Number(),Toast.LENGTH_LONG).show();
			
 	    	 final LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
 	         final LocationListener milocListener = new MiLocationListener();
 	         
 	         milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME, 0, milocListener);
 	         
        super.onCreate(savedInstanceState);  final Button estadolib = (Button) findViewById(R.id.btn_estadolib);
        final TextView identificador = (TextView) findViewById(R.id.identificador);
        final Button estadoocup = (Button) findViewById(R.id.btn_estadooc);
        final Button salir = (Button) findViewById(R.id.btn_salir);
        final Button taximetro = (Button) findViewById(R.id.btn_taximetro);
        final Button fueraServicio = (Button) findViewById(R.id.btn_fueraServicio);
        final Button mensajes = (Button) findViewById(R.id.btn_mensajes);
        final TextView estado = (TextView) findViewById(R.id.estado);
        final TextView nmensajes = (TextView) findViewById(R.id.nmensajes);
        final TextView serviciocliente = (TextView) findViewById(R.id.serviciocliente);
        anuncios = (RelativeLayout)findViewById(R.id.anuncios);
        imagen = (ImageView)findViewById(R.id.LugarAnuncios);
        
        estado.setText("LIBRE");
        estado.setTextColor(Color.BLUE);
        taximetro.setVisibility(View.VISIBLE	);
        estadolib.setVisibility(View.INVISIBLE);
        estadoocup.setVisibility(View.VISIBLE);
        serviciocliente.setVisibility(View.INVISIBLE);
        
        
        nameImages.add("Taxis_01.jpg");
        nameImages.add("Taxis_02.jpg");
        nameImages.add("Taxis_03.jpg");
        nameImages.add("Taxis_04.jpg");
        nameImages.add("Taxis_05.jpg");
        nameImages.add("Taxis_06.jpg");
        nameImages.add("Taxis_07.jpg");
        nameImages.add("Taxis_08.jpg");
        nameImages.add("Taxis_09.jpg");
        nameImages.add("Taxis_10.jpg");
        nameImages.add("Taxis_11.jpg");
        nameImages.add("Taxis_12.jpg");
        nameImages.add("Taxis_13.jpg");
        nameImages.add("Taxis_14.jpg");
        nameImages.add("Taxis_15.jpg");
        nameImages.add("Taxis_16.jpg");
        nameImages.add("Taxis_17.jpg");
        nameImages.add("Taxis_18.jpg");
        nameImages.add("Taxis_19.jpg");


        contImages = 0;
        
        this.actualtime = System.currentTimeMillis();
        
       
        
        
 salir.setOnClickListener(new Button.OnClickListener(){   
            
        	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		
        		finish();
        		
        	}
 });
 
 taximetro.setOnClickListener(new Button.OnClickListener(){   
     
 	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
 		  milocManager.removeUpdates(milocListener);
 		
 		 Intent i = new Intent(context, taximetro.class );
         startActivity(i);
         finish();
 		
 	    }
     });
        
mensajes.setOnClickListener(new Button.OnClickListener(){   
     
	 	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	 		  milocManager.removeUpdates(milocListener);
	 		
	 		 Intent i = new Intent(context, mensajeria.class );
	         startActivity(i);
	         finish();
	 		
	 	    }
	     });
	   
	  
        estadoocup.setOnClickListener(new Button.OnClickListener(){   
            
        	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		
        		estadoocup.setVisibility(View.INVISIBLE);
        		estadolib.setVisibility(View.VISIBLE);
        		estado.setText("OCUPADO");
        		estado.setTextColor(Color.RED);
        		try {
					enviaestado("2");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
				
			}
     	   public void enviaestado(String c) throws ClientProtocolException, IOException{
    	 	   
  	     	 
    	       HttpClient httpclient = new DefaultHttpClient();  
    		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/estadounidad.php");
    		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
    		   
    		    postValues.add(new BasicNameValuePair("estado", c));
    		    postValues.add(new BasicNameValuePair("id_device", deviceId));
    		    //Encapsulamos
    		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
    		    //Lanzamos la petici�n
    		    HttpResponse respuesta = httpclient.execute(httppost);
    	     	
    		    
    		    //Log.d("Resultado","------> "+ respuesta);
    		    //Conectamos para recibir datos de respuesta
    		    HttpEntity entity = respuesta.getEntity();
    		    //Creamos el InputStream como su propio nombre indica
    		    InputStream is = entity.getContent();
    		    String resultado= StreamToString(is);
    		    identificador.setText(resultado); 
    		 
    } 

        
        
        });
        
        
       estadolib.setOnClickListener(new Button.OnClickListener(){   
            
        	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		
        		estadolib.setVisibility(View.INVISIBLE);
        		estadoocup.setVisibility(View.INVISIBLE);
        		fueraServicio.setVisibility(View.VISIBLE);
        		serviciocliente.setVisibility(View.INVISIBLE);
        		serviciocliente.setText("");
        		 estado.setText("LIBRE");
        	        estado.setTextColor(Color.BLUE);
        		try {
					enviaestado("1");
					enruta = 0;
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

     	   public void enviaestado(String c) throws ClientProtocolException, IOException{
    	 	   
  	     	 
    	       HttpClient httpclient = new DefaultHttpClient();  
    		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/estadounidad.php");
    		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
    		   
    		    postValues.add(new BasicNameValuePair("estado", c));
    		    postValues.add(new BasicNameValuePair("id_device", deviceId));
    		    //Encapsulamos
    		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
    		    //Lanzamos la petici�n
    		    HttpResponse respuesta = httpclient.execute(httppost);
    	     	
    		     
    		    //Log.d("Resultado","------> "+ respuesta);
    		    //Conectamos para recibir datos de respuesta
    		    HttpEntity entity = respuesta.getEntity();
    		    //Creamos el InputStream como su propio nombre indica
    		    InputStream is = entity.getContent();
    		    String resultado= StreamToString(is);
    		   
    	  
    } 			
        
        
        });
            
	  fueraServicio.setOnClickListener(new Button.OnClickListener(){   
          
      	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
      		
      		estadolib.setVisibility(View.VISIBLE);
      		estadoocup.setVisibility(View.INVISIBLE);
      		fueraServicio.setVisibility(View.INVISIBLE);
      		estado.setText("FUERA DE SERVICIO");
      	        estado.setTextColor(Color.YELLOW);
      		try {
					enviaestado("3");
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

   	   public void enviaestado(String c) throws ClientProtocolException, IOException{
  	 	   
	     	 
  	       HttpClient httpclient = new DefaultHttpClient();  
  		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/estadounidad.php");
  		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
  		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
  		   
  		    postValues.add(new BasicNameValuePair("estado", c));
  		    postValues.add(new BasicNameValuePair("id_device", deviceId));
  		    //Encapsulamos
  		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
  		    //Lanzamos la petici�n
  		    HttpResponse respuesta = httpclient.execute(httppost);
  	     	
  		     
  		    //Log.d("Resultado","------> "+ respuesta);
  		    //Conectamos para recibir datos de respuesta
  		    HttpEntity entity = respuesta.getEntity();
  		    //Creamos el InputStream como su propio nombre indica
  		    InputStream is = entity.getContent();
  		    String resultado= StreamToString(is);
  		   
  	  
  } 			
      
      
      });
	  
	  
	  
	  
      ImageButton btnDismiss = (ImageButton)findViewById(R.id.dismiss);
      btnDismiss.setOnClickListener(new Button.OnClickListener(){
			    @Override
			    public void onClick(View v) {
			     // TODO Auto-generated method stub
			     messageUp = false;
			     anuncios.setVisibility(View.GONE);
		     
			    // VerificarTiempoDeAnuncios();
			     
	    }});
      

     // VerificarTiempoDeAnuncios();
      contImages=0;
      contadorTiempoImagenes=13;
	  messageUp=true;
	  anunciosAsync= new ImageAdd();   
	  anunciosAsync.execute();
	

   
	}
	
	
    private class ImageAdd extends AsyncTask<Void, Integer, Boolean> {   	
    	
        @Override
        protected void onPreExecute() {
        	anuncios.setVisibility(View.VISIBLE);
        	 messageUp=true;
        }
    	 
        @Override 
        protected Boolean doInBackground(Void... params) {
        	Log.d("entro oncrate", "doInBackground");
        	try {      		
        		while(true ){
        			

        			if(messageUp){
        				if(contadorTiempoImagenes>12){
        					publishProgress();
        					contadorTiempoImagenes=0;
        				}	
        				contadorTiempoImagenes++;
        			}else{
						long diferencia = System.currentTimeMillis()-actualtime;
						if(diferencia > 50000){
							Log.d("paso tiempo", "PASO TIEMPO");
							publishProgress();
						}
        			}
        			Thread.sleep(1000);
        			
        				
        		}		

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

        	
            return true;
        }
        
        protected void onProgressUpdate(Integer... progress) {
        	if(messageUp){
		        	Drawable d;
					try {
						if(contImages>= nameImages.size()){
							contImages=0;
						}
						
						d = Drawable.createFromStream(getAssets().open(nameImages.get(contImages)), null);
						imagen.setBackgroundDrawable(d);
						contImages++;	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	}else{
        		
        		messageUp=true;
        		anuncios.setVisibility(View.VISIBLE); 
        	}
        	
        }
     

        @Override
        protected void onPostExecute(Boolean result) {

        }

    }
    
  /*  
    
	public void VerificarTiempoDeAnuncios(){
		
		   verificandoTiempo = true;
		   Thread verificarTiempoThread = new Thread(){
		        public void run(){	        	
		        	while(verificandoTiempo){
		        		try {
							Thread.sleep(1000);
							 long diferencia = System.currentTimeMillis()-actualtime;
							if(diferencia > 50000){
								Log.d("paso tiempo", "PASO TIEMPO");
								
								  runOnUiThread(new Runnable() {  
						              @Override
						              public void run() {
						            	  messageUp=true;
						            	 anuncios.setVisibility(View.VISIBLE); 
						              } });
								  verificandoTiempo=false;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		        }
		      };				   
		      verificarTiempoThread.start();
		
	}
	*/
	
		     
	     
	     @Override
	     public void onUserInteraction(){
	    	 
	    	 
	    	 long diferencia = System.currentTimeMillis()-this.actualtime;
	    	 Log.d("STATUS-TIEMPO",diferencia  + "");
	    	 
	    	 this.actualtime = System.currentTimeMillis();
	    	 
	     }
    
	
	
    public class MiLocationListener implements LocationListener
    {
    	
    	 final Button estadolib = (Button) findViewById(R.id.btn_estadolib);
         final Button estadoocup = (Button) findViewById(R.id.btn_estadooc);

    	protected void enviaestado(String c) throws ClientProtocolException, IOException{
    	 	   
    	     	 
    	       HttpClient httpclient = new DefaultHttpClient();  
    		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/estadounidad.php");
    		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
    		   
    		    postValues.add(new BasicNameValuePair("estado", c));
    		    postValues.add(new BasicNameValuePair("id_device", deviceId));
    		    //Encapsulamos
    		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
    		    //Lanzamos la petici�n
    		    HttpResponse respuesta = httpclient.execute(httppost);
    	     	 //Log.d("Resultado","------> "+ respuesta);
    		    //Conectamos para recibir datos de respuesta
    		    HttpEntity entity = respuesta.getEntity();
    		    //Creamos el InputStream como su propio nombre indica
    		    InputStream is = entity.getContent();
    		    String resultado= StreamToString(is);
    		    
    		   
    		    
    		    
    		    
    		    
    		    
    	  
    } 	

	public void onLocationChanged(Location loc)
    {
     //Capturamos la posicion de la terminal	
      loc.getLatitude();
      loc.getLongitude();
     
      double latitude = loc.getLatitude();; 
      double longitude = loc.getLongitude(); 
      String aLatitude = Double.toString(latitude); 
	  String aLongitude =Double.toString(longitude);
	  
	  
	  
	  ConnectivityManager cm =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	 
	  if (netInfo != null && netInfo.isConnected()) {
         try {
			EnviarDatos(aLatitude,aLongitude);
			actualizaEstado();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
    
    
    
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
    
    public void actualizaEstado( ) throws UnsupportedEncodingException{
    	   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    	   HttpClient httpclient = new DefaultHttpClient();  
		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/obtenerServicioUnidad.php");
		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
		   
		   
		    postValues.add(new BasicNameValuePair("device_id", deviceId));
		    //Encapsulamos
		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
		    //Lanzamos la petici�n
		    HttpResponse respuesta = null;
			try {
				respuesta = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     	 //Log.d("Resultado","------> "+ respuesta);
		    //Conectamos para recibir datos de respuesta
		    HttpEntity entity = respuesta.getEntity();
		  
		    
		    InputStream is = null;
			try {
				is = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  		    String resultado= StreamToString(is);
  		    String cadena = "libre";
  		    //Toast.makeText(getApplicationContext(), "ESTADO:" + resultado.length() + " - " + cadena , Toast.LENGTH_LONG).show();
			  
  		  final TextView serviciocliente = (TextView) findViewById(R.id.serviciocliente);
  		  final TextView estado = (TextView) findViewById(R.id.estado);
  		  final TextView estado2 = (TextView) findViewById(R.id.estado);
  		  final Button estadolib = (Button) findViewById(R.id.btn_estadolib);
  		  final Button estadoocup = (Button) findViewById(R.id.btn_estadooc);
  		  final Button fueraServicio = (Button) findViewById(R.id.btn_fueraServicio);
  		  if(resultado.length() != 7 && resultado.length() != 19)
  		    {
  		  	  estado2.setText("OCUPADO");
    		  estado2.setTextColor(Color.RED); 
  			  serviciocliente.setVisibility(View.VISIBLE);
  			  serviciocliente.setText(resultado);
  			  estadolib.setVisibility(View.VISIBLE);
  		    }
  		  if(resultado.length() == 7){
  			  
  			 estado2.setText("LIBRE");
  		     estado2.setTextColor(Color.BLUE);
  		     serviciocliente.setVisibility(View.INVISIBLE);
  		     estadoocup.setVisibility(View.VISIBLE);
  		   
  		   }
  		  if(resultado.length() == 19){
  			estado2.setText("FUERA DE SERVICIO");
  		    estado2.setTextColor(Color.YELLOW);
  		    estadolib.setVisibility(View.VISIBLE);
  		    fueraServicio.setVisibility(View.INVISIBLE);
  		   serviciocliente.setVisibility(View.INVISIBLE);
  			  
  		  }
    	
    	
    }
    public void EnviarDatos(String latitude, String longitude) throws IllegalStateException, IOException {
    	
      	
      	 
            String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    	
            
            HttpClient httpclient2 = new DefaultHttpClient();  
  		   HttpPost httppost2 = new HttpPost("http://mitaxiseguro.net/gps/nmensajes.php");
  		   String deviceId2 = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
  		   List<NameValuePair> postValues2 = new ArrayList<NameValuePair>(2);         
  		   
  		   
  		    postValues2.add(new BasicNameValuePair("id_device", deviceId));
  		    //Encapsulamos
  		    httppost2.setEntity(new UrlEncodedFormEntity(postValues2));          
  		    //Lanzamos la petici�n
  		    HttpResponse respuesta2 = httpclient2.execute(httppost2);
  	     	 //Log.d("Resultado","------> "+ respuesta);
  		    //Conectamos para recibir datos de respuesta
  		    HttpEntity entity2 = respuesta2.getEntity();
  		    //Creamos el InputStream como su propio nombre indica
  		    InputStream is2 = entity2.getContent();
  		    String resultado2= StreamToString(is2);
  		    
        final TextView nmensajes = (TextView) findViewById(R.id.nmensajes);  
            nmensajes.setTextColor(Color.YELLOW);
			nmensajes.setText(resultado2);
  			
            if (resultado2.length() != 0){
            	//buildNotificationmensaje(context," Mensajes: ");
            }
          
            
            
            List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
		    postValues.add(new BasicNameValuePair("latitude", latitude));  
		    postValues.add(new BasicNameValuePair("longitude", longitude));
		    postValues.add(new BasicNameValuePair("id_device", deviceId));
  		
  		
  		try{
  			
  		   TelephonyManager mTelephonyManager;
  		   mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
  		  //Toast.makeText( getApplicationContext(),"telefono: "+ mTelephonyManager.getLine1Number(),Toast.LENGTH_LONG).show();
			
  			
  			HttpClient httpclient = new DefaultHttpClient();
  			HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/posicion.php");
  			httppost.setEntity(new UrlEncodedFormEntity(postValues));
  			HttpResponse response = httpclient.execute(httppost);
  			HttpEntity entity = response.getEntity();
  			InputStream is = entity.getContent();
  			String resultado= StreamToString(is);
  			final String[] servicio = resultado.split(",");
  		  
  			
  			if(enruta == 1){
  				List<NameValuePair> postValuesRuta = new ArrayList<NameValuePair>(2);
  				String ruta = latitude +","+longitude;
  			    postValuesRuta.add(new BasicNameValuePair("ruta", ruta));  
  			    postValuesRuta.add(new BasicNameValuePair("id_device", deviceId));
  			    postValuesRuta.add(new BasicNameValuePair("servicio", servicio[0]));
  			    postValuesRuta.add(new BasicNameValuePair("notelefono", mTelephonyManager.getLine1Number()));
  			    
  			    Log.d("se ejecutara", "Se ejecutara codigo async");
  			  new CambiarEstado().execute(postValuesRuta);
  			    
  			  /*
  				HttpClient httpruta = new DefaultHttpClient();
  	  			HttpPost httprutapost = new HttpPost("http://mitaxiseguro.net/gps/rutaposicion.php");
  	  			httprutapost.setEntity(new UrlEncodedFormEntity(postValuesRuta));
  	  			HttpResponse responseRuta = httpruta.execute(httprutapost);
  	  			HttpEntity entityruta = responseRuta.getEntity();
  	  			InputStream isruta = entityruta.getContent();
  	  			String resultadoruta= StreamToString(isruta);
  	  		   // Toast.makeText( getApplicationContext(),resultadoruta,Toast.LENGTH_LONG).show();
  	  		    
  	  		    */
  	  			
  			}
  			
  			
  			if(alerta == 1){
  				
  			//buildNotification(context,servicio[0]);
  			}
  			//String a="No-Mensaje";
  			//Toast.makeText( getApplicationContext(),"lengh: "+ resultado.length() + " valor: "+ resultado,Toast.LENGTH_LONG).show();
  			if(resultado.length() > 0 && msg == 0 ){
  				
  				msg = 1;
  				alerta = 1;
  				
  				
   				// set title
  				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 				
   				alertDialogBuilder.setTitle("SERVICIO DISPONIBLE #" + servicio[0]);
   	 
   				// set dialog message
   				alertDialogBuilder
   					.setMessage("msj: "+ servicio[1])
   					.setCancelable(false)
   					.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
   						public void onClick(DialogInterface dialog,int id) {
   							// if this button is clicked, close
   							// current activity
   							//Main.this.finish();
   							try {
   								
 								enviaSolicitudServicio("1",servicio[0]);
 								msg = 0;
 								alerta = 0;
 								enruta = 1;
 								
							     if (context.NOTIFICATION_SERVICE!=null) {
 							         String ns = context.NOTIFICATION_SERVICE;
 							         NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
 							         nMgr.cancel(mNotificationId);
 							     }
 								
 								dialog.cancel();
 				        		
 							} catch (ClientProtocolException e) {
 								// TODO Auto-generated catch block
 							    e.printStackTrace();
 							} catch (IOException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
   						}
   					  })
   					.setNegativeButton("Rechazar",new DialogInterface.OnClickListener() {
   						public void onClick(DialogInterface dialog,int id) {
   							// if this button is clicked, just close
   							// the dialog box and do nothing
   							try {
   								
   								
							     if (context.NOTIFICATION_SERVICE!=null) {
 							         String ns = context.NOTIFICATION_SERVICE;
 							         NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
 							         nMgr.cancel(mNotificationId);
 							     }
   								
   								dialog.cancel();
 								enviaSolicitudServicio("2",servicio[0]);
 								msg = 0;
 								alerta = 0;
 								
 							} catch (ClientProtocolException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							} catch (IOException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
   							
   						}
   					});
   	 
   					// create alert dialog
   					AlertDialog alertDialog = alertDialogBuilder.create();
   					
   				   // alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.mensaje);
   				    
   				    
   					// show it
   				    
   					alertDialog.show();
   					buildNotification(context,servicio[0]);
  				
  			
  			}// fin de condicion
  			else
  			{
  				
  			}
  			
  			
  			
  		   }
  		   catch(Exception e){
  			 Toast.makeText(getApplicationContext(),"Error en conexion web "+e.toString(), Toast.LENGTH_LONG).show();	
  			 Log.e("log_tag", "Error en conexion web "+e.toString());
  			}
  			
  		
  			
  			
    	
    	
    	
    }	
    
  
    
    
	
    private class CambiarEstado extends AsyncTask<List<NameValuePair>, Integer, Boolean> {   	
    	
        @Override
        protected void onPreExecute() {

        }
    	 
        @Override 
        protected Boolean doInBackground(List<NameValuePair>... params) {
        	
        	List<NameValuePair> postValuesRuta = new ArrayList<NameValuePair>(2);
        	postValuesRuta = params[0];
        	
				HttpClient httpruta = new DefaultHttpClient();
  	  			HttpPost httprutapost = new HttpPost("http://mitaxiseguro.net/gps/rutaposicion.php");
  	  			try {
					httprutapost.setEntity(new UrlEncodedFormEntity(postValuesRuta));
	  	  			HttpResponse responseRuta = httpruta.execute(httprutapost);
	  	  			HttpEntity entityruta = responseRuta.getEntity();
	  	  			InputStream isruta = entityruta.getContent();
	  	  			String resultadoruta= StreamToString(isruta);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


            return true;
        }
        
        protected void onProgressUpdate(Integer... progress) {
        	
        	
        }

        @Override
        protected void onPostExecute(Boolean result) {
	

        }
    }
   


	public String StreamToString(InputStream is) {
	    //Creamos el Buffer
	    BufferedReader reader = 
	        new BufferedReader(new InputStreamReader(is));
	   StringBuilder sb = new StringBuilder();
	 String line = null;
	 try {
	 //Bucle para leer todas las l�neas
	 //En este ejemplo al ser solo 1 la respuesta
	 //Pues no har�a falta
	 while ((line = reader.readLine()) != null) {
	      sb.append(line + "\n");
	 }
	 } catch (IOException e) {
	     e.printStackTrace();
	 } finally {
	    try {
	    is.close();
	    } catch (IOException e) {
	    e.printStackTrace();
	    }
	 }
	 //retornamos el codigo l�mpio
	return sb.toString();
	}
	
	
	
	
  
    
        }
    
    public String StreamToString(InputStream is) {
	    //Creamos el Buffer
	    BufferedReader reader = 
	        new BufferedReader(new InputStreamReader(is));
	   StringBuilder sb = new StringBuilder();
	 String line = null;
	 try {
	 //Bucle para leer todas las l�neas
	 //En este ejemplo al ser solo 1 la respuesta
	 //Pues no har�a falta
	 while ((line = reader.readLine()) != null) {
	      sb.append(line + "\n");
	 }
	 } catch (IOException e) {
	     e.printStackTrace();
	 } finally {
	    try {
	    is.close();
	    } catch (IOException e) {
	    e.printStackTrace();
	    }
	 }
	 //retornamos el codigo l�mpio
	return sb.toString();
	}
    
    public void getNotification() {
		// TODO Auto-generated method stub
		
	}

	public void enviaSolicitudServicio(String c,String ids) throws ClientProtocolException, IOException{
    	    
    	   final Button estadolib = (Button) findViewById(R.id.btn_estadolib);
           final Button estadoocup = (Button) findViewById(R.id.btn_estadooc);
	       HttpClient httpclient = new DefaultHttpClient();  
		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/pilotoRespuesta.php");
		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(3);
		   
		   Log.d("enviando datos respuesta", "11111111111");
		    postValues.add(new BasicNameValuePair("respuesta", c));
		    postValues.add(new BasicNameValuePair("id_device", deviceId));
		    postValues.add(new BasicNameValuePair("id_service", ids));
		    //Encapsulamos
		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
		    //Lanzamos la petici�n
		    HttpResponse respuesta = httpclient.execute(httppost);
	     	
		     
		    //Log.d("Resultado","------> "+ respuesta);
		    //Conectamos para recibir datos de respuesta
		    HttpEntity entity = respuesta.getEntity();
		    //Creamos el InputStream como su propio nombre indica
		    InputStream is = entity.getContent();
		    final String resultado= StreamToString(is);
		    final String estado = "0";
		    final TextView estado2 = (TextView) findViewById(R.id.estado);
	        final TextView serviciocliente = (TextView) findViewById(R.id.serviciocliente);
		  //  Toast.makeText(getApplicationContext(), "ATENCION:" + resultado.length() , Toast.LENGTH_LONG).show();
		   
		    if(resultado.length() == 6){
		    	Log.d("enviando datos respuesta", "22222222");
		    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
		    		estadoocup.setVisibility(View.VISIBLE);
		        	estadolib.setVisibility(View.INVISIBLE);
		        	
		        	
		        	
		        	// set dialog message
					alertDialogBuilder
					    .setTitle("SERVICIO NO ACEPTADO:")
						.setMessage("El servicio ya ha sido tomado.")
						.setCancelable(false)
						.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								//Main.this.finish();
								
							}
						  });

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					
				   // alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.mensaje);
				    
				    
					// show it
					alertDialog.show();
		        	
		    	}
		    	else
		    	{
		    	 if(resultado.length() != 0){	
		    		estadoocup.setVisibility(View.INVISIBLE);
		        	estadolib.setVisibility(View.VISIBLE);
		        	
		        	
		        	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		        	// set dialog message
					alertDialogBuilder
					    .setTitle("DETALLES DEL SERVICIO:")
						.setMessage(resultado)
						.setCancelable(false)
						.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								estadoocup.setVisibility(View.INVISIBLE);
					        	estadolib.setVisibility(View.VISIBLE);
					        	estado2.setText("OCUPADO");
					        	serviciocliente.setVisibility(View.VISIBLE);
					        	serviciocliente.setText(resultado);
				        		estado2.setTextColor(Color.RED);
				        		try {
									enviaestado("2");
								} catch (ClientProtocolException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/* try {
									//enviaestado("1");
								} catch (ClientProtocolException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								*/
								estadoocup.setVisibility(View.INVISIBLE);
					        	estadolib.setVisibility(View.VISIBLE);
					        	
					        	
					        	 Toast.makeText( getApplicationContext(),"Recuerde presionar el boton LIBRE para tomar otro servicio",Toast.LENGTH_LONG).show();
								
							}
						});
					

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					
				   // alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.mensaje);
				    
				    
					// show it
					alertDialog.show();
		    
					
		    	 }// fin estado diferente a 0
		    	 else{
		    		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
			    		estadoocup.setVisibility(View.INVISIBLE);
			        	estadolib.setVisibility(View.INVISIBLE);
			        	
			        	// set dialog message
						alertDialogBuilder
						    .setTitle("SERVICIO NO ACEPTADO:")
							.setMessage("El servicio ya ha sido tomado.")
							.setCancelable(false)
							.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									//Main.this.finish();
									
								}
							  });
		    		 
		    	 }
		    	}
		    	
		    
 	         
				
	 
				
	 
		   
		    
		    

		    
	    	
	  
  }
	
	
	
    @Override
    public void  onDestroy(){
    	super.onDestroy();
    	Log.d("entro oncrate", "salio");
    	anunciosAsync.cancel(true);
    }

	protected void enviaestado(String c) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		 
	       HttpClient httpclient = new DefaultHttpClient();  
		   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/estadounidad.php");
		   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		   List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);         
		   
		    postValues.add(new BasicNameValuePair("estado", c));
		    postValues.add(new BasicNameValuePair("id_device", deviceId));
		    //Encapsulamos
		    httppost.setEntity(new UrlEncodedFormEntity(postValues));          
		    //Lanzamos la petici�n
		    HttpResponse respuesta = httpclient.execute(httppost);
	     	
		     
		    //Log.d("Resultado","------> "+ respuesta);
		    //Conectamos para recibir datos de respuesta
		    HttpEntity entity = respuesta.getEntity();
		    //Creamos el InputStream como su propio nombre indica
		    InputStream is = entity.getContent();
		    String resultado= StreamToString(is);
		   
	}



	@android.annotation.TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void buildNotification(Context context, String serv){
		  NotificationManager notificationManager 
		  = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification.Builder builder = new Notification.Builder(context);
		  
		  Intent intent = new Intent(context, Activity.class);
		  PendingIntent pendingIntent 
		  = PendingIntent.getActivity(context, 0, intent, 0);
		  
		  builder
		  //.setSmallIcon(R.drawable.customer_service)
		  .setContentTitle("Nuevo Servicio")
		 // .setContentText("Servicio #"+serv)
		  //.setContentInfo("Informacion del servicio")
		  .setTicker("Ticker")
		  .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
		  .setContentIntent(pendingIntent)
		  .setAutoCancel(true);
		  Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		  builder.setSound(alarmSound);
		  Notification notification = builder.getNotification();
		  
		  notificationManager.notify(mNotificationId, notification);
		 }

	     
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void buildNotificationmensaje(Context context, String serv){
		  NotificationManager notificationManager 
		  = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification.Builder builder = new Notification.Builder(context);
		  
		  Intent intent = new Intent(context, Activity.class);
		  PendingIntent pendingIntent 
		  = PendingIntent.getActivity(context, 0, intent, 0);
		  
		  builder
		  .setSmallIcon(R.drawable.btn_mensajes)
		  .setContentTitle("Nuevo Mensaje")
		  .setContentText("Tiene un Nuevo Mensaje")
		  .setContentInfo("Ingrese a Mensajes para consultarlo")
		  .setTicker("Ticker")
		  .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
		  .setContentIntent(pendingIntent)
		  .setAutoCancel(true);
		  Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		  builder.setSound(alarmSound);
		  Notification notification = builder.getNotification();
		  
		  notificationManager.notify(R.drawable.btn_mensajes, notification);
		 }
	 
	
    
    
    
}
