package com.example.android.movieapp;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kareemismail on 10/21/16.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagesURL = new ArrayList<String>();

//    public ImageAdapter(Context context, int resource, List<String> objects) {
//        super(context, resource, objects);
//    }

    public ImageAdapter(Context context, ArrayList<String> imageLinks) {
        this.context = context;
        imagesURL = imageLinks;
    }

    @Override
    public int getCount() {
        if (imagesURL == null) {
            return 0;
        }
        return imagesURL.size();
    }

    @Override
    public String getItem(int position) {
        return imagesURL.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = (ImageView) convertView;
        if (image == null) {
            image = (ImageView) LayoutInflater.from(context).inflate(R.layout.grid_view_images, null);
        }
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        String imageURL = getItem(position);
        Picasso.with(context).load(imageURL).into(image);
        return image;
    }
}