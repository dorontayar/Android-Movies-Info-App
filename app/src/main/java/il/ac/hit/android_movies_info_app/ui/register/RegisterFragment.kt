package il.ac.hit.android_movies_info_app.ui.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentRegisterBinding
import il.ac.hit.android_movies_info_app.ui.register.viewmodel.RegisterViewModel
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.NetworkState
import il.ac.hit.android_movies_info_app.utils.Resource
import il.ac.hit.android_movies_info_app.utils.autoCleared

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var binding : FragmentRegisterBinding by autoCleared()
    private val viewmodel: RegisterViewModel by viewModels()
    private var profilePictureUri: Uri? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        imagePickerSetup()
        buttonSetup()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRegistrationStatus()

    }

    private fun userRegistrationStatus() {
        viewmodel.userRegistrationStatus.observe(viewLifecycleOwner){

            when(it.status){
                is Loading -> {
                    binding.registerProgress.isVisible = true
                    binding.userRegisterButton.isEnabled = false
                }
                is Success -> {
                    Toast.makeText(requireContext(),
                        getString(R.string.registration_successful),Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_mainScreenFragment)
                }
                is Error -> {
                    binding.registerProgress.isVisible = false
                    binding.userRegisterButton.isEnabled = true
                    if(it.status.message=="The email address is already in use by another account."){
                        Toast.makeText(requireContext(),
                            getString(R.string.the_email_already_registered), Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), it.status.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun buttonSetup(){
        binding.userRegisterButton.setOnClickListener{
            if (NetworkState.isNetworkAvailable(requireContext())) {
                viewmodel.createUser(
                    binding.edxtUserName.editText?.text.toString(),
                    binding.edxtEmailAddress.editText?.text.toString(),
                    binding.edxtPhoneNum.editText?.text.toString(),
                    binding.edxtPassword.editText?.text.toString(),
                    profilePictureUri
                )
            }else{
                Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }

        }
        binding.selectProfileImageButton.setOnClickListener {
            selectProfileImage()
        }
    }
    private fun imagePickerSetup(){
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                profilePictureUri = result.data?.data
                binding.profileImageView.setImageURI(profilePictureUri)
            }
        }

    }
    private fun selectProfileImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

}
