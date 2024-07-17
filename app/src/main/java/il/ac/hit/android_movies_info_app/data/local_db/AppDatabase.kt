package il.ac.hit.android_movies_info_app.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.BelongsToCollectionConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.GenresConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.ImagesConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.ProductionCompanyConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.ProductionCountryConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.SpokenLanguageConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.StringListConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.VideosConverter
import il.ac.hit.android_movies_info_app.data.model.movie_search.GenreIdsConverter
import il.ac.hit.android_movies_info_app.data.model.top_rated_movies.TopRatedMovie
import il.ac.hit.android_movies_info_app.data.model.upcoming_movies.UpcomingMovie

@Database(entities = [TopRatedMovie::class, FavoriteMovie::class, UpcomingMovie::class], version=3, exportSchema = false)
@TypeConverters(GenreIdsConverter::class,
    GenresConverter::class,
    BelongsToCollectionConverter::class,
    ProductionCompanyConverter::class,
    ProductionCountryConverter::class,
    SpokenLanguageConverter::class,
    VideosConverter::class,
    ImagesConverter::class,
    StringListConverter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun topRatedMovieDao(): TopRatedMovieDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun upcomingMovieDao(): UpcomingMovieDao


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
                    .fallbackToDestructiveMigration()
                    .build().also {
                        instance = it
                    }
            }
        }
    }
}