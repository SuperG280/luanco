package com.superg280.dev.luanco;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Super on 29/03/2018.
 */

public class LuancoDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Luanco.db";

    public LuancoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL( LuancoContract.SQL_CREATE_TABLA_GASTOS);
        sqLiteDatabase.execSQL( LuancoContract.SQL_CREATE_TABLA_INGRESOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertNewGasto( Gasto gasto) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                LuancoContract.GastoEntry.TABLE_NAME,
                null,
                gasto.toContentValues());
    }

    public long insertNewIngreso( Ingreso ingreso) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                LuancoContract.IngresoEntry.TABLE_NAME,
                null,
                ingreso.toContentValues());
    }

    public ArrayList<Gasto> getAllGastos() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cur = sqLiteDatabase.query(LuancoContract.GastoEntry.TABLE_NAME,null, null, null, null, null, LuancoContract.GastoEntry.FECHA + " DESC");

        ArrayList<Gasto> gastos = new ArrayList<Gasto>();

        while( cur.moveToNext()) {
            String id           = cur.getString( cur.getColumnIndex( LuancoContract.GastoEntry.ID));
            String descripcion  = cur.getString( cur.getColumnIndex( LuancoContract.GastoEntry.DESCRIPCION));
            long fecha          = cur.getLong( cur.getColumnIndex( LuancoContract.GastoEntry.FECHA));
            long importe        = cur.getLong( cur.getColumnIndex( LuancoContract.GastoEntry.IMPORTE));
            gastos.add( new Gasto( id, fecha, descripcion, importe));
        }
        return gastos;
    }

    public ArrayList<Ingreso> getAllIngresos() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cur = sqLiteDatabase.query(LuancoContract.IngresoEntry.TABLE_NAME,null, null, null, null, null, LuancoContract.IngresoEntry.FECHA + " DESC");

        ArrayList<Ingreso> ingresos = new ArrayList<Ingreso>();

        while( cur.moveToNext()) {
            String id           = cur.getString( cur.getColumnIndex( LuancoContract.IngresoEntry.ID));
            String descripcion  = cur.getString( cur.getColumnIndex( LuancoContract.IngresoEntry.DESCRIPCION));
            long fecha          = cur.getLong( cur.getColumnIndex( LuancoContract.IngresoEntry.FECHA));
            long importe        = cur.getLong( cur.getColumnIndex( LuancoContract.IngresoEntry.IMPORTE));
            long userID          = cur.getLong( cur.getColumnIndex( LuancoContract.IngresoEntry.USUARIO));

            ingresos.add( new Ingreso( id, fecha, descripcion, importe, (int)userID));
        }
        return ingresos;
    }

    public void deleteGasto( String id) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int result = sqLiteDatabase.delete( LuancoContract.GastoEntry.TABLE_NAME, LuancoContract.GastoEntry.ID + "=?", new String[]{id});
    }

    public void deleteIngreso( String id) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int result = sqLiteDatabase.delete( LuancoContract.IngresoEntry.TABLE_NAME, LuancoContract.IngresoEntry.ID + "=?", new String[]{id});
    }
}
