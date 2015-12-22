package com.example.fanwise.pm25.service;

import com.example.fanwise.pm25.domain.PM25;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Fanwise on 2015/12/22.
 */
public interface AirService {

    @GET("/api/querys/aqi_details.json")
    Call<List<PM25>> getPM25(@Query("token") String token, @Query("city") String city);
}
