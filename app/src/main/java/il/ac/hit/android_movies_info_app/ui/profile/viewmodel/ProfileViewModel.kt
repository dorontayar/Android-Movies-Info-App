package il.ac.hit.android_movies_info_app.ui.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>> = _currentUser

    private val _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String> get() = _photoUri

    private val _updateStatus = MutableLiveData<Resource<User>?>()
    val updateStatus: MutableLiveData<Resource<User>?> get() = _updateStatus

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUser.postValue(Resource.loading())
            _currentUser.postValue(repository.currentUser())
        }
    }

    fun fetchProfileImage() {
        viewModelScope.launch {
            val user = repository.currentUser()
            user.status.data?.profilePictureUrl?.let {
                _photoUri.value = it
            }
        }
    }

    fun updateProfile(name: String?, profilePictureUri: Uri?) {
        if (name.isNullOrBlank() || name.length < 3) {
            _updateStatus.value = Resource.error("Name must be at least 3 characters long.")
            return
        }

        if (profilePictureUri != null || name.isNotBlank()) {
            viewModelScope.launch {
                _updateStatus.postValue(Resource.loading())
                val result = repository.changeUserParams(name, profilePictureUri)
                _updateStatus.postValue(result)
                if (profilePictureUri != null) {
                    _photoUri.value = profilePictureUri.toString()
                }
            }
        } else {
            _updateStatus.value = Resource.error("No changes to update.")
        }
    }

    fun resetUpdateStatus() {
        _updateStatus.value = null
    }
}
