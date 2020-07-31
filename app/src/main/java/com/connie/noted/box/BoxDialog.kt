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
import java.util.*
import kotlin.concurrent.schedule

class BoxDialog : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DialogBoxBinding.inflate(inflater, container, false)

        val message = BoxDialogArgs.fromBundle(requireArguments()).messageKey

        binding.boxMessage.text = message


        return binding.root
    }


    override fun onResume() {
        super.onResume()
        Timer("SettingUp", false).schedule(2000) {
            close()
        }
    }

    private fun close() {
        this.dismiss()
    }
}

