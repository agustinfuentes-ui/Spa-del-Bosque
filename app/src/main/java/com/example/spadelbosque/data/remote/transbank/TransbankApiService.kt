package com.example.spadelbosque.data.remote.transbank

import com.example.spadelbosque.model.transbank.TransbankCommitResponse
import com.example.spadelbosque.model.transbank.TransbankCreateRequest
import com.example.spadelbosque.model.transbank.TransbankCreateResponse
import retrofit2.http.*

interface TransbankApiService {

    @POST("rswebpaytransaction/api/webpay/v1.2/transactions")
    @Headers("Content-Type: application/json")
    suspend fun createTransaction(
        @Header("Tbk-Api-Key-Id") commerceCode: String,
        @Header("Tbk-Api-Key-Secret") apiKey: String,
        @Body request: TransbankCreateRequest
    ): TransbankCreateResponse

    @PUT("rswebpaytransaction/api/webpay/v1.2/transactions/{token}")
    suspend fun commitTransaction(
        @Header("Tbk-Api-Key-Id") commerceCode: String,
        @Header("Tbk-Api-Key-Secret") apiKey: String,
        @Path("token") token: String
    ): TransbankCommitResponse
}