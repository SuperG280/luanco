package com.superg280.dev.luanco;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.icu.util.Calendar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


public class TabUsuarios extends Fragment {

    public final String[] luz = { "luz", "electricidad", "edp"};
    public final String[] agua = { "agua"};
    public final String[] banco = { "comision", "comisión", "banco", "sabadell" };
    public final String[] impuestos = { "contribucion", "contribución", "ibi", "impuesto", "principado", "ayuntamiento"};
    public final String[] comunidad = { "comunidad", "vecinos", "derrama"};

    private ArrayList<Gasto> gastos = null;

    public final String[] meses = new String[]{"Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    public ArrayList<Integer> Years;

    private int mnMonth;
    private int mnYear;
    private int mnYearTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View tab = inflater.inflate(R.layout.fragment_tab_usuarios, container, false);

        gastos = ((LuTabActivity)this.getActivity()).gastos;
        differentYears();

        ArrayAdapter<String> mesesAdapter = new ArrayAdapter< >( this.getActivity(), android.R.layout.simple_spinner_item, meses);
        mesesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerMeses = tab.findViewById( R.id.spinner_pie_gastos_mes);
        spinnerMeses.setAdapter( mesesAdapter);

        mnMonth = Calendar.getInstance().get( Calendar.MONTH);
        mnYear  = Calendar.getInstance().get( Calendar.YEAR);
        mnYearTotal = mnYear;

        spinnerMeses.setSelection( mnMonth);
        spinnerMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mnMonth = i;
                refillPieGastos( tab);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<Integer> yearsAdapter = new ArrayAdapter< >( this.getActivity(), android.R.layout.simple_spinner_item, Years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerYears = tab.findViewById( R.id.spinner_pie_gastos_ano);
        spinnerYears.setAdapter( yearsAdapter);
        spinnerYears.setSelection( Calendar.getInstance().get( Calendar.YEAR) - mnYear);
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mnYear = (Integer) adapterView.getItemAtPosition(i);
                refillPieGastos(tab);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<Integer> yearsTotalAdapter = new ArrayAdapter< >( this.getActivity(), android.R.layout.simple_spinner_item, Years);
        yearsTotalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerTotalYears = tab.findViewById( R.id.spinner_pie_gastos_total_ano);
        spinnerTotalYears.setAdapter( yearsTotalAdapter);
        spinnerTotalYears.setSelection( Calendar.getInstance().get( Calendar.YEAR) - mnYearTotal);
        spinnerTotalYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mnYearTotal = (Integer) adapterView.getItemAtPosition(i);
                refillPieGastosTotal(tab);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return tab;
    }

    public void differentYears() {

        Years = new ArrayList<>();

        for( Gasto g: gastos) {
            int year = g.fechaToCalendar().get( Calendar.YEAR);
            if( !Years.contains( year)) {
                Years.add( year);
            }
        }
    }

    public void refillPieGastosTotal( View tab) {

        if( gastos == null || gastos.size() == 0)
            return;

        ArrayList<Gasto> gastosYear = getGastosYear( mnYearTotal);
        ArrayList<Gasto> gastosAgrupados = packGastos( gastosYear);

        long total = 0;
        for( Gasto g: gastosAgrupados) {
            total += g.getImporte();
        }

        PieChart pieChart = tab.findViewById(R.id.piechart_gastos_total_ano);
        pieChart.setUsePercentValues(true);

        List<PieEntry> yvalues = new ArrayList<>();
        int index = 0;
        for( Gasto g: gastosAgrupados) {
            yvalues.add(new PieEntry( getPorcentaje( total, g.getImporte()), g.getDescripcion()));
            index++;
        }

        TextView mensajeNoGastos = tab.findViewById( R.id.textView_pie_sin_gastos_total);

        if( index > 0) {
            mensajeNoGastos.setVisibility( View.INVISIBLE);
        } else {
            mensajeNoGastos.setVisibility( View.VISIBLE);
        }

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(13f);
        dataSet.setValueTextColor(Color.RED);

        PieData data = new PieData( dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData( data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(38f);

        Description description = new Description();
        description.setText( "Gastos " + mnYearTotal);
        description.setTextSize( 13f);
        pieChart.setDescription( description);
        pieChart.getLegend().setEnabled(false);
        pieChart.setEntryLabelColor( Color.BLACK);
        pieChart.animateXY(1400, 1400);
        pieChart.invalidate();
    }

    public void refillPieGastos( View tab) {

        if( gastos == null || gastos.size() == 0)
            return;

        ArrayList<Gasto> gastosMes = getGastosMonth( mnMonth, mnYear);

        long total = 0;
        for( Gasto g: gastosMes) {
            total += g.getImporte();
        }

        PieChart pieChart = tab.findViewById(R.id.piechart_gastos_mes);
        pieChart.setUsePercentValues(true);

        List<PieEntry> yvalues = new ArrayList<>();
        int index = 0;
        for( Gasto g: gastosMes) {
            yvalues.add(new PieEntry( getPorcentaje( total, g.getImporte()), g.getDescripcion()));
            index++;
        }

        TextView mensajeNoGastos = tab.findViewById( R.id.textView_pie_sin_gastos);

        if( index > 0) {
            mensajeNoGastos.setVisibility( View.INVISIBLE);
        } else {
            mensajeNoGastos.setVisibility( View.VISIBLE);
        }

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(13f);
        dataSet.setValueTextColor(Color.RED);

        PieData data = new PieData( dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData( data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(38f);

        Description description = new Description();
        description.setText( "Gastos " + meses[ mnMonth] + " " + mnYear);
        description.setTextSize( 13f);
        pieChart.setDescription( description);
        pieChart.getLegend().setEnabled(false);
        pieChart.setEntryLabelColor( Color.BLACK);

        pieChart.animateXY(1400, 1400);
        pieChart.invalidate();
    }

    public float getPorcentaje( long total, long gasto) {

        if( total == 0 || gasto == 0)
            return 0;

        float fgasto = (float)gasto / (float)100;
        float ftotal = (float)total / (float)100;

        return (fgasto * (float)100)/  ftotal;

    }

    public ArrayList<Gasto> getGastosMonth( int month, int year) {

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

    public ArrayList<Gasto> getGastosYear( int year) {

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

    public ArrayList<Gasto> packGastos( ArrayList<Gasto> gastosOrg) {

        ArrayList<Gasto> gastosPack = new ArrayList<>();

        if( gastosOrg == null || gastosOrg.size() == 0)
            return gastosPack;

        Gasto gastoLuz = new Gasto();
        Gasto gastoAgua = new Gasto();
        Gasto gastoBanco = new Gasto();
        Gasto gastoImpuestos = new Gasto();
        Gasto gastoComunidad = new Gasto();
        gastoLuz.setDescripcion( getString( R.string.label_pie_luz));
        gastoAgua.setDescripcion( getString( R.string.label_pie_agua));
        gastoBanco.setDescripcion( getString( R.string.label_pie_banco));
        gastoImpuestos.setDescripcion( getString( R.string.label_pie_impuestos));
        gastoComunidad.setDescripcion( getString( R.string.label_pie_comunidad));

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
}
