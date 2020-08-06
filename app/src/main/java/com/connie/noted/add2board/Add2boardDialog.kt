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
import androidx.navigation.fragment.findNavController
import com.connie.noted.NaviDirections
import com.connie.noted.R
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.DialogAdd2boardBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.DialogBoxMessageType
import com.connie.noted.util.Logger


class Add2boardDialog : DialogFragment() {


    private val viewModel by viewModels<Add2boardViewModel> { getVmFactory() }
    private lateinit var binding: DialogAdd2boardBinding


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
            binding.textAdd2boardHelp.visibility = View.VISIBLE
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


}
