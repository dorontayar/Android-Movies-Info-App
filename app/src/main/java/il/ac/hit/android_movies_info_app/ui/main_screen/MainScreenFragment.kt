package il.ac.hit.android_movies_info_app.ui.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarView
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentMainScreenBinding
import il.ac.hit.android_movies_info_app.repositories.firebase_implementation.AuthRepositoryFirebase
import il.ac.hit.android_movies_info_app.ui.explore.Explore
import il.ac.hit.android_movies_info_app.ui.profile.Profile
import il.ac.hit.android_movies_info_app.ui.search.Search
import il.ac.hit.android_movies_info_app.util.autoCleared
import kotlin.math.log

class MainScreenFragment : Fragment() {

    private var binding: FragmentMainScreenBinding by autoCleared()
    private val viewModel : MainScreenViewModel by viewModels {
        MainScreenViewModel.MainScreenViewModelFactory(AuthRepositoryFirebase())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
//Blabla
        //Setting it to search as default
//        childFragmentManager.beginTransaction()
//            .replace(R.id.frame_layout, Search())
//            .commit()
        replaceFragment(Explore())

//        binding.bottomNavigationView.setOnNavigationItemSelectedListener()
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btm_nav_explore -> {
                    replaceFragment(Explore())
                    true
                }

                R.id.btm_nav_search -> {
                    replaceFragment(Search())
                    true
                }

                R.id.btm_nav_profile -> {
                    replaceFragment(Profile())
                    true
                }

                else -> false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sign_out -> {
                        viewModel.signOut()
                        findNavController().navigate(R.id.action_mainScreenFragment_to_loginFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

}
