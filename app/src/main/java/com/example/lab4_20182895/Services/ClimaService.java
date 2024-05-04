package com.example.lab4_20182895.Services;

import com.example.lab4_20182895.Dto.DtoCiudad;
import com.example.lab4_20182895.Dto.DtoClima;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClimaService {

    @GET("/data/2.5/weather?")
    Call<DtoClima> getClima(@Query("lat") String ciudad,
                                   @Query("lon") String limit,
                                   @Query("appid") String api
    );
}
