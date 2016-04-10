package com.ibisatebarredo.isports2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anton_000 on 10/04/2016.
 */

public class TareaArrayAdapter extends ArrayAdapter<recorrido> {

    public TareaArrayAdapter(Context context, List<recorrido> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.image_list_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView c_fecha = (TextView)listItemView.findViewById(R.id.c_fecha);
        TextView c_tiempo = (TextView)listItemView.findViewById(R.id.c_tiempo);
        TextView c_distancia = (TextView)listItemView.findViewById(R.id.c_duracion);
        TextView c_actividad = (TextView)listItemView.findViewById(R.id.c_actividad);
        TextView c_observacion = (TextView)listItemView.findViewById(R.id.c_observacion);

        //Obteniendo instancia de la Tarea en la posición actual
        recorrido item = getItem(position);

        c_fecha.setText("Fecha: " + item.getFecha());
        c_tiempo.setText("Tiempo: " + item.getTiempo());
        c_distancia.setText("Distancia: " + item.getDistancia());
        c_actividad.setText("Actividad: " + item.getActividad());
        c_observacion.setText("Comentarios: " + item.getObservacion());

        //Devolver al ListView la fila creada
        return listItemView;

    }
}