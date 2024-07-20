package il.ac.hit.android_movies_info_app.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.HiltApp
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.utils.NetworkState
import il.ac.hit.android_movies_info_app.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    private val _userSignInStatus = MutableLiveData<Resource<User>>()
    val userSignInStatus: LiveData<Resource<User>> = _userSignInStatus

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>> = _currentUser

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    init {
        checkNetworkStatus()
        observeNetworkStatus()
    }
    private fun observeNetworkStatus() {
        isNetworkAvailable.observeForever { isAvailable ->
            if (isAvailable) {
                fetchCurrentUser()
            }
        }
    }
    private fun fetchCurrentUser() {
        if (_isNetworkAvailable.value == true) {
            viewModelScope.launch {
                _currentUser.postValue(Resource.loading())
                _currentUser.postValue(authRepository.currentUser())
            }
        }
    }
    private fun checkNetworkStatus() {
        viewModelScope.launch {
            _isNetworkAvailable.postValue(NetworkState.isNetworkAvailable(application))
        }
    }
    fun signInUser(userEmail: String, userPass: String) {
        if (userEmail.isEmpty() || userPass.isEmpty()) {
            _userSignInStatus.postValue(HiltApp.getContext()?.getString(R.string.empty_email_or_password)
                ?.let { Resource.error(it) })
        } else {
            _userSignInStatus.postValue(Resource.loading())
            viewModelScope.launch {
                val loginResult = authRepository.login(userEmail, userPass)
                _userSignInStatus.postValue(loginResult)
            }
        }
    }
}
