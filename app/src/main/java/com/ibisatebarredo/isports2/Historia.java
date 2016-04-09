package com.ibisatebarredo.isports2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Historia extends AppCompatActivity {

    ListView c_lista;

    BBDD base_datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        setTitle("Isports. Historia");

        //c_lista = (ListView) findViewById(R.id.listView);
        List<recorrido> r = new ArrayList<recorrido>();

        // Arrancamos Base Datos
        base_datos = new BBDD(this);
        base_datos.open_BD();
        //  Cargar el arrayList
        r = base_datos.consultar_recorridos();
        // Cerrar Base de Datos
        base_datos.close_BD();

        //c_lista.setAdapter(new ItemAdapter(this, r));

        /*
        private LazyAdapter la = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_carreras);




        // Creamos un adaptador con carga diferida o "perezosa"
        LazyAdapter adaptador = new LazyAdapter(this, carreras);

        // Conectamos el adaptador al control
        Spinner sp3 = (Spinner) findViewById(R.id.spinner1);
        sp3.setAdapter(adaptador);


    }
         */

/*
        findViewById(R.id.b_salir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Historia.this, Inicio.class));
            }
        });
*/

    }
}
