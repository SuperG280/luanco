package com.superg280.dev.luanco;

public class Categories {


    public final static int CAT_AGUA = 0;
    public final static int CAT_LUZ = 1;
    public final static int CAT_BANCO = 2;
    public final static int CAT_IMPUESTOS = 3;
    public final static int CAT_COMUNIDAD = 4;
    public final static int CAT_CASA = 5;
    public final static int CAT_ELECTRODOMESTICOS = 6;
    public final static int CAT_REPARACIONES = 7;
    public final static int CAT_SEGUROS = 8;
    public final static int CAT_OTRO = 9;


    private static final int cat_icons[] = { R.drawable.cat_agua,
                                            R.drawable.cat_luz,
                                            R.drawable.cat_banco,
                                            R.drawable.cat_impuesto,
                                            R.drawable.cat_comunidad,
                                            R.drawable.cat_casa,
                                            R.drawable.cat_electrodomesticos,
                                            R.drawable.cat_reparacion,
                                            R.drawable.cat_seguros,
                                            R.drawable.cat_otro};

    private static final String cat_literales[] = {  "Agua, basuras,...",
                                                    "Luz",
                                                    "Gasto banco",
                                                    "Impuestos",
                                                    "Comunidad de vecinos",
                                                    "Gasto de casa",
                                                    "Electrodomesticos",
                                                    "Reparaciones",
                                                    "Seguros",
                                                    "Otra categoria"};



    public static int[] getCat_icons() {
        return cat_icons;
    }

    public static String[] getCat_literales() {
        return cat_literales;
    }

    public static int getCategoryIcon( int categoryID) {

        if( categoryID >= cat_icons.length || categoryID < 0) {
            return cat_icons[ CAT_OTRO];
        }

        return cat_icons[ categoryID];
    }

    public static String getCategoryLiteral( int categoryID) {

        if( categoryID >= cat_literales.length || categoryID < 0) {
            return cat_literales[ CAT_OTRO];
        }

        return cat_literales[ categoryID];
    }

}
