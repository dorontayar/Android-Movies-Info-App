package il.ac.hit.android_movies_info_app.data.model.movie_search_detailed

import com.google.gson.annotations.SerializedName

data class BelongsToCollection(
    val id: Int,
    val name: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?
)
