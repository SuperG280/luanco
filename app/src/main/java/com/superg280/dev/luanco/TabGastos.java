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
import android.widget.TextView;

import java.util.ArrayList;


public class TabGastos extends Fragment {

    private ArrayList<Gasto> gastos = null;
    private AdapterGasto adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View tab = inflater.inflate(R.layout.fragment_tab_gastos, container, false);

        refillGastos();
        adapter = new AdapterGasto(getActivity(), gastos);

        ListView lv = (ListView) tab.findViewById(R.id.listView_gastos);

        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int posicion = i;

                AlertDialog.Builder dlgBorrar = new AlertDialog.Builder(getActivity());
                dlgBorrar.setTitle(getString(R.string.dlg_delete_title_gastos));
                dlgBorrar.setMessage(getString(R.string.dlg_delete_mensaje_gastos));
                dlgBorrar.setCancelable(false);
                dlgBorrar.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        gastos.remove(posicion);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlgBorrar.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgBorrar.show();

                return false;

                /*
                Snackbar.make(view, "Borrado de gasto", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
                */
            }
        });




        // Inflate the layout for this fragment
        return tab;
    }

    public void refillGastos() {

        gastos = new ArrayList<Gasto>();

        Calendar cal = Calendar.getInstance();
        cal.set(2018, 1, 18);
        gastos.add(new Gasto( cal.getTimeInMillis(), "Recibo de la luz", 10846));
        cal.set(2018, 1, 21);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Contribucion municipal", 4832));
        cal.set(2018, 1, 24);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de agua", 1367));
        cal.set(2018, 1, 28);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de la luz", 14546));
        cal.set(2018, 2, 2);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Basura", 6212));
        cal.set(2018, 2, 12);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Calentador", 34508));
        cal.set(2018, 2, 15);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de la luz", 2146));
        cal.set(2018, 2, 19);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de agua", 1236));
        cal.set(2018, 2, 22);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Asistenta", 156716));
        cal.set(2018, 2, 23);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Comunidad", 10848));
        cal.set(2018, 3, 1);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Bombona gas", 18889));
        cal.set(2018, 3, 6);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de la luz", 9878));
        cal.set(2018, 4, 18);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Contribucion", 3456));
        cal.set(2018, 5, 23);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Otros", 45834));
        cal.set(2018, 5, 30);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de la luz", 5816));
        cal.set(2018, 6, 1);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Recibo de la luz", 2476));
        cal.set(2018, 6, 22);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Municipal", 15209));
        cal.set(2018, 7, 18);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Paguilla", 408645));
        cal.set(2018, 7, 19);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Cortinas nuevas", 3834));
        cal.set(2018, 8, 21);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Sartenes", 20846));
        cal.set(2018, 8, 24);
        gastos.add( new Gasto( cal.getTimeInMillis(), "Ultimo", 1200));
    }

}
