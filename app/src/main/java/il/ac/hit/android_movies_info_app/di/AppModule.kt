package il.ac.hit.android_movies_info_app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.ac.hit.android_movies_info_app.data.local_db.AppDatabase
import il.ac.hit.android_movies_info_app.data.local_db.FavoriteMovieDao
import il.ac.hit.android_movies_info_app.data.local_db.TopRatedMovieDao
import il.ac.hit.android_movies_info_app.data.local_db.UpcomingMovieDao
import il.ac.hit.android_movies_info_app.data.remote_db.MovieService
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.firebase_implementation.AuthRepositoryFirebase
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): AuthRepository {
        return AuthRepositoryFirebase(firebaseAuth, firestore,storage)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideRetrofit (gson: Gson): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
    @Provides
    @Singleton
    fun providesTopRatedMovieDao(database: AppDatabase): TopRatedMovieDao {
        return database.topRatedMovieDao()
    }
    @Provides
    @Singleton
    fun providesUpcomingMovieDao(database: AppDatabase): UpcomingMovieDao {
        return database.upcomingMovieDao()
    }
    @Provides
    @Singleton
    fun providesFavoriteMovieDao(database: AppDatabase): FavoriteMovieDao {
        return database.favoriteMovieDao()
    }

}