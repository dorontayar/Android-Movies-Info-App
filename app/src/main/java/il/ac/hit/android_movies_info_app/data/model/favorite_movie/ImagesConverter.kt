package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.Images

class ImagesConverter {
    @TypeConverter
    fun fromImages(images: Images?): String? {
        if (images == null) {
            return null
        }
        val gson = Gson()
        val type = Images::class.java
        return gson.toJson(images, type)
    }

    @TypeConverter
    fun toImages(imagesString: String?): Images? {
        if (imagesString == null) {
            return null
        }
        val gson = Gson()
        val type = Images::class.java
        return gson.fromJson(imagesString, type)
    }
}
