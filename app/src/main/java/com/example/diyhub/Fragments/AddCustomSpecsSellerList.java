package com.example.diyhub.Fragments;

import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomSpecsSellerList {
    String specsName;

    public AddCustomSpecsSellerList() {
    }

    public AddCustomSpecsSellerList(String specsName) {
        this.specsName = specsName;
    }

    public String getSpecsName() {
        return specsName;
    }

    public void setSpecsName(String specsName) {
        this.specsName = specsName;
    }
}
