package com.ibisatebarredo.isports2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class Historia extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView c_lista;
    ArrayAdapter adaptador;

    BBDD base_datos;

    int posicion_selecionada=999999;

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
        c_lista.setOnItemClickListener(this);


        findViewById(R.id.b_salir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Historia.this, Inicio.class));
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_historia, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            //Limpiar todos los elementos
            adaptador.clear();
            return true;
        }
        if (id == R.id.action_delete) {
            //borrar elemento
            if (posicion_selecionada != 999999) {
                recorrido r = (recorrido)adaptador.getItem(posicion_selecionada);
                // Arrancamos Base Datos
                base_datos = new BBDD(this);
                base_datos.open_BD();
                // borro de la base de datos y del adaptador
                base_datos.eliminar_Recorrido(r);
                adaptador.remove(r);
                // Cerrar Base de Datos
                base_datos.close_BD();

                posicion_selecionada=999999;
            }else {
                Toast.makeText(this, "Seleccione un Recorrido", Toast.LENGTH_LONG).show();
            }

            return true;
        }

         if (id == R.id.action_delete_all || id == R.id.action_order_1 || id == R.id.action_order_2) {
            //Borrar todos los elementos
            Toast.makeText(this, "No disponible", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        posicion_selecionada=position;
        recorrido r = (recorrido)adaptador.getItem(position);
        String msg = "Seleccion: \n"+r.getFecha()+" - "+r.getActividad();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
