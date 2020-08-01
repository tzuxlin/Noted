package com.connie.noted.add2board

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.connie.noted.R
import com.connie.noted.databinding.DialogAdd2boardBinding
import com.connie.noted.ext.getVmFactory


/**
 * Created by Wayne Chen in Jul. 2019.
 */
class Add2boardDialog : DialogFragment() {

    /**
     * Lazily initialize our [Add2boardDialog].
     */
    private val viewModel by viewModels<Add2boardViewModel> { getVmFactory() }
    private lateinit var binding: DialogAdd2boardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Style_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.liveNotes.value = Add2boardDialogArgs.fromBundle(requireArguments()).notesKey.toList()

        binding = DialogAdd2boardBinding.inflate(inflater, container, false)
        binding.layoutAdd2board.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.iconAdd2boardPublicHelp.setOnClickListener {
            binding.textAdd2boardHelp.visibility = View.VISIBLE
        }



        val noteRecyclerView = binding.noteRecyclerView


        noteRecyclerView.adapter = Add2boardAdapter()

        viewModel.toUploadBoard.observe(viewLifecycleOwner, Observer {
            val isPublicSwitch = binding.switchAdd2boardPublic

            viewModel.isPublic = isPublicSwitch.isChecked
            viewModel.uploadBoard()

        })

        viewModel.liveNotes.observe(viewLifecycleOwner, Observer {
            it?.let{
                (noteRecyclerView.adapter as Add2boardAdapter).submitList(it)
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

    override fun dismiss() {
        binding.layoutAdd2board.startAnimation(
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
