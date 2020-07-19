package com.connie.noted.notepage.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.connie.noted.databinding.FragmentNoteArticleBinding
import com.connie.noted.ext.getVmFactory

class ArticleFragment : Fragment() {

    private val viewModel by viewModels<ArticleViewModel> {
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


        val binding = FragmentNoteArticleBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.note.value = viewModel.noteKey
        return binding.root
    }

}