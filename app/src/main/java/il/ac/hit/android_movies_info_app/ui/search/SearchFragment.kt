package il.ac.hit.android_movies_info_app.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentSearchBinding
import il.ac.hit.android_movies_info_app.ui.search.viewmodel.SearchAdapter
import il.ac.hit.android_movies_info_app.ui.search.viewmodel.SearchViewModel
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.MoviesItemListener {

    private var binding: FragmentSearchBinding by autoCleared()

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchAdapter(this)
        binding.moviesRvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRvSearch.adapter = adapter

        binding.searchButton.setOnClickListener{
            viewModel.setQuery(binding.searchEditText.text.toString())
            Log.d("Oh nyo", binding.searchEditText.text.toString())
        }
        viewModel.movies.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    binding.progressBar.isVisible = false
                    Log.d("query", binding.searchEditText.text.toString())
                    Log.d("query", it.status.data?.results.toString())

                    //Fix the !! call later. For some reason .isNullOrEmpty() refuses to cooperate
                    adapter.setMovies(ArrayList(it.status.data!!.results))

                }
                is Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMovieClick(movieId: Int) {
        Toast.makeText(requireContext(), "Movie Clicked", Toast.LENGTH_SHORT).show()
    }
}