package com.connie.noted.add2board

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.databinding.DialogAdd2boardBinding
import com.connie.noted.ext.getVmFactory


/**
 * Created by Wayne Chen in Jul. 2019.
 */
class Add2boardDialog : DialogFragment() {

    /**
     * Lazily initialize our [Add2cartViewModel].
     */
    private val viewModel by viewModels<Add2boardViewModel> { getVmFactory() }
    private lateinit var binding: DialogAdd2boardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Add2BoardDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.liveNotes.value = Add2boardDialogArgs.fromBundle(requireArguments()).notesKey.toList()

        binding = DialogAdd2boardBinding.inflate(inflater, container, false)
        binding.layoutAdd2cart.startAnimation(
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


//        binding.buttonAdd2cartClose.setTouchDelegate()
//        binding.recyclerAdd2cartColorSelector.adapter = Add2cartColorAdapter(viewModel)
//
//        binding.iconSizeRecommend.setOnClickListener {
//            findNavController().navigate(NavigationDirections.actionGlobalSizeDialog())
//
//            viewModel.traceSize()
//
//
//        }

//        viewModel.navigateToAddedSuccess.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                findNavController().navigate(
//                    NavigationDirections.navigateToMessageDialog(
//                        MessageDialog.MessageType.ADDED_SUCCESS
//                    )
//                )
//                viewModel.onAddedSuccessNavigated()
//            }
//        })
//
//        viewModel.navigateToAddedFail.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                findNavController().navigate(NavigationDirections.navigateToMessageDialog(
//                    MessageDialog.MessageType.MESSAGE.apply {
//                        value.message = getString(R.string.product_exist)
//                    }
//                ))
//                viewModel.onAddedFailNavigated()
//            }
//        })



        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.onLeaveCompleted()
            }
        })



        return binding.root
    }

    override fun dismiss() {
        binding.layoutAdd2cart.startAnimation(
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
