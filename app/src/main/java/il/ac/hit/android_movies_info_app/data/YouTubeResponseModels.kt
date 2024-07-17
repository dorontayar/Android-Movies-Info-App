package il.ac.hit.android_movies_info_app.data


data class YouTubeResponse(
    val items: List<YouTubeVideo>
)

data class YouTubeVideo(
    val id: VideoId
)

data class VideoId(
    val videoId: String
)
