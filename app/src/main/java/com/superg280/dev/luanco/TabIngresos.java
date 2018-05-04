package com.superg280.dev.luanco;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;


public class TabIngresos extends Fragment {

    private ArrayList<Ingreso> ingresos = null;
    private AdapterIngreso adapter = null;
    public EditText editTextFecha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab = inflater.inflate(R.layout.fragment_tab_ingresos, container, false);

        ingresos = ((LuTabActivity)this.getActivity()).ingresos;
        adapter = new AdapterIngreso( getActivity(), ingresos);

        if( ingresos == null || ingresos.size() == 0) {
            Toast.makeText( getActivity(), getResources().getString(R.string.toast_no_ingresos), Toast.LENGTH_LONG).show();
        }

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
                        deleteIngreso( posicion);
                    }
                });

                dlgBorrar.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgBorrar.show();

                return true;

                /*Snackbar.make(view, "Borrado de ingreso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;*/
            }
        });

        FloatingActionButton fab = (FloatingActionButton) tab.findViewById(R.id.fab_new_ingresos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dlg = createNewIngresoDialogo();
                dlg.show();
            }
        });

        // Inflate the layout for this fragment
        return tab;
    }

    public void setImageRounded( View v, int user) {

        //extraemos el drawable en un bitmap
        Drawable originalDrawable;
        if (user == MainActivity.USER_RAMON) {
            originalDrawable = getResources().getDrawable(R.drawable.yo);
        } else if( user == MainActivity.USER_MARIA) {
            originalDrawable = getResources().getDrawable(R.drawable.maria_perfil);
        } else {
            originalDrawable = getResources().getDrawable(R.drawable.luis_perfil);
        }

        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCircular(true);

        ImageView imageView;

        if( user == MainActivity.USER_RAMON) {
            imageView = (ImageView) v.findViewById(R.id.imageView_new_ingreso_user1);
        } else if( user == MainActivity.USER_MARIA) {
            imageView = (ImageView) v.findViewById(R.id.imageView_new_ingreso_user2);
        } else {
            imageView = (ImageView) v.findViewById(R.id.imageView_new_ingreso_user3);
        }

        imageView.setImageDrawable(roundedDrawable);

    }

    public AlertDialog createNewIngresoDialogo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.new_ingreso, null);

        builder.setView(v);

        setImageRounded( v, MainActivity.USER_RAMON);
        setImageRounded( v, MainActivity.USER_MARIA);
        setImageRounded( v, MainActivity.USER_LUIS);

        final EditText editTextImporte     = (EditText)v.findViewById( R.id.editText_new_ingreso_importe);
        final EditText editTextDescripcion = (EditText)v.findViewById( R.id.editText_new_ingreso_descripcion);

        editTextFecha = (EditText)v.findViewById(R.id.editText_new_ingreso_fecha);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        editTextFecha.setText( df.format(cal.getTime()));

        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        final RadioButton radioUser1 = v.findViewById( R.id.radioButton_new_ingreso_user1);
        final RadioButton radioUser2 = v.findViewById( R.id.radioButton_new_ingreso_user2);
        final RadioButton radioUser3 = v.findViewById( R.id.radioButton_new_ingreso_user3);

        radioUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioUser2.setChecked( false);
                radioUser3.setChecked( false);
            }
        });

        radioUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioUser1.setChecked( false);
                radioUser3.setChecked( false);
            }
        });

        radioUser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioUser1.setChecked( false);
                radioUser2.setChecked( false);
            }
        });

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String importe      = editTextImporte.getText().toString();
                        String fecha        = editTextFecha.getText().toString();
                        String descripcion  = editTextDescripcion.getText().toString();
                        int userID = 0;

                        if( radioUser1.isChecked()) {
                            userID = MainActivity.USER_RAMON;
                        } else if( radioUser2.isChecked()) {
                            userID = MainActivity.USER_MARIA;
                        } else {
                            userID = MainActivity.USER_LUIS;
                        }

                        addNewIngreso( fecha, importe, descripcion, userID);
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

    public boolean addNewIngreso( String fecha, String importe, String descripcion, int user) {

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

        int num = cal.get( Calendar.MONTH);
        long lImporte;
        try {
            lImporte = (long)(new Double( importe).doubleValue() * 100);
        } catch( Exception ex) {
            return false;
        }

        Ingreso newIngreso = new Ingreso( cal.getTimeInMillis(), descripcion, lImporte, user);

        String resultado = newIngreso.toString();

        addIngresoInFireBase( newIngreso);

        insertNewIngresoInArray( newIngreso);
        adapter.notifyDataSetChanged();
        return true;
    }

    public void insertNewIngresoInArray( Ingreso ingreso) {

        if( ingresos.size() == 0) {
            ingresos.add( ingreso);
            return;
        }

        long NewFecha = ingreso.getFecha();
        for( int i = 0; i < ingresos.size(); i++) {

            Ingreso ing = ingresos.get( i);
            if( ing.getFecha() <= NewFecha) {
                ingresos.add( i, ingreso);
                return;
            }
        }
        //Si ha llegado a salir del bucle es que no ha encontrado
        //una fecha menor y esta es la menor, así que lo mete el último.
        ingresos.add( ingreso);
    }

    public void deleteIngreso( int posicion) {

        deleteIngresoInFireBase( ingresos.get( posicion).getId());
        ingresos.remove(posicion);
        adapter.notifyDataSetChanged();
    }

    public void addIngresoInFireBase( Ingreso ingreso) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ingresos");

        mDatabase.child( ingreso.getId()).setValue(ingreso);
    }

    public void deleteIngresoInFireBase( String ingresoId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ingresos");

        mDatabase.child( ingresoId).removeValue();
    }
}
