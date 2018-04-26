package com.superg280.dev.luanco;

import android.content.ContentValues;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import com.google.firebase.database.Exclude;

import java.util.UUID;

/**
 * Created by Super on 10/03/2018.
 */

public class Gasto implements java.io.Serializable{

    private String id;
    private long fecha;
    private String descripcion;
    private long importe;

    public Gasto( String id, long fecha, String descripcion, long importe) {
        this.id          = id;
        this.fecha       = fecha;
        this.descripcion = new String( descripcion);
        this.importe     = importe;
    }

    public Gasto( long fecha, String descripcion, long importe) {
        regenerateID() ;
        this.fecha          = fecha;
        this.descripcion    = new String( descripcion);
        this.importe        = importe;
    }

    public Gasto() {
        regenerateID();
        this.importe = 0;
        this.fecha = Calendar.getInstance().getTimeInMillis();
        this.descripcion = new String();
    }

    public void regenerateID() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getFecha() {
        return fecha;
    }

    public String formatFecha() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( fecha);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        return df.format(cal);
    }

    public long getImporte() {
        return importe;
    }

    @Exclude
    public String formatImporte() {
        return String.format("%.2f€", (double) ((double)importe / 100));

    }

    public void setDescripcion(String descripcion) {
        this.descripcion = new String( descripcion);
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public void setImporte(long importe) {
        this.importe =  importe;
    }

    public Calendar fechaToCalendar( ) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( this.fecha);
        return cal;
    }
}
