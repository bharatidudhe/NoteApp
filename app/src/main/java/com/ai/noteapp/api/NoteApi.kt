package com.ai.noteapp.api

import com.ai.noteapp.models.NoteRequest
import com.ai.noteapp.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteApi {

    @GET("/note")
    suspend fun getNotes():Response<List<NoteResponse>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId :String, @Body noteRequest: NoteRequest):Response<NoteResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String):Response<NoteResponse>
}