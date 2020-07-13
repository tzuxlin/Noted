package com.connie.noted.board.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.connie.noted.R
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.databinding.FragmentBoardBinding

class BoardItemFragment(private val boardType: BoardTypeFilter): Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBoardBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
//        binding.viewModel = viewModel


        return binding.root
    }


}
