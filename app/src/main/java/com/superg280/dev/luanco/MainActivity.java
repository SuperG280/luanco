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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdapterGastoMain gastoAdapter;
    private AdapterIngresoMain ingresoAdapter;

    public final static int USER_RAMON = 1;
    public final static int USER_MARIA = 2;
    public final static int USER_LUIS = 3;

    private ArrayList<Gasto> gastos = null;
    private ArrayList<Ingreso> ingresos = null;

    private DatabaseReference mFirebaseDatabaseGastos;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabaseIngresos;
    private ValueEventListener mFireBaseGastosEventListener;
    private ValueEventListener mFireBaseIngresosEventListener;

    //Objeto de la base de datos.
    //public LuancoDBHelper LuancoDB;

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

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabaseGastos = mFirebaseInstance.getReference("gastos");
        mFirebaseDatabaseIngresos = mFirebaseInstance.getReference("ingresos");

        gastos = new ArrayList<Gasto>();
        ingresos = new ArrayList<Ingreso>();


        //LuancoDB = new LuancoDBHelper( this);
        //refillGastos();
        //refillIngresos();

        launchMainScreenListeners();
    }
    @Override
    public void onResume(){
        super.onResume();
        refillFireBaseGastos();
        refillFireBaseIngresos();
        //refillMainWindow();
    }

    private void refillMainWindow() {
        updateSaldoActual();
        SaldoUsuario1 = refillTextViewSaldoUser( USER_RAMON);
        SaldoUsuario2 = refillTextViewSaldoUser( USER_MARIA);
        SaldoUsuario3 = refillTextViewSaldoUser( USER_LUIS);
        prepareGastosListView();
        prepareIngresosListView();
    }

    @Override
    protected void onPause() {

        mFirebaseDatabaseGastos.removeEventListener( mFireBaseGastosEventListener);
        mFirebaseDatabaseIngresos.removeEventListener( mFireBaseIngresosEventListener);
        super.onPause();
    }

    public void launchMainScreenListeners() {
        ImageView user1 = (ImageView) findViewById(R.id.imageView_user1);

        user1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 2);
                inte.putExtra("GASTOS", gastos);
                inte.putExtra("INGRESOS", ingresos);

                startActivity(inte);
            }
        });

        user1.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                if (SaldoUsuario1 > 0)
                    return false;

                AlertDialog.Builder dlgAlDia = new AlertDialog.Builder(MainActivity.this);
                dlgAlDia.setTitle(getString(R.string.dlg_aldia_title));
                dlgAlDia.setMessage(getString(R.string.dlg_aldia_mensaje1) + " " +
                        getString(R.string.app_name_user1) + " " +
                        getString(R.string.dlg_aldia_mensaje2) + " " +
                        String.format("%.2f€", (double) ((double) (SaldoUsuario1 * -1) / (double) 100)));
                dlgAlDia.setCancelable(false);
                dlgAlDia.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        alDiaUser(USER_RAMON);
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

        ImageView user2 = (ImageView) findViewById(R.id.imageView_user2);

        user2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 2);
                inte.putExtra("GASTOS", gastos);
                inte.putExtra("INGRESOS", ingresos);

                startActivity(inte);
            }
        });

        user2.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                if (SaldoUsuario2 > 0)
                    return false;

                AlertDialog.Builder dlgAlDia = new AlertDialog.Builder(MainActivity.this);
                dlgAlDia.setTitle(getString(R.string.dlg_aldia_title));
                dlgAlDia.setMessage(getString(R.string.dlg_aldia_mensaje1) + " " +
                        getString(R.string.app_name_user2) + " " +
                        getString(R.string.dlg_aldia_mensaje2) + " " +
                        String.format("%.2f€", (double) ((double) (SaldoUsuario2 * -1) / (double) 100)));
                dlgAlDia.setCancelable(false);
                dlgAlDia.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        alDiaUser(USER_MARIA);
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

        ImageView user3 = (ImageView) findViewById(R.id.imageView_user3);

        user3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 2);
                inte.putExtra("GASTOS", gastos);
                inte.putExtra("INGRESOS", ingresos);

                startActivity(inte);
            }
        });

        user3.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                if (SaldoUsuario3 > 0)
                    return false;

                AlertDialog.Builder dlgAlDia = new AlertDialog.Builder(MainActivity.this);
                dlgAlDia.setTitle(getString(R.string.dlg_aldia_title));
                dlgAlDia.setMessage(getString(R.string.dlg_aldia_mensaje1) + " " +
                        getString(R.string.app_name_user3) + " " +
                        getString(R.string.dlg_aldia_mensaje2) + " " +
                        String.format("%.2f€", (double) ((double) (SaldoUsuario3 * -1) / (double) 100)));
                dlgAlDia.setCancelable(false);
                dlgAlDia.setPositiveButton(getString(R.string.dlg_delete_but_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        alDiaUser(USER_LUIS);
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

    public void prepareGastosListView() {

        if( gastos.size() < 0)
            return;

        ArrayList<Gasto> mainGastos = new ArrayList<Gasto>();
        Gasto realGasto;
        for( int i = 0; i < 8 && i < gastos.size(); i++) {
            realGasto = gastos.get( i);
            Gasto newGasto = new Gasto();
            newGasto.setFecha      ( realGasto.getFecha());
            newGasto.setDescripcion( realGasto.getDescripcion());
            newGasto.setImporte    ( realGasto.getImporte());

            mainGastos.add( newGasto);
        }

        gastoAdapter = new AdapterGastoMain( this, mainGastos);

        ListView lv = (ListView) findViewById(R.id.listView_main_gastos);

        lv.setAdapter(gastoAdapter);
    }

    public void prepareIngresosListView() {

        if( ingresos.size() < 0)
            return;

        ArrayList<Ingreso> mainIngresos = new ArrayList<Ingreso>();
        Ingreso realIngreso;

        for( int i = 0; i < 8 && i < ingresos.size(); i++) {
            realIngreso = ingresos.get( i);
            Ingreso newIngreso = new Ingreso();
            newIngreso.setFecha      ( realIngreso.getFecha());
            newIngreso.setDescripcion( realIngreso.getDescripcion());
            newIngreso.setImporte    ( realIngreso.getImporte());

            mainIngresos.add( newIngreso);
        }

        ingresoAdapter = new AdapterIngresoMain( this, mainIngresos);

        ListView lv = (ListView) findViewById(R.id.listView_main_ingresos);

        lv.setAdapter(ingresoAdapter);
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
                ingresosDeUsario += i.getImporte();
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
            TotalGastos += g.getImporte();
        }

        for( Ingreso i: ingresos) {
            totalIngresos += i.getImporte();
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
        ing.createFechaToday();
        ing.setDescripcion( getString(R.string.app_new_aldia_ingreso_descripcion));

        //LuancoDB.insertNewIngreso( ing);

        //refillIngresos();
        updateSaldoActual();
        SaldoUsuario1 = refillTextViewSaldoUser( USER_RAMON);
        SaldoUsuario2 = refillTextViewSaldoUser( USER_MARIA);
        SaldoUsuario3 = refillTextViewSaldoUser( USER_LUIS);
        prepareIngresosListView();
    }
    /*
    public void refillGastos() {

        gastos = LuancoDB.getAllGastos();
    }
    */
    public void refillFireBaseGastos() {

        mFireBaseGastosEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gastos = new ArrayList<Gasto>();
                Gasto g;
                for( DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    g = postSnapshot.getValue( Gasto.class);
                    gastos.add( g);
                }
                sortGastos();
                refillMainWindow();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mFirebaseDatabaseGastos.addValueEventListener( mFireBaseGastosEventListener);
    }

    public void sortGastos() {

        Collections.sort(gastos, new Comparator<Gasto>() {
            @Override
            public int compare(Gasto gasto, Gasto t1) {
                return new Long(t1.getFecha()).compareTo(new Long(gasto.getFecha()));
            }
        });
    }

    public void refillFireBaseIngresos() {

        mFireBaseIngresosEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingresos = new ArrayList<Ingreso>();
                Ingreso ing;
                for( DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ing = postSnapshot.getValue( Ingreso.class);
                    ingresos.add( ing);
                }
                sortIngresos();
                refillMainWindow();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mFirebaseDatabaseIngresos.addValueEventListener( mFireBaseIngresosEventListener );
    }

    public void sortIngresos() {

        Collections.sort(ingresos, new Comparator<Ingreso>() {
            @Override
            public int compare(Ingreso ingreso, Ingreso t1) {
                return new Long(t1.getFecha()).compareTo(new Long(ingreso.getFecha()));
            }
        });
    }
    /*
    public void refillIngresos() {

        ingresos = LuancoDB.getAllIngresos();
    }
    */
}
