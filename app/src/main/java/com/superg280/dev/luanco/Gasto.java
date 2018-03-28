package com.superg280.dev.luanco;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

/**
 * Created by Super on 10/03/2018.
 */

public class Gasto {

    private int id;
    private long fecha;
    private String descripcion;
    private long importe;

    public Gasto() {
        super();
    }

    public Gasto( int id, long fecha, String descripcion, long importe) {
        this.id          = id;
        this.fecha       = fecha;
        this.descripcion = new String( descripcion);
        this.importe     = importe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.importe =  importe;
    }
}
