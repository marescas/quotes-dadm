package bonet.esteve.citas.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bonet.esteve.citas.POJO.Quotation;
import bonet.esteve.citas.R;

public class favouriteAdapter extends ArrayAdapter<Quotation> {
    private int res;
    private ArrayList<Quotation> listaCitas;
    private static LayoutInflater layoutInflater = null;
    public favouriteAdapter(Context context, int resource,  ArrayList<Quotation> objects) {
        super(context, resource, objects);
        listaCitas = objects;
        this.res =  resource;


    }

    private class ViewHolder{
        TextView textCita, textAutor;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder = new ViewHolder();
        if(rowView == null) {
            layoutInflater = ((Activity) getContext()).getLayoutInflater();
            rowView = layoutInflater.inflate(res, null);

            holder.textCita = rowView.findViewById(R.id.textoCita);
            holder.textAutor = rowView.findViewById(R.id.textAutor);
            Quotation cita = listaCitas.get(position);
            holder.textAutor.setText(holder.textAutor.getText());
            holder.textCita.setText(holder.textCita.getText());
            rowView.setTag(holder);
        }
            holder = (ViewHolder) rowView.getTag();
            holder.textAutor.setText(getItem(position).getAutor());
            holder.textCita.setText(getItem(position).getCita());

        return rowView;
    }
}
