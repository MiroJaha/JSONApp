package com.example.jsonapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("eur.json")
    fun doGetListResources(): Call<ConvertDetails?>?
}