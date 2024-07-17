package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.Videos

class VideosConverter {
    @TypeConverter
    fun fromVideos(videos: Videos?): String? {
        if (videos == null) {
            return null
        }
        val gson = Gson()
        val type = Videos::class.java
        return gson.toJson(videos, type)
    }

    @TypeConverter
    fun toVideos(videosString: String?): Videos? {
        if (videosString == null) {
            return null
        }
        val gson = Gson()
        val type = Videos::class.java
        return gson.fromJson(videosString, type)
    }
}
