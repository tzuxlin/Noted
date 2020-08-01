package com.connie.noted.box

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.R
import com.connie.noted.boardpage.BoardPageFragmentArgs
import com.connie.noted.databinding.DialogBoxBinding
import com.connie.noted.util.DialogBoxMessageType
import java.util.*
import kotlin.concurrent.schedule

class BoxDialog : DialogFragment() {

    private lateinit var message: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DialogBoxBinding.inflate(inflater, container, false)


        message = BoxDialogArgs.fromBundle(requireArguments()).messageKey


        when (message) {

            DialogBoxMessageType.LOADING_NOTE.message -> {
                binding.boxMessage.visibility = View.GONE
                binding.iconSuccessWhite.visibility = View.GONE
                binding.animLoading.visibility = View.VISIBLE
            }

            else -> {
                binding.boxMessage.text = message
            }

        }





        return binding.root
    }


    override fun onResume() {
        super.onResume()



        Timer("SettingUp", false).schedule(2000) {
            close()
        }


    }

    private fun close() {
        when (message) {

            DialogBoxMessageType.LOADING_NOTE.message -> {

            }

            else -> {
                this.dismiss()
            }
        }
    }
}

