package com.superg280.dev.luanco;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Super on 10/03/2018.
 */

public class AdapterIngreso extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Ingreso> items;

    public AdapterIngreso(Activity activity, ArrayList<Ingreso> items) {
        this.activity = activity;
        this.items    = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Ingreso> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_ingreso, null);
        }

        Ingreso ingreso = items.get(position);

        TextView fecha = (TextView) v.findViewById(R.id.item_ingreso_fecha);
        fecha.setText(ingreso.getFecha());

        TextView description = (TextView) v.findViewById(R.id.item_ingreso_descripcion);
        description.setText(ingreso.getDescripcion());

        TextView importe = (TextView) v.findViewById(R.id.item_ingreso_importe);
        importe.setText(ingreso.getImporte());

        ImageView imagen = (ImageView) v.findViewById(R.id.item_ingreso_image);
        Drawable originalDrawable;

        //Establece la imagen de usuario de cada ingreso.
        int user = ingreso.getUserID();
        if( user == 1) {
            originalDrawable = parent.getResources().getDrawable(R.drawable.yo);
        } else if( user == 2) {
            originalDrawable = parent.getResources().getDrawable(R.drawable.maria_perfil);
        } else {
            originalDrawable = parent.getResources().getDrawable(R.drawable.luis_perfil);
        }

        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(activity.getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCircular(true);

        imagen.setImageDrawable(roundedDrawable);
        //imagen.setImageDrawable(originalDrawable);

        return v;
    }
}
