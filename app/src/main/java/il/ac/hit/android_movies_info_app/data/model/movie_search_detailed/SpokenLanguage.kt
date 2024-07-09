package il.ac.hit.android_movies_info_app.data.model.movie_search_detailed

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String,
    @SerializedName("iso_639_1") val languageCode: String,
    val name: String
)
