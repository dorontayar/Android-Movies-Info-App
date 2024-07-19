package il.ac.hit.android_movies_info_app.data.local_db

import il.ac.hit.android_movies_info_app.data.model.upcoming_movies.UpcomingMovie
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.DEFAULT_UPCOMING_MOVIES_TO_SHOW_IN_EXPLORE


@Dao
interface UpcomingMovieDao {
    @Query("SELECT * FROM upcoming_movies")
    fun getAllMovies(): LiveData<List<UpcomingMovie>>

    @Query("SELECT * FROM upcoming_movies WHERE id = :id")
    fun getMovie(id: Int): LiveData<UpcomingMovie>

    @Query("SELECT * FROM upcoming_movies ORDER BY releaseDate DESC LIMIT $DEFAULT_UPCOMING_MOVIES_TO_SHOW_IN_EXPLORE")
    fun getUpcomingMovies(): LiveData<List<UpcomingMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: UpcomingMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<UpcomingMovie>)
}