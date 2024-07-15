package il.ac.hit.android_movies_info_app.ui.movie_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse
import il.ac.hit.android_movies_info_app.databinding.FragmentMovieDetailBinding
import il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel.MovieDetailViewModel
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_W185
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_ORIGINAL
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class MovieDetailFragment: Fragment() {
    private var binding: FragmentMovieDetailBinding by autoCleared()

    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movie.observe(viewLifecycleOwner){
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    binding.progressBar.isVisible = false
                    updateMovie(it.status.data!!)
                }
                is Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_SHORT).show()
                }

            }

        }

        arguments?.getInt("id")?.let{
            viewModel.setId(it)
        }
    }

    private fun updateMovie(movie: MovieDetailsResponse){
        binding.movieTitle.text = movie.title
        val imagePath:String = IMAGE_TYPE_ORIGINAL +movie.posterPath
        Glide.with(requireContext()).load(imagePath).into(binding.moviePoster)
        binding.movieDescription.text= movie.overview
        binding.movieRating.text = movie.voteAverage.toString()
        binding.movieVote.text = movie.voteCount.toString()

    }
}