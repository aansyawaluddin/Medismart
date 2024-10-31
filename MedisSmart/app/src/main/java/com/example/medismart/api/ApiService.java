package com.example.medismart.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/")
    Call<String> getResponse();
}
