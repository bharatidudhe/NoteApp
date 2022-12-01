package com.ai.noteapp.features.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ai.noteapp.R
import com.ai.noteapp.databinding.FragmentLoginBinding
import com.ai.noteapp.models.userRequest
import com.ai.noteapp.utils.NetworkResult
import com.ai.noteapp.utils.TokenManager
import com.ai.noteapp.features.login.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding :FragmentLoginBinding? =null
    private val binding get()  = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager:TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val result = validateInputs()
            if (result.first){
                authViewModel.loginUser(userRequest(getUserRequest().first,getUserRequest().second,""))
            }else{
                binding.txtError.text = result.second
            }
        }
        binding.btnSignUp.setOnClickListener {
        findNavController().popBackStack()
        }

        bindObserver()
    }

    private fun validateInputs():Pair<Boolean,String>{
       val userRequest = getUserRequest()

        return  authViewModel.validteCredential("",userRequest.first,userRequest.second,true)
    }

    private fun getUserRequest():Pair<String,String>{
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return Pair(email,password)
    }

    private fun bindObserver(){
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success ->{
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error ->{
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}