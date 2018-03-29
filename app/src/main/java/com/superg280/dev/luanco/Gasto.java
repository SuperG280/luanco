package com.superg280.dev.luanco;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.util.UUID;

/**
 * Created by Super on 10/03/2018.
 */

public class Gasto {

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
