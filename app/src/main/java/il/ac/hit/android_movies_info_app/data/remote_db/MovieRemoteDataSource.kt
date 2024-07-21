package il.ac.hit.android_movies_info_app.data.remote_db

import il.ac.hit.android_movies_info_app.utils.getApiLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteDataSource @Inject constructor(
    private val movieService:MovieService
) :BaseDataSource()
{

    private val language = getApiLanguage()

    suspend fun searchMovie(query: String) = getResult { movieService.searchMovie(query,language=language) }
    suspend fun searchMovieScrolling(query: String,page:Int) = getResult { movieService.searchMovieScrolling(query,page,language=language) }
    suspend fun getMovieDetails(id: Int) = getResult { movieService.getMovieDetails(id,language=language) }
    suspend fun getTopRatedMovies() = getResult { movieService.getTopRatedMovies(language=language) }
    suspend fun getUpcomingMovies() = getResult { movieService.getUpcomingMovies(language=language) }

    suspend fun getUpcomingMoviesByDateRange(startDate: String, endDate: String) = getResult {
        movieService.getUpcomingMoviesByDateRange(startDate.toString(), endDate.toString(),language=language) }
}