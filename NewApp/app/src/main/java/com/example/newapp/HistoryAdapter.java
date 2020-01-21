package com.example.newapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<Note> implements Filterable {
    private static final String TAG = "NoteListAdapter";

    private Context mContext;
    private int mResource;
    private ArrayList<Note> mOriginalValues;
    private ArrayList<Note> mDisplayedValues;

    String area, fg, fid, japa, name, session, time, zfl, zmob, zread, ztl, zzdate, zzone, url,
            program, reading, category;

    public HistoryAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.mOriginalValues = objects;
        this.mDisplayedValues = objects;
    }

    public String getName() {
        return this.name;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        //get the person's information

        area = getItem(position).getArea();
        fg = getItem(position).getFg();
        fid = getItem(position).getFid();
        japa = "Japa: " + getItem(position).getJapa();
        name = getItem(position).getName();
        session = getItem(position).getSession();
        time = getItem(position).getTime();
        zfl = getItem(position).getZfl();
        zmob = getItem(position).getZmob();
        zread = getItem(position).getZread();
        ztl = getItem(position).getZtl();
        zzdate = getItem(position).getZzdate();
        zzone = getItem(position).getZzone();
        url = getItem(position).getUrl();
        program = getItem(position).getProgram();
        reading = getItem(position).getZread();
        category = getItem(position).getCategory();

        if(url.equals("")) {
            url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
        }

        //Create the person object with the information
        Note note =  new Note(name, zmob, area, time);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvProgram = convertView.findViewById(R.id.textView1);
        ImageView tvURL = convertView.findViewById(R.id.img);
        TextView tvCategory = convertView.findViewById(R.id.textView2);
        TextView tvDate = convertView.findViewById(R.id.textView4);
        TextView tvTime = convertView.findViewById(R.id.textView5);
        TextView tvReading = convertView.findViewById(R.id.textView6);
        TextView tvJapa = convertView.findViewById(R.id.textView7);
        TextView tvSession = convertView.findViewById(R.id.textView10);

        tvProgram.setText(program);
        Glide.with(mContext).load(url).crossFade().into(tvURL);
        tvCategory.setText(category);
        tvTime.setText(time);
        tvDate.setText(zzdate);
        tvJapa.setText(japa);
        tvReading.setText(reading);
        tvSession.setText(session);

        tvURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DialogPhoto.class);
                url = getItem(position).getUrl();
                name = getItem(position).getName();
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putString("URL", url);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public Note getItem(int position) {
        return mDisplayedValues.get(position);
    }

    public int getCount() {
        return mDisplayedValues.size();
    }
}
