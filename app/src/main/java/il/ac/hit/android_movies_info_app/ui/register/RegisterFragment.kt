package il.ac.hit.android_movies_info_app.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentRegisterBinding
import il.ac.hit.android_movies_info_app.repository.firebase_implementation.AuthRepositoryFirebase
import il.ac.hit.android_movies_info_app.ui.register.RegisterViewModel
import il.ac.hit.android_movies_info_app.util.Resource
import il.ac.hit.android_movies_info_app.util.autoCleared

class RegisterFragment: Fragment(){

    private var binding : FragmentRegisterBinding by autoCleared()
    private val viewmodel : RegisterViewModel by viewModels() {
        RegisterViewModel.RegisterViewModelFactory(AuthRepositoryFirebase())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        binding.userRegisterButton.setOnClickListener{
            viewmodel.createUser(
                binding.edxtUserName.editText?.text.toString(),
                binding.edxtEmailAddress.editText?.text.toString(),
                binding.edxtPhoneNum.editText?.text.toString(),
                binding.edxtPassword.editText?.text.toString()
                )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.userRegistrationStatus.observe(viewLifecycleOwner){

            when(it){
                is Resource.Loading -> {
                    binding.registerProgress.isVisible = true
                    binding.userRegisterButton.isEnabled = false
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(),"Registration Successful",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_mainScreenFragment)
                }
                is Resource.Error -> {
                    binding.registerProgress.isVisible = false
                    binding.userRegisterButton.isEnabled = true
                }
            }
        }

    }
}
