package com.ibisatebarredo.isports2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;


public class Historia extends AppCompatActivity {

    ListView c_lista;
    ArrayAdapter adaptador;

    BBDD base_datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        setTitle("Isports. Historia");

        List<recorrido> r = new ArrayList<recorrido>();

        //Instancia del ListView
        c_lista = (ListView) findViewById(R.id.listView);

        // Arrancamos Base Datos
        base_datos = new BBDD(this);
        base_datos.open_BD();

        //  Cargar el arrayList
        r = base_datos.consultar_recorridos();

        //Inicializar el adaptador con la fuente de datos
        adaptador = new TareaArrayAdapter(this, r);

        //Relacionando la lista con el adaptador
        c_lista.setAdapter(adaptador);

        // Cerrar Base de Datos
        base_datos.close_BD();

        //Estableciendo la escucha
        //c_lista.setOnItemClickListener(Historia.this);



        findViewById(R.id.b_salir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Historia.this, Inicio.class));
            }
        });

    }
}
