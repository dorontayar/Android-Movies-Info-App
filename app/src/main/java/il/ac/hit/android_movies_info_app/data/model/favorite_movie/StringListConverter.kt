package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        return string?.split(",")?.map { it.trim() }
    }
}
