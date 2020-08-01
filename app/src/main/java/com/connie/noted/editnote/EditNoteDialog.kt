package com.connie.noted.editnote

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
import com.connie.noted.databinding.DialogEditNoteBinding
import com.connie.noted.ext.getVmFactory


/**
 * Created by Wayne Chen in Jul. 2019.
 */
class EditNoteDialog : DialogFragment() {

    /**
     * Lazily initialize our [EditNoteViewModel].
     */
    private val viewModel by viewModels<EditNoteViewModel> { getVmFactory() }
    private lateinit var binding: DialogEditNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Style_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.note.value = EditNoteDialogArgs.fromBundle(requireArguments()).noteKey
        viewModel.originTitle.value = EditNoteDialogArgs.fromBundle(requireArguments()).noteKey.title

        binding = DialogEditNoteBinding.inflate(inflater, container, false)
        binding.layoutEditNote.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

//        binding.iconAdd2boardPublicHelp.setOnClickListener {
//            binding.textAdd2boardHelp.visibility = View.VISIBLE
//        }


        viewModel.toUpdateNote.observe(viewLifecycleOwner, Observer {
            viewModel.updateNote()
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
        binding.layoutEditNote.startAnimation(
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
