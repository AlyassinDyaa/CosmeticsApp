package com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY;

import com.google.firebase.database.Exclude;

public class aboutUsUpdate
{
    String englishDesc , arabicDesc, SnapImage, facebImage, instaImage ,key;


    public aboutUsUpdate(String englishDesc, String arabicDesc, String snapImage, String facebImage, String instaImage) {
        this.englishDesc = englishDesc;
        this.arabicDesc = arabicDesc;
        SnapImage = snapImage;
        this.facebImage = facebImage;
        this.instaImage = instaImage;
    }

    public String getEnglishDesc() {
        return englishDesc;
    }

    public void setEnglishDesc(String englishDesc) {
        this.englishDesc = englishDesc;
    }

    public String getArabicDesc() {
        return arabicDesc;
    }

    public void setArabicDesc(String arabicDesc) {
        this.arabicDesc = arabicDesc;
    }

    public String getSnapImage() {
        return SnapImage;
    }

    public void setSnapImage(String snapImage) {
        SnapImage = snapImage;
    }

    public String getFacebImage() {
        return facebImage;
    }

    public void setFacebImage(String facebImage) {
        this.facebImage = facebImage;
    }

    public String getInstaImage() {
        return instaImage;
    }

    public void setInstaImage(String instaImage) {
        this.instaImage = instaImage;
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
