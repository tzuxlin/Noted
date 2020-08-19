package com.connie.noted.notepage.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.connie.noted.databinding.FragmentNoteVideoBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.notepage.article.ArticleFragmentArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class VideoFragment : Fragment() {

    private val viewModel by viewModels<VideoViewModel> {
        getVmFactory(
            ArticleFragmentArgs.fromBundle(
                requireArguments()
            ).noteKey
        )
    }

    lateinit var binding: FragmentNoteVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNoteVideoBinding.inflate(inflater, container, false)

        viewModel.note.value = viewModel.noteKey

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val youtubeView = binding.noteYoutubeSupportFragment

        youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(viewModel.getYoutubeId(), 0f)
                youTubePlayer.play()
            }
        })

        lifecycle.addObserver(youtubeView)


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