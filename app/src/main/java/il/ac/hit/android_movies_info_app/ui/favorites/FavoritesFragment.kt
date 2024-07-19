package il.ac.hit.android_movies_info_app.ui.favorites

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentFavoritesBinding
import il.ac.hit.android_movies_info_app.ui.favorites.viewmodel.FavoritesAdapter
import il.ac.hit.android_movies_info_app.ui.favorites.viewmodel.FavoritesViewModel
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class FavoritesFragment : Fragment(),FavoritesAdapter.MoviesItemListener {

    private var binding : FragmentFavoritesBinding by autoCleared()
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter:FavoritesAdapter
    private val mainScreenViewModel: MainScreenViewModel by viewModels({ requireActivity() })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        handleOnBackPressed()
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(this)
        binding.moviesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRv.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allFavoriteMovies?.observe(viewLifecycleOwner) { favoriteMovies ->
            favoriteMovies?.let {
                adapter.setMovies(it)
            }
        }
    }
    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(mainScreenViewModel.getDrawerState() == false) {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    else{
                        mainScreenViewModel.setDrawerState(false)

                    }
                }
            })
    }
    override fun onMovieClick(movieId: Int) {
        findNavController().navigate(R.id.action_favorites_nav_to_movieDetailFragment,
            bundleOf("id" to movieId)
        )
    }

}