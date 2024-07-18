package il.ac.hit.android_movies_info_app.data.model.top_rated_movies

import com.google.gson.annotations.SerializedName

data class TopRatedMovieResponse(
    val page: Int,
    val results: List<TopRatedMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)