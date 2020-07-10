package com.connie.noted.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.connie.noted.R
import com.connie.noted.databinding.FragmentNoteBinding
import com.connie.noted.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel




        binding.viewpagerProfile.let {
            binding.tabsProfile.setupWithViewPager(it)
            it.adapter = ProfileAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsProfile))
        }



        return binding.root
    }


}
