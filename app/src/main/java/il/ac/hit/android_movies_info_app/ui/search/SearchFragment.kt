package il.ac.hit.android_movies_info_app.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentSearchBinding
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.ui.search.viewmodel.SearchAdapter
import il.ac.hit.android_movies_info_app.ui.search.viewmodel.SearchViewModel
import il.ac.hit.android_movies_info_app.utils.DrawerController
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.NetworkState
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.MoviesItemListener {

    private var binding: FragmentSearchBinding by autoCleared()

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: SearchAdapter

    private val mainScreenViewModel: MainScreenViewModel by viewModels({ requireActivity() })

    private var page = 1
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
        adapterSetup()
        uiSetup()
        viewModelSetup()
        handleOnBackPressed()
    }
    private fun adapterSetup(){
        adapter = SearchAdapter(this)
        binding.moviesRvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRvSearch.adapter = adapter
    }
    private fun uiSetup(){
        binding.searchButton.setOnClickListener{
            if (NetworkState.isNetworkAvailable(requireContext())) {
                val query = binding.searchEditText.text.toString()
                page = 1
                viewModel.setQuery(query)
            }else{
                Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }

        }

        //If Rv reached bottom generate next page of query
        binding.moviesRvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    viewModel.setPage(++page)
                }
            }
        })
    }
    private fun viewModelSetup(){
        viewModel.movies.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    binding.progressBar.isVisible = false
                    adapter.setMovies(ArrayList(it.status.data!!.results))
                    Log.d("noResultsCount",adapter.itemCount.toString())
                    if(adapter.itemCount != 0) {
                        binding.noResultsText.isVisible = false
                        binding.noResultsImg.isVisible = false
                        binding.moviesRvSearch.isVisible = true
                    } else {
                        binding.moviesRvSearch.isVisible = false
                        binding.noResultsText.isVisible = true
                        binding.noResultsImg.isVisible = true
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

        viewModel.moreMovies.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.isVisible = true
                is Success -> {
                    binding.progressBar.isVisible = false
                    adapter.generateMoreMovies(ArrayList(it.status.data!!.results))
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
        if (NetworkState.isNetworkAvailable(requireContext())) {
            findNavController().navigate(R.id.action_search_nav_to_movieDetailFragment,
                bundleOf("id" to movieId)
            )
        }else{
            Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }
}
