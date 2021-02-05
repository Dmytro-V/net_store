package com.netstore.home.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Sys {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private Integer sunrise;
    @SerializedName("sunset")
    @Expose
    private Integer sunset;

}
