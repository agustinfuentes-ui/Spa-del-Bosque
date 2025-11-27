package com.example.spadelbosque.data.remote.api

import com.example.spadelbosque.data.remote.dto.AuthResponse
import com.example.spadelbosque.data.remote.dto.EstadoDTO
import com.example.spadelbosque.data.remote.dto.LoginRequest
import com.example.spadelbosque.data.remote.dto.RegisterRequest
import com.example.spadelbosque.data.remote.dto.RolDTO
import com.example.spadelbosque.data.remote.dto.UpdateUsuarioRequest
import com.example.spadelbosque.data.remote.dto.UsuarioResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserApiService {
    @POST("api/v1/users/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("api/v1/users/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @GET("api/v1/users")
    suspend fun getAll(): List<UsuarioResponse>

    @GET("api/v1/users/{id}")
    suspend fun getById(@Path("id") id: Long): UsuarioResponse

    @PUT("api/v1/users/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body body: UpdateUsuarioRequest
    ): UsuarioResponse

    @DELETE("api/v1/users/{id}")
    suspend fun delete(@Path("id") id: Long)

    @PATCH("api/v1/users/{id}/rol")
    suspend fun cambiarRol(
        @Path("id") id: Long,
        @Body body: RolDTO
    ): UsuarioResponse

    @PATCH("api/v1/users/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Long,
        @Body body: EstadoDTO
    ): UsuarioResponse
}
