package il.ac.hit.android_movies_info_app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.databinding.FragmentLoginBinding
import il.ac.hit.android_movies_info_app.data.repositories.firebase_implementation.AuthRepositoryFirebase
import il.ac.hit.android_movies_info_app.ui.login.viewmodel.LoginViewModel
import il.ac.hit.android_movies_info_app.util.Loading
import il.ac.hit.android_movies_info_app.util.Success
import il.ac.hit.android_movies_info_app.util.Error
import il.ac.hit.android_movies_info_app.util.autoCleared

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var binding :FragmentLoginBinding by autoCleared()
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.noAcountTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener {

            viewModel.signInUser(binding.editTextLoginEmail.editText?.text.toString(),
                binding.editTextLoginPass.editText?.text.toString())
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userSignInStatus.observe(viewLifecycleOwner) {

            when(it.status) {
                is Loading -> {
                    binding.loginProgressBar.isVisible = true
                    binding.buttonLogin.isEnabled = false
                }
                is Success -> {
                    Toast.makeText(requireContext(),"Login successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_mainScreenFragment)
                }
                is Error -> {
                    binding.loginProgressBar.isVisible = false
                    binding.buttonLogin.isEnabled = true
                    Toast.makeText(requireContext(),it.status.message,Toast.LENGTH_SHORT).show()
                }

            }
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {

            when(it.status) {
                is Loading -> {
                    binding.loginProgressBar.isVisible = true
                    binding.buttonLogin.isEnabled = false
                }
                is Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainScreenFragment)
                }
                is Error -> {
                    binding.loginProgressBar.isVisible = false
                    binding.buttonLogin.isEnabled = true
                }
            }
        }
    }
}