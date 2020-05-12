package com.thethreebees.poligo;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIEndpoints {
    String BaseUrl = "http://192.168.0.108:8000/api/v1/";

    @POST("login/")
    Call<JsonObject> loginUser(@Body JsonObject data);

    @POST("addtocart/")
    Call<JsonObject> addToCart(@Body JsonObject data);

    @POST("shoppingcart/")
    Call<JsonObject> shoppingCart(@Body JsonObject data);

    @POST("placeorder/")
    Call<JsonObject> placeOrder(@Body JsonObject data);

    @POST("shoppinghistory/")
    Call<JsonObject> shoppingHistory(@Body JsonObject data);
}
