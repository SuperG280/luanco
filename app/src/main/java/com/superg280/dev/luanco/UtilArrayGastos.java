package com.superg280.dev.luanco;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

public class UtilArrayGastos {

    public final String[] meses = new String[]{"Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private Context TheContext;

    public UtilArrayGastos( Context context) {
        TheContext = context;
    }

    public final String[] luz = { "luz", "electricidad", "edp"};
    public final String[] agua = { "agua"};
    public final String[] banco = { "comision", "comisión", "banco", "sabadell" };
    public final String[] impuestos = { "contribucion", "contribución", "ibi", "impuesto", "principado", "ayuntamiento"};
    public final String[] comunidad = { "comunidad", "vecinos", "derrama"};

    public ArrayList<Gasto> packGastos(ArrayList<Gasto> gastosOrg) {

        ArrayList<Gasto> gastosPack = new ArrayList<>();

        if( gastosOrg == null || gastosOrg.size() == 0)
            return gastosPack;

        Gasto gastoLuz = new Gasto();
        Gasto gastoAgua = new Gasto();
        Gasto gastoBanco = new Gasto();
        Gasto gastoImpuestos = new Gasto();
        Gasto gastoComunidad = new Gasto();
        gastoLuz.setDescripcion( TheContext.getString( R.string.label_pie_luz));
        gastoAgua.setDescripcion( TheContext.getString( R.string.label_pie_agua));
        gastoBanco.setDescripcion( TheContext.getString( R.string.label_pie_banco));
        gastoImpuestos.setDescripcion( TheContext.getString( R.string.label_pie_impuestos));
        gastoComunidad.setDescripcion( TheContext.getString( R.string.label_pie_comunidad));

        for( Gasto g: gastosOrg) {
            String description = g.getDescripcion();

            if( descriptionInArray( description, luz)) {
                gastoLuz.setImporte( gastoLuz.getImporte() + g.getImporte());
            } else if( descriptionInArray( description, agua)){
                gastoAgua.setImporte( gastoAgua.getImporte() + g.getImporte());
            } else if( descriptionInArray( description, banco)) {
                gastoBanco.setImporte(gastoBanco.getImporte() + g.getImporte());
            } else if( descriptionInArray( description, impuestos)) {
                gastoImpuestos.setImporte(gastoImpuestos.getImporte() + g.getImporte());
            } else if( descriptionInArray( description, comunidad)) {
                gastoComunidad.setImporte(gastoComunidad.getImporte() + g.getImporte());
            } else {
                gastosPack.add( g);
            }
        }

        if( gastoLuz.getImporte() > 0)
            gastosPack.add( gastoLuz);

        if( gastoAgua.getImporte() > 0)
            gastosPack.add( gastoAgua);

        if( gastoBanco.getImporte() > 0)
            gastosPack.add( gastoBanco);

        if( gastoImpuestos.getImporte() > 0)
            gastosPack.add( gastoImpuestos);

        if( gastoComunidad.getImporte() > 0)
            gastosPack.add( gastoComunidad);

        return gastosPack;
    }

    public boolean descriptionInArray( String description, String[] array) {

        if( description == null || array == null || array.length == 0)
            return false;

        for( String s: array) {
            if(description.toUpperCase().contains(s.toUpperCase()))
                return true;
        }
        return false;
    }

    public ArrayList<Gasto> getGastosYear( ArrayList<Gasto> gastos, int year) {

        ArrayList<Gasto> gastosYear = new ArrayList<>();

        for( Gasto g: gastos) {
            Calendar fechaGasto = g.fechaToCalendar();
            if( fechaGasto.get(Calendar.YEAR) < year) {
                break;
            }
            if( fechaGasto.get( Calendar.YEAR) == year) {
                gastosYear.add( g);
            }
        }

        return gastosYear;
    }

    public ArrayList<Gasto> getGastosMonth( ArrayList<Gasto> gastos, int month, int year) {

        ArrayList<Gasto> gastosMes = new ArrayList<>();

        for( Gasto g: gastos) {
            Calendar fechaGasto = g.fechaToCalendar();
            if( fechaGasto.get(Calendar.YEAR) < year) {
                break;
            }
            if( fechaGasto.get( Calendar.YEAR) == year) {
                if( fechaGasto.get( Calendar.MONTH) < month) {
                    break;
                }
                if( fechaGasto.get( Calendar.MONTH) == month) {
                    gastosMes.add( g);
                }
            }
        }

        return gastosMes;
    }
}
