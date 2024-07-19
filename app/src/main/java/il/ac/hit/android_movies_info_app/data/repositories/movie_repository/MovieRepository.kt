package il.ac.hit.android_movies_info_app.data.repositories.movie_repository


import il.ac.hit.android_movies_info_app.data.local_db.FavoriteMovieDao
import il.ac.hit.android_movies_info_app.data.local_db.TopRatedMovieDao
import il.ac.hit.android_movies_info_app.data.local_db.UpcomingMovieDao
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
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
    private val localDataSourceTopRated: TopRatedMovieDao,
    private val localDataSourceFavoriteMovie: FavoriteMovieDao?,
    private val localDataSourceUpcoming: UpcomingMovieDao,
    ) {
    // Movies API related repo functions
    fun getTopMovies() = performFetchingAndSaving(
        { localDataSourceTopRated.getTopRatedMovies() },
        { remoteDataSource.getTopRatedMovies() },
        { localDataSourceTopRated.insertMovies(it.results.sortedByDescending { movie -> movie.voteAverage }) }
    )
    fun getUpcomingMovies() = performFetchingAndSaving(
        { localDataSourceUpcoming.getUpcomingMovies() },
        { remoteDataSource.getUpcomingMovies() },
        { localDataSourceUpcoming.insertMovies(it.results.sortedByDescending { movie -> movie.releaseDate }) }
    )
    fun getMovie(id: Int) = performFetching { remoteDataSource.getMovieDetails(id) }
    fun getSearchedMovies(query : String) = performFetching { remoteDataSource.searchMovie(query) }
    fun getSearchedMoviesScrolling(query : String,page:Int) = performFetching { remoteDataSource.searchMovieScrolling(query,page) }



    // Room related repo functions
    fun getAllFavoriteMovies(userId:String) = localDataSourceFavoriteMovie?.getAllFavoriteMovies(userId)
    fun getFavoriteMovie(id: Int,userId:String) = localDataSourceFavoriteMovie?.getFavoriteMovie(id,userId)
    suspend fun saveFavoriteMovie(movie: FavoriteMovie) = localDataSourceFavoriteMovie?.insertFavoriteMovie(movie)
    suspend fun deleteFavoriteMovie(id: Int,userId:String) = localDataSourceFavoriteMovie?.deleteFavoriteMovie(id,userId)

}