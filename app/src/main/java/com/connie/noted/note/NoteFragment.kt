package com.connie.noted.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connie.noted.MainActivity
import com.connie.noted.NaviDirections
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



        viewModel.checkLogin()

        viewModel.userIsReady.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.getLiveNotes()
            }
        })


        viewModel.viewType.observe(viewLifecycleOwner, Observer {

            when (it) {

                0 -> {
                    noteRecyclerView.adapter =
                        NoteAdapter(NoteAdapter.OnClickListener {
                            (activity as MainActivity).navigateToNote(note)
                        }, viewModel)

                    noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
//                    noteRecyclerView.isAnimating.not()

                    viewModel.notes.value = viewModel.notes.value

                }

                1 -> {
                    noteRecyclerView.adapter = NoteAdapter(NoteAdapter.OnClickListener {
                        (activity as MainActivity).navigateToNote(note)
                    }, viewModel)
                    noteRecyclerView.layoutManager =
                        LinearLayoutManager(NotedApplication.instance.applicationContext)
//                    noteRecyclerView.isAnimating.not()


                    viewModel.notes.value = viewModel.notes.value

                }

            }

        })


        viewModel.newNote.observe(viewLifecycleOwner, Observer {
            it?.let {

                Log.d("Connie", "newNote = $it")
                viewModel.create(it)

            }
        })


        viewModel.notes.observe(viewLifecycleOwner, Observer {

            it?.let {

                Log.d("Connie", "liveNotes = $it")
                (noteRecyclerView.adapter as NoteAdapter).submitList(it)

                viewModel.noteToAdd = it.filter { note ->
                    note.isSelected
                }
                Log.d("Connie", viewModel.noteToAdd.size.toString())

            }
        })

        viewModel.isEditMode.observe(viewLifecycleOwner, Observer {

            it?.let {
                Log.e("Connie", "Note Fragment, isEditMode: $it")
                if (!it) {
                    viewModel.notes.value?.forEach { note ->
                        note.isSelected = false
                        (noteRecyclerView.adapter as NoteAdapter).notifyDataSetChanged()
                    }
                }
            }

        })


        binding.noteIconChangeLayout.setOnClickListener {

            when (viewModel.viewType.value) {
                0 -> viewModel.viewType.value = 1
                1 -> viewModel.viewType.value = 0
            }

        }

        binding.noteAdd2boardButton.setOnClickListener {

            findNavController().navigate(NaviDirections.actionGlobalAdd2boardDialog(viewModel.noteToAdd.toTypedArray()))
        }




        return binding.root

    }

    override fun onResume() {
        super.onResume()
        viewModel.getLiveNotes()
    }
}
