package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.ProductionCompany

class ProductionCompanyConverter {
    @TypeConverter
    fun fromProductionCompany(company: List<ProductionCompany>?): String? {
        if (company == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCompany>>() {}.type
        return gson.toJson(company, type)
    }

    @TypeConverter
    fun toProductionCompany(companyString: String?): List<ProductionCompany>? {
        if (companyString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCompany>>() {}.type
        return gson.fromJson(companyString, type)
    }
}
