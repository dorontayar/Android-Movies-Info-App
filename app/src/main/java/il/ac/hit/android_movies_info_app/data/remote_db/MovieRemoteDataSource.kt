package il.ac.hit.android_movies_info_app.data.remote_db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteDataSource @Inject constructor(
    private val movieService:MovieService
) :BaseDataSource()
{
    suspend fun searchMovie(query: String) = getResult { movieService.searchMovie(query) }
    suspend fun searchMovieScrolling(query: String,page:Int) = getResult { movieService.searchMovieScrolling(query,page) }
    suspend fun getMovieDetails(id: Int) = getResult { movieService.getMovieDetails(id) }
    suspend fun getTopRatedMovies() = getResult { movieService.getTopRatedMovies() }
    suspend fun getUpcomingMovies() = getResult { movieService.getUpcomingMovies() }

    suspend fun getUpcomingMoviesByDateRange(startDate: String, endDate: String) = getResult {
        movieService.getUpcomingMoviesByDateRange(startDate.toString(), endDate.toString()) }
}