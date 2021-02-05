package com.netstore.home.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Clouds {

    @SerializedName("all")
    @Expose
    private Integer all;

}
