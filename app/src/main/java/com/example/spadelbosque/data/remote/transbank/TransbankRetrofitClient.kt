package com.example.spadelbosque.data.remote.transbank

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TransbankRetrofitClient {
    // URL del ambiente de integración (sandbox)
    private const val BASE_URL = "https://webpay3gint.transbank.cl/"

    // Credenciales públicas de integración
    const val COMMERCE_CODE = "597055555532"
    const val API_KEY = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: TransbankApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransbankApiService::class.java)
    }
}