package il.ac.hit.android_movies_info_app.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userSignInStatus = MutableLiveData<Resource<User>>()
    val userSignInStatus: LiveData<Resource<User>> = _userSignInStatus

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>> = _currentUser

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUser.postValue(Resource.loading())
            _currentUser.postValue(authRepository.currentUser())
        }
    }

    fun signInUser(userEmail: String, userPass: String) {
        if (userEmail.isEmpty() || userPass.isEmpty()) {
            _userSignInStatus.postValue(Resource.error("Empty email or password"))
        } else {
            _userSignInStatus.postValue(Resource.loading())
            viewModelScope.launch {
                val loginResult = authRepository.login(userEmail, userPass)
                _userSignInStatus.postValue(loginResult)
            }
        }
    }
}
