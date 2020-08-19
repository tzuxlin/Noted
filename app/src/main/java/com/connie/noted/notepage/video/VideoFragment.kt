package com.connie.noted.notepage.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.connie.noted.BuildConfig
import com.connie.noted.databinding.FragmentNoteVideoBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.notepage.article.ArticleFragmentArgs
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerSupportFragment
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
            }
        }

        )


        lifecycle.addObserver(youtubeView)


        return binding.root
    }


}