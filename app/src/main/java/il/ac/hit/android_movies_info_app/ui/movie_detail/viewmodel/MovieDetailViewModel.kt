package il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import il.ac.hit.android_movies_info_app.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val authRepository: AuthRepository

) : ViewModel() {

    private val _id = MutableLiveData<Int>()
    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>> = _currentUser
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId
    init {
        fetchCurrentUser()
        fetchUserId()
    }
    private fun fetchUserId() {
        viewModelScope.launch {
            val user = authRepository.currentUser()
            user.status.data?.name?.let {
                _userId.value = it
            }
        }
    }
    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUser.postValue(Resource.loading())
            _currentUser.postValue(authRepository.currentUser())
        }
    }

    val movie = _id.switchMap {
        movieRepository.getMovie(it)
    }

    fun setId(id: Int) {
        _id.value = id
    }

    fun addFavorite(movie: FavoriteMovie,userId: String) {
        viewModelScope.launch {
            try {
                    val favMovie:FavoriteMovie = movie
                    favMovie.userId = userId
                    movieRepository.saveFavoriteMovie(favMovie)
                //movieRepository.saveFavoriteMovie(movie)
                Log.d("MovieDetailViewModel", "Added to favorites: $movie")
            } catch (e: Exception) {
                Log.e("MovieDetailViewModel", "Failed to add favorite: $e")
            }
        }
    }

    fun removeFavorite(userId: String) {
        viewModelScope.launch {
            _id.value?.let {
                try {
                    movieRepository.deleteFavoriteMovie(it, userId)
                    Log.d("MovieDetailViewModel", "Removed from favorites: $it")
                } catch (e: Exception) {
                    Log.e("MovieDetailViewModel", "Failed to remove favorite: $e")
                }
            }
        }
    }

    fun findFavorite(userId:String): LiveData<FavoriteMovie> {
        return _id.switchMap { movieId ->
                movieRepository.getFavoriteMovie(movieId, userId)
            }
    }

}

