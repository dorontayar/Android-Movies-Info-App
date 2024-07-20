package il.ac.hit.android_movies_info_app.ui.movie_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse
import il.ac.hit.android_movies_info_app.databinding.FragmentMovieDetailBinding
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel.GalleryAdapter
import il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel.MovieDetailViewModel
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_ORIGINAL
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.UserPreferences
import il.ac.hit.android_movies_info_app.utils.autoCleared
import il.ac.hit.android_movies_info_app.utils.toFavoriteMovie

@AndroidEntryPoint
class MovieDetailFragment: Fragment() {
    private var binding: FragmentMovieDetailBinding by autoCleared()
    private val viewModel: MovieDetailViewModel by viewModels()
    private val mainScreenViewModel: MainScreenViewModel by activityViewModels()
    private var currentVideoPosition: Float = 0f
    private var isPlaying: Boolean = false
    private var movieDetailResult: MovieDetailsResponse? = null
    private var userid:String=""
    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userid=UserPreferences.getUserEmail(requireContext()).toString()
        mainScreenViewModel.setBottomNavigationVisibility(false)
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            currentVideoPosition = savedInstanceState.getFloat("CURRENT_VIDEO_POSITION", 0f)
            isPlaying = savedInstanceState.getBoolean("IS_PLAYING", false)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })

        lifecycle.addObserver(binding.youtubePlayerView)

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
                    setButtons()
                    updateFavoriteButtons()
                    populateImageGallery()
                }
                is Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), it.status.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        handleOnBackPressed()
    }

    private fun populateImageGallery(){
        movieDetailResult?.let {
            adapter = GalleryAdapter(it.images.backdrops)
            binding.movieImagesGallery.adapter = adapter }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("CURRENT_VIDEO_POSITION", currentVideoPosition)
        outState.putBoolean("IS_PLAYING", isPlaying)
    }

    private fun setButtons(){
        binding.btnAddFavorite.setOnClickListener {
            movieDetailResult?.let { movie ->
                viewModel.addFavorite(movie.toFavoriteMovie(),userid)
                Log.d("MovieDetailsLog", "Added to favorites: ${movie.toFavoriteMovie()}")
                Toast.makeText(requireContext(),
                    getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRemoveFavorite.setOnClickListener {
            viewModel.removeFavorite(userid)
            Toast.makeText(requireContext(),
                getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMovie(movie: MovieDetailsResponse) {
        binding.movieTitle.text = movie.title
        val imagePath: String = IMAGE_TYPE_ORIGINAL + movie.posterPath
        Glide.with(requireContext()).load(imagePath).placeholder(R.drawable.movie_placeholder).into(binding.moviePoster)
        binding.movieDescription.text = movie.overview.takeIf { it.isNotEmpty() } ?: getString(R.string.description_not_found)
        binding.movieReleaseDateText.text = movie.releaseDate.takeIf { it.isNotEmpty() }?.let {
            getString(R.string.release_date, it)
        } ?: getString(R.string.release_date_not_found)
        val voteAveragePercent: String = if (movie.voteAverage == 0.0) "-" else (movie.voteAverage * 10).toInt().toString() + "%"
        val voteCountText: String = if (movie.voteCount == 0) "-" else movie.voteCount.toString()

        binding.movieRating.text = voteAveragePercent
        binding.movieVote.text = voteCountText
        binding.genres.text = movie.genres.takeIf { it.isNotEmpty() }?.let {
            it.joinToString(", ") { genre -> genre.name }
        } ?: getString(R.string.genres_not_found)

            var trailerKey = movie.videos.results.firstOrNull { it.type == "Trailer" }?.key
        if (trailerKey == null) {
            trailerKey = "dQw4w9WgXcQ"
            binding.noTrailerText.visibility = View.VISIBLE
        }
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (isPlaying){
                    youTubePlayer.loadVideo(trailerKey, currentVideoPosition)
                } else {
                    youTubePlayer.cueVideo(trailerKey, currentVideoPosition)
                }
            }
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                currentVideoPosition = second
            }
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                isPlaying = state == PlayerConstants.PlayerState.PLAYING
            }
        })
    }

    private fun updateFavoriteButtons() {
        viewModel.findFavorite(userid).observe(viewLifecycleOwner) { favoriteMovie ->
            Log.d("MovieDetailsLog", "Favorite: $favoriteMovie")
            val isFavorite = favoriteMovie != null
            binding.btnAddFavorite.isVisible = !isFavorite
            binding.btnRemoveFavorite.isVisible = isFavorite
            Log.d("MovieDetailsLog", "Is favorite: $isFavorite")
        }
    }
    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(mainScreenViewModel.getDrawerState() == false) {
                        mainScreenViewModel.setBottomNavigationVisibility(true)
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    else{
                        mainScreenViewModel.setDrawerState(false)

                    }
                }
            })
    }

}

