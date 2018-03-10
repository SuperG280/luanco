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

import java.util.ArrayList;


public class TabIngresos extends Fragment {

    private ArrayList<Ingreso> ingresos = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab = inflater.inflate(R.layout.fragment_tab_ingresos, container, false);

        ListView lv = (ListView) tab.findViewById(R.id.listView_ingresos);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Snackbar.make(view, "Borrado de ingreso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        refillIngresos();
        AdapterIngreso adapter = new AdapterIngreso( getActivity(), ingresos);

        lv.setAdapter(adapter);

        // Inflate the layout for this fragment
        return tab;
    }

    public void refillIngresos() {

        ingresos = new ArrayList<Ingreso>();

        ingresos.add( new Ingreso( "18-02-2018", "Pago de Ramon", "108,46€", 1));
        ingresos.add( new Ingreso( "21-02-2018", "Pago de Maria", "48,32€", 2));
        ingresos.add( new Ingreso( "24-02-2018", "Pago de Maria", "13,67€", 2));
        ingresos.add( new Ingreso( "28-02-2018", "Pago de Luis", "145,46€", 3));
        ingresos.add( new Ingreso( "02-03-2018", "Pago de Ramon", "62,12€", 1));
        ingresos.add( new Ingreso( "12-03-2018", "Pago de Maria", "345,08€", 2));
        ingresos.add( new Ingreso( "15-03-2018", "Pago de Luis", "21,46€", 3));
        ingresos.add( new Ingreso( "19-03-2018", "Pago de Ramon", "12,36€", 1));
        ingresos.add( new Ingreso( "22-03-2018", "Pago de Maria", "1567,16€", 2));
        ingresos.add( new Ingreso( "23-03-2018", "Pago de Ramon", "108,48€", 1));
        ingresos.add( new Ingreso( "01-04-2018", "Pago de Luis", "188,89€", 3));
        ingresos.add( new Ingreso( "06-04-2018", "Pago de Maria", "98,78€", 2));
        ingresos.add( new Ingreso( "18-05-2018", "Pago de Ramon", "34,56€", 1));
        ingresos.add( new Ingreso( "23-06-2018", "Pago de Maria", "458,34€", 2));
        ingresos.add( new Ingreso( "30-06-2018", "Pago de Luis", "58,16€", 3));
        ingresos.add( new Ingreso( "01-07-2018", "Pago de Luis", "24,76€", 3));
        ingresos.add( new Ingreso( "22-07-2018", "Pago de Luis", "152,09€", 3));
        ingresos.add( new Ingreso( "18-08-2018", "Pago de Maria", "408,645€", 2));
        ingresos.add( new Ingreso( "19-08-2018", "Pago de Ramon", "38,34€", 1));
        ingresos.add( new Ingreso( "21-09-2018", "Pago de Luis", "208,46€", 3));
    }

}
