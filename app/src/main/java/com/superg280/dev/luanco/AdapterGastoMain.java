package com.superg280.dev.luanco;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Super on 10/03/2018.
 */

public class AdapterGastoMain extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Gasto> items;

    public AdapterGastoMain(Activity activity, ArrayList<Gasto> items) {
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
            v = inf.inflate(R.layout.item_gasto_main, null);
        }

        Gasto dir = items.get(position);

        TextView fecha = (TextView) v.findViewById(R.id.item_gasto_main_fecha);
        fecha.setText(dir.getFecha());

        TextView description = (TextView) v.findViewById(R.id.item_gasto_main_descripcion);
        description.setText(dir.getDescripcion());

        TextView importe = (TextView) v.findViewById(R.id.item_gasto_main_importe);
        importe.setText(dir.getImporte());

        return v;
    }
}
