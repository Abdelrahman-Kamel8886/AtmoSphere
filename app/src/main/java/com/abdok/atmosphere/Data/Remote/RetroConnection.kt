package com.abdok.atmosphere.Data.Remote

import com.abdok.atmosphere.Utils.Consts
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroConnection {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .url(
                    chain.request().url.newBuilder()
                        .addQueryParameter("appid", Consts.API_KEY)
                        .build()
                )
                .build()
            chain.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(Consts.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val retroServices = retrofit.create(RetroServices::class.java)

}