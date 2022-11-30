package com.ai.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ai.noteapp.api.NoteApi
import com.ai.noteapp.models.NoteRequest
import com.ai.noteapp.models.NoteResponse
import com.ai.noteapp.models.userResponse
import com.ai.noteapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteApi) {

    private val getNotesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData : LiveData<NetworkResult<List<NoteResponse>>> get() = getNotesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData :LiveData<NetworkResult<String>> get() = _statusLiveData

    suspend fun getNotes(){

        getNotesLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            getNotesLiveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            getNotesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            getNotesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        handleResponse(response,"Note Created")

    }

    suspend fun updateNote(noteId:String,noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(noteId,noteRequest)
        handleResponse(response,"Note Updated")
    }

    suspend fun deleteNote(noteId:String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        handleResponse(response,"Note Deleted")
    }


    private fun handleResponse(response: Response<NoteResponse>,message:String) {
        if (response.isSuccessful && response.body()  != null){
            _statusLiveData.postValue(NetworkResult.Success(message))
        }else{
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}