package com.superg280.dev.luanco;

import android.content.ContentValues;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

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
        this.descripcion = new String( descripcion);
        this.importe     = importe;
        this.userID      = userid;
    }

    public Ingreso( long fecha, String descripcion, long importe, int userid) {
        regenerateID();
        this.fecha       = fecha;
        this.descripcion = new String( descripcion);
        this.importe     = importe;
        this.userID      = userid;
    }

    public Ingreso() {
        regenerateID();
        this.fecha = Calendar.getInstance().getTimeInMillis();
        this.descripcion = new String();
        this.importe = 0;
        this.userID = 0;
    }
    public void regenerateID() {
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
        return String.format("%.2f€", (double) ((double)importe / 100));

    }
    public void setDescripcion(String descripcion) {
        this.descripcion = new String( descripcion);
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public void setFechaToday( ) {
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

    //Convierte el objeto ingreso actual en el ContentValues para insertarlo en la base de datos.
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put( LuancoContract.IngresoEntry.ID            , id);
        values.put( LuancoContract.IngresoEntry.FECHA         , fecha);
        values.put( LuancoContract.IngresoEntry.DESCRIPCION   , descripcion);
        values.put( LuancoContract.IngresoEntry.IMPORTE       , importe);
        values.put( LuancoContract.IngresoEntry.USUARIO       , userID);
        return values;
    }
}
