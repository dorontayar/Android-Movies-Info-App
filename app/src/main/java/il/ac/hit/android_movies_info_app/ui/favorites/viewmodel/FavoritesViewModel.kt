package il.ac.hit.android_movies_info_app.ui.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieRepository: MovieRepository

):ViewModel() {
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    val allFavoriteMovies: LiveData<List<FavoriteMovie>> = _userId.switchMap { id ->
        movieRepository.getAllFavoriteMovies(id)
    }

    fun setUserId(userId: String) {
        _userId.value = userId
    }

    fun removeFavorite(movie: FavoriteMovie) {
        viewModelScope.launch {
            _userId.value?.let {
                try {
                    movieRepository.deleteFavoriteMovie(movie.id, it)
                    Log.d("FavoriteViewModel", "Removed from favorites: $it")
                } catch (e: Exception) {
                    Log.e("FavoriteViewModel", "Failed to remove favorite: $it")
                }
            }
        }
    }
}