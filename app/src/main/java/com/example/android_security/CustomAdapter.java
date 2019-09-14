package com.example.android_security;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.android_security.MainActivity;
//import com.example.android_security.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<MainActivity.PInfo> data;


    public CustomAdapter(Activity activity, ArrayList<MainActivity.PInfo> items) {
        this.activity = activity;
        this.data = items;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }*/


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        try {

            ImageView slika = convertView.findViewById(R.id.slika);
            TextView naslov = convertView.findViewById(R.id.naslov);
            TextView paket = convertView.findViewById(R.id.paket);
            TextView critical = convertView.findViewById(R.id.critical);


            String pname = data.get(position).pname;
            Drawable icon;

            try {

                icon = convertView.getContext().getPackageManager().getApplicationIcon(pname);

                slika.setImageDrawable(icon);
                naslov.setText(data.get(position).appname);
                paket.setText(data.get(position).pname);
                critical.setText("" + data.get(position).critical.size());

            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (ParseException e) {

            e.printStackTrace();

        }

        return convertView;

    }
}