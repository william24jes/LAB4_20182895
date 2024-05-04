package com.example.lab4_20182895.Services;

import com.example.lab4_20182895.Dto.DtoCiudad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CiudadService {

    @GET("/geo/1.0/direct?")
    Call<List<DtoCiudad>> getCiudad(@Query("q") String ciudad,
                              @Query("limit") String limit,
                              @Query("appid") String api
    );
}
