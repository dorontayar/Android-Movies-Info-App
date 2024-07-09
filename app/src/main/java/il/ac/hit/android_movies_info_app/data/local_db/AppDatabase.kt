package il.ac.hit.android_movies_info_app.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import il.ac.hit.android_movies_info_app.data.model.movie_search.GenreIdsConverter
import il.ac.hit.android_movies_info_app.data.model.movie_search.Movie

@Database(entities = [Movie::class], version=1, exportSchema = false)
@TypeConverters(GenreIdsConverter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun movieDao():MovieDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movies_db"
                )
                    .fallbackToDestructiveMigration().build().also {
                        instance = it
                    }
            }
        }
    }
}