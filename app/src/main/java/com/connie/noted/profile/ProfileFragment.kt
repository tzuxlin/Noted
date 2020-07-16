package com.connie.noted.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.databinding.FragmentProfileBinding
import com.connie.noted.login.UserManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
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


        Log.e(
            "Connie", "Profile, User = ${UserManager.user.value}"
        )

        binding.viewpagerProfile.let {
            binding.tabsProfile.setupWithViewPager(it)
            it.adapter = ProfileAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsProfile))
        }



        getUserTags()


        return binding.root
    }

    private fun getUserTags() {

        UserManager.user.value?.followingTags?.let {

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




            chip.setOnClickListener {
                chip.isCloseIconEnabled = !chip.isCloseIconEnabled


                //Added click listener on close icon to remove tag from ChipGroup
                chip.setOnCloseIconClickListener {
                    tagList.remove(tagName)
                    chipGroup.removeView(chip)
                }

            }


            chipGroup.addView(chip)


        }


    }

}




