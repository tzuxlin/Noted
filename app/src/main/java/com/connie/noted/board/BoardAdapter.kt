package com.connie.noted.board

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.connie.noted.board.item.BoardItemFragment

class BoardAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = BoardTypeFilter.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return BoardTypeFilter.values()[position].value
    }

    override fun getItem(position: Int): Fragment {
        return BoardItemFragment(BoardTypeFilter.values()[position])
    }
}