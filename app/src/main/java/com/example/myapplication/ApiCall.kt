package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET

interface ApiCall {
    @GET("/quotes")
    suspend fun fetchData(): DataClass
}