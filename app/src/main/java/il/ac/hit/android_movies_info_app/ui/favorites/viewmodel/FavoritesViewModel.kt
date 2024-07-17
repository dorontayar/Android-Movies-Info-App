package il.ac.hit.android_movies_info_app.ui.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    movieRepository: MovieRepository
):ViewModel(){
    val allFavoriteMovies = movieRepository.getAllFavoriteMovies()

}