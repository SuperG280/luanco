package com.superg280.dev.luanco;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class TabGastos extends Fragment {

    private ArrayList<Gasto> gastos = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View tab = inflater.inflate(R.layout.fragment_tab_gastos, container, false);

        ListView lv = (ListView) tab.findViewById(R.id.listView_gastos);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Snackbar.make(view, "Borrado de gasto", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        refillGastos();
        AdapterGasto adapter = new AdapterGasto(getActivity(), gastos);

        lv.setAdapter(adapter);

        // Inflate the layout for this fragment
        return tab;
    }

    public void refillGastos() {

        gastos = new ArrayList<Gasto>();

        gastos.add( new Gasto( "18-02-2018", "Recibo de la luz", "108,46€"));
        gastos.add( new Gasto( "21-02-2018", "Contribucion municipal", "48,32€"));
        gastos.add( new Gasto( "24-02-2018", "Recibo de agua", "13,67€"));
        gastos.add( new Gasto( "28-02-2018", "Recibo de la luz", "145,46€"));
        gastos.add( new Gasto( "02-03-2018", "Basura", "62,12€"));
        gastos.add( new Gasto( "12-03-2018", "Calentador", "345,08€"));
        gastos.add( new Gasto( "15-03-2018", "Recibo de la luz", "21,46€"));
        gastos.add( new Gasto( "19-03-2018", "Recibo de agua", "12,36€"));
        gastos.add( new Gasto( "22-03-2018", "Asistenta", "1567,16€"));
        gastos.add( new Gasto( "23-03-2018", "Comunidad", "108,48€"));
        gastos.add( new Gasto( "01-04-2018", "Bombona gas", "188,89€"));
        gastos.add( new Gasto( "06-04-2018", "Recibo de la luz", "98,78€"));
        gastos.add( new Gasto( "18-05-2018", "Contribucion", "34,56€"));
        gastos.add( new Gasto( "23-06-2018", "Otros", "458,34€"));
        gastos.add( new Gasto( "30-06-2018", "Recibo de la luz", "58,16€"));
        gastos.add( new Gasto( "01-07-2018", "Recibo de la luz", "24,76€"));
        gastos.add( new Gasto( "22-07-2018", "Municipal", "152,09€"));
        gastos.add( new Gasto( "18-08-2018", "Paguilla", "408,645€"));
        gastos.add( new Gasto( "19-08-2018", "Cortinas nuevas", "38,34€"));
        gastos.add( new Gasto( "21-09-2018", "Sartenes", "208,46€"));
    }

}
