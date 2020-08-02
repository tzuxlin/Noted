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
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.FragmentNoteBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.CurrentFilterType
import com.connie.noted.util.DialogBoxMessageType

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

            Log.e(
                "Connie",
                "Note Fragment, observe viewType: $it, mainCurrentFilterType: ${mainViewModel.currentFilterType.value}"
            )

            when (it) {

                0 -> {
                    viewModel.viewType.value = it

                    noteRecyclerView.adapter =
                        NoteAdapter(NoteAdapter.OnClickListener { clickedNote ->
                            (activity as MainActivity).navigateToNote(clickedNote)
                        }, viewModel)

                    val layoutManager = StaggeredGridLayoutManager(2, 1)
                    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE


                    noteRecyclerView.layoutManager = layoutManager

                    Log.e("Connie", mainViewModel.currentFilterType.value.toString())

                    val type = mainViewModel.currentFilterType.value ?: CurrentFilterType.ALL
                    checkFilterType(type, noteRecyclerView.adapter as NoteAdapter)

                }

                1 -> {
                    viewModel.viewType.value = it

                    noteRecyclerView.adapter =
                        NoteAdapter(NoteAdapter.OnClickListener { clickedNote ->
                            (activity as MainActivity).navigateToNote(clickedNote)
                        }, viewModel)
                    noteRecyclerView.layoutManager =
                        LinearLayoutManager(NotedApplication.instance.applicationContext)

                    Log.e("Connie", mainViewModel.currentFilterType.value.toString())

                    val type = mainViewModel.currentFilterType.value ?: CurrentFilterType.ALL
                    checkFilterType(type, noteRecyclerView.adapter as NoteAdapter)
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

                (noteRecyclerView.adapter as NoteAdapter).submitList(it)
                Log.d("Connie", "liveNotes = $it")
//                viewModel.notes.value = viewModel.notes.value
//                (noteRecyclerView.adapter as NoteAdapter).notifyDataSetChanged()

            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {

            when (it) {

                LoadApiStatus.LOADING -> {
                    findNavController().navigate(
                        NaviDirections.actionGlobalBoxDialog(
                            DialogBoxMessageType.LOADING_NOTE.message
                        )
                    )
                }

                LoadApiStatus.DONE -> {
                    findNavController().navigateUp()
                    mainViewModel.urlString.value = null
                    findNavController().navigate(
                        NaviDirections.actionGlobalBoxDialog(
                            DialogBoxMessageType.NEW_NOTE.message
                        )
                    )
                }

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
            viewModel.noteToAdd.value?.let {
                findNavController().navigate(NaviDirections.actionGlobalAdd2boardDialog(it.toTypedArray()))
                viewModel.isEditMode.value = false
            }
        }


        return binding.root

    }

    private fun checkFilterType(filterType: CurrentFilterType, noteAdapter: NoteAdapter) {

        Log.e("Connie", "Note, checkFilterType with type: ${filterType.type}")

        val notes = viewModel.notes.value

        notes?.let { notes ->

            when (filterType) {

                CurrentFilterType.ALL -> {
                    noteAdapter.submitList(notes)
                    Log.i("Connie", "Note, checkFilterType with type: ${filterType.type}")
                }

                CurrentFilterType.LIKED -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.isLiked
                    })
                    Log.i("Connie", "Note, checkFilterType with type: ${filterType.type}")
                }

                CurrentFilterType.ARTICLE -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.type == CurrentFilterType.ARTICLE.type
                    })
                    Log.i("Connie", "Note, checkFilterType with type: ${filterType.type}")
                }

                CurrentFilterType.LOCATION -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.type == CurrentFilterType.LOCATION.type
                    })
                    Log.i("Connie", "Note, checkFilterType with type: ${filterType.type}")
                }

                CurrentFilterType.VIDEO -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.type == CurrentFilterType.VIDEO.type
                    })
                    Log.i("Connie", "Note, checkFilterType with type: ${filterType.type}")
                }

            }
        }
    }

}
