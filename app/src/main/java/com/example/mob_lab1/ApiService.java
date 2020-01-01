package com.example.mob_lab1;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("plane")
    Call<Void> addNewPlane(@Body Trip trip);

    @GET("plane")
    Call<ServerResponce> getAllTrips();
}
