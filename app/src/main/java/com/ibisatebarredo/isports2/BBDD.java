package com.ibisatebarredo.isports2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by anton_000 on 07/04/2016.
 */

public class BBDD {

    private SQLiteDatabase bd = null;
    private BDHelper helper = null;
    String tabla = "recorridos";

    public BBDD(Context context) {
        helper = new BDHelper(context, "recorridos.sqlite", null, 1);
    }

    public void open_BD() {
        if (bd == null)
            bd = helper.getWritableDatabase();
    }

    public void close_BD() {
        if (bd != null)
            bd.close();
    }

    public void insertar_Recorrido(recorrido r) {
        if (bd.isOpen() && r != null) {

            ContentValues values = new ContentValues();

            values.put("fecha", (String) r.getFecha());
            values.put("tiempo", (String) r.getTiempo());
            values.put("distancia", (String) r.getDistancia());
            values.put("actividad", (String) r.getActividad());
            values.put("observacion", (String) r.getObservacion());

            bd.insert(tabla, null, values);
        }
    }

    public void eliminar_Recorrido(recorrido r) {
        if (bd.isOpen() && r != null) {

            String where = "fecha = ?";
            // CharSequence[] argumentoswhere = new CharSequence[]{r.getFecha()};
            //bd.delete(tabla, where, (String[]) argumentoswhere);
            String [] argumentoswhere = convertToStringArray(new CharSequence[]{r.getFecha()});
            bd.delete(tabla, where, argumentoswhere);

        }
    }

    public List<recorrido> consultar_recorridos() {

        List<recorrido> lr = new ArrayList<>();

        if (bd.isOpen()) {

            String[] columnas = new String[]{"id", "fecha", "tiempo", "distancia", "actividad", "observacion"};
            String where = null; // "id = ?"
            String[] argumentoswhere = null; // = new String[] {"35"};
            String groupby = null;
            String having = null;
            String orderby = null;
            String limit = null;

            Cursor c1 = bd.query(tabla, columnas, where, argumentoswhere, groupby, having, orderby, limit);

            if (c1.moveToFirst()) {
                // Recorremos el cursor hasta que no haya más registros
                do {
                    recorrido r = new recorrido(c1.getString(1), c1.getString(2), c1.getString(3),c1.getString(4),c1.getString(5));   // El campo 0 es el ID
                    lr.add(r);
                } while (c1.moveToNext());
            }
        }

        return lr;
    }

    public String[] convertToStringArray(CharSequence[] charSequences) {
        if (charSequences instanceof String[]) {
            return (String[]) charSequences;
        }

        String[] strings = new String[charSequences.length];
        for (int index = 0; index < charSequences.length; index++) {
            strings[index] = charSequences[index].toString();
        }

        return strings;
    }

    private class BDHelper extends SQLiteOpenHelper {

        String create_table = "CREATE TABLE  RECORRIDOS ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,fecha TEXT,tiempo TEXT,distancia TEXT,actividad TEXT,observacion TEXT);";

        public BDHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            if (!db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
                db.execSQL(create_table);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (!db.isReadOnly()) {
                db.execSQL("DROP TABLE IF EXISTS RECORRIDOS");

                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
                db.execSQL(create_table);
            }

        }



    }

}
