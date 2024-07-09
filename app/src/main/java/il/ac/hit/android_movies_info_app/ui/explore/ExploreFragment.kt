package il.ac.hit.android_movies_info_app.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentExploreBinding
import il.ac.hit.android_movies_info_app.ui.explore.viewmodel.ExploreAdapter
import il.ac.hit.android_movies_info_app.ui.explore.viewmodel.ExploreViewModel
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class ExploreFragment : Fragment(), ExploreAdapter.MoviesItemListener {

    private var binding : FragmentExploreBinding by autoCleared()

    private val viewModel: ExploreViewModel by viewModels()

    private  lateinit var  adapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExploreAdapter(this)
        binding.moviesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRv.adapter = adapter


        viewModel.topMovies.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    if(!it.status.data.isNullOrEmpty()) {
                        binding.progressBar.isVisible = false
                        adapter.setMovies(ArrayList(it.status.data))
                    }
                }
                is Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_SHORT).show()
                }


            }
        }

    }

    override fun onMovieClick(movieId: Int) {
        Toast.makeText(requireContext(),"Movie Clicked",Toast.LENGTH_SHORT).show()
    }
}