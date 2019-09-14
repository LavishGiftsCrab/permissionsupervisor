package com.example.android_security;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScreenSlidePageFragment extends Fragment {
    Activity act;
    public ArrayList<MainActivity.PInfo> appovi;
    //public ListView Lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        act = getActivity();//getActivity():获得Fragment依附的Activity对象

        ListView Lista = rootView.findViewById( R.id.Lista );


        appovi = getArguments().getParcelableArrayList("list");
        CustomAdapter adapter = new CustomAdapter(act,appovi);
        Lista.setAdapter(adapter);
        Collections.sort(appovi, new Comparator<MainActivity.PInfo>() {
            public int compare(MainActivity.PInfo one, MainActivity.PInfo other) {

                Integer i = other.critical.size();

                return i.compareTo(one.critical.size());
            }
        });

        Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.PInfo item = (MainActivity.PInfo)parent.getItemAtPosition(position);
                String appna = item.pname;
//                Intent intent = new Intent(Intent.ACTION_DELETE);
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:"+appna));
                startActivity(intent);
            }
        });


        return rootView;
    }

    public ScreenSlidePageFragment(){}

    /*public ScreenSlidePageFragment(ArrayList<MainActivity.PInfo> lista){
        appovi = lista;

        Collections.sort(appovi, new Comparator<MainActivity.PInfo>() {
            public int compare(MainActivity.PInfo one, MainActivity.PInfo other) {

                Integer i = new Integer(other.critical.size());

                return i.compareTo(one.critical.size());
            }
        });
    }*/
}