package com.connie.noted.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.databinding.FragmentNoteBinding

class NoteFragment : Fragment() {

    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNoteBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.noteRecyclerView.adapter = NoteAdapter(viewModel)


        viewModel.newNote.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Connie", it.toString())
            }
        })


        return binding.root

    }

}
