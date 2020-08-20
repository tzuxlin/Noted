package com.connie.noted.note

import android.os.Bundle
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
import com.connie.noted.util.Logger

class NoteFragment(private val note: Note = Note()) : Fragment() {

    private val viewModel by viewModels<NoteViewModel> { getVmFactory(note) }
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNoteBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val noteRecyclerView = binding.noteRecyclerView

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        /**
         * If any url input exists and is observed from [MainViewModel],
         * it will call [viewModel]: [determineParseType] to launch the appropriate parse function.
         */

        mainViewModel.rawUrlString.observe(viewLifecycleOwner, Observer {
            it?.let { rawUrl ->

                Logger.d("Note Fragment, url observed from MainViewModel: $rawUrl")
                viewModel.determineParseType(rawUrl)
                mainViewModel.clearUrl()

            }
        })


        viewModel.checkLogin()


        /**
         * Wait for retrieving User data, will get Live Notes with User Id.
         */

        viewModel.userIsReady.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getLiveNotes()
            }
        })


        /**
         * Observe  [CurrentFilterType] changes from [MainViewModel],
         * and call [checkFilterType] to submit the appropriate note list.
         */

        mainViewModel.currentFilterType.observe(viewLifecycleOwner, Observer {

            it?.let { type ->
                noteRecyclerView.adapter?.let { adapter ->
                    checkFilterType(type, adapter as NoteAdapter)
                }
            }

        })


        /**
         * Observe current viewType from [MainViewModel],
         * and setup the appropriate LayoutManager to RecyclerView.
         */

        mainViewModel.viewType.observe(viewLifecycleOwner, Observer {

            viewModel.viewType.value = it

            noteRecyclerView.adapter =
                NoteAdapter(NoteAdapter.OnClickListener { clickedNote ->
                    (activity as MainActivity).navigateToNote(clickedNote)
                }, viewModel)


            when (it) {

                0 -> {
                    val layoutManager = StaggeredGridLayoutManager(2, 1)
                    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

                    noteRecyclerView.layoutManager = layoutManager
                }

                1 -> {
                    noteRecyclerView.layoutManager =
                        LinearLayoutManager(NotedApplication.instance)
                }

            }

            val type = mainViewModel.currentFilterType.value ?: CurrentFilterType.ALL
            checkFilterType(type, noteRecyclerView.adapter as NoteAdapter)

        })


        /**
         * Once new note is done parsing and observed, it will be update to firebase.
         */

        viewModel.parsedNote.observe(viewLifecycleOwner, Observer {
            it?.let { note ->
                viewModel.updateNoteToFirebase(note)
            }
        })

        viewModel.toObserveNote.observe(viewLifecycleOwner, Observer { b ->

            b?.let {

                if (it) {

                    viewModel.notes.observe(viewLifecycleOwner, Observer { note ->

                        note?.let { notes ->

                            (noteRecyclerView.adapter as NoteAdapter).submitList(notes)
                            (noteRecyclerView.adapter as NoteAdapter).notifyItemRangeInserted(
                                0,
                                if (note.size < 10) note.size else 10
                            )
                            noteRecyclerView.layoutManager?.scrollToPosition(0)
                            Logger.d("liveNotes, toObserveNote = $notes")


                        }
                    })

                    viewModel.onNoteObserved()
                }
            }
        })


        viewModel.notes.observe(viewLifecycleOwner, Observer {

            it?.let {

                (noteRecyclerView.adapter as NoteAdapter).submitList(it)
                Logger.e("liveNotes, origin = $it")

            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {

            when (it) {

                LoadApiStatus.LOADING -> {
                    findNavController().navigate(
                        NaviDirections.actionGlobalBoxDialog(
                            DialogBoxMessageType.LOADING.message
                        )
                    )
                }

                LoadApiStatus.DONE -> {
                    findNavController().navigateUp()
                    findNavController().navigate(
                        NaviDirections.actionGlobalBoxDialog(
                            DialogBoxMessageType.NEW_NOTE.message
                        )
                    )
                    viewModel.getLiveNotes()
                }
            }
        })




        /**
         * Restore [Note.isSelected] value when user exit the edit mode
         */

        viewModel.isEditMode.observe(viewLifecycleOwner, Observer {

            it?.let { editMode ->

                if (!editMode) {
                    viewModel.notes.value?.forEach { note ->
                        note.isSelected = false
                        (noteRecyclerView.adapter as NoteAdapter).notifyDataSetChanged()
                    }
                }
            }

        })


        binding.noteAdd2boardButton.setOnClickListener {
            viewModel.noteToAdd.value?.let {
                findNavController().navigate(NaviDirections.actionGlobalAdd2boardDialog(it.toTypedArray()))
                viewModel.isEditMode.value = false
            }
        }


        return binding.root

    }

    private fun checkFilterType(filterType: CurrentFilterType, noteAdapter: NoteAdapter) {

        Logger.v("Note, checkFilterType with type: ${filterType.type}")

        val notes = viewModel.notes.value

        notes?.let { notes ->

            when (filterType) {

                CurrentFilterType.ALL -> {
                    noteAdapter.submitList(notes)
                }

                CurrentFilterType.LIKED -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.isLiked
                    })
                }

                CurrentFilterType.ARTICLE -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.type == CurrentFilterType.ARTICLE.type
                    })
                }

                CurrentFilterType.LOCATION -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.type == CurrentFilterType.LOCATION.type
                    })
                }

                CurrentFilterType.VIDEO -> {
                    noteAdapter.submitList(notes.filter { note ->
                        note.type == CurrentFilterType.VIDEO.type
                    })
                }

            }
        }
    }

}
