package com.ibisatebarredo.isports2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class Inicio extends AppCompatActivity {

    Button b_musica;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("ISports. Inicio");


        b_musica = (Button) findViewById(R.id.b_musica);
        b_musica.setText("Musica. Iniciar");

        // -----

        //Listener del botón musica
        b_musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( b_musica.getText() == "Musica. Iniciar") {
                   //Cuando aprieto el parar
                    b_musica.setText("Musica. Parar");

                    mediaPlayer = MediaPlayer.create(Inicio.this, R.raw.cancion2);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(30, 30);
                    mediaPlayer.start();
                }else {
                    b_musica.setText("Musica. Iniciar");
                    mediaPlayer.stop();
                }
            }

        });





        findViewById(R.id.b_recorrido).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, Mapa.class));
            }
        });
        findViewById(R.id.b_historia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, Historia.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
