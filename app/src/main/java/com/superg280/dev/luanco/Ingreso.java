package com.superg280.dev.luanco;

import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

/**
 * Created by Super on 10/03/2018.
 */

public class Ingreso {

    private int id;
    private long fecha;
    private String descripcion;
    private long importe;
    private int    userID;

    public Ingreso() {
        super();
    }

    public Ingreso( int id, long fecha, String descripcion, long importe, int userid) {
        this.id          = id;
        this.fecha       = fecha;
        this.descripcion = new String( descripcion);
        this.importe     = importe;
        this.userID      = userid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getFechaLong() {
        return fecha;
    }

    public String getFecha() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( fecha);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        return df.format(cal);
    }

    public long getImporteLong() {
        return importe;
    }

    public String getImporte() {
        return "" + (double) ((double)importe / 100) + "€";

    }
    public void setDescripcion(String descripcion) {
        this.descripcion = new String( descripcion);
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
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
}
