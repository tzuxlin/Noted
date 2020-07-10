package com.connie.noted.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connie.noted.NotedApplication
import com.connie.noted.data.Note
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

        val noteRecyclerView = binding.noteRecyclerView


        viewModel.viewType.observe(viewLifecycleOwner, Observer {
            
            when (it) {

                0 -> {
                    noteRecyclerView.adapter = NoteAdapter(viewModel)
                    noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)

                    viewModel.liveNotes.value = viewModel.liveNotes.value

                }

                1 -> {
                    noteRecyclerView.adapter = NoteAdapter(viewModel)
                    noteRecyclerView.layoutManager =
                        LinearLayoutManager(NotedApplication.instance.applicationContext)

                    viewModel.liveNotes.value = viewModel.liveNotes.value

                }

            }

        })


        viewModel.newNote.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Connie", "newNote = $it")

                viewModel.create(it)
            }
        })


        viewModel.liveNotes.observe(viewLifecycleOwner, Observer {

            it?.let {
                Log.d("Connie", "liveNotes = $it")
                (noteRecyclerView.adapter as NoteAdapter).submitList(it)
            }

        })


        binding.noteIconChangeLayout.setOnClickListener {

            when (viewModel.viewType.value) {
                0 -> viewModel.viewType.value = 1
                1 -> viewModel.viewType.value = 0
            }

        }


        return binding.root

    }

}
