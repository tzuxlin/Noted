import com.connie.noted.BuildConfig
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment


class YoutubePlayerFragment : YouTubePlayerFragment() {


    private val googleKey = BuildConfig.GOOGLE_KEY
    var videoId: String? = null

    init {

        initialize(googleKey, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationFailure(
                arg0: YouTubePlayer.Provider?,
                arg1: YouTubeInitializationResult?
            ) {


            }

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {

                        player.setFullscreen(false)
                        player.loadVideo(videoId)
                        player.play()

                }
            }
        })
    }

}