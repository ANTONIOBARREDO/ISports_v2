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
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    GoogleMap mMap;
    Polyline polyline;
    Marker marker;
    List<LatLng> lista;


    String fecha;
    String tiempo="23:59";
    String distancia="9.59";

    Chronometer c_crono;
    TextView l_crono;
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
        b_accion = (Button) findViewById(R.id.b_accion);
        b_vista = (Button) findViewById(R.id.b_vista);

        l_crono.setText("Duracion");
        b_accion.setText("Iniciar");
        b_vista.setText("Vista");

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


        /*
        map.setOnInfoWindowClickListener(this);
        //Recoge las coordenadas cada 5 segundos
        map.setOnMyLocationChangeListener(this);
         */

        // ----
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // Add a marker in Sydney and move the camera
                LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
                // posicionGPS.setText(" GPS: " + location.getLatitude() + ", " + location.getLongitude());
                lista.add(gps);
                getDistance();
                pintar_ruta();

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(gps).title("En Vitoria City"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(gps));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15),200,null);
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

/*
        // http://developer.android.com/intl/es/training/permissions/requesting.html
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }

*/
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

    public void getDistance () {
        double distance=0;
        for (int i = 0, tam = lista.size(); i < tam; i++){
            if (i < tam -1) {
                distance += cDistance(lista.get(i), lista.get(i + 1));
            }
        }

    }

    public static double cDistance(LatLng StartP,LatLng EndP){
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
        return (Radius * c);

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
                } else {
                    c_crono.stop();
                    b_accion.setText("Iniciar");

                    /*
                    long minutos = ((SystemClock.elapsedRealtime() - cronometro.getBase()) /1000)/60;
                    long segundos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)%60;
                    time =(minutos +" : "+segundos);
                    cronometro.setText(time);

                    intent.putExtra("duracion",cronometro.getText());
                    */

                    // Creamos el Intent para Lanzar actividad de FIN
                    Intent i = new Intent(getApplicationContext(), Fin.class);

                    // Cargamos los datos en el Intent
                    i.putExtra("fecha", fecha);
                    i.putExtra("tiempo", tiempo);
                    i.putExtra("distancia", distancia);

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

/*
            case R.id.b_iniciar:

                cronometro.setVisibility(View.VISIBLE);

                date = (DateFormat.format("dd-MM-yyyy HH:mm:ss", new java.util.Date()).toString());
                //Iniciar el cronómetro
          --      cronometro.setBase(SystemClock.elapsedRealtime());
          --      cronometro.start();


                comenzar = true;

                location = map.getMyLocation();
                onMyLocationChange(location);
                latLong = new LatLng(map.getMyLocation().getLatitude(),
                        map.getMyLocation().getLongitude());
                String cad = String.valueOf((latLong.latitude));
                cad = cad.substring(0, 9);
                String cad2 = String.valueOf((latLong.longitude));
                cad2 = cad2.substring(0, 8);

                this.map.addMarker(new MarkerOptions().position(latLong)
                        .title("INICIO:")
                        .snippet("Latitud: " + String.valueOf(cad)
                                + "\nLongitud: " + String.valueOf(cad2)));

                list.add(latLong);

                ruta();

                break;

            case R.id.bparar:
                cronometro.setVisibility(View.INVISIBLE);
                cronometro.stop();
                long minutos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)/60;
                long segundos = ((SystemClock.elapsedRealtime()-cronometro.getBase())/1000)%60;
                time =(minutos +" : "+segundos);
                cronometro.setText(time);


                latLong = new LatLng(map.getMyLocation().getLatitude(),
                        map.getMyLocation().getLongitude());

                list.add(latLong);

                comenzar = false;
                Intent intent = new Intent(MapsActivity.this,ResumenCarrera.class);
                intent.putExtra("duracion",cronometro.getText());
                intent.putExtra("date",date);
                intent.putExtra("recorrido",po);
                startActivity(intent);
                break;
*/
            default:
                break;

        }
    }
}
