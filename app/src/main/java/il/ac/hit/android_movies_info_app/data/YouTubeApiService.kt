package il.ac.hit.android_movies_info_app.data

import il.ac.hit.android_movies_info_app.utils.Constants.Companion.YT_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 1,
        @Query("key") apiKey: String = YT_API_KEY
    ): YouTubeResponse
}