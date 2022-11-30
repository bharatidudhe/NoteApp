package com.ai.noteapp

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.noteapp.models.userRequest
import com.ai.noteapp.models.userResponse
import com.ai.noteapp.repository.UserRepository
import com.ai.noteapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) :ViewModel() {

    val userResponseLiveData :LiveData<NetworkResult<userResponse>> get()= userRepository.userResponseLiveData

    fun registerUser(userRequest: userRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userRequest: userRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }

    }
    
    fun validteCredential(userName :String,email:String,password:String,isLogin :Boolean): Pair<Boolean,String>{
        var result =Pair(true,"")
        
        if (!isLogin && TextUtils.isEmpty(userName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false,"Please provide credentials")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email")
        }else if (password.length < 5){
            result= Pair(false,"Password should be greater than 5")
        }
        
        return result
    }
}