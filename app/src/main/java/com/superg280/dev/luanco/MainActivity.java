package com.superg280.dev.luanco;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static int USER_RAMON = 1;
    public final static int USER_MARIA = 2;
    public final static int USER_LUIS = 3;

    private ArrayList<Gasto> gastos = null;
    private ArrayList<Ingreso> ingresos = null;

    //Objeto de la base de datos.
    public LuancoDBHelper LuancoDB;

    //Guarda el total de gastos actual. Para acelerar la ejecución. Se actualiza
    //en updateSaldoActual y se utiliza en refillTextViewSaldoUser para refrescar
    //el TextView.
    private long TotalGastos;

    private long SaldoUsuario1;
    private long SaldoUsuario2;
    private long SaldoUsuario3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setImageRounded(USER_RAMON);
        setImageRounded(USER_MARIA);
        setImageRounded(USER_LUIS);

        LuancoDB = new LuancoDBHelper( this);
        refillGastos();
        //refillTextViewMainGastos();
        refillIngresos();
        launchMainScreenListeners();
    }
    @Override
    public void onResume(){
        super.onResume();
        refillGastos();
        refillIngresos();
        updateSaldoActual();
        SaldoUsuario1 = refillTextViewSaldoUser( USER_RAMON);
        SaldoUsuario2 = refillTextViewSaldoUser( USER_MARIA);
        SaldoUsuario3 = refillTextViewSaldoUser( USER_LUIS);
        refillTextViewMainGastos();
        refillTextViewMainIngresos();
    }

    public void launchMainScreenListeners() {
        ImageView user1 = (ImageView) findViewById( R.id.imageView_user1);

        user1.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent inte = new Intent( MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 2);
                inte.putExtra( "GASTOS", gastos);
                inte.putExtra( "INGRESOS", ingresos);

                startActivity(inte);
            }
        });

        user1.setOnLongClickListener( new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                if( SaldoUsuario1 > 0)
                    return false;

                AlertDialog.Builder dlgAlDia = new AlertDialog.Builder( MainActivity.this);
                dlgAlDia.setTitle( getString(R.string.dlg_aldia_title));
                dlgAlDia.setMessage( getString( R.string.dlg_aldia_mensaje1) + " " +
                                     getString( R.string.app_name_user1)     + " " +
                                     getString( R.string.dlg_aldia_mensaje2) + " " +
                                     String.format("%.2f€", (double) ((double)(SaldoUsuario1 * -1) / (double)100)));
                dlgAlDia.setCancelable(false);
                dlgAlDia.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        alDiaUser( USER_RAMON);
                    }
                });

                dlgAlDia.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgAlDia.show();
                return true;
            }
        });

        ImageView user2 = (ImageView) findViewById( R.id.imageView_user2);

        user2.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent inte = new Intent( MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 2);
                inte.putExtra( "GASTOS", gastos);
                inte.putExtra( "INGRESOS", ingresos);

                startActivity(inte);
            }
        });

        user2.setOnLongClickListener( new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                if( SaldoUsuario2 > 0)
                    return false;

                AlertDialog.Builder dlgAlDia = new AlertDialog.Builder( MainActivity.this);
                dlgAlDia.setTitle( getString(R.string.dlg_aldia_title));
                dlgAlDia.setMessage( getString( R.string.dlg_aldia_mensaje1) + " " +
                        getString( R.string.app_name_user2)     + " " +
                        getString( R.string.dlg_aldia_mensaje2) + " " +
                        String.format("%.2f€", (double) ((double)(SaldoUsuario2 * -1) / (double)100)));
                dlgAlDia.setCancelable(false);
                dlgAlDia.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        alDiaUser( USER_MARIA);
                    }
                });

                dlgAlDia.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgAlDia.show();
                return true;
            }
        });

        ImageView user3 = (ImageView) findViewById( R.id.imageView_user3);

        user3.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent inte = new Intent( MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 2);
                inte.putExtra( "GASTOS", gastos);
                inte.putExtra( "INGRESOS", ingresos);

                startActivity(inte);
            }
        });

        user3.setOnLongClickListener( new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                if( SaldoUsuario3 > 0)
                    return false;

                AlertDialog.Builder dlgAlDia = new AlertDialog.Builder( MainActivity.this);
                dlgAlDia.setTitle( getString(R.string.dlg_aldia_title));
                dlgAlDia.setMessage( getString( R.string.dlg_aldia_mensaje1) + " " +
                        getString( R.string.app_name_user3)     + " " +
                        getString( R.string.dlg_aldia_mensaje2) + " " +
                        String.format("%.2f€", (double) ((double)(SaldoUsuario3 * -1) / (double)100)));
                dlgAlDia.setCancelable(false);
                dlgAlDia.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        alDiaUser( USER_LUIS);
                    }
                });

                dlgAlDia.setNegativeButton(getString(R.string.dlg_delete_but_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dlgAlDia.show();
                return true;
            }
        });

        TextView gastosView = (TextView) findViewById( R.id.textView_gastos);
        gastosView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent inte = new Intent(MainActivity.this, LuTabActivity.class);
               inte.putExtra("TAB_INDEX", 0);
               inte.putExtra("GASTOS", gastos);
               inte.putExtra( "INGRESOS", ingresos);

               startActivity(inte);
            }
        });

        TextView ingresosView = (TextView) findViewById( R.id.textView_ingresos);
        ingresosView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 1);
                inte.putExtra( "GASTOS", gastos);
                inte.putExtra( "INGRESOS", ingresos);

                startActivity(inte);
            }
        });
    }

    public void setImageRounded( int user) {

        //extraemos el drawable en un bitmap
        Drawable originalDrawable;
        if (user == USER_RAMON) {
            originalDrawable = getResources().getDrawable(R.drawable.yo);
        } else if( user == USER_MARIA) {
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

        if( user == USER_RAMON) {
            imageView = (ImageView) findViewById(R.id.imageView_user1);
        } else if( user == USER_MARIA) {
            imageView = (ImageView) findViewById(R.id.imageView_user2);
        } else {
            imageView = (ImageView) findViewById(R.id.imageView_user3);
        }

        imageView.setImageDrawable(roundedDrawable);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gastos) {
            Intent inte = new Intent(this, LuTabActivity.class);
            inte.putExtra("TAB_INDEX", 0);
            inte.putExtra( "GASTOS", gastos);
            inte.putExtra( "INGRESOS", ingresos);
            startActivity(inte);

        } else if (id == R.id.nav_ingresos) {
            Intent inte = new Intent(this, LuTabActivity.class);
            inte.putExtra("TAB_INDEX", 1);
            inte.putExtra( "GASTOS", gastos);
            inte.putExtra( "INGRESOS", ingresos);
            startActivity(inte);


        } else if (id == R.id.nav_usuarios) {
            Intent inte = new Intent(this, LuTabActivity.class);
            inte.putExtra("TAB_INDEX", 2);
            inte.putExtra( "GASTOS", gastos);
            inte.putExtra( "INGRESOS", ingresos);
            startActivity(inte);
        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void refillTextViewMainIngresos() {

        TextView txIngresosMain = ( TextView)findViewById(R.id.textView_ingresos);

        StringBuffer contenido = new StringBuffer();
        contenido.append( "Ingresos\n");

        for( int i = 0; i < 8; i++) {
            if( i < ingresos.size() ) {
                contenido.append( ingresos.get(i).getFecha() + " " + ingresos.get(i).getDescripcion() + " " + ingresos.get(i).getImporte() + "\n");
            }
        }
        txIngresosMain.setText( contenido);

    }

    public void refillTextViewMainGastos() {

        TextView txGastosMain = ( TextView)findViewById(R.id.textView_gastos);

        StringBuffer contenido = new StringBuffer();
        contenido.append( "Gastos\n");

        for( int i = 0; i < 8; i++) {
            if( i < gastos.size() ) {
                contenido.append( gastos.get(i).getFecha() + " " + gastos.get(i).getDescripcion() + " " + gastos.get(i).getImporte() + "\n");
            }
        }
        txGastosMain.setText( contenido);
    }

    //Hay que llamarla después de haber llamado a updateSaldoActual, para
    //que utilice la variable de la clase TotalGastos que ya estará actualizada.
    public long refillTextViewSaldoUser( int user) {

        long ingresosDeUsario = 0;

        TextView txSaldoUsuario;

        if( user == USER_RAMON) {
            txSaldoUsuario = ( TextView) findViewById( R.id.textView_current_user1);
        } else if( user == USER_MARIA) {
            txSaldoUsuario = ( TextView) findViewById( R.id.textView_current_user2);
        } else if( user == USER_LUIS){
            txSaldoUsuario = ( TextView) findViewById( R.id.textView_current_user3);
        } else {
            return 0;
        }

        for( Ingreso i: ingresos) {
            if( i.getUserID() == user) {
                ingresosDeUsario += i.getImporteLong();
            }
        }

        long saldoUsuario = ingresosDeUsario - (TotalGastos / 3);

        if( saldoUsuario < 0) {
            txSaldoUsuario.setTextColor( ContextCompat.getColor( this, R.color.colorSaldoNegativoUsuario));
        } else {
            txSaldoUsuario.setTextColor( ContextCompat.getColor( this, R.color.colorSaldoNeutroUsuario));
        }

        txSaldoUsuario.setText( String.format("%.2f€", (double) ((double)saldoUsuario / (double)100)));

        return saldoUsuario;
    }

    public void updateSaldoActual() {

        TextView txSaldoActual = (TextView) findViewById( R.id.textView_current_amount);

        long totalIngresos = 0;
        long total = 0;

        TotalGastos = 0;

        for( Gasto g: gastos) {
            TotalGastos += g.getImporteLong();
        }

        for( Ingreso i: ingresos) {
            totalIngresos += i.getImporteLong();
        }
        total = totalIngresos - TotalGastos;

        if( total < 0) {
            txSaldoActual.setTextColor( ContextCompat.getColor( this, R.color.colorSaldoNegativo));
        } else {
            txSaldoActual.setTextColor( ContextCompat.getColor( this, R.color.colorSaldoNeutro));
        }

        txSaldoActual.setText( String.format("%.2f€", (double) ((double)total / (double)100)));
    }

    public void alDiaUser( int user) {

        TextView txSaldoUsuario;
        long saldoUsuario = 0;

        if( user == USER_RAMON) {
            txSaldoUsuario = ( TextView) findViewById( R.id.textView_current_user1);
            saldoUsuario = SaldoUsuario1;
        } else if( user == USER_MARIA) {
            txSaldoUsuario = ( TextView) findViewById( R.id.textView_current_user2);
            saldoUsuario = SaldoUsuario2;
        } else if( user == USER_LUIS){
            txSaldoUsuario = ( TextView) findViewById( R.id.textView_current_user3);
            saldoUsuario = SaldoUsuario3;
        } else {
            return;
        }

        Ingreso ing = new Ingreso();
        ing.setUserID( user);
        ing.setImporte( saldoUsuario * -1);
        ing.setFechaToday();
        ing.setDescripcion( getString(R.string.app_new_aldia_ingreso_descripcion));

        LuancoDB.insertNewIngreso( ing);

        refillIngresos();
        updateSaldoActual();
        SaldoUsuario1 = refillTextViewSaldoUser( USER_RAMON);
        SaldoUsuario2 = refillTextViewSaldoUser( USER_MARIA);
        SaldoUsuario3 = refillTextViewSaldoUser( USER_LUIS);
        refillTextViewMainIngresos();
    }
    public void refillGastos() {

        gastos = LuancoDB.getAllGastos();

        /*
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
        */
    }

    public void refillIngresos() {

        ingresos = LuancoDB.getAllIngresos();
        /*
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
        */
    }
}
