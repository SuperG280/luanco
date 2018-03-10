package com.superg280.dev.luanco;

/**
 * Created by Super on 10/03/2018.
 */

public class Ingreso {

    private String fecha;
    private String descripcion;
    private String importe;
    private int    userID;

    public Ingreso() {
        super();
    }

    public Ingreso(String fecha, String descripcion, String importe, int userid) {
        this.fecha       = new String(fecha);
        this.descripcion = new String( descripcion);
        this.importe     = new String( importe);
        this.userID      = userid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getImporte() {
        return importe;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = new String( descripcion);
    }

    public void setFecha(String fecha) {
        this.fecha = new String( fecha);
    }

    public void setImporte(String importe) {
        this.importe = new String( importe);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
