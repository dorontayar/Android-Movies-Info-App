package il.ac.hit.android_movies_info_app.ui.explore.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    movieRepository: MovieRepository
):ViewModel(){
    val topMovies = movieRepository.getTopMovies()
}