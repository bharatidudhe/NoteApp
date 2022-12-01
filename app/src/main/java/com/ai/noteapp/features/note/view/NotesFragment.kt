package com.ai.noteapp.features.note.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ai.noteapp.features.note.viewmodel.NoteViewModel
import com.ai.noteapp.databinding.FragmentNotesBinding
import com.ai.noteapp.models.NoteRequest
import com.ai.noteapp.models.NoteResponse
import com.ai.noteapp.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding : FragmentNotesBinding? = null
    val binding get() = _binding!!

    private var note : NoteResponse? =null

    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            if (validateInput()) {
                val (title, desc) = getNoteRequest()
                if (note == null) {
                    noteViewModel.createNote(NoteRequest(desc, title))
                }else{
                    noteViewModel.updateNote(note!!._id, NoteRequest(desc,title))
                }
            }else{
                Toast.makeText(requireContext(),"Please  provide details",Toast.LENGTH_SHORT).show()
            }
        }

        setInialData()
        bindObserver()
    }

    private fun setInialData(){
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null){
            note = Gson().fromJson(jsonNote,NoteResponse::class.java)
            note?.let {
               binding.txtTitle.setText(it.title)
               binding.txtDescription.setText(it.description)
            }
        }else{
            binding.addEditText.setText("Add Note")
        }
    }

    private fun validateInput():Boolean{
        val (title, desc) = getNoteRequest()

        return noteViewModel.vaidateInputs(title,desc)
    }

    private fun getNoteRequest(): Pair<String, String> {
        val title = binding.txtTitle.text.toString()
        val desc = binding.txtDescription.text.toString()
        return Pair(title, desc)
    }

    private fun bindObserver(){
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}