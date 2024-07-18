package il.ac.hit.android_movies_info_app.data.model.upcoming_movies

import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(
    val dates: Dates?,
    val page: Int,
    val results: List<UpcomingMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)