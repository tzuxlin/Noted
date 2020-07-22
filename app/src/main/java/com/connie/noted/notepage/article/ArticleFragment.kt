package com.connie.noted.notepage.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.connie.noted.BuildConfig
import com.connie.noted.databinding.FragmentNoteArticleBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.Util

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

        Log.i("Connie", "Article Fragment")

        val binding = FragmentNoteArticleBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (viewModel.noteKey.images.isEmpty()){
            viewModel.noteKey.images.add("")
            binding.imageNote.visibility = View.GONE
        }

        if (viewModel.noteKey.type == "Location"){

            val googleKey = BuildConfig.GOOGLE_KEY
            val width = Util.getWindowWidth()
            val zoomSize = 20

            binding.imageString = "https://maps.googleapis.com/maps/api/staticmap?center=${viewModel.noteKey.title}&zoom=${zoomSize}&size=${width}x700&key=$googleKey"

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