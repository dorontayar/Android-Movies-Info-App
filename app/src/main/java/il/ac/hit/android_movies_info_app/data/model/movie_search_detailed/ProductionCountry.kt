package il.ac.hit.android_movies_info_app.data.model.movie_search_detailed

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1") val countryCode: String,
    val name: String
)
