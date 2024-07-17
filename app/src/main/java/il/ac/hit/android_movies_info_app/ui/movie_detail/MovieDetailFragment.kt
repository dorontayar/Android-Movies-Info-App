package il.ac.hit.android_movies_info_app.ui.movie_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse
import il.ac.hit.android_movies_info_app.databinding.FragmentMovieDetailBinding
import il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel.MovieDetailViewModel
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_W185
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_ORIGINAL
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared
import il.ac.hit.android_movies_info_app.utils.toFavoriteMovie

@AndroidEntryPoint
class MovieDetailFragment: Fragment() {
    private var binding: FragmentMovieDetailBinding by autoCleared()

    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var movieDetailResult: MovieDetailsResponse? = null



        arguments?.getInt("id")?.let {
            viewModel.setId(it)
        }

        viewModel.movie.observe(viewLifecycleOwner) {
            when (it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    binding.progressBar.isVisible = false
                    updateMovie(it.status.data!!)
                    movieDetailResult = it.status.data
                    viewModel.fetchTrailerUrl(it.status.data.title)  // Fetch trailer URL
                    Log.w("MovieDetailsLog", it.status.data.title)
                }

                is Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), it.status.message, Toast.LENGTH_SHORT).show()
                }

            }

        }

        viewModel.trailerUrl.observe(viewLifecycleOwner) { videoUrl ->
            videoUrl?.let {
                initializeYouTubePlayer(it)
            }
        }


        binding.btnAddFavorite.setOnClickListener {
            movieDetailResult?.let { movie ->
                viewModel.addFavorite(movie.toFavoriteMovie())
                Log.d("MovieDetailsLog", "Added to favorites: ${movie.toFavoriteMovie()}")
                Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRemoveFavorite.setOnClickListener {
            viewModel.removeFavorite()
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        }

        updateFavoriteButtons()
    }

    private fun updateMovie(movie: MovieDetailsResponse) {
        binding.movieTitle.text = movie.title
        val imagePath: String = IMAGE_TYPE_ORIGINAL + movie.posterPath
        Glide.with(requireContext()).load(imagePath).into(binding.moviePoster)
        binding.movieDescription.text = movie.overview
        binding.movieRating.text = movie.voteAverage.toString()
        binding.movieVote.text = movie.voteCount.toString()

    }

    private fun updateFavoriteButtons() {
        viewModel.findFavorite().observe(viewLifecycleOwner) { favoriteMovie ->
            Log.d("MovieDetailsLog", "Favorite: $favoriteMovie")
            val isFavorite = favoriteMovie != null
            binding.btnAddFavorite.isVisible = !isFavorite
            binding.btnRemoveFavorite.isVisible = isFavorite
            Log.d("MovieDetailsLog", "Is favorite: $isFavorite")
        }
    }

    private fun initializeYouTubePlayer(videoUrl: String) {
        binding.youtubePlayerView.apply {
            lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = videoUrl.substringAfter("v=")
                    Log.d("MovieDetailsLog", "VideoID is: $videoId")
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }
    }
}