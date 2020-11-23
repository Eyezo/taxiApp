package com.example.eyezo.taxiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Person>
{

    private Context context;
    private List<Person> values;

    public ListAdapter(Context context,  List<Person> list) {

        super(context, R.layout.driver_list, list);
        this.context = context;
        this.values = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.driver_list, parent , false);

        TextView tvChar = convertView.findViewById(R.id.tvChar);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvNum =  convertView.findViewById(R.id.tvNumOfP);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount
        );

        tvChar.setText(values.get(position).getName().toUpperCase().charAt(0)+"");
        tvName.setText(values.get(position).getName());
        tvNum.setText(values.get(position).getNumOfPass());
        tvAmount.setText("R" + values.get(position).getAmount());


        return convertView;
    }
}
