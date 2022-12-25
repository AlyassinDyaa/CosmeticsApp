package com.example.batyaa.tulan_cosmetics.Classes;

import com.google.firebase.database.Exclude;

public class category {

    String title  , key, search;
int  image,id;
    public category()
    {

    }
    public category(String titleC, int imageC, int id)
    {
        this.title = titleC;
        this.image = imageC;
        this.id = id;
    }
/*
    public category(String Title, int image ,String s) {
        title = Title;
        this.image = image;
        this.search = s;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Exclude
    public String getKey()
    {
        return key;
    }

    @Exclude
    public void setKey(String key)
    {
        this.key = key;
    }



}
