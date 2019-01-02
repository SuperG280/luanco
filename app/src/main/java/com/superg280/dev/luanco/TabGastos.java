package com.superg280.dev.luanco;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TabGastos extends Fragment {

    private ArrayList<Gasto> gastos = null;
    private AdapterGasto adapter = null;
    public EditText editTextFecha;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View tab = inflater.inflate(R.layout.fragment_tab_gastos, container, false);

        gastos = ((LuTabActivity)this.getActivity()).gastos;
        adapter = new AdapterGasto(getActivity(), gastos);

        if( gastos == null || gastos.size() == 0) {
            Toast.makeText( getActivity(), getResources().getString(R.string.toast_no_gastos), Toast.LENGTH_LONG).show();
        }

        ///////////////////
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("gastos");

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
                        deleteGasto( posicion);
                    }
                });

                dlgBorrar.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgBorrar.show();

                return true;

                /*
                Snackbar.make(view, "Borrado de gasto", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
                */
            }
        });

        FloatingActionButton fab = (FloatingActionButton) tab.findViewById(R.id.fab_new_gastos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dlg = createNewGastoDialogo();
                dlg.show();
            }
        });

        // Inflate the layout for this fragment
        return tab;
    }

    public AlertDialog createNewGastoDialogo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.new_gasto, null);

        builder.setView(v);

        final EditText editTextImporte = (EditText)v.findViewById( R.id.editText_new_gasto_importe);

        final AutoCompleteTextView  editTextDescripcion = (AutoCompleteTextView)v.findViewById( R.id.editText_new_gasto_descripcion);

        final Spinner spinnerCategories = v.findViewById( R.id.spinner_new_gasto_categorias);

        CategoriesSpinnerAdapter categoriesAdapter = new CategoriesSpinnerAdapter( getContext(), Categories.getCat_icons(), Categories.getCat_literales());
        spinnerCategories.setAdapter( categoriesAdapter);

        ArrayList<String> descriptions = UtilArrayGastos.getDescriptions(gastos);
        if( descriptions != null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, descriptions.toArray());

            editTextDescripcion.setAdapter(adapter);
            editTextDescripcion.setThreshold(1);
        }

        editTextFecha = (EditText)v.findViewById(R.id.editText_new_gasto_fecha);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        editTextFecha.setText( df.format(cal.getTime()));

        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String importe = editTextImporte.getText().toString();
                        String fecha = editTextFecha.getText().toString();
                        String descripcion = editTextDescripcion.getText().toString();
                        int categoria = spinnerCategories.getSelectedItemPosition();
                        addNewGasto( fecha, importe, descripcion, categoria);
                    }
                });

        builder.setNegativeButton("CANCELAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText( getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }

    private void showDatePickerDialog() {

        //Calendario para obtener fecha & hora
        Calendar c = Calendar.getInstance();

        //Variables para obtener la fecha
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int anio = c.get(Calendar.YEAR);

        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? 0 + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? 0 + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                editTextFecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
            *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    public boolean addNewGasto( String fecha, String importe, String descripcion, int categoria) {

        String fecha_formated = fecha.replace( '/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = sdf.parse(fecha_formated);
        } catch( Exception ex) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        long lImporte;
        try {
            lImporte = (long)(new Double( importe).doubleValue() * 100);
        } catch( Exception ex) {
            return false;
        }

        Gasto newGasto = new Gasto( cal.getTimeInMillis(), descripcion, lImporte);
        newGasto.setCategoria(categoria);

        addGastoInFireBase( newGasto);

        insertNewGastoInArray( newGasto);
        adapter.notifyDataSetChanged();
        return true;
    }

    public void insertNewGastoInArray( Gasto gasto) {

        if( gastos.size() == 0) {
            gastos.add( gasto);
            return;
        }

        long NewFecha = gasto.getFecha();
        for( int i = 0; i < gastos.size(); i++) {

            Gasto g = gastos.get( i);
            if( g.getFecha() <= NewFecha) {
                gastos.add( i, gasto);
                return;
            }
        }
        //Si ha llegado a salir del bucle es que no ha encontrado
        //una fecha menor y esta es la menor, así que lo mete el último.
        gastos.add( gasto);
    }

    public void deleteGasto( int posicion) {

        deleteGastoInFireBase( gastos.get(posicion).getId());
        //getLuancoBD().deleteGasto( gastos.get(posicion).getId());
        gastos.remove(posicion);
        adapter.notifyDataSetChanged();
    }

    public void addGastoInFireBase( Gasto gasto) {

        mFirebaseDatabase.child( gasto.getId()).setValue(gasto);
    }

    public void deleteGastoInFireBase( String gastoId) {

        mFirebaseDatabase.child( gastoId).removeValue();
    }
}
