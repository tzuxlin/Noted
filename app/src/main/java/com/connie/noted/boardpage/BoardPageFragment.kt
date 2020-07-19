package com.connie.noted.boardpage

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
import com.connie.noted.NaviDirections
import com.connie.noted.NotedApplication
import com.connie.noted.databinding.FragmentBoardPageBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.note.NoteAdapter

class BoardPageFragment : Fragment() {

    private val viewModel by viewModels<BoardPageViewModel> {
        getVmFactory(
            BoardPageFragmentArgs.fromBundle(
                requireArguments()
            ).boardKey
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBoardPageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val recyclerView = binding.boardPageRecyclerView

        recyclerView.adapter = BoardNotesAdapter(BoardNotesAdapter.OnClickListener { note ->

            Log.i("Connie", "Note is clicked, $note")
            findNavController().navigate(
                NaviDirections.actionGlobalNotePageFragment(
                    note
                )
            )

        }, viewModel)
        recyclerView.layoutManager =
            LinearLayoutManager(NotedApplication.instance.applicationContext)


        viewModel.board.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e("Connie", it.toString())
            }
        })


        return binding.root
    }
}