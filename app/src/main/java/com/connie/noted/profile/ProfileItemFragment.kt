package com.connie.noted.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.connie.noted.R
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.databinding.*

class ProfileItemFragment(private val profileType: ProfileTypeFilter): Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = when (profileType) {
            ProfileTypeFilter.TAGS -> FragmentProfileFirstBinding.inflate(inflater, container, false)
            ProfileTypeFilter.ACHIEVEMENT -> FragmentProfileSecondBinding.inflate(inflater, container, false)
            else -> FragmentProfileThirdBinding.inflate(inflater, container, false)
        }
        binding.lifecycleOwner = this
//        binding.viewModel = viewModel


        return binding.root
    }


}
