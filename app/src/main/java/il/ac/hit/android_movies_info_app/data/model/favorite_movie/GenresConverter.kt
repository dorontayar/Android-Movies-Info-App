package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.Genre

class GenresConverter {
    @TypeConverter
    fun fromGenresList(genres: List<Genre>?): String? {
        if (genres == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(genres, type)
    }

    @TypeConverter
    fun toGenresList(genresString: String?): List<Genre>? {
        if (genresString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genresString, type)
    }
}
