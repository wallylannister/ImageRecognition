package com.example.imagerecognition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JGson {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("link")
    @Expose
    private String mLink;

    public String toString() {
        return name;
    }
}
