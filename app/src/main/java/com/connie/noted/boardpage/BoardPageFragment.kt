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
import com.connie.noted.MainActivity
import com.connie.noted.NaviDirections
import com.connie.noted.NotedApplication
import com.connie.noted.databinding.FragmentBoardPageBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.DialogBoxMessageType

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

            (activity as MainActivity).navigateToNote(note)

        }, viewModel)
        recyclerView.layoutManager =
            LinearLayoutManager(NotedApplication.instance.applicationContext)


        viewModel.board.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e("Connie", it.toString())
            }
        })

        viewModel.savedCompleted.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it) {
                    if (viewModel.toSaved) {
                        findNavController().navigate(
                            NaviDirections.actionGlobalBoxDialog(
                                DialogBoxMessageType.SAVED_BOARD.message
                            )
                        )
                    } else {
                        findNavController().navigate(
                            NaviDirections.actionGlobalBoxDialog(
                                DialogBoxMessageType.UNSAVED_BOARD.message
                            )
                        )
                    }
                }
            }
        })


        return binding.root
    }
}