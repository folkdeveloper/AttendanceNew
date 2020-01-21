package com.example.newapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class RegToAttendAdapter extends BaseAdapter {
    private final ArrayList mData;

    public RegToAttendAdapter(Map<String, RegToAttend> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, RegToAttend> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_occupation_item, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, RegToAttend> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());
        ((TextView) result.findViewById(android.R.id.text2)).setText(String.valueOf(item.getValue().getReg()));
        ((TextView) result.findViewById(R.id.text3)).setText(String.valueOf(item.getValue().getCom()));
        ((TextView) result.findViewById(R.id.text4)).setText(String.valueOf(item.getValue().getNotcom()));
        ((TextView) result.findViewById(R.id.text5)).setText(String.valueOf(item.getValue().getAtt()));

        return result;

    }
}
