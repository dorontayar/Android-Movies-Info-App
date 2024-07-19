package il.ac.hit.android_movies_info_app.data.local_db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movies WHERE userId = :userId")
    fun getAllFavoriteMovies(userId: String): LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_movies WHERE id = :id AND userId = :userId")
    fun getFavoriteMovie(id: Int, userId: String): LiveData<FavoriteMovie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: FavoriteMovie)

    @Query("DELETE FROM favorite_movies WHERE id = :id AND userId = :userId")
    suspend fun deleteFavoriteMovie(id: Int, userId: String)
}
