package com.superg280.dev.luanco;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Super on 10/03/2018.
 */

public class Ingreso implements java.io.Serializable{

    private String id;
    private long fecha;
    private String descripcion;
    private long importe;
    private int    userID;

    public Ingreso( String id, long fecha, String descripcion, long importe, int userid) {
        this.id          = id;
        this.fecha       = fecha;
        this.descripcion = descripcion;
        this.importe     = importe;
        this.userID      = userid;
    }

    Ingreso( long fecha, String descripcion, long importe, int userid) {
        regenerateID();
        this.fecha       = fecha;
        this.descripcion = descripcion;
        this.importe     = importe;
        this.userID      = userid;
    }

    Ingreso() {
        regenerateID();
        this.fecha = Calendar.getInstance().getTimeInMillis();
        this.descripcion = "";
        this.importe = 0;
        this.userID = 0;
    }
    private void regenerateID() {
        this.id = UUID.randomUUID().toString();
    }

    public void setId( String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
        return df.format(cal.getTime());
    }

    public long getImporte() {
        return importe;
    }

    public String formatImporte() {

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return  nf.format( (double)importe / 100);

    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public void createFechaToday( ) {
        this.fecha = Calendar.getInstance().getTimeInMillis();
    }

    public void setImporte(long importe) {
        this.importe = importe;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Calendar fechaToCalendar( ) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( this.fecha);
        return cal;
    }
}
