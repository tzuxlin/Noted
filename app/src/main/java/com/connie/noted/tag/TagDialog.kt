package com.connie.noted.tag

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.DialogTagBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.login.UserManager
import com.connie.noted.util.DialogBoxMessageType
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


/**
 * Created by Wayne Chen in Jul. 2019.
 */
class TagDialog : DialogFragment() {

    /**
     * Lazily initialize our [TagDialog].
     */
    private val viewModel by viewModels<TagViewModel> { getVmFactory() }

    private lateinit var mainViewModel: MainViewModel


    private lateinit var binding: DialogTagBinding
    private lateinit var chipGroup: ChipGroup
    private lateinit var switchButton: CompoundButton

    private lateinit var inputMethodManager: InputMethodManager


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
        getUserTags()

        inputMethodManager =
            NotedApplication.instance.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        binding.editTagAdd2tag.setOnFocusChangeListener { view: View, isFocus: Boolean ->
            if (!isFocus) {
                (activity as MainActivity).hideSoftInput(view)
            }
        }

        switchButton = binding.switchTagEdit

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.editTagAdd2tag.visibility = View.VISIBLE
                binding.buttonTagAdd2tag.visibility = View.VISIBLE
                binding.buttonBottom.text = "確認修改"
                binding.switchTagEdit.visibility = View.GONE
                binding.buttonBottomCancel.visibility = View.VISIBLE

                binding.buttonBottom.setOnClickListener {
                    viewModel.updateTags()
                }

                viewModel.inTagEditMode = true

            } else {
                binding.buttonBottom.setOnClickListener {
                    viewModel.leave()
                }
                viewModel.inTagEditMode = false
            }
        }


        binding.buttonBottomCancel.setOnClickListener {
            binding.editTagAdd2tag.visibility = View.GONE
            binding.buttonTagAdd2tag.visibility = View.GONE
            binding.buttonBottom.text = "我瞭解了"
            binding.switchTagEdit.visibility = View.VISIBLE
            binding.buttonBottomCancel.visibility = View.GONE
            binding.switchTagEdit.isChecked = false

            chipGroup.removeAllViews()
            getUserTags()
        }
        binding.buttonTagAdd2tag.setOnClickListener {
            setUpTags(mutableListOf(viewModel.newTag.value ?: "Hello"))
            viewModel.newTag.value = null
        }

        viewModel.status.observe(viewLifecycleOwner, Observer {

            when (it) {
                LoadApiStatus.LOADING -> {

                    binding.editTagAdd2tag.visibility = View.GONE
                    binding.buttonTagAdd2tag.visibility = View.GONE
                    binding.buttonBottom.text = ""
                    binding.buttonBottom.isActivated = false
                    binding.switchTagEdit.visibility = View.VISIBLE
                    binding.buttonBottomCancel.visibility = View.GONE
                    binding.switchTagEdit.isChecked = false


                }
                LoadApiStatus.DONE -> {

                    mainViewModel.syncUserData()

                    findNavController().navigate(
                        NaviDirections.actionGlobalBoxDialog(
                            DialogBoxMessageType.EDITED.message
                        )
                    )

                    binding.editTagAdd2tag.visibility = View.GONE
                    binding.buttonTagAdd2tag.visibility = View.GONE
                    binding.buttonBottom.text = "關閉"
                    binding.switchTagEdit.visibility = View.VISIBLE
                    binding.buttonBottomCancel.visibility = View.GONE
                    binding.switchTagEdit.isChecked = false

                    chipGroup.removeAllViews()
                    getUserTags()

                }
            }
        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.onLeaveCompleted()
            }
        })

        checkTags()

        return binding.root
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

    private fun getUserTags() {

        UserManager.user.value?.followingTags?.let {

            if (it.isNotEmpty()) {
                setUpTags(it)
            }

        }
    }


    private fun setUpTags(tagList: MutableList<String>) {

        viewModel.tagsToAdd.addAll(tagList)

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


            chip.setOnClickListener {

                chip.isCloseIconEnabled = !chip.isCloseIconEnabled

                //Added click listener on close icon to remove tag from ChipGroup
                chip.setOnCloseIconClickListener {
                    tagList.remove(tagName)
                    chipGroup.removeView(chip)
                    viewModel.tagsToAdd.remove(tagName)
                    Log.e("Connie", tagList.toString())
                }

                if (!viewModel.inTagEditMode) {
                    switchButton.isChecked = true
                }

            }
            chipGroup.addView(chip)

        }


    }

    private fun checkTags() {

        val followingTags = (UserManager.user.value?.followingTags) ?: listOf<String?>()

        if (followingTags.isEmpty()) {
            switchButton.isChecked = true
        }
    }

}
