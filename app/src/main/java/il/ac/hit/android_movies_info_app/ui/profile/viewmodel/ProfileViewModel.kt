package il.ac.hit.android_movies_info_app.ui.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.DEFAULT_PROFILE_IMAGE_URL
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
                Log.w("PVM_TEST", it)
                _photoUri.value = it
            }

        }
    }

}
