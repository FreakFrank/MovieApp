package com.example.android.movieapp;

import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by kareemismail on 12/3/16.
 */

public class Movie extends RealmObject {
    private String id;
    private String allInfo;

    public String getId() {
        return id;
    }

    public String getAllInfo() {
        return allInfo;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setAllInfo(String  allInfo){
        this.allInfo = allInfo;
    }
}
