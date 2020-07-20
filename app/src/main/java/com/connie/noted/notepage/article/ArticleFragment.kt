package com.connie.noted.notepage.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        if (viewModel.noteKey.images.isEmpty()){
            viewModel.noteKey.images.add("")
            binding.imageNote.visibility = View.GONE
        }

        viewModel.note.value = viewModel.noteKey

        viewModel.navigateToUrl.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it) {
                    val uri = Uri.parse(viewModel.noteKey.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)

                    viewModel.onUrlIntentCompleted()
                }
            }
        })



        return binding.root
    }

}