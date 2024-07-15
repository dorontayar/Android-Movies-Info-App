package il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _id = MutableLiveData<Int>()

    val movie = _id.switchMap() {
        movieRepository.getMovie(it)
    }

    fun setId(id:Int){
        _id.value = id
    }

}