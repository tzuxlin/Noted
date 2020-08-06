package com.connie.noted.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.connie.noted.MainViewModel
import com.connie.noted.NaviDirections
import com.connie.noted.databinding.FragmentProfileBinding
import com.connie.noted.util.Logger
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var mainViewModel: MainViewModel

    private lateinit var chipGroup: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        chipGroup = binding.groupProfileTag

        binding.buttonProfileAddTag.setOnClickListener {
            findNavController().navigate(NaviDirections.actionGlobalTagDialog())
        }

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        mainViewModel.userIsSynced.observe(viewLifecycleOwner, Observer { isSynced ->
            isSynced?.let {

                if (isSynced) {

                    mainViewModel.user.observe(viewLifecycleOwner, Observer {
                        it?.let { user ->
                            viewModel.user.value = user
                            mainViewModel.onSyncUserDataFinished()
                            resetTags()
                            Logger.d(
                                "ProfileFragment, viewModel.user = ${viewModel.user.value}"
                            )

                        }

                    })


                }

            }
        })

        return binding.root
    }

    private fun resetTags() {
        chipGroup.removeAllViews()
        getUserTags()
    }

    private fun getUserTags() {

        viewModel.user.value?.followingTags?.let {

            if (it.isNotEmpty()) {
                setUpTags(it)
            }

        }
    }


    private fun setUpTags(tagList: MutableList<String>) {


        for (index in tagList.indices) {
            val tagName = tagList[index]
            val chip = Chip(chipGroup.context)
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                resources.displayMetrics
            ).toInt()
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            chip.text = tagName
            chip.textSize = 12f



            chip.setTextColor(Color.WHITE)

            val states = arrayOf(intArrayOf(-android.R.attr.state_checked))

            val chipColors = intArrayOf(Color.parseColor("#657153"))
            val chipColorsStateList = ColorStateList(states, chipColors)

            chip.chipBackgroundColor = chipColorsStateList
            chip.closeIconTint = ColorStateList(states, intArrayOf(Color.WHITE))

            chip.isClickable = false

            chipGroup.addView(chip)


        }


    }

}




