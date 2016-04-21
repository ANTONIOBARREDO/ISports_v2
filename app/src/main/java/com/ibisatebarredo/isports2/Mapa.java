package com.ibisatebarredo.isports2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    GoogleMap mMap;
    Polyline polyline;
    Marker marker;
    List<LatLng> lista;
    Boolean marca=false;

    String fecha;
    Double distancia=0.0;

    Chronometer c_crono;
    TextView l_crono;
    TextView l_distancia;
    TextView c_distancia;
    TextView l_velocidad;
    TextView c_velocidad;
    TextView c_latlon;

    Button b_accion;
    Button b_vista;
    String vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        c_crono = (Chronometer) findViewById(R.id.c_crono);
        l_crono = (TextView) findViewById(R.id.l_crono);
        l_distancia = (TextView) findViewById(R.id.l_distancia);
        c_distancia = (TextView) findViewById(R.id.c_distancia);
        l_velocidad = (TextView) findViewById(R.id.l_velocidad);
        c_velocidad = (TextView) findViewById(R.id.c_velocidad);
        c_latlon = (TextView) findViewById(R.id.c_latlon);
        b_accion = (Button) findViewById(R.id.b_accion);
        b_vista = (Button) findViewById(R.id.b_vista);

        l_crono.setText("Duracion");
        l_distancia.setText("Distancia");
        l_velocidad.setText("Velocidad");
        c_distancia.setText("0.0");
        c_velocidad.setText("0.0");
        c_latlon.setText("A la espera de posicion GPS");
        b_accion.setText("Iniciar");
        b_accion.setBackgroundResource(R.color.verde);
        //b_accion.setTextColor(getResources().getColor(R.color.verde));
        b_vista.setText("Vista");
        b_vista.setBackgroundResource(R.color.gris);

        b_accion.setOnClickListener(this);
        b_vista.setOnClickListener(this);

        vista="S";
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lista = new ArrayList<LatLng>();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Vitoria and move the camera
        LatLng v = new LatLng(42.846350, -2.672246);
        // mMap.addMarker(new MarkerOptions().position(v).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Tamos en ello").snippet("Aqui en Siberia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(v));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }

    public void iniciar_ruta() {

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());

                // marca inicio
                if (!marca) {
                    marca=true; // entro la primera vez y pongo marker
                    mMap.addMarker(new MarkerOptions().position(gps).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Inicio Recorrido").snippet("Buen día"));
                }
                // mover camara
                mMap.moveCamera(CameraUpdateFactory.newLatLng(gps));

                // proceso
                lista.add(gps);                 // Añadir posicion al array
                distancia=dame_Distancia();     // distancia entre los puntos
                pintar_ruta();                  // pintar Ruta en el mapa

                // mostrar datos en pantalla
                c_latlon.setText(" GPS: " + location.getLatitude() + ", " + location.getLongitude());
                c_distancia.setText(Double.toString(distancia) + "\n m");
                float num = location.getSpeed();
                c_velocidad.setText(num * 3.6 + "\n km/h");   // \n

                //if (marker != null) {
                //    marker.remove();
                //}

                // marker = mMap.addMarker(new MarkerOptions().position(gps).title("En Vitoria City"));
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(gps));
                // mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 200, null);
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Toast.makeText(Mapa.this, ">>>>> onStatusChanged ", Toast.LENGTH_LONG).show();
            }
            public void onProviderEnabled(String provider) {
                Toast.makeText(Mapa.this, "GPS activado", Toast.LENGTH_LONG).show();
            }
            public void onProviderDisabled(String provider) {
                Toast.makeText(Mapa.this, "Activar el GPS", Toast.LENGTH_LONG).show();
            }
        };

        // http://developer.android.com/intl/es/training/permissions/requesting.html
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }
    }

    public void pintar_ruta() {
        PolylineOptions po;

        if (polyline == null) {
            po = new PolylineOptions();

            for (int i = 0, tam = lista.size(); i < tam; i++){
                po.add(lista.get(i));
                po.color(Color.BLUE);
                polyline = mMap.addPolyline(po);
            }
        }
        else {
            polyline.setPoints(lista);
        }
    }

    public double dame_Distancia () {
        double distance=0;
        for (int i = 0, tam = lista.size(); i < tam; i++){
            if (i < tam -1) {
                distance += calcular_Distancia(lista.get(i), lista.get(i + 1));
            }
        }
        return (double) (Math.round((distance)*1)/1); //  (Math.round((distance)*10)/10 Redondeo a dos decimales y paso a KM
    }

    public static double calcular_Distancia(LatLng StartP,LatLng EndP){
        // fórmula de Haversine
        int Radius = 6371000; //Radio de la tierra
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double d = (Radius * c); // en m
        return d;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.b_accion:

                if ( b_accion.getText() == "Iniciar") {
                    // Cronomentro arrancar
                    fecha = (DateFormat.format("dd-MM-yyyy HH:mm:ss", new java.util.Date()).toString());
                    c_crono.setBase(SystemClock.elapsedRealtime());
                    c_crono.start();
                    b_accion.setText("Parar");
                    b_accion.setBackgroundResource(R.color.rojo);
                    // b_accion.setTextColor(getResources().getColor(R.color.rojo));
                    iniciar_ruta();

                } else {
                    c_crono.stop();
                    b_accion.setText("Iniciar");
                    b_accion.setBackgroundResource(R.color.verde);
                    //b_accion.setTextColor(getResources().getColor(R.color.verde));

                    long minutos = ((SystemClock.elapsedRealtime() - c_crono.getBase()) /1000)/60;
                    long segundos = ((SystemClock.elapsedRealtime()-c_crono.getBase())/1000)%60;
                    String tiempo =(minutos+":"+segundos);

                    // Creamos el Intent para Lanzar actividad de FIN
                    Intent i = new Intent(getApplicationContext(), Fin.class);

                    // Cargamos los datos en el Intent
                    i.putExtra("fecha", fecha);
                    i.putExtra("tiempo", tiempo);
                    i.putExtra("distancia", Double.toString(distancia));    //c_distancia.getText());

                    // Arrancar la actividad
                    startActivity(i);
                }

                break;

            // Controlar el Botón de Vista. Tipo de Mapa
            case R.id.b_vista:
                /*
                GoogleMap.MAP_TYPE_NONE - Una cuadrícula vacía sin azulejos mapeo mostradas.
                GoogleMap.MAP_TYPE_NORMAL - La vista estándar que consiste en la hoja de ruta clásica.
                GoogleMap.MAP_TYPE_SATELLITE - Muestra las imágenes de satélite de la región del mapa.
                GoogleMap.MAP_TYPE_HYBRID - Muestra las imágenes de satélite con los mapas de carreteras superpuestas.
                GoogleMap.MAP_TYPE_TERRAIN - Muestra información topográfica como líneas de contorno y colores.
                */
                if (vista == "N") {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    vista = "S";
                    return;
                }
                if (vista == "S") {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    vista = "H";
                    return;
                }
                if (vista == "H") {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    vista = "T";
                    return;
                }
                if (vista == "T") {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    vista = "N";
                    return;
                }

                break;


            default:
                break;

        }
    }
}
