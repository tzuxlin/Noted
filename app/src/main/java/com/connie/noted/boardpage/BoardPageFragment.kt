package com.connie.noted.boardpage

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connie.noted.MainActivity
import com.connie.noted.NaviDirections
import com.connie.noted.NotedApplication
import com.connie.noted.databinding.FragmentBoardPageBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.DialogBoxMessageType
import com.connie.noted.util.Logger
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class BoardPageFragment : Fragment() {

    private val viewModel by viewModels<BoardPageViewModel> {
        getVmFactory(
            BoardPageFragmentArgs.fromBundle(requireArguments()).boardKey
        )
    }
    private lateinit var chipGroup: ChipGroup


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBoardPageBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        val recyclerView = binding.boardPageRecyclerView

        recyclerView.adapter = BoardNotesAdapter(BoardNotesAdapter.OnClickListener { note ->
            (activity as MainActivity).navigateToNote(note)
        })

        recyclerView.layoutManager =
            LinearLayoutManager(NotedApplication.instance.applicationContext)


        viewModel.savedCompleted.observe(viewLifecycleOwner, Observer {
            it?.let { completed ->

                if (completed) {

                    if (viewModel.toSaved) {

                        findNavController().navigate(
                            NaviDirections.actionGlobalBoxDialog(DialogBoxMessageType.SAVED_BOARD.message)
                        )
                        viewModel.doneNavigate()

                    } else {

                        findNavController().navigate(
                            NaviDirections.actionGlobalBoxDialog(DialogBoxMessageType.UNSAVED_BOARD.message)
                        )
                        viewModel.doneNavigate()

                    }
                }

            }
        })

        chipGroup = binding.groupBoardPageTag

        viewModel.board.value?.let {
            setUpTags(it.tags)
        }

        return binding.root

    }


    private fun setUpTags(tagList: MutableList<String?>) {


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