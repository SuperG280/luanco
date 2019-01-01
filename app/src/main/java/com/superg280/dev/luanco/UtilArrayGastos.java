package com.superg280.dev.luanco;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Calcula la media de gastos de un mes en un año concreto.
     * @param month Indice de la clase Calendar del mes (Enero: 0).
     * @param year Numero de año de la clase Calendar.
     * @return la media aritmetica de los gastos de ese mes.
     */
    public static float getMediaDeMesAno( ArrayList<Gasto> gastos, int month, int year) {

        int numero = 0;
        long suma = 0;

        for( Gasto g: gastos) {
            Calendar fechaGasto = g.fechaToCalendar();
            if( fechaGasto.get( Calendar.YEAR) < year)
                break;
            if (fechaGasto.get(Calendar.YEAR) == year) {
                if (g.fechaToCalendar().get(Calendar.MONTH) == month) {
                    numero++;
                    suma += g.getImporte();
                }
            }
        }

        if( numero == 0 || suma == 0) {
            return 0;
        }

        return ( (float)suma / (float)100) / (float)numero;
    }

    public static long getGastosMes( ArrayList<Gasto> gastos, int month, int year) {

        long importe = 0;

        for( Gasto g: gastos) {

            Calendar fechaGasto = g.fechaToCalendar();

            if( fechaGasto.get( Calendar.YEAR) < year) {
                break;
            }
            if( fechaGasto.get( Calendar.YEAR) == year) {

                if( fechaGasto.get( Calendar.MONTH) < month) {
                    break;
                }

                if( month == fechaGasto.get( Calendar.MONTH)) {
                    importe += g.getImporte();
                }
            }
        }

        return importe;
    }

    public static ArrayList<Integer> differentYears( ArrayList<Gasto> gastos) {

        ArrayList<Integer> years = new ArrayList<>();

        for( Gasto g: gastos) {
            int year = g.fechaToCalendar().get( Calendar.YEAR);
            if( !years.contains( year)) {
                years.add( year);
            }
        }
        return years;

    }

    public static ArrayList<Float> getMedias(ArrayList<Gasto> gastos) {

        ArrayList<Integer> years = differentYears( gastos);
        Map< Integer, ArrayList<Float>> medias = new HashMap< >();

        for( Integer y: years) {
            ArrayList<Float> mediasOneYear = new ArrayList<>();
            for( int i = 0; i < 12; i++) {
                mediasOneYear.add( UtilArrayGastos.getMediaDeMesAno( gastos, i, y));
            }
            medias.put( y, mediasOneYear);
        }

        ArrayList< Float> result = new ArrayList<>();


        for( int i = 0; i < 12; i++) {
            float suma = 0;
            for (Map.Entry<Integer, ArrayList<Float>> entry : medias.entrySet()) {
                suma += entry.getValue().get(i);
            }
            result.add( suma / (float)medias.size());
        }

        return result;
    }

    public static long getTotalGastosAnoActual(ArrayList<Gasto> gastos) {

        if( gastos == null || gastos.size() == 0) {
            return 0;
        }

        ArrayList<Long> gastosAActual = getGastosAnoActual( gastos);

        long importe = 0;
        for( Long g: gastosAActual) {
            importe += g;
        }
        return importe;
    }

    public static ArrayList<Long> getGastosAnoActual(ArrayList<Gasto> gastos) {

        ArrayList<Long> gastosA = new ArrayList<Long>();

        Calendar hoy = Calendar.getInstance();

        for( Gasto g: gastos) {
            Calendar fechaGasto = g.fechaToCalendar();
            if( fechaGasto.get( Calendar.YEAR) < hoy.get( Calendar.YEAR)) {

                break;
            }

            if( hoy.get( Calendar.YEAR) == fechaGasto.get( Calendar.YEAR)) {
                gastosA.add( g.getImporte());
            }
        }

        return gastosA;
    }

    public static long getTotalIngresosAnoActual(ArrayList<Ingreso> ingresos) {

        if( ingresos == null || ingresos.size() == 0) {
            return 0;
        }

        ArrayList<Long> ingresosAActual = getIngresosAnoActual(ingresos);

        long importe = 0;
        for( Long i: ingresosAActual) {
            importe += i;
        }
        return importe;
    }

    public static ArrayList<Long> getIngresosAnoActual(ArrayList<Ingreso> ingresos) {

        ArrayList<Long> ingresosA = new ArrayList<Long>();

        Calendar hoy = Calendar.getInstance();

        for( Ingreso i: ingresos) {
            Calendar fechaIngreso = i.fechaToCalendar();
            if( fechaIngreso.get( Calendar.YEAR) < hoy.get( Calendar.YEAR)) {

                break;
            }

            if( hoy.get( Calendar.YEAR) == fechaIngreso.get( Calendar.YEAR)) {
                ingresosA.add( i.getImporte());
            }
        }

        return ingresosA;
    }
}
