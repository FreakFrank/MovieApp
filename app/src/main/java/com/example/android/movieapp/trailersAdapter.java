package com.example.android.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kareemismail on 12/2/16.
 */

public class trailersAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> trailersLink = new ArrayList<>();

    public trailersAdapter (Context context, ArrayList<String> trailersLink){
        this.context = context;
        this.trailersLink = trailersLink;
    }
    public int getCount() {
        return trailersLink.size();
    }

    @Override
    public Object getItem(int position) {
        return trailersLink.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView trailersView = (TextView) LayoutInflater.from(context).inflate(R.layout.trailers_links_text_view, null);
        trailersView.setText(getItem(position).toString());
        return trailersView;
    }
}
