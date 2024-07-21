package il.ac.hit.android_movies_info_app.ui.explore.viewmodel

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.repositories.movie_repository.MovieRepository
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.DEFAULT_DATE_RANGE_FOR_UPCOMING_MOVIES
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    movieRepository: MovieRepository
):ViewModel() {
    val topMovies = movieRepository.getTopMovies()

    private val startDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    private val endDate = getDateAfterMonths(DEFAULT_DATE_RANGE_FOR_UPCOMING_MOVIES)
    val upcomingMoviesByRange = movieRepository.getUpcomingMoviesByDateRange(startDate, endDate)

    private fun getDateAfterMonths(months: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, months)
        return dateFormat.format(calendar.time)
    }
}