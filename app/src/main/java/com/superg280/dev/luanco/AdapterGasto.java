package com.superg280.dev.luanco;

import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Super on 10/03/2018.
 */

public class AdapterGasto extends BaseAdapter {

    private Activity activity;
    private ArrayList<Gasto> items;

    AdapterGasto(Activity activity, ArrayList<Gasto> items) {
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

    public void addAll(ArrayList<Gasto> category) {
        items.addAll(category);
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
            assert inf != null;
            v = inf.inflate(R.layout.item_gasto, null);
        }

        Gasto dir = items.get(position);

        TextView fecha = v.findViewById(R.id.item_gasto_fecha);
        fecha.setText(dir.formatFecha());

        TextView description = v.findViewById(R.id.item_gasto_descripcion);
        description.setText(dir.getDescripcion());

        TextView importe = v.findViewById(R.id.item_gasto_importe);
        importe.setText(String.format("-%s", dir.formatImporte()));

        ImageView icon = v.findViewById(R.id.item_gasto_icon);
        icon.setImageResource(Categories.getCategoryIcon( dir.getCategoria()));

        return v;
    }
}
