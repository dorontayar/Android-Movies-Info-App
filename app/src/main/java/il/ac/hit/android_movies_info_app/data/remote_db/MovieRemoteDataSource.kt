package il.ac.hit.android_movies_info_app.data.remote_db

import il.ac.hit.android_movies_info_app.data.model.movie_search.Movie
import il.ac.hit.android_movies_info_app.data.model.movie_search.MovieResponse
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.API_KEY
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteDataSource @Inject constructor(
    private val movieService:MovieService
) :BaseDataSource()
{
    suspend fun searchMovie(query: String) = getResult { movieService.searchMovie(query) }
    suspend fun getMovieDetails(id: Int) = getResult { movieService.getMovieDetails(id) }
    suspend fun getTopRatedMovies() = getResult { movieService.getTopRatedMovies() }

}