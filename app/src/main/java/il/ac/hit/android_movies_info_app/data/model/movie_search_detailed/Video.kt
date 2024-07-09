package il.ac.hit.android_movies_info_app.data.model.movie_search_detailed

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("iso_639_1") val languageCode: String,
    @SerializedName("iso_3166_1") val countryCode: String,
    val name: String,
    val key: String,
    @SerializedName("published_at") val publishedAt: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val id: String
)
