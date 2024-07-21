package il.ac.hit.android_movies_info_app.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentExploreNewBinding
import il.ac.hit.android_movies_info_app.ui.explore.viewmodel.TopRatedAdapter
import il.ac.hit.android_movies_info_app.ui.explore.viewmodel.ExploreViewModel
import il.ac.hit.android_movies_info_app.ui.explore.viewmodel.UpcomingAdapter
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.NetworkState
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared


@AndroidEntryPoint
class ExploreFragment : Fragment(), TopRatedAdapter.MoviesItemListener ,UpcomingAdapter.MoviesItemListener{

    private var binding : FragmentExploreNewBinding by autoCleared()
    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var  topRatedAdapter: TopRatedAdapter
    private lateinit var  upcomingAdapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreNewBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterSetup()
        viewModelSetup()

    }
    private fun viewModelSetup(){
        viewModel.topMovies.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    if(!it.status.data.isNullOrEmpty()) {
                        binding.progressBar.isVisible = false
                        topRatedAdapter.setMovies(ArrayList(it.status.data))
                    }
                }
                is Error -> {
                    binding.progressBar.isVisible = false
                    if (NetworkState.isNetworkAvailable(requireContext())) {
                        Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
                    }

                }


            }
        }
        viewModel.upcomingMoviesByRange.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    if(!it.status.data.isNullOrEmpty()) {
                        binding.progressBar.isVisible = false
                        upcomingAdapter.setMovies(ArrayList(it.status.data))
                    }
                }
                is Error -> {
                    binding.progressBar.isVisible = false
                    if (NetworkState.isNetworkAvailable(requireContext())) {
                        Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
    }

    private fun adapterSetup(){
        topRatedAdapter = TopRatedAdapter(this)
        binding.moviesRvTopRated.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.moviesRvTopRated.adapter = topRatedAdapter

        upcomingAdapter = UpcomingAdapter(this)
        binding.moviesRvUpcoming.setLayoutManager(GridLayoutManager(requireContext(), 3))
        binding.moviesRvUpcoming.setAdapter(upcomingAdapter)
    }
    override fun onMovieClick(movieId: Int) {
        if (NetworkState.isNetworkAvailable(requireContext())) {
            findNavController().navigate(R.id.action_explore_nav_to_movieDetailFragment, bundleOf("id" to movieId))
        }else{
            Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }
}