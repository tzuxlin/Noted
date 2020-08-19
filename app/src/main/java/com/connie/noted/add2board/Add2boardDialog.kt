package com.connie.noted.add2board

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.connie.noted.MainActivity
import com.connie.noted.NaviDirections
import com.connie.noted.R
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.DialogAdd2boardBinding
import com.connie.noted.databinding.DialogTagBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.DialogBoxMessageType
import com.connie.noted.util.Logger
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class Add2boardDialog : DialogFragment() {


    private val viewModel by viewModels<Add2boardViewModel> { getVmFactory() }
    private lateinit var binding: DialogAdd2boardBinding
    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Style_Dialog)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel.liveNotes.value =
            Add2boardDialogArgs.fromBundle(requireArguments()).notesKey.toList()

        binding = DialogAdd2boardBinding.inflate(inflater, container, false)

        binding.layoutAdd2board.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.anim_slide_up)
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.iconAdd2boardPublicHelp.setOnClickListener {
            binding.textAdd2boardHelp.visibility = when (binding.textAdd2boardHelp.visibility) {
                View.GONE -> View.VISIBLE
                else -> View.GONE
            }
        }

        /**
         * Call [MainActivity]: [hideSoftInput] method to hide SoftInput when edit view is not on focus.
         */

        binding.editAdd2boardTitle.setOnFocusChangeListener { view: View, isFocus: Boolean ->
            if (!isFocus) {
                (activity as MainActivity).hideSoftInput(view)
            }
        }


        val noteRecyclerView = binding.noteRecyclerView

        noteRecyclerView.adapter = Add2boardAdapter()


        viewModel.status.observe(viewLifecycleOwner, Observer {

            it?.let { status ->

                when (status) {

                    LoadApiStatus.DONE -> {
                        findNavController().navigate(
                            NaviDirections.actionGlobalBoxDialog(DialogBoxMessageType.NEW_BOARD.message)
                        )
                        viewModel.leave()
                    }

                    LoadApiStatus.ERROR -> {

                        Logger.w("Add2board load API error: ${viewModel.error}")

                        findNavController().navigate(
                            NaviDirections.actionGlobalBoxDialog(DialogBoxMessageType.ERROR.message)
                        )
                        viewModel.leave()
                    }

                    else -> {

                    }

                }
            }

        })


        viewModel.leave.observe(viewLifecycleOwner, Observer {

            it?.let {
                dismiss()
                viewModel.onLeaveCompleted()
            }

        })

        viewModel.title.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    viewModel.restoreInvalidInput()
                }
            }
        })

        chipGroup = binding.groupAdd2boardTag
        setUpTags()

        binding.buttonTagAdd2board.setOnClickListener {
            viewModel.newTag.value?.let{
                viewModel.tags.add(it)
                setUpTags()
                viewModel.newTag.value = null
            }
        }


        return binding.root
    }


    override fun dismiss() {
        binding.layoutAdd2board.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_down
            )
        )
        Handler().postDelayed({ super.dismiss() }, 500)
    }


    fun leave() {
        dismiss()
    }

    private fun setUpTags() {

        chipGroup.removeAllViews()

        val tags = viewModel.tags

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
                    viewModel.tags.remove(tagName)
                }


            }
            chipGroup.addView(chip)


        }


    }


}
