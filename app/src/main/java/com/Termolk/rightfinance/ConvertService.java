package com.Termolk.rightfinance;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ConvertService {
    @GET("p24api/pubinfo?json&exchange&coursid=5")


    Call<com.example.Termolk.rightfinance.Converter> convertMoney(@Query("country") String country, @Query("day") String day);
}
