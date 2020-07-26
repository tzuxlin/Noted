package com.connie.noted.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connie.noted.MainActivity
import com.connie.noted.MainViewModel
import com.connie.noted.NaviDirections
import com.connie.noted.NotedApplication
import com.connie.noted.data.Note
import com.connie.noted.databinding.FragmentNoteBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.CurrentFilterType

class NoteFragment(private val note: Note = Note()) : Fragment() {

    private val viewModel by viewModels<NoteViewModel> { getVmFactory(note) }
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNoteBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val noteRecyclerView = binding.noteRecyclerView

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        mainViewModel.urlString.observe(viewLifecycleOwner, Observer {
            it?.let {
                val url = it

                Log.e("ConnieCrawler", "mainViewModel observed from NoteFragment, $it")
                mainViewModel.urlString.value = null
                Log.e(
                    "ConnieCrawler",
                    "mainViewModel.urlString = ${mainViewModel.urlString.value} (expected: null)"
                )

                viewModel.goGo(url)
            }
        })

        viewModel.checkLogin()

        viewModel.userIsReady.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getLiveNotes()
            }
        })

        mainViewModel.currentFilterType.observe(viewLifecycleOwner, Observer {

            it?.let { type ->
                noteRecyclerView.adapter?.let { adapter ->
                    checkFilterType(type, adapter as NoteAdapter)
                }
            }

        })


        mainViewModel.viewType.observe(viewLifecycleOwner, Observer {

            when (it) {

                0 -> {
                    viewModel.viewType.value = it

                    noteRecyclerView.adapter =
                        NoteAdapter(NoteAdapter.OnClickListener {
                            (activity as MainActivity).navigateToNote(note)
                        }, viewModel)

                    noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)

                    mainViewModel.currentFilterType.value?.let { type ->
                        checkFilterType(type, noteRecyclerView.adapter as NoteAdapter)
                    }

                }

                1 -> {
                    viewModel.viewType.value = it

                    noteRecyclerView.adapter = NoteAdapter(NoteAdapter.OnClickListener {
                        (activity as MainActivity).navigateToNote(note)
                    }, viewModel)
                    noteRecyclerView.layoutManager =
                        LinearLayoutManager(NotedApplication.instance.applicationContext)

                    mainViewModel.currentFilterType.value?.let { type ->
                        checkFilterType(type, noteRecyclerView.adapter as NoteAdapter)
                    }
                }

            }

        })

        viewModel.newNote.observe(viewLifecycleOwner, Observer {
            it?.let {

                Log.w("Connie", "newNote = $it")
                viewModel.create(it)

            }
        })


        viewModel.notes.observe(viewLifecycleOwner, Observer {

            it?.let {

                Log.d("Connie", "liveNotes = $it")
                (noteRecyclerView.adapter as NoteAdapter).notifyDataSetChanged()
                viewModel.notes.value = viewModel.notes.value

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

    private fun checkFilterType(filterType: CurrentFilterType, noteAdapter: NoteAdapter) {

        filterType.let { type ->

            when (type) {

                CurrentFilterType.ALL -> {
                    viewModel.notes.value = viewModel.notes.value
                }

                CurrentFilterType.LIKED -> {
                    noteAdapter.submitList(viewModel.notes.value?.filter { note ->
                        note.isLiked
                    })
                }

                CurrentFilterType.ARTICLE -> {
                    noteAdapter.submitList(viewModel.notes.value?.filter { note ->
                        note.type == CurrentFilterType.ARTICLE.type
                    })
                    Log.i("Connie", CurrentFilterType.ARTICLE.type)
                }

                CurrentFilterType.LOCATION -> {
                    noteAdapter.submitList(viewModel.notes.value?.filter { note ->
                        note.type == CurrentFilterType.LOCATION.type
                    })
                    Log.i("Connie", CurrentFilterType.LOCATION.type)
                }

                CurrentFilterType.VIDEO -> {
                    noteAdapter.submitList(viewModel.notes.value?.filter { note ->
                        note.type == CurrentFilterType.VIDEO.type
                    })
                    Log.i("Connie", CurrentFilterType.VIDEO.type)
                }

            }
        }
    }

}
