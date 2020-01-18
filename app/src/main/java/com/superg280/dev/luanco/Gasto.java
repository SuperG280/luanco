package com.superg280.dev.luanco;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Super on 10/03/2018.
 */

@IgnoreExtraProperties
public class Gasto implements java.io.Serializable{

    private String id;
    private long fecha;
    private String descripcion;
    private long importe;
    private int categoria;
    private String nota;
    private boolean imputable;


    public Gasto( long fecha, String descripcion, long importe, boolean imputable) {
        regenerateID() ;
        this.fecha          = fecha;
        this.descripcion    = descripcion;
        this.importe        = importe;
        this.categoria      = Categories.CAT_OTRO;
        this.nota           = "";
        this.imputable      = imputable;
    }

    public Gasto() {
        regenerateID();
        this.importe        = 0;
        this.fecha          = Calendar.getInstance().getTimeInMillis();
        this.descripcion    = "";
        this.categoria      = Categories.CAT_OTRO;
        this.nota           = "";
        this.imputable      = true;
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
        return df.format(cal.getTime());
    }

    public long getImporte() {
        return importe;
    }

    @Exclude
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

    public void setImporte(long importe) {
        this.importe =  importe;
    }

    public Calendar fechaToCalendar( ) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( this.fecha);
        return cal;
    }

    public String toMail() {
        return formatFecha() + " " + getDescripcion() + ": " + formatImporte();
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public boolean hasNota() {
        return !this.nota.isEmpty();
    }

    public boolean isImputable() { return this.imputable;}

    public void setImputable( boolean imputable) { this.imputable = imputable;}
}
