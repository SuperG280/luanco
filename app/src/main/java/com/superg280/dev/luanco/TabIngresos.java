package com.superg280.dev.luanco;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class TabIngresos extends Fragment {

    private ArrayList<Ingreso> ingresos = null;
    private AdapterIngreso adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab = inflater.inflate(R.layout.fragment_tab_ingresos, container, false);

        refillIngresos();
        adapter = new AdapterIngreso( getActivity(), ingresos);

        ListView lv = (ListView) tab.findViewById(R.id.listView_ingresos);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int posicion = i;

                AlertDialog.Builder dlgBorrar = new AlertDialog.Builder(getActivity());
                dlgBorrar.setTitle(getString(R.string.dlg_delete_title_ingresos));
                dlgBorrar.setMessage(getString(R.string.dlg_delete_mensaje_ingresos));
                dlgBorrar.setCancelable(false);
                dlgBorrar.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        ingresos.remove(posicion);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlgBorrar.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgBorrar.show();

                return false;

                /*Snackbar.make(view, "Borrado de ingreso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;*/
            }
        });


        // Inflate the layout for this fragment
        return tab;
    }

    public void refillIngresos() {

        ingresos = new ArrayList<Ingreso>();

        Calendar cal = Calendar.getInstance();
        cal.set(2018, 1, 18);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Ramon", 10846, 1));
        cal.set(2018, 1, 21);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 4832, 2));
        cal.set(2018, 1, 24);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 1367, 2));
        cal.set(2018, 1, 28);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 14546, 3));
        cal.set(2018, 2, 02);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Ramon", 6212, 1));
        cal.set(2018, 2, 12);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 34508, 2));
        cal.set(2018, 2, 15);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 2146, 3));
        cal.set(2018, 2, 19);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Ramon", 1236, 1));
        cal.set(2018, 2, 22);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 156716, 2));
        cal.set(2018, 2, 23);
        ingresos.add(new Ingreso( cal.getTimeInMillis(), "Pago de Ramon", 10848, 1));
        cal.set(2018, 3, 1);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 18889, 3));
        cal.set(2018, 3, 06);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 9878, 2));
        cal.set(2018, 4, 18);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Ramon", 3456, 1));
        cal.set(2018, 5, 23);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 45834, 2));
        cal.set(2018, 5, 30);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 5816, 3));
        cal.set(2018, 6, 1);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 2475, 3));
        cal.set(2018, 6, 22);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 15209, 3));
        cal.set(2018, 7, 18);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Maria", 40845, 2));
        cal.set(2018, 7, 19);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Ramon", 3834, 1));
        cal.set(2018, 8, 21);
        ingresos.add( new Ingreso( cal.getTimeInMillis(), "Pago de Luis", 20846, 3));
    }

}
