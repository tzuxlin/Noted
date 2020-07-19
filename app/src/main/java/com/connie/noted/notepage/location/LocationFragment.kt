package com.connie.noted.notepage.location

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.connie.noted.R
import com.connie.noted.databinding.FragmentNoteArticleBinding
import com.connie.noted.databinding.FragmentNoteLocationBinding
import com.connie.noted.databinding.FragmentNoteLocationBindingImpl
import com.connie.noted.ext.getVmFactory
import com.connie.noted.notepage.article.ArticleFragmentArgs
import com.connie.noted.notepage.article.ArticleViewModel

class LocationFragment : Fragment() {

    private val viewModel by viewModels<LocationViewModel> {
        getVmFactory(
            ArticleFragmentArgs.fromBundle(
                requireArguments()
            ).noteKey
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentNoteLocationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.note.value = viewModel.noteKey
        return binding.root
    }

}