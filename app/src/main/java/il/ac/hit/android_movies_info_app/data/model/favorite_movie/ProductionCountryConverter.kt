package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.ProductionCountry

class ProductionCountryConverter {
    @TypeConverter
    fun fromProductionCountry(country: List<ProductionCountry>?): String? {
        if (country == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCountry>>() {}.type
        return gson.toJson(country, type)
    }

    @TypeConverter
    fun toProductionCountry(countryString: String?): List<ProductionCountry>? {
        if (countryString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCountry>>() {}.type
        return gson.fromJson(countryString, type)
    }
}
