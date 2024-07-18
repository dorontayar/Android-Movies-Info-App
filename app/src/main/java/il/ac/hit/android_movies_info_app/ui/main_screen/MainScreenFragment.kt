package il.ac.hit.android_movies_info_app.ui.main_screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.InvalidationTracker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentMainScreenBinding
import il.ac.hit.android_movies_info_app.databinding.FragmentMainScreenDrawerBinding
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.utils.autoCleared


@AndroidEntryPoint
class MainScreenFragment : Fragment() , NavigationView.OnNavigationItemSelectedListener{

    private var binding: FragmentMainScreenDrawerBinding by autoCleared()
    private val viewModel : MainScreenViewModel by activityViewModels()
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val toolbar: Toolbar = binding.appBarMain.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        drawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )

        drawerLayout.addDrawerListener(drawerToggle!!)
        drawerToggle!!.syncState()

        val navigationView: NavigationView = binding.navSideView
        navigationView.setNavigationItemSelectedListener(this)

        val navView: BottomNavigationView = binding.appBarMain.contentMain.navView

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_main_screen) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.explore_nav, R.id.search_nav,R.id.favorites_nav, R.id.profile_nav,R.id.movieDetailFragment),
            drawerLayout
        )
        setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        viewModel.bottomNavigationVisibility.observe(viewLifecycleOwner)
            { isVisible ->
                setBottomNavigationVisibility(isVisible)
            }

        handleOnBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController().navigate(R.id.mainScreenFragment)
            }
            R.id.nav_settings -> {

            }

            R.id.nav_logout -> {
                Toast.makeText(requireContext(), "Logout!", Toast.LENGTH_SHORT).show()
                viewModel.signOut()
                findNavController().navigate(R.id.action_mainScreenFragment_to_loginFragment)
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
                        drawerLayout?.closeDrawer(GravityCompat.START)
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher
                    }
                }
            })
    }

    private fun setBottomNavigationVisibility(isVisible:Boolean){
        binding.appBarMain.contentMain.bottomAppBar.isVisible = isVisible
    }
}
