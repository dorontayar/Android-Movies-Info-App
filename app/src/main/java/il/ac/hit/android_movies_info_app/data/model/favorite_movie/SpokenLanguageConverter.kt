package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.SpokenLanguage

class SpokenLanguageConverter {
    @TypeConverter
    fun fromSpokenLanguage(language: List<SpokenLanguage>?): String? {
        if (language == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<SpokenLanguage>>() {}.type
        return gson.toJson(language, type)
    }

    @TypeConverter
    fun toSpokenLanguage(languageString: String?): List<SpokenLanguage>? {
        if (languageString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<SpokenLanguage>>() {}.type
        return gson.fromJson(languageString, type)
    }
}
