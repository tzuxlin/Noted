package com.connie.noted.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ProfileAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = ProfileTypeFilter.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return ProfileTypeFilter.values()[position].value
    }

    override fun getItem(position: Int): Fragment {
        return ProfileItemFragment(ProfileTypeFilter.values()[position])
    }
}