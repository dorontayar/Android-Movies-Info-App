package il.ac.hit.android_movies_info_app.data.remote_db

import il.ac.hit.android_movies_info_app.data.model.movie_search.MovieResponse
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse
import il.ac.hit.android_movies_info_app.data.model.top_rated_movies.TopRatedMovieResponse
import il.ac.hit.android_movies_info_app.data.model.upcoming_movies.UpcomingMovieResponse
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    // Search movies by name
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String
    ): Response<MovieResponse>
    // Search movies with pages
    @GET("search/movie")
    suspend fun searchMovieScrolling(
        @Query("query") query: String,
        @Query("page") page:Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String
    ): Response<MovieResponse>

    // Get details of a movie by its ID with videos and images
    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") appendToResponse: String = "videos,images",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String,
        @Query("include_image_language") includeImageLanguage: String = "en"
    ): Response<MovieDetailsResponse>

    // Get top rated movies
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String
    ): Response<TopRatedMovieResponse>

    // Get upcoming movies
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String
    ): Response<UpcomingMovieResponse>

    @GET("discover/movie")
    suspend fun getUpcomingMoviesByDateRange(
        @Query("primary_release_date.gte") startDate: String,
        @Query("primary_release_date.lte") endDate: String,
        @Query("sort_by") sortBy: String = "release_date.dsc",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String
    ): Response<UpcomingMovieResponse>


}