package com.ibisatebarredo.isports2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Fin extends AppCompatActivity  {

    BBDD base_datos;

    // Etiquetas y Campos Actividad 1
    TextView l_fecha;
    TextView c_fecha;
    TextView l_tiempo;
    TextView c_tiempo;
    TextView l_distancia;
    TextView c_distancia;
    TextView l_velocidad;
    TextView c_velocidad;
    TextView l_observacion;
    EditText c_observacion;
    TextView l_actividad;
    Spinner s_actividad;

    Button b_aceptar;
    Button b_cancelar;

    String fecha;
    String tiempo;
    String distancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        // Titulo de Actividad
        setTitle("ISports. Fin Recorrido");

        // Etiquetas y Campos.Asignacion
        l_fecha = (TextView) findViewById(R.id.l_fecha);
        c_fecha = (TextView) findViewById(R.id.c_fecha);
        l_tiempo = (TextView) findViewById(R.id.l_tiempo);
        c_tiempo = (TextView) findViewById(R.id.c_tiempo);
        l_distancia = (TextView) findViewById(R.id.l_distancia);
        c_distancia = (TextView) findViewById(R.id.c_distancia);
        l_velocidad = (TextView) findViewById(R.id.l_velocidad);
        c_velocidad = (TextView) findViewById(R.id.c_velocidad);
        l_observacion = (TextView) findViewById(R.id.l_observacion);
        c_observacion = (EditText) findViewById(R.id.c_observacion);
        l_actividad = (TextView) findViewById(R.id.l_actividad);
        s_actividad = (Spinner) findViewById(R.id.s_actividad);

        // Etiquetas. Asignar Texto
        l_tiempo.setText("Fecha:");
        l_tiempo.setText("Tiempo:");
        l_distancia.setText("Distancia:");
        l_velocidad.setText("Velocidad media:");
        l_observacion.setText("Comentario:");
        l_actividad.setText("Seleccionar Actividad:");

        // Botones. Asignacion
        b_aceptar = (Button) findViewById(R.id.b_aceptar);
        b_cancelar = (Button) findViewById(R.id.b_cancelar);

        // Botones. Asignar Texto
        b_aceptar.setText("Aceptar");
        b_cancelar.setText("Cancelar");

        // Spinner de actividad. ArrayAdapter -
        // Array de String que contiene los datos
        String[] actividades = {"> Caminar", "> Correr", "> Ciclismo", "> Patinaje", "> Esquiar"};
        // Creamos el adaptador partiendo del array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actividades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Conectamos el adaptador al control
        s_actividad.setAdapter(adapter);

        // Asignar valores pasados
        fecha= getIntent().getStringExtra("fecha");
        tiempo= getIntent().getStringExtra("tiempo");
        distancia= getIntent().getStringExtra("distancia");

        c_fecha.setText(fecha);
        c_tiempo.setText(tiempo);
        c_distancia.setText(distancia);

        findViewById(R.id.b_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fin.this, Inicio.class));
            }
        });
        findViewById(R.id.b_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arrancamos Base Datos
                base_datos = new BBDD(Fin.this);
                base_datos.open_BD();
                // Guardamos el objeto
                recorrido r = new recorrido(c_fecha.getText(),c_tiempo.getText(),c_distancia.getText(),s_actividad.getSelectedItem().toString(),c_observacion.getText().toString());
                base_datos.insertar_Recorrido(r);
                // Cerrar Base de Datos
                base_datos.close_BD();

                // Mostrar la Historia
                startActivity(new Intent(Fin.this, Historia.class));
            }
        });


    }
}
