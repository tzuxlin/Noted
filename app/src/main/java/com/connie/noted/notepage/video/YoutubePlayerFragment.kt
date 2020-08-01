package com.connie.noted.notepage.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.connie.noted.R
import com.google.android.youtube.player.YouTubePlayerFragment

class YoutubePlayerFragment : YouTubePlayerFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_youtube_player, container, false)
    }

}