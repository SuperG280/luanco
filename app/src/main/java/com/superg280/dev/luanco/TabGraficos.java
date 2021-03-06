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
import java.util.Calendar;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


public class TabGraficos extends Fragment {

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
        final View tab = inflater.inflate(R.layout.fragment_tab_graficos, container, false);

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

        UtilArrayGastos UtilGastos = new UtilArrayGastos( this.getContext());
        ArrayList<Gasto> gastosYear = UtilGastos.getGastosYear( gastos, mnYearTotal);
        ArrayList<Gasto> gastosAgrupados = UtilGastos.packGastos( gastosYear);

        long total = 0;
        for( Gasto g: gastosAgrupados) {
            total += g.getImporte();
        }

        PieChart pieChart = tab.findViewById(R.id.piechart_gastos_total_ano);
        pieChart.setUsePercentValues(true);

        List<PieEntry> yvalues = new ArrayList<>();
        int index = 0;
        for( Gasto g: gastosAgrupados) {
            String Descripcion = String.format("%s %s", g.getDescripcion(), g.formatImporte());
            yvalues.add(new PieEntry( getPorcentaje( total, g.getImporte()), Descripcion));//( g.getDescripcion().length() >= 10) ? g.getDescripcion().substring(0, 10) : g.getDescripcion()));
            index++;
        }

        TextView mensajeNoGastos = tab.findViewById( R.id.textView_pie_sin_gastos_total);

        if( index > 0) {
            mensajeNoGastos.setVisibility( View.INVISIBLE);
        } else {
            mensajeNoGastos.setVisibility( View.VISIBLE);
        }

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
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

        ArrayList<Gasto> gastosMes = new UtilArrayGastos( getContext()).getGastosMonth( gastos, mnMonth, mnYear);

        long total = 0;
        for( Gasto g: gastosMes) {
            total += g.getImporte();
        }

        PieChart pieChart = tab.findViewById(R.id.piechart_gastos_mes);
        pieChart.setUsePercentValues(true);

        List<PieEntry> yvalues = new ArrayList<>();
        int index = 0;
        for( Gasto g: gastosMes) {
            String Descripcion = String.format("%s %s", g.getDescripcion(), g.formatImporte());
            yvalues.add(new PieEntry( getPorcentaje( total, g.getImporte()), Descripcion));//( g.getDescripcion().length() >= 10) ? g.getDescripcion().substring(0, 10) : g.getDescripcion()));
            index++;
        }

        TextView mensajeNoGastos = tab.findViewById( R.id.textView_pie_sin_gastos);

        if( index > 0) {
            mensajeNoGastos.setVisibility( View.INVISIBLE);
        } else {
            mensajeNoGastos.setVisibility( View.VISIBLE);
        }

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
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
}
