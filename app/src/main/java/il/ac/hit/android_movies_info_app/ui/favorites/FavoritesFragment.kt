package il.ac.hit.android_movies_info_app.ui.favorites

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.databinding.FragmentFavoritesBinding
import il.ac.hit.android_movies_info_app.ui.favorites.viewmodel.FavoritesAdapter
import il.ac.hit.android_movies_info_app.ui.favorites.viewmodel.FavoritesViewModel
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.utils.NetworkState
import il.ac.hit.android_movies_info_app.utils.UserPreferences
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
        UserPreferences.getUserEmail(requireContext()).toString().let { viewModel.setUserId(it) }
        setupRecyclerView()
        observeViewModel()
        handleOnBackPressed()
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(this)
        binding.moviesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRv.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = adapter.movies[position]

                showDeleteConfirmationDialog(position, movie)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.moviesRv)
    }

    private fun observeViewModel() {
        viewModel.allFavoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
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
                    if (mainScreenViewModel.getDrawerState() == false) {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                        mainScreenViewModel.setDrawerState(false)

                    }
                }
            })
    }

    private fun showDeleteConfirmationDialog(position: Int, movie: FavoriteMovie) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.remove_from_favorites_dialog_title))
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_remove_this_movie_from_your_favorites))
        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.yes_dialog)) { dialog, _ ->
            adapter.removeMovie(position)
            viewModel.removeFavorite(movie)
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }




    override fun onMovieClick(movieId: Int) {
        if (NetworkState.isNetworkAvailable(requireContext())) {
            findNavController().navigate(R.id.action_favorites_nav_to_movieDetailFragment,
                bundleOf("id" to movieId)
            )
        }else{
            Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        }

    }

}