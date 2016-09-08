package com.authorwjf.quickgpsdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;


public class mensajeria extends Activity  {
	
	final Context context = this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.mensajeria);
		super.onCreate(savedInstanceState);
		final Button btn_regresar = (Button) findViewById(R.id.regresar);
		final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

		SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList,
				android.R.layout.simple_list_item_1, new String[] { "planet" },
				new int[] { android.R.id.text1 });

		try {
			initList();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// We get the ListView component from the layout
		ListView lv = (ListView) findViewById(R.id.listView);

		// This is a simple adapter that accepts as parameter
		// Context
		// Data list
		// The row layout that is used during the row creation
		// The keys used to retrieve the data
		// The View id used to show the data. The key number and the view id
		// must match
		simpleAdpt = new SimpleAdapter(this, planetsList,
				android.R.layout.simple_list_item_1, new String[] { "planet" },
				new int[] { android.R.id.text1 });
 
		lv.setAdapter(simpleAdpt);
		registerForContextMenu(lv);

		// React to user clicks on item
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			private CharSequence[] tarifa;

			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {

				// We know the View is a TextView so we can cast it
				TextView clickedView = (TextView) view;

				// Toast.makeText(mensajeria.this,
				// "Item with id ["+id+"] - Position ["+position+"] - Planet ["+clickedView.getText()+"]",
				// Toast.LENGTH_SHORT).show();

				// prepare the alert box
				String mensaje = (String) clickedView.getText();

				final String[] idmensaje = mensaje.split("#");
				String mensaje2 = idmensaje[1];
				final String[] idmensaje2 = mensaje2.split("#");
				List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://mitaxiseguro.net/gps/respuesta.php");
				String deviceId = android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

				postValues.add(new BasicNameValuePair("id", idmensaje2[0]));
				// Encapsulamos
				try {
					httppost.setEntity(new UrlEncodedFormEntity(postValues));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				String resultado = StreamToString(is);

				final String[] mensajes = resultado.split(",");
				final CharSequence[] tarifa = { mensajes[0], mensajes[1],
						mensajes[2] };

				alert.setTitle(clickedView.getText());

				alert.setSingleChoiceItems(tarifa, -1, new

				DialogInterface.OnClickListener()

				{

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText( getApplicationContext(),"idmsj: "+
						// idmensaje[0] ,Toast.LENGTH_SHORT ).show();

						// tarifa[which];

						List<NameValuePair> postValues = new ArrayList<NameValuePair>(
								3);
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://mitaxiseguro.net/gps/enviarRespuesta.php");
						String deviceId = android.provider.Settings.Secure
								.getString(
										getContentResolver(),
										android.provider.Settings.Secure.ANDROID_ID);

						postValues.add(new BasicNameValuePair("user_id",
								(String) idmensaje[0]));
						postValues.add(new BasicNameValuePair("respuesta",
								(String) tarifa[which]));
						postValues.add(new BasicNameValuePair("mensaje_id",
								idmensaje2[0]));
						// Encapsulamos
						try {
							httppost.setEntity(new UrlEncodedFormEntity(
									postValues));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

						dialog.cancel();
						dialog.dismiss();
						finish();
						startActivity(getIntent());
					}

				});
				alert.show();
			}
		});
		btn_regresar.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(context, Main.class);
				startActivity(i);
				finish();
			}
		});

	}

	  
	// The data to show
	List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();

	 List<NameValuePair> postValues = new ArrayList<NameValuePair>(2); 	
	 private void initList() throws ClientProtocolException, IOException {
		    // We menssages

		            
			   
		       HttpClient httpclient = new DefaultHttpClient();  
			   HttpPost httppost = new HttpPost("http://mitaxiseguro.net/gps/pilotomensajeria.php");
			   String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			
			    postValues.add(new BasicNameValuePair("id_device", deviceId));
			    //Encapsulamos
			    httppost.setEntity(new UrlEncodedFormEntity(postValues)); 
			    HttpResponse respuesta = httpclient.execute(httppost);
		   	    HttpEntity entity = respuesta.getEntity();
			    InputStream is = entity.getContent();
			    String resultado= StreamToString(is);
				
				final String[] mensajes = resultado.split(",");
			   //Toast.makeText( getApplicationContext(),mensajes[1],Toast.LENGTH_LONG).show();
				for (int i = 0; i < mensajes.length; i++) {
			         planetsList.add(createPlanet("planet", mensajes[i]));
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

		private HashMap<String, String> createPlanet(String key, String name) {
		    HashMap<String, String> respuesta = new HashMap<String, String>();
		    respuesta.put(key, name);

		    return respuesta;
		}
		
		

}
