package com.ai.noteapp.api

import com.ai.noteapp.models.userRequest
import com.ai.noteapp.models.userResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface userApi {

    @POST("/users/signup")
    suspend fun signUp(@Body userRequest: userRequest) : Response<userResponse>

    @POST("/users/signin")
    suspend fun signIn(@Body userRequest: userRequest) : Response<userResponse>
}