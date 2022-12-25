package com.example.batyaa.tulan_cosmetics.Classes;

import android.widget.ImageView;

import com.example.batyaa.tulan_cosmetics.R;
import com.google.firebase.database.Exclude;
import com.squareup.picasso.Picasso;

public class Product {


    private String name;
    private String imageURL;
    private String key;
    private String description;
    private String Price;
    private String Catagory;
    private String Search;
    private String size;
    private String Stock;

public Product()
{

}
        public Product(String name, String imageUrl, String Des, String Price, String category , String search, String Size, String stock)
        {
            if (name.trim().equals(""))
            {
                name = "No Name";
            }

            this.name = name;
            this.imageURL = imageUrl;
            this.description = Des;
            this.Price = Price;
            this.Catagory = category;
            this.Search = search;
            this.size = Size;
            this.Stock = stock;
        }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }

    public String getCatagory()
    {
        return Catagory;
    }

    public void setCatagory(String catagory)
    {
        Catagory = catagory;
    }

    public String getPrice()
    {
        return Price;
    }

    public void setPrice(String price)
    {
        Price = price;
    }

    public String getDescription()
    {
            return description;
    }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getImageUrl()
        {
            return imageURL;
        }

        public void setImageUrl(String imageUrl)
        {
            this.imageURL = imageUrl;
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
