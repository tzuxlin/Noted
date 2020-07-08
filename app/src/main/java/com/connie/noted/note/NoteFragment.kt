package com.connie.noted.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.databinding.FragmentNoteBinding
import com.connie.noted.ext.getVmFactory

class NoteFragment(private val note: Note = Note()) : Fragment() {

    private val viewModel by viewModels<NoteViewModel> { getVmFactory(note) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNoteBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.noteRecyclerView.adapter = NoteAdapter(viewModel)


        viewModel.newNote.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Connie", it.toString())

                viewModel.create(it)
            }
        })

        viewModel.liveNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Connie", "liveNotes = $it")
                NoteAdapter(viewModel).submitList(it)

            }
        })


        return binding.root

    }

}
