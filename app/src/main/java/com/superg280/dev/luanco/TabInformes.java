package com.superg280.dev.luanco;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


public class TabInformes extends Fragment {

    private ArrayList<Gasto> gastos = null;

    public final String[] meses = new String[]{"Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    public ArrayList<Integer> Years;
    private AdapterGasto adapter = null;
    //private int mnMonth;
    //private int mnYear;
    //private int mnYearTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View tab = inflater.inflate(R.layout.fragment_tab_informes, container, false);

        gastos = ((LuTabActivity)this.getActivity()).gastos;
        differentYears();

        Calendar cal = Calendar.getInstance();

        //mnMonth = cal.get( Calendar.MONTH);
        //mnYear  = cal.get( Calendar.YEAR);
        //mnYearTotal = mnYear;

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String strFechaFin = df.format(cal.getTime());
        cal.set( Calendar.MONTH, Calendar.JANUARY);
        cal.set( Calendar.DAY_OF_MONTH, 1);
        String strFechaInicio = df.format(cal.getTime());


        final EditText editTextFechaInicio = tab.findViewById(R.id.editText_informes_fecha_inicio);
        editTextFechaInicio.setText( strFechaInicio);
        editTextFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog( editTextFechaInicio);
            }
        });

        final EditText editTextFechaFin = tab.findViewById(R.id.editText_informes_fecha_fin);
        editTextFechaFin.setText( strFechaFin);
        editTextFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog( editTextFechaFin);
            }
        });

        final Spinner spinnerCategories = tab.findViewById( R.id.spinner_informes_categorias);

        CategoriesSpinnerAdapter categoriesAdapter = new CategoriesSpinnerAdapter( getContext(), Categories.getCat_iconsAll(), Categories.getCat_literalesAll());
        spinnerCategories.setAdapter( categoriesAdapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        ImageButton buttonSearch = tab.findViewById( R.id.imageButton_informes_buscar);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAndPaintResults(tab);
            }
        });

        final Switch switchInicio = tab.findViewById( R.id.switch_informres_inicio);

        switchInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( switchInicio.isChecked())
                    editTextFechaInicio.setEnabled( false);
                else
                    editTextFechaInicio.setEnabled( true);
            }
        });
        return tab;
    }

    private void showDatePickerDialog( final EditText obj) {

        //Calendario para obtener fecha & hora
        Calendar c = stringToDate(obj.getText().toString());

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
                obj.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
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

    public void searchAndPaintResults( final View tab) {

        final TextView NumItems = tab.findViewById( R.id.textView_informes_encontrados_num);
        final TextView Total = tab.findViewById( R.id.textView_informes_total_num);
        final TextView FechaInicio = tab.findViewById( R.id.editText_informes_fecha_inicio);
        final TextView FechaFin = tab.findViewById( R.id.editText_informes_fecha_fin);
        final Spinner Categories = tab.findViewById( R.id.spinner_informes_categorias);
        final Switch switchFinal = tab.findViewById( R.id.switch_informres_inicio);
        boolean bFinal = switchFinal.isChecked();

        ArrayList<Gasto> gastosEncontrados = findGastos( (bFinal ? "" : FechaInicio.getText().toString()), FechaFin.getText().toString(), Categories.getSelectedItemPosition());

        long total = 0;
        if( !gastosEncontrados.isEmpty()) {
            for( Gasto g: gastosEncontrados) {
                total += g.getImporte();
            }

        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String strTotal = nf.format( (double)total / 100);
        Total.setText( strTotal);

        NumItems.setText( "" + gastosEncontrados.size());

        adapter = new AdapterGasto(getActivity(), gastosEncontrados);

        ListView lv = (ListView) tab.findViewById(R.id.listView_informes_gastos);

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public Calendar stringToDate( String fecha) {

        String fecha_formated = fecha.replace( '/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(fecha_formated);

            cal.setTime(date);
            return cal;
        } catch( Exception ex) {

        }
        return cal;
    }

    public long convertDate( String fecha) {
        return stringToDate( fecha).getTimeInMillis();
    }

    public ArrayList<Gasto> findGastos( String fechaInicio, String fechaFin, int categories) {

        long Inicio = 0;
        if( !fechaInicio.isEmpty())
            Inicio = convertDate( fechaInicio);
        long Fin = convertDate( fechaFin);
        ArrayList<Gasto> ArrayResult = new ArrayList<Gasto>();

        for( Gasto g : gastos) {
            if( g.getFecha() >= Inicio && g.getFecha() <= Fin ) {
                if( categories > 0) {
                    if (g.getCategoria() == categories - 1)
                        ArrayResult.add(g);
                } else {
                    //categories == 0 = todas.
                    ArrayResult.add( g);
                }
            }
        }

        return ArrayResult;
    }
}
