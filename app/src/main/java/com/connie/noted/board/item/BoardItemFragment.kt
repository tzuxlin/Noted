package com.connie.noted.board.item

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
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connie.noted.MainViewModel
import com.connie.noted.NaviDirections
import com.connie.noted.NotedApplication
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.databinding.FragmentBoardItemBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.CurrentFilterType
import com.connie.noted.util.Logger
import com.connie.noted.util.Util.setUpThinTags
import com.google.android.material.chip.ChipGroup

class BoardItemFragment(private val boardType: BoardTypeFilter) : Fragment() {

    private val viewModel by viewModels<BoardItemViewModel> { getVmFactory(boardType) }
    private lateinit var mainViewModel: MainViewModel


    lateinit var chipGroup: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBoardItemBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        val boardRecyclerView = binding.boardRecyclerView
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        /**
         * Observe  [CurrentFilterType] changes from [MainViewModel],
         * and call [checkFilterType] to submit the appropriate board list.
         */

        mainViewModel.currentFilterType.observe(viewLifecycleOwner, Observer {

            it?.let { type ->
                boardRecyclerView.adapter?.let { adapter ->
                    checkFilterType(type, adapter as BoardItemAdapter)
                }
            }

        })


        /**
         * Observe current viewType from [MainViewModel],
         * and setup the appropriate LayoutManager to RecyclerView.
         */

        mainViewModel.viewType.observe(viewLifecycleOwner, Observer {

            viewModel.viewType.value = it

            boardRecyclerView.adapter =
                BoardItemAdapter(BoardItemAdapter.OnClickListener { board ->
                    findNavController().navigate(NaviDirections.actionGlobalBoardPageFragment(board))
                }, viewModel)

            when (it) {

                0 -> {
                    boardRecyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
                    viewModel.liveBoards.value = viewModel.liveBoards.value
                }

                1 -> {
                    boardRecyclerView.layoutManager = LinearLayoutManager(NotedApplication.instance)
                    viewModel.liveBoards.value = viewModel.liveBoards.value
                }

            }

        })

        chipGroup = binding.groupBoardTag


        viewModel.liveBoards.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.loadApiStatusDone()
            }
        })


        return binding.root
    }

    private fun checkFilterType(filterType: CurrentFilterType, boardAdapter: BoardItemAdapter) {

        filterType.let { type ->

            when (type) {

                CurrentFilterType.LIKED -> {
                    boardAdapter.submitList(viewModel.liveBoards.value?.filter { board ->
                        board.isLiked
                    })
                }

                else -> {
                    viewModel.liveBoards.value = viewModel.liveBoards.value
                }

            }
        }
    }


}
