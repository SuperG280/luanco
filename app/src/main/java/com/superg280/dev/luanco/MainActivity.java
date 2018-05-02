package com.superg280.dev.luanco;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


    //Guarda el total de gastos actual. Para acelerar la ejecución. Se actualiza
    //en updateSaldoActual y se utiliza en refillTextViewSaldoUser para refrescar
    //el TextView.
    private long TotalGastos;

    private long SaldoUsuario1;
    private long SaldoUsuario2;
    private long SaldoUsuario3;

    private FirebaseAuth auth;

    public boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLoading = true;

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

        launchMainScreenListeners();

        auth = FirebaseAuth.getInstance();
        View header = navigationView.getHeaderView(0);

        TextView userMail = (TextView) header.findViewById( R.id.textView_nav_user_mail);
        if( auth != null) {
            userMail.setText(auth.getCurrentUser().getEmail());
        }


    }
    @Override
    public void onResume(){
        super.onResume();
        isLoading = true;
        new SomeTask().execute();
        refillFireBaseGastos();
        refillFireBaseIngresos();
    }

    private void refillMainWindow() {
        updateSaldoActual();
        SaldoUsuario1 = refillTextViewSaldoUser( USER_RAMON);
        SaldoUsuario2 = refillTextViewSaldoUser( USER_MARIA);
        SaldoUsuario3 = refillTextViewSaldoUser( USER_LUIS);
        prepareCardCuentas();
        prepareCardGrafico();
        isLoading = false;
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
                inte.putExtra("TAB_INDEX", 1);
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
                        formatImporte((double) (SaldoUsuario1 * -1) / (double) 100));
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
                inte.putExtra("TAB_INDEX", 1);
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
                        formatImporte((double) (SaldoUsuario2 * -1) / (double) 100));
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
                inte.putExtra("TAB_INDEX", 1);
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
                        formatImporte((double) (SaldoUsuario3 * -1) / (double) 100));
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

        View gastosVerTodos = ( View) findViewById( R.id.cardView_gastos);

        gastosVerTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar hoy = Calendar.getInstance();

                Intent inte = new Intent(MainActivity.this, LuTabActivity.class);
                inte.putExtra("TAB_INDEX", 0);
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
        //getMenuInflater().inflate(R.menu.main, menu);
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
        } else if (id == R.id.nav_cerrar_sesion) {

            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void prepareCardCuentas() {

        long totalGastosAnoActual = getTotalGastosAnoActual();

        TextView txTotalGastosAnoActual = ( TextView) findViewById( R.id.textView_card_gastos_esteano);
        txTotalGastosAnoActual.setText( formatImporte((double)totalGastosAnoActual / (double)100));

        long totalIngresosAnoActual = getTotalIngresosAnoActual();

        TextView txTotalIngresosAnoActual = ( TextView) findViewById( R.id.textView_card_ingresos_esteano);
        txTotalIngresosAnoActual.setText( formatImporte((double)totalIngresosAnoActual / (double)100));

    }

    public ArrayList<Entry> getEntriesMedia() {

        ArrayList<Entry> entriesMedia = new ArrayList<>();
        ArrayList<Float> medias = getMedias();
        for( int i = 0; i < 12; i++) {
            entriesMedia.add( new Entry( i, medias.get(i)));
        }

        return entriesMedia;
    }
    public void prepareCardGrafico() {

        LineChart lineChart = (LineChart) findViewById( R.id.chart);
        ArrayList<Entry> entriesCurrent = new ArrayList<>();

        //lineChart.setTouchEnabled( false);

        final String[] xValues = new String[] { "E", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};

        Calendar hoy = Calendar.getInstance();
        for( int i = 0; i < 12; i++) {

            entriesCurrent.add( new Entry( i, (float)getGastosMes( i, hoy.get( Calendar.YEAR))/100));

        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        LineDataSet dataSetCurrent = new LineDataSet( entriesCurrent, df.format(hoy));
        dataSetCurrent.setColor(getResources().getColor(R.color.colorSaldoPositivo));
        dataSetCurrent.setCircleColor(getResources().getColor(R.color.colorSaldoPositivo));


        LineDataSet dataSetMedia = new LineDataSet( getEntriesMedia(), getString(R.string.label_media_grafico));
        dataSetMedia.setColor(getResources().getColor(R.color.colorSaldoNegativoUsuario));
        dataSetMedia.setCircleColor(getResources().getColor(R.color.colorSaldoNegativoUsuario));

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetCurrent);
        dataSets.add(dataSetMedia);

        LineData lineData = new LineData( dataSets);
        lineChart.setData( lineData);
        Description description = new Description();
        description.setText( getResources().getString(R.string.leyenda_grafico));
        lineChart.setDescription( description);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValues[(int) value % xValues.length];
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        lineChart.animateXY(1600, 1600);
        lineChart.invalidate();
    }

    public String formatImporte( double importe) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return  nf.format(importe);
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

        txSaldoUsuario.setText( formatImporte((double)saldoUsuario / (double)100));

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

        txSaldoActual.setText( formatImporte((double)total / (double)100));
    }

    public void alDiaUser( int user) {

        long saldoUsuario = 0;

        if( user == USER_RAMON) {
            saldoUsuario = SaldoUsuario1;
        } else if( user == USER_MARIA) {
            saldoUsuario = SaldoUsuario2;
        } else if( user == USER_LUIS){
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
        mFirebaseDatabaseIngresos.child( ing.getId()).setValue(ing);

        //refillIngresos();
        /*
        updateSaldoActual();
        SaldoUsuario1 = refillTextViewSaldoUser( USER_RAMON);
        SaldoUsuario2 = refillTextViewSaldoUser( USER_MARIA);
        SaldoUsuario3 = refillTextViewSaldoUser( USER_LUIS);
        prepareIngresosListView();
        */
    }

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
                return new Long(t1.getFecha()).compareTo(Long.valueOf(gasto.getFecha()));
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

    public ArrayList<Float> getMedias() {

        ArrayList<Integer> years = differentYears();
        Map< Integer, ArrayList<Float>> medias = new HashMap< Integer, ArrayList<Float>>();

        for( Integer y: years) {
            ArrayList<Float> mediasOneYear = new ArrayList<>();
            for( int i = 0; i < 12; i++) {
                mediasOneYear.add( getMediaDeMesAno( i, y));
            }
            medias.put( y, mediasOneYear);
        }

        ArrayList< Float> result = new ArrayList<>();


        for( int i = 0; i < 12; i++) {
            float suma = 0;
            for (Map.Entry<Integer, ArrayList<Float>> entry : medias.entrySet()) {
                suma += entry.getValue().get(i);
            }
            result.add( suma / (float)medias.size());
        }

        return result;
    }

    public ArrayList<Integer> differentYears() {

        ArrayList<Integer> years = new ArrayList<>();

        for( Gasto g: gastos) {
            int year = g.fechaToCalendar().get( Calendar.YEAR);
            if( !years.contains( year)) {
                years.add( year);
            }
        }
        return years;

    }

    /**
     * Calcula la media de gastos de un mes en un año concreto.
     * @param month Indice de la clase Calendar del mes (Enero: 0).
     * @param year Numero de año de la clase Calendar.
     * @return la media aritmetica de los gastos de ese mes.
     */
    public float getMediaDeMesAno( int month, int year) {

        int numero = 0;
        long suma = 0;

        for( Gasto g: gastos) {
            Calendar fechaGasto = g.fechaToCalendar();
            if( fechaGasto.get( Calendar.YEAR) < year)
                break;
            if (fechaGasto.get(Calendar.YEAR) == year) {
                if (g.fechaToCalendar().get(Calendar.MONTH) == month) {
                    numero++;
                    suma += g.getImporte();
                }
            }
        }

        if( numero == 0 || suma == 0) {
            return 0;
        }

        return ( (float)suma / (float)100) / (float)numero;
    }

    public long getGastosMes( int month, int year) {

       long importe = 0;

        for( Gasto g: gastos) {

            Calendar fechaGasto = g.fechaToCalendar();

            if( fechaGasto.get( Calendar.YEAR) < year) {
                break;
            }
            if( fechaGasto.get( Calendar.YEAR) == year) {

                if( fechaGasto.get( Calendar.MONTH) < month) {
                    break;
                }

                if( month == fechaGasto.get( Calendar.MONTH)) {
                    importe += g.getImporte();
                }
            }
        }

        return importe;
    }

    public long getTotalGastosAnoActual() {

        if( gastos == null || gastos.size() == 0) {
            return 0;
        }

        ArrayList<Long> gastosAActual = getGastosAnoActual();

        long importe = 0;
        for( Long g: gastosAActual) {
            importe += g;
        }
        return importe;
    }

    public ArrayList<Long> getGastosAnoActual() {

        ArrayList<Long> gastosA = new ArrayList<Long>();

        Calendar hoy = Calendar.getInstance();

        for( Gasto g: gastos) {
            Calendar fechaGasto = g.fechaToCalendar();
            if( fechaGasto.get( Calendar.YEAR) < hoy.get( Calendar.YEAR)) {

                break;
            }

            if( hoy.get( Calendar.YEAR) == fechaGasto.get( Calendar.YEAR)) {
                gastosA.add( g.getImporte());
            }
        }

        return gastosA;
    }

    public long getTotalIngresosAnoActual() {

        if( ingresos == null || ingresos.size() == 0) {
            return 0;
        }

        ArrayList<Long> ingresosAActual = getIngresosAnoActual();

        long importe = 0;
        for( Long i: ingresosAActual) {
            importe += i;
        }
        return importe;
    }

    public ArrayList<Long> getIngresosAnoActual() {

        ArrayList<Long> ingresosA = new ArrayList<Long>();

        Calendar hoy = Calendar.getInstance();

        for( Ingreso i: ingresos) {
            Calendar fechaIngreso = i.fechaToCalendar();
            if( fechaIngreso.get( Calendar.YEAR) < hoy.get( Calendar.YEAR)) {

                break;
            }

            if( hoy.get( Calendar.YEAR) == fechaIngreso.get( Calendar.YEAR)) {
                ingresosA.add( i.getImporte());
            }
        }

        return ingresosA;
    }

    /** Inner class for implementing progress bar before fetching data **/
    private class SomeTask extends AsyncTask<Void, Void, Integer>
    {
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute()
        {
            Dialog.setMessage(getString( R.string.toas_loading_main));
            Dialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params)
        {
            //Task for doing something
            try {
                while ( isLoading) {
                    Thread.sleep(100);
                }
            } catch( Exception ex) {}
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result)
        {

            if(result==0)
            {
                //do some thing
            }
            // after completed finished the progressbar
            Dialog.dismiss();
        }
    }
}
