package il.ac.hit.android_movies_info_app.ui.login

import android.os.Bundle
import android.util.Log
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
import il.ac.hit.android_movies_info_app.ui.login.viewmodel.LoginViewModel
import il.ac.hit.android_movies_info_app.utils.Loading
import il.ac.hit.android_movies_info_app.utils.Success
import il.ac.hit.android_movies_info_app.utils.Error
import il.ac.hit.android_movies_info_app.utils.NetworkState
import il.ac.hit.android_movies_info_app.utils.UserPreferences
import il.ac.hit.android_movies_info_app.utils.autoCleared

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


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNetworkStatus()
        buttonsSetup()
        observeUserSignIn()
        observeCurrentUser()

    }
    private fun observeNetworkStatus() {
        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isAvailable ->
            if (!isAvailable) {
                Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun observeUserSignIn() {
        viewModel.userSignInStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                is Loading -> {
                    binding.loginProgressBar.isVisible = true
                    binding.buttonLogin.isEnabled = false
                }
                is Success -> {
                    Toast.makeText(requireContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                    it.status.data?.let { user -> UserPreferences.saveUser(requireContext(), user.email, user.name) }
                    findNavController().navigate(R.id.action_loginFragment_to_mainScreenFragment)
                }
                is Error -> {
                    binding.loginProgressBar.isVisible = false
                    binding.buttonLogin.isEnabled = true
                    val message = when (it.status.message) {
                        "The supplied auth credential is incorrect, malformed or has expired." ->
                            getString(R.string.incorrect_email_or_password)
                        "The email address is badly formatted." ->
                            getString(R.string.email_badly_formatted)
                        else -> it.status.message
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) {
            when (it.status) {
                is Loading -> {
                    binding.loginProgressBar.isVisible = true
                    binding.buttonLogin.isEnabled = false
                }
                is Success -> {
                    it.status.data?.let { user -> UserPreferences.saveUser(requireContext(), user.email, user.name) }
                    findNavController().navigate(R.id.action_loginFragment_to_mainScreenFragment)
                }
                is Error -> {
                    binding.loginProgressBar.isVisible = false
                    binding.buttonLogin.isEnabled = true
                }
            }
        }
    }
    private fun buttonsSetup(){
        binding.noAcountTv.setOnClickListener {
            if (NetworkState.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }else{
                Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }

        }

        binding.buttonLogin.setOnClickListener {
            if (NetworkState.isNetworkAvailable(requireContext())) {
                viewModel.signInUser(binding.editTextLoginEmail.editText?.text.toString(),
                    binding.editTextLoginPass.editText?.text.toString())
            }else{
                Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }

        }
    }

}