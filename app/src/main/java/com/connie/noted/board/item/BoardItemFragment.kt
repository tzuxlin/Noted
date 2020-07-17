package com.connie.noted.board.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connie.noted.NotedApplication

import com.connie.noted.R
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.databinding.FragmentBoardBinding
import com.connie.noted.databinding.FragmentBoardItemBinding
import com.connie.noted.databinding.ItemBoardBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.note.NoteAdapter
import com.connie.noted.note.NoteViewModel
import kotlinx.android.synthetic.main.fragment_board_item.*

class BoardItemFragment(private val boardType: BoardTypeFilter): Fragment() {

    private val viewModel by viewModels<BoardItemViewModel> { getVmFactory(boardType) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBoardItemBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val boardRecyclerView = binding.boardRecyclerView


        viewModel.viewType.observe(viewLifecycleOwner, Observer {

            when (it) {

                0 -> {
                    boardRecyclerView.adapter = BoardItemAdapter(BoardItemAdapter.OnClickListener{}, viewModel)
                    boardRecyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)

                    viewModel.liveBoards.value = viewModel.liveBoards.value

                }

                1 -> {
                    boardRecyclerView.adapter = BoardItemAdapter(BoardItemAdapter.OnClickListener{}, viewModel)
                    boardRecyclerView.layoutManager =
                        LinearLayoutManager(NotedApplication.instance.applicationContext)

                    viewModel.liveBoards.value = viewModel.liveBoards.value

                }

            }

        })

        binding.boardIconChangeLayout.setOnClickListener {

            when (viewModel.viewType.value) {
                0 -> viewModel.viewType.value = 1
                1 -> viewModel.viewType.value = 0
            }

        }



        return binding.root
    }


}
