package com.ibisatebarredo.isports;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Main2Activity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    TextView l_text;
    TextView l_lat;
    TextView l_lon;
    TextView l_pre;
    TextView l_text_pro;
    TextView l_pro;

    Button b_mapa;
    Button b_activar;
    Button b_desactivar;

    LocationManager locManager;
    LocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.b2_mapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, MapsActivity.class));
            }
        });

        l_text = (TextView) findViewById(R.id.textView2);
        l_lat = (TextView) findViewById(R.id.textView3);
        l_lon = (TextView) findViewById(R.id.textView4);
        l_pre = (TextView) findViewById(R.id.textView5);
        l_text_pro = (TextView) findViewById(R.id.textView6);
        l_pro = (TextView) findViewById(R.id.textView7);

        b_mapa = (Button) findViewById(R.id.b2_mapa);
        b_activar = (Button) findViewById(R.id.b2_activar);
        b_desactivar = (Button) findViewById(R.id.b2_desactivar);

        l_text.setText("Posicion Actual");
        l_lat.setText("Latitud: (sin_datos)");
        l_lon.setText("Longitud: (sin_datos)");
        l_pre.setText("Precision: (sin_datos)");
        l_text_pro.setText("Estado Proveedor");
        l_pro.setText("Boton pulsado ...");

        b_mapa.setText("Mostrar Mapa");
        b_activar.setText("Activar GPS");
        b_desactivar.setText("DesActivar GPS");

        b_activar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_pro.setText("ACTIVAR");
                comenzarLocalizacion();
            }
        });

        b_desactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_pro.setText("DESACTIVAR");
                // locManager.removeUpdates(locListener);
            }
        });

    }

    private void comenzarLocalizacion()
    {
        //Obtenemos una referencia al LocationManager
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

  //---
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }

        //----



        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Mostramos la última posición conocida
        mostrarPosicion(loc);

        //Nos registramos para recibir actualizaciones de la posición
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                l_pro.setText("Provider Status: " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                l_pro.setText("Provider ONN");
            }

            @Override
            public void onProviderDisabled(String provider) {
                l_pro.setText("Provider OFF");
            }


        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
    }

    private void mostrarPosicion(Location loc) {
        if (loc != null) {
            l_lat.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            l_lon.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            l_pre.setText("Precision: " + String.valueOf(loc.getAccuracy()));
        } else {
            l_lat.setText("Latitud: (sin_datos)");
            l_lon.setText("Longitud: (sin_datos)");
            l_pre.setText("Precision: (sin_datos)");
        }
    }
}
