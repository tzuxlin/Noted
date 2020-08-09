package com.connie.noted.tag

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.connie.noted.*
import com.connie.noted.data.Note
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.DialogTagBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.login.UserManager
import com.connie.noted.util.DialogBoxMessageType
import com.connie.noted.util.Logger
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class TagDialog : DialogFragment() {


    private val viewModel by viewModels<TagViewModel> { getVmFactory() }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: DialogTagBinding
    private lateinit var chipGroup: ChipGroup
    private lateinit var switchButton: CompoundButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Style_Dialog_resize)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogTagBinding.inflate(inflater, container, false)
        binding.layoutTag.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        chipGroup = binding.groupProfileTag
        switchButton = binding.switchTagEdit




        /**
         * Call [MainActivity]: [hideSoftInput] method to hide SoftInput when edit view is not on focus.
         */

        binding.editTagAdd2tag.setOnFocusChangeListener { view: View, isFocus: Boolean ->
            if (!isFocus) {
                (activity as MainActivity).hideSoftInput(view)
            }
        }


        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                binding.buttonBottom.setOnClickListener {
                    viewModel.updateTags()
                }
                viewModel.setEditMode(true)

//                viewModel.inTagEditMode = true

            } else {
                binding.buttonBottom.setOnClickListener {
                    viewModel.leave()
                }
                viewModel.setEditMode(false)
//                viewModel.inTagEditMode = false
            }
        }

        /**
         * Call [getUserTags] method to sync following tags with [UserManager].
         */

        getUserTags()


        binding.buttonBottomCancel.setOnClickListener {

                binding.switchTagEdit.isChecked = false

                chipGroup.removeAllViews()
                getUserTags()

                viewModel.leave()


        }

        binding.buttonTagAdd2tag.setOnClickListener {
            viewModel.newTag.value?.let {
                if (it.isNotEmpty()){
                addTagsAndSetUp(mutableListOf(it))
                viewModel.newTag.value = null
            }}
        }


        viewModel.status.observe(viewLifecycleOwner, Observer {

            when (it) {
                LoadApiStatus.LOADING -> {

                    binding.switchTagEdit.isChecked = false

                }

                /**
                 * [LoadApiStatus.DONE] -> Call [MainViewModel] to sync the latest user data.
                 */

                LoadApiStatus.DONE -> {

                    mainViewModel.syncUserData()

                    findNavController().navigate(
                        NaviDirections.actionGlobalBoxDialog(DialogBoxMessageType.EDITED.message)
                    )

                    binding.switchTagEdit.isChecked = false
                    resetTags()
                    viewModel.restoreStatus()

                }
            }
        })



        mainViewModel.userIsSynced.observe(viewLifecycleOwner, Observer { isSynced ->
            isSynced?.let {

                if (isSynced) {

                    mainViewModel.user.observe(viewLifecycleOwner, Observer {
                        it?.let { user ->

                            viewModel.followingTags = user.followingTags ?: mutableListOf()
                            mainViewModel.onSyncUserDataFinished()

                        }

                    })
                }
            }
        })





        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.onLeaveCompleted()
            }
        })


        return binding.root
    }


    private fun getUserTags() {

        UserManager.user.value?.followingTags?.let {

            viewModel.followingTags = it

            if (it.isEmpty()) {
                switchButton.isChecked = true
            } else {
                setUpTags()
            }


        }
    }

    private fun addTagsAndSetUp(tagList: MutableList<String>) {
        viewModel.followingTags.addAll(tagList)
        setUpTags()
    }


    private fun setUpTags() {

        chipGroup.removeAllViews()

        val tags = viewModel.followingTags

        for (index in tags.indices) {
            val tagName = tags[index]
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


            chip.setOnClickListener {

                chip.isCloseIconEnabled = !chip.isCloseIconEnabled

                //Added click listener on close icon to remove tag from ChipGroup
                chip.setOnCloseIconClickListener {
                    tags.remove(tagName)
                    chipGroup.removeView(chip)
                    viewModel.followingTags.remove(tagName)
                }

                if (viewModel.editMode.value != true) {
                    switchButton.isChecked = true
                }

            }
            chipGroup.addView(chip)


        }


    }

    private fun resetTags() {
        chipGroup.removeAllViews()
        getUserTags()
    }

    override fun dismiss() {
        binding.layoutTag.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_down
            )
        )
        Handler().postDelayed({ super.dismiss() }, 200)
    }

    fun leave() {
        dismiss()
    }

}
