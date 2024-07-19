package il.ac.hit.android_movies_info_app.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentProfileBinding
import il.ac.hit.android_movies_info_app.databinding.FragmentProfileDrawerBinding
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.ui.profile.viewmodel.ProfileViewModel
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class ProfileFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private var binding:FragmentProfileDrawerBinding by autoCleared()
    private val viewModel: ProfileViewModel by viewModels()
    private var profilePictureUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private val mainScreenViewModel: MainScreenViewModel by activityViewModels()
    private var drawerToggle: ActionBarDrawerToggle? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = binding.appBarProfile.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val drawerLayout:DrawerLayout = binding.drawerLayout
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

        viewModel.fetchProfileImage()
        viewModel.fetchUserName()

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                profilePictureUri = result.data?.data
                profilePictureUri?.let { uri ->
                    Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder)
                        .into(binding.appBarProfile.profileFromDrawer.profileImageView)
                }
            }
        }

        binding.appBarProfile.profileFromDrawer.profileImageView.setOnClickListener {
            selectProfileImage()
        }
        binding.appBarProfile.profileFromDrawer.changeProfileImage.setOnClickListener {
            selectProfileImage()
        }

        binding.appBarProfile.profileFromDrawer.updateProfileButton.setOnClickListener {
            val newName = binding.appBarProfile.profileFromDrawer.profileNameEditText.text.toString()
            viewModel.updateProfile(newName, profilePictureUri)
        }

        viewModel.photoUri.observe(viewLifecycleOwner) { photoUri ->
            Glide.with(this)
                .load(photoUri)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .into(binding.appBarProfile.profileFromDrawer.profileImageView)
        }
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.appBarProfile.profileFromDrawer.currentName.text = name
        }


        viewModel.updateStatus.observe(viewLifecycleOwner) { resource ->
            when (resource?.status) {
                is Loading -> {
                    binding.appBarProfile.profileFromDrawer.profileProgressBar.isVisible = true
                    binding.appBarProfile.profileFromDrawer.updateProfileButton.isEnabled = false
                }
                is Success -> {
                    binding.appBarProfile.profileFromDrawer.profileProgressBar.isVisible = false
                    binding.appBarProfile.profileFromDrawer.updateProfileButton.isEnabled = true
                    Toast.makeText(requireContext(),
                        getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                    viewModel.resetUpdateStatus()
                }
                is Error -> {
                    binding.appBarProfile.profileFromDrawer.profileProgressBar.isVisible = false
                    binding.appBarProfile.profileFromDrawer.updateProfileButton.isEnabled = true
                    Toast.makeText(requireContext(),
                        getString(R.string.error_updating_profile, resource?.status.message), Toast.LENGTH_SHORT).show()
                    viewModel.resetUpdateStatus()
                }
                else -> {

                    binding.appBarProfile.profileFromDrawer.profileProgressBar.isVisible = false
                    binding.appBarProfile.profileFromDrawer.updateProfileButton.isEnabled = true
                }
            }
        }




       handleOnBackPressed()
    }
    override fun onResume() {
        binding.drawerLayout.closeDrawers()
        super.onResume()
    }
    private fun selectProfileImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        mainScreenViewModel.setDrawerState(false)
                        childFragmentManager.popBackStack()
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }

                }
            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.mainScreenFragment, true)
                    .build()
                mainScreenViewModel.setBottomNavigationVisibility(true)
                findNavController().navigate(R.id.action_profileFragmentDrawer_to_mainScreenFragment,null,navOptions)
            }
            R.id.nav_profile_manage -> {

            }

            R.id.nav_github -> {
                val url = "https://github.com/dorontayar/Android-Movies-Info-App"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            R.id.nav_logout -> {
                Toast.makeText(requireContext(),
                    getString(R.string.logout_toast), Toast.LENGTH_SHORT).show()
                mainScreenViewModel.signOut()
                findNavController().navigate(R.id.action_mainScreenFragment_to_loginFragment)
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }


}
