package com.connie.noted.notepage

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.R
import com.connie.noted.data.Note
import com.connie.noted.databinding.FragmentNoteArticleBinding
import com.connie.noted.databinding.FragmentNoteLocationBinding
import com.connie.noted.databinding.FragmentNoteVideoBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.note.NoteViewModel

class NotePageFragment() : Fragment() {

    private val viewModel by viewModels<NotePageViewModel> {
        getVmFactory(
            NotePageFragmentArgs.fromBundle(
                requireArguments()
            ).noteKey
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        when (viewModel.noteKey.type) {
            "Location" -> {
                val binding = FragmentNoteLocationBinding.inflate(inflater, container, false)
                binding.lifecycleOwner = this@NotePageFragment
                binding.viewModel = viewModel

                viewModel.note.value = viewModel.noteKey
                return binding.root
            }

            "Video" -> {
                val binding = FragmentNoteVideoBinding.inflate(inflater, container, false)
                binding.lifecycleOwner = this@NotePageFragment
                binding.viewModel = viewModel

                viewModel.note.value = viewModel.noteKey
                return binding.root
            }
            else -> {
                val binding = FragmentNoteArticleBinding.inflate(inflater, container, false)
                binding.lifecycleOwner = this@NotePageFragment
                binding.viewModel = viewModel

                viewModel.note.value = viewModel.noteKey
                return binding.root
            }
        }

    }

}