package il.ac.hit.android_movies_info_app.ui.search.viewmodel

import android.util.Log
import androidx.constraintlayout.widget.ConstraintSet.Transform
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bumptech.glide.load.Transformation
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.movie_search.Movie
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    movieRepository: MovieRepository
):ViewModel() {
    private val _query = MutableLiveData<String>()
    private val _page = MutableLiveData<Int>()

    //God bless this man, Transformations has become deprecated.
    //https://stackoverflow.com/questions/75465435/unresolved-reference-transformations-after-upgrading-lifecycle-dependency/75465436#75465436
    val movies = _query.switchMap(){
        movieRepository.getSearchedMovies(it)
    }
    val moreMovies = _page.switchMap(){
        _query.value?.let { query -> movieRepository.getSearchedMoviesScrolling(query,it) }
    }

    fun setQuery(query : String){
        _query.value = query
    }
    fun setPage(page:Int){
        _page.value = page
    }
}