package com.example.fanwise.pm25.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fanwise on 2015/12/22.
 */
public class PM25 {
    @SerializedName("position_name")
    private String positionName;       //检测站点

    @SerializedName("aqi")
    private String aqi;                 //空气指数

    @SerializedName("quality")
    private String quality;            //质量状况

    @SerializedName("pm2_5")
    private String pm25;               //PM2.5浓度

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
}
