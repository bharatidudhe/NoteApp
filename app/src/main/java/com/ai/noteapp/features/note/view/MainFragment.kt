package com.ai.noteapp.features.note.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ai.noteapp.features.note.adapter.NoteAdapter
import com.ai.noteapp.features.note.viewmodel.NoteViewModel
import com.ai.noteapp.R
import com.ai.noteapp.databinding.FragmentMainBinding
import com.ai.noteapp.models.NoteResponse
import com.ai.noteapp.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding :FragmentMainBinding? = null
    val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()
    lateinit var noteAdapter : NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentMainBinding.inflate(layoutInflater,container,false)
        noteAdapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObserver()
        noteViewModel.getNote()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = noteAdapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_notesFragment)
        }

    }
    private fun bindObserver(){
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    noteAdapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        })

        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    noteViewModel.getNote()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        })
    }
    private fun onNoteClicked(noteResponse: NoteResponse,isdelete:Boolean){
        if (isdelete){
            noteViewModel.deleteNote(noteResponse._id)
        }else {
            val bundle = Bundle().apply {
                putString("note", Gson().toJson(noteResponse))
            }
            findNavController().navigate(R.id.action_mainFragment_to_notesFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}