package com.connie.noted.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.connie.noted.databinding.FragmentBoardBinding
import com.google.android.material.tabs.TabLayout

class BoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        FragmentBoardBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner

            viewpagerBoard.let {
                tabsBoard.setupWithViewPager(it)
                it.adapter = BoardAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsBoard))
            }

            return@onCreateView root

        }
    }

}
