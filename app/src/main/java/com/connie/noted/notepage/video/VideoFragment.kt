package com.connie.noted.notepage.video

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.connie.noted.databinding.FragmentNoteVideoBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.notepage.article.ArticleFragmentArgs
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment


class VideoFragment : Fragment() {

    private val youtubePlayerFragment: YoutubePlayerFragment = YoutubePlayerFragment()

    private val viewModel by viewModels<VideoViewModel> {
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




        Log.i("Connie", "Video Fragment")
        val binding = FragmentNoteVideoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.note.value = viewModel.noteKey




        youtubePlayerFragment.initialize("AIzaSyCI_y44jt8O0d9kgYzzv922sWAufrnh3vo", object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                Log.e("Connie", "YouTube Player onInitializationSuccess")


            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                TODO("Not yet implemented")
            }

            //            override fun onInitializationFailure(
//                provider: YouTubePlayer.Provider,
//                youTubeInitializationResult: YouTubeInitializationResult
//            ) {
//                Log.i("Detail", "Failed: $youTubeInitializationResult")
//                if (youTubeInitializationResult.isUserRecoverableError) {
//                    youTubeInitializationResult.getErrorDialog(
//                        getMainActivity(),
//                        RECOVERY_DIALOG_REQUEST
//                    ).show()
//                } else {
//                    callToast(youTubeInitializationResult.toString(), true)
//                }


        })








        return binding.root
    }


//    private fun initYouTube() {
//        if (youTubePlayerFragment == null) {
//            youTubePlayerFragment = getFragmentById(R.id.youtube_frag) as YouTubePlayerFragment
//            youTubePlayerFragment.initialize(
//                getString(R.string.api_key),
//                object : YouTubePlayer.OnInitializedListener {
//                    override fun onInitializationSuccess(
//                        provider: YouTubePlayer.Provider,
//                        player: YouTubePlayer,
//                        wasRestored: Boolean
//                    ) {
//                        Log.i("Detail", "YouTube Player onInitializationSuccess")
//
//                        // Don't do full screen
//                        player.setFullscreen(false)
//                        if (!wasRestored) {
//                            youTubePlayer = player
//                            cueVideoIfNeeded()
//                        }
//                    }
//
//                    override fun onInitializationFailure(
//                        provider: YouTubePlayer.Provider,
//                        youTubeInitializationResult: YouTubeInitializationResult
//                    ) {
//                        Log.i("Detail", "Failed: $youTubeInitializationResult")
//                        if (youTubeInitializationResult.isUserRecoverableError) {
//                            youTubeInitializationResult.getErrorDialog(
//                                getMainActivity(),
//                                RECOVERY_DIALOG_REQUEST
//                            ).show()
//                        } else {
//                            callToast(youTubeInitializationResult.toString(), true)
//                        }
//                    }
//                })
//        }
//    }
}