package il.ac.hit.android_movies_info_app.data.model.movie_search

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreIdsConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return gson.toJson(list)
    }
}
