package il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.YouTubeApiService
import il.ac.hit.android_movies_info_app.data.YouTubeResponse
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.YT_API_KEY
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val youtubeApiService: YouTubeApiService
) : ViewModel() {

    private val _id = MutableLiveData<Int>()
    private val _trailerUrl = MutableLiveData<String>()
    val trailerUrl: LiveData<String> = _trailerUrl

    val movie = _id.switchMap {
        movieRepository.getMovie(it)
    }

    fun setId(id: Int) {
        _id.value = id
    }

    fun addFavorite(movie: FavoriteMovie) {
        viewModelScope.launch {
            try {
                movieRepository.saveFavoriteMovie(movie)
                Log.d("MovieDetailViewModel", "Added to favorites: $movie")
            } catch (e: Exception) {
                Log.e("MovieDetailViewModel", "Failed to add favorite: $e")
            }
        }
    }

    fun removeFavorite() {
        viewModelScope.launch {
            _id.value?.let {
                try {
                    movieRepository.deleteFavoriteMovie(it)
                    Log.d("MovieDetailViewModel", "Removed from favorites: $it")
                } catch (e: Exception) {
                    Log.e("MovieDetailViewModel", "Failed to remove favorite: $e")
                }
            }
        }
    }

    fun findFavorite(): LiveData<FavoriteMovie> {
        return _id.switchMap {
            movieRepository.getFavoriteMovie(it)
        }
    }

    fun fetchTrailerUrl(movieTitle: String) {
        viewModelScope.launch {
            try {
                Log.d("MovieDetailViewModel", "Using API key: $YT_API_KEY")
                val response = youtubeApiService.searchVideos(
                    query = "$movieTitle trailer"
                )
                val videoId = response.items.firstOrNull()?.id?.videoId
                val videoUrl = "https://www.youtube.com/watch?v=$videoId"
                _trailerUrl.postValue(videoUrl)
            } catch (e: HttpException) {
                Log.e("MovieDetailViewModel", "HTTP error: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Log.e("MovieDetailViewModel", "Failed to fetch trailer URL: ${e.message}")
            }
        }
    }
}
