package com.superg280.dev.luanco;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import java.util.Map;

import android.icu.util.Calendar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


public class TabUsuarios extends Fragment {

    private ArrayList<Gasto> gastos = null;

    public final String[] meses = new String[]{"Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View tab = inflater.inflate(R.layout.fragment_tab_usuarios, container, false);

        gastos = ((LuTabActivity)this.getActivity()).gastos;

        ArrayAdapter<String> mesesAdapter = new ArrayAdapter< String>( ((LuTabActivity)this.getActivity()), android.R.layout.simple_spinner_item, meses);
        Spinner spinnerMeses = tab.findViewById( R.id.spinner_pie_gastos_mes);
        spinnerMeses.setAdapter( mesesAdapter);

        spinnerMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refillPieGastos( tab, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return tab;
    }

    public void refillPieGastos( View tab, int month) {

        if( gastos == null || gastos.size() == 0)
            return;

        Calendar hoy = Calendar.getInstance();
        ArrayList<Gasto> gastosMes = getGastosMonth( month, hoy.get( Calendar.YEAR));

        long total = 0;
        for( Gasto g: gastosMes) {
            total += g.getImporte();
        }

        PieChart pieChart = (PieChart) tab.findViewById(R.id.piechart_gastos_mes);
        pieChart.setUsePercentValues(true);

        List<PieEntry> yvalues = new ArrayList<>();
        int index = 0;
        for( Gasto g: gastosMes) {
            yvalues.add(new PieEntry( getPorcentaje( total, g.getImporte()), g.getDescripcion()));

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
        description.setText( "Gastos mes " + meses[ month]);
        description.setTextSize( 13f);
        pieChart.setDescription( description);
        pieChart.getLegend().setEnabled(false);
        pieChart.setEntryLabelColor( Color.BLACK);
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
}
