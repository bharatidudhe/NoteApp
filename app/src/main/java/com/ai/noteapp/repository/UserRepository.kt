package com.ai.noteapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ai.noteapp.api.userApi
import com.ai.noteapp.models.userRequest
import com.ai.noteapp.models.userResponse
import com.ai.noteapp.utils.Constant.TAG
import com.ai.noteapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: userApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<userResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<userResponse>> get() = _userResponseLiveData

    suspend fun registerUser(userRequest: userRequest) {
        val response = userApi.signUp(userRequest)
        Log.d(TAG, response.body().toString())
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: userRequest) {
        val response = userApi.signIn(userRequest)
        Log.d(TAG, response.body().toString())
        handleResponse(response)
    }

    private fun handleResponse(response: Response<userResponse>) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}