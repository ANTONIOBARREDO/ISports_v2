package com.ibisatebarredo.isports;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Polyline polyline;
    private Marker marker;
    // --- private SupportMapFragment mapFrag;
    private List<LatLng> lista;
    int cont=0;
    TextView e_Distancia;
    Button tmapa;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        e_Distancia = (TextView) findViewById(R.id.textView);
        tmapa = (Button) findViewById(R.id.bmapa);
        e_Distancia.setText("DISTANCIA");
        tmapa.setText("Tipo Mapa");
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
        /*
        GoogleMap.MAP_TYPE_NONE - Una cuadrícula vacía sin azulejos mapeo mostradas.
        GoogleMap.MAP_TYPE_NORMAL - La vista estándar que consiste en la hoja de ruta clásica.
        GoogleMap.MAP_TYPE_SATELLITE - Muestra las imágenes de satélite de la región del mapa.
        GoogleMap.MAP_TYPE_HYBRID - Muestra las imágenes de satélite con los mapas de carreteras superpuestas.
        GoogleMap.MAP_TYPE_TERRAIN - Muestra información topográfica como líneas de contorno y colores.
         */

        mMap.setMapType (GoogleMap.MAP_TYPE_SATELLITE);
        lista = new ArrayList<LatLng>();



        //mMap.setBuiltInZoomControls(true);


        // Register the listener with the Location Manager to receive location updates


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                // posicionGPS.setText(" GPS: " + location.getLatitude() + ", " + location.getLongitude());

                // Add a marker in Sydney and move the camera
                LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
                lista.add(gps);
                cont = cont + 1;
                // e_Distancia.setText(" A ver si Funciona" + cont);
                getDistance();
                pintar_ruta();


                if (marker != null) {
                    marker.remove();
                }
               //marker = MAP.addMarker(new MarkerOptions().position(new LatLng(arg0.latitude,arg0.longitude)).draggable(true).visible(true));
                // marker = MAP.addMarker(new MarkerOptions().position(currentPosition).snippet("Lat:" + location.getLatitude() + "Lng:"
                // + location.getLongitude()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("ME"));
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


        // http://developer.android.com/intl/es/training/permissions/requesting.html
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }

        // Add a marker in Vitoria and move the camera
        LatLng v = new LatLng(42.846350, -2.672246);
        mMap.addMarker(new MarkerOptions().position(v).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Tamos en ello").snippet("Aqui en Siberia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(v));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

        public void onMapClick(LatLng puntoPulsado) {
        mMap.addMarker(new MarkerOptions().position(puntoPulsado).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
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
        // tmapa.setText((int) distance);
        // ----e_Distancia.setText((int) distance);
        //Toast.makeText(MapsActivity.this, "", Toast.LENGTH_SHORT).show();

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


}
