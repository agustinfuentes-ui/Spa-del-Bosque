package com.example.spadelbosque.repository

import com.example.spadelbosque.data.remote.transbank.TransbankRetrofitClient
import com.example.spadelbosque.model.transbank.TransbankCommitResponse
import com.example.spadelbosque.model.transbank.TransbankCreateRequest
import com.example.spadelbosque.model.transbank.TransbankCreateResponse

class TransbankRepository {
    private val api = TransbankRetrofitClient.api
    private val commerceCode = TransbankRetrofitClient.COMMERCE_CODE
    private val apiKey = TransbankRetrofitClient.API_KEY

    suspend fun crearTransaccion(
        buyOrder: String,
        sessionId: String,
        amount: Int,
        returnUrl: String
    ): Result<TransbankCreateResponse> {
        return try {
            val request = TransbankCreateRequest(
                buy_order = buyOrder,
                session_id = sessionId,
                amount = amount,
                return_url = returnUrl
            )

            val response = api.createTransaction(
                commerceCode = commerceCode,
                apiKey = apiKey,
                request = request
            )

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmarTransaccion(token: String): Result<TransbankCommitResponse> {
        return try {
            val response = api.commitTransaction(
                commerceCode = commerceCode,
                apiKey = apiKey,
                token = token
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}