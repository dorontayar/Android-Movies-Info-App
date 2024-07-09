package il.ac.hit.android_movies_info_app.data.model.movie_search_detailed

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("aspect_ratio") val aspectRatio: Double,
    val height: Int,
    @SerializedName("iso_639_1") val languageCode: String?,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    val width: Int
)
