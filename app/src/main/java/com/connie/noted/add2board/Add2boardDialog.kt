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
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Add2BoardDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAdd2boardBinding.inflate(inflater, container, false)
        binding.layoutAdd2cart.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
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
