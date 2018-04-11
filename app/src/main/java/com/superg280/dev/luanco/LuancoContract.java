package com.superg280.dev.luanco;

import android.provider.BaseColumns;

/**
 * Created by Super on 29/03/2018.
 */

public final class LuancoContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_NOT_NULL_TYPE = " TEXT NOT NULL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    //Sentencia SQL para crear la tabla de gastos
    public static final String SQL_CREATE_TABLA_GASTOS =
            "CREATE TABLE " + GastoEntry.TABLE_NAME + " (" +
                    GastoEntry._ID + " INTEGER PRIMARY KEY," +
                    GastoEntry.ID + TEXT_NOT_NULL_TYPE + COMMA_SEP +
                    GastoEntry.FECHA + INTEGER_TYPE + COMMA_SEP +
                    GastoEntry.DESCRIPCION + TEXT_NOT_NULL_TYPE + COMMA_SEP +
                    GastoEntry.IMPORTE + INTEGER_TYPE +
                    " )";

    //Sentencia SQL para crear la tabla de Ingresos.
    public static final String SQL_CREATE_TABLA_INGRESOS =
            "CREATE TABLE " + IngresoEntry.TABLE_NAME + " (" +
                    IngresoEntry._ID + " INTEGER PRIMARY KEY," +
                    IngresoEntry.ID + TEXT_NOT_NULL_TYPE + COMMA_SEP +
                    IngresoEntry.FECHA + INTEGER_TYPE + COMMA_SEP +
                    IngresoEntry.DESCRIPCION + TEXT_NOT_NULL_TYPE + COMMA_SEP +
                    IngresoEntry.IMPORTE + INTEGER_TYPE + COMMA_SEP +
                    IngresoEntry.USUARIO + INTEGER_TYPE +
                    " )";

    //Constructor privado para evitar que se creen objetos de esta clase.
    private LuancoContract() {}

    public static abstract class GastoEntry implements BaseColumns {

        //Nombre de la tabla de gastos.
        public static final String TABLE_NAME = "gastos";

        //Nombres de los campos de la tabla.
        public static final String ID               = "id";
        public static final String FECHA            = "fecha";
        public static final String DESCRIPCION      = "descripcion";
        public static final String IMPORTE          = "importe";
    }

    public static abstract class IngresoEntry implements BaseColumns {

        //Nombre de la tabla de ingresos.
        public static final String TABLE_NAME = "ingresos";

        //Nombres de los campos de la tabla.
        public static final String ID               = "id";
        public static final String FECHA            = "fecha";
        public static final String DESCRIPCION      = "descripcion";
        public static final String IMPORTE          = "importe";
        public static final String USUARIO          = "usuario";
    }
}
