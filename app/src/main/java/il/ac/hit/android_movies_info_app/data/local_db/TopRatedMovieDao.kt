package il.ac.hit.android_movies_info_app.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import il.ac.hit.android_movies_info_app.data.model.top_rated_movies.TopRatedMovie

@Dao
interface TopRatedMovieDao {
    @Query("SELECT * FROM top_rated_movies")
    fun getAllMovies() : LiveData<List<TopRatedMovie>>

    @Query("SELECT * FROM top_rated_movies WHERE id = :id")
    fun getMovie(id:Int) : LiveData<TopRatedMovie>

    @Query("SELECT * FROM top_rated_movies ORDER BY voteAverage DESC")
    fun getTopRatedMovies(): LiveData<List<TopRatedMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: TopRatedMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<TopRatedMovie>)

}