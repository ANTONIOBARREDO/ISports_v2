package com.ibisatebarredo.isports2;

/**
 * Created by anton_000 on 07/04/2016.
 */

public class recorrido {
    private CharSequence fecha;
    private CharSequence tiempo;
    private CharSequence distancia;
    private CharSequence actividad;
    private CharSequence observacion;


    public recorrido() {
    }

    public recorrido(CharSequence fecha, CharSequence tiempo, CharSequence distancia, CharSequence actividad,CharSequence observacion ) {
        this.setFecha(fecha);
        this.setTiempo(tiempo);
        this.setDistancia(distancia);
        this.setActividad(actividad);
        this.setObservacion(observacion);
    }


    public CharSequence getFecha() {
        return fecha;
    }

    public void setFecha(CharSequence fecha) {
        this.fecha = fecha;
    }

    public CharSequence getTiempo() {
        return tiempo;
    }

    public void setTiempo(CharSequence tiempo) {
        this.tiempo = tiempo;
    }

    public CharSequence getDistancia() {
        return distancia;
    }

    public void setDistancia(CharSequence distancia) {
        this.distancia = distancia;
    }

    public CharSequence getActividad() {
        return actividad;
    }

    public void setActividad(CharSequence actividad) {
        this.actividad = actividad;
    }

    public CharSequence getObservacion() {
        return observacion;
    }

    public void setObservacion(CharSequence observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "recorrido{" +
                "fecha=" + fecha +
                ", tiempo=" + tiempo +
                ", distancia=" + distancia +
                ", actividad=" + actividad +
                ", observacion=" + observacion +
                '}';
    }
}
