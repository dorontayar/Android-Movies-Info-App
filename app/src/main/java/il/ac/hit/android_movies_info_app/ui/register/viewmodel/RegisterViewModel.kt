package il.ac.hit.android_movies_info_app.ui.register.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.HiltApp
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _userRegistrationStatus = MutableLiveData<Resource<User>>()
    val userRegistrationStatus: LiveData<Resource<User>> = _userRegistrationStatus

    fun createUser(userName: String, userEmail: String, userPhone: String, userPass: String, profilePictureUri: Uri?) {
        val error = when {
            userEmail.isEmpty() || userName.isEmpty() || userPass.isEmpty() || userPhone.isEmpty() -> HiltApp.getContext()?.getString(
                R.string.empty_strings
            )
            !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> HiltApp.getContext()?.getString(R.string.not_a_valid_email)
            else -> null
        }
        error?.let {
            _userRegistrationStatus.postValue(Resource.error(it))
            return
        }
        _userRegistrationStatus.value = Resource.loading()
        viewModelScope.launch {
            val registrationResult = repository.createUser(userName, userEmail, userPhone, userPass, profilePictureUri)
            _userRegistrationStatus.postValue(registrationResult)
        }
    }
}
