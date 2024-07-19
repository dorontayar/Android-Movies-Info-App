package il.ac.hit.android_movies_info_app.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentProfileBinding
import il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel.MainScreenViewModel
import il.ac.hit.android_movies_info_app.ui.profile.viewmodel.ProfileViewModel
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding by autoCleared()
    private val viewModel: ProfileViewModel by viewModels()
    private var profilePictureUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private val mainScreenViewModel: MainScreenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        .into(binding.profileImageView)
                }
            }
        }

        binding.profileImageView.setOnClickListener {
            selectProfileImage()
        }
        binding.changeProfileImage.setOnClickListener {
            selectProfileImage()
        }

        binding.updateProfileButton.setOnClickListener {
            val newName = binding.profileNameEditText.text.toString()
            viewModel.updateProfile(newName, profilePictureUri)
        }

        viewModel.photoUri.observe(viewLifecycleOwner) { photoUri ->
            Glide.with(this)
                .load(photoUri)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .into(binding.profileImageView)
        }
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.currentName.text = name
        }


        viewModel.updateStatus.observe(viewLifecycleOwner) { resource ->
            when (resource?.status) {
                is Loading -> {
                    binding.profileProgressBar.isVisible = true
                    binding.updateProfileButton.isEnabled = false
                }
                is Success -> {
                    binding.profileProgressBar.isVisible = false
                    binding.updateProfileButton.isEnabled = true
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    viewModel.resetUpdateStatus()
                }
                is Error -> {
                    binding.profileProgressBar.isVisible = false
                    binding.updateProfileButton.isEnabled = true
                    Toast.makeText(requireContext(), "Error updating profile: ${resource?.status.message}", Toast.LENGTH_SHORT).show()
                    viewModel.resetUpdateStatus()
                }
                else -> {

                    binding.profileProgressBar.isVisible = false
                    binding.updateProfileButton.isEnabled = true
                }
            }
        }




        handleOnBackPressed()
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

                    findNavController().navigate(R.id.mainScreenFragment)
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            })
    }


}
