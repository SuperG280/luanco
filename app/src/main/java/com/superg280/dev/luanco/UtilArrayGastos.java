package com.superg280.dev.luanco;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UtilArrayGastos {

    public final String[] meses = new String[]{"Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private static Context TheContext;

    public UtilArrayGastos(Context context) {
        TheContext = context;
    }

    @Nullable
    public static Gasto addAllGastosOfCategory(ArrayList<Gasto> gastos, int categoryID) {

        if( categoryID < Categories.CAT_AGUA || categoryID > Categories.CAT_OTRO)
            return null;

        Gasto gResult = new Gasto();
        gResult.setCategoria( categoryID);

        for( Gasto g: gastos) {
            if( g.getCategoria() == categoryID) {
                gResult.setImporte( gResult.getImporte() + g.getImporte());
            }
        }

        return gResult;
    }

    public static ArrayList<Gasto> packGastos(ArrayList<Gasto> gastosOrg) {

        ArrayList<Gasto> gastosPack = new ArrayList<>();

        if( gastosOrg == null || gastosOrg.size() == 0)
            return gastosPack;

        for( int i = Categories.CAT_AGUA; i < Categories.CAT_OTRO; i++) {
            Gasto g = addAllGastosOfCategory( gastosOrg, i);
            if( g != null && g.getImporte() > 0) {
                g.setDescripcion( Categories.getCategoryLiteral( i));
                gastosPack.add( g);
            }
        }

        return gastosPack;
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

    public static ArrayList<String> getDescriptions(ArrayList<Gasto> gastos) {

        if( gastos.size() == 0)
            return null;

        ArrayList<String> descriptions = new ArrayList<>();

        String strDescription;
        for( int i = 0; i < gastos.size(); i++) {
            strDescription = gastos.get(i).getDescripcion();

            if (!descriptions.contains(strDescription)) {
                descriptions.add(strDescription);
            }
        }
        return descriptions;
    }
}
