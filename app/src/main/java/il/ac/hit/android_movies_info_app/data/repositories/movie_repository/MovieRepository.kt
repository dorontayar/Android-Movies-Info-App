package il.ac.hit.android_movies_info_app.data.repositories.movie_repository

import il.ac.hit.android_movies_info_app.data.local_db.MovieDao
import il.ac.hit.android_movies_info_app.data.model.movie_search.Movie
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse
import il.ac.hit.android_movies_info_app.data.remote_db.MovieRemoteDataSource
import il.ac.hit.android_movies_info_app.utils.performFetching
import il.ac.hit.android_movies_info_app.utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource : MovieRemoteDataSource,
    private val localDataSource: MovieDao
) {

    fun getTopMovies()  = performFetchingAndSaving(
        {localDataSource.getAllMovies()},
        {remoteDataSource.getTopRatedMovies()},
        {localDataSource.insertMovies(it.results)}
    )

    fun getMovie(id: Int) = performFetching { remoteDataSource.getMovieDetails(id) }

    fun getSearchedMovies(query : String) = performFetching { remoteDataSource.searchMovie(query) }

}