package il.ac.hit.android_movies_info_app.ui.register

import android.util.Patterns
import androidx.lifecycle.*
import il.ac.hit.android_movies_info_app.model.User
import il.ac.hit.android_movies_info_app.repository.AuthRepository
import il.ac.hit.android_movies_info_app.util.Resource
import kotlinx.coroutines.launch


class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _userRegistrationStatus = MutableLiveData<Resource<User>>()
    val userRegistrationStatus: LiveData<Resource<User>> = _userRegistrationStatus

    fun createUser(userName:String, userEmail:String, userPhone:String, userPass:String) {
        val error = if(userEmail.isEmpty() || userName.isEmpty() || userPass.isEmpty() || userPhone.isEmpty())
            "Empty Strings"
        else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            "Not a valid email"
        }else null
        error?.let {
            _userRegistrationStatus.postValue(Resource.error(it))
        }
        _userRegistrationStatus.value = Resource.loading()
        viewModelScope.launch {
            val registrationResult = repository.createUser(userName,userEmail,userPhone,userPass)
            _userRegistrationStatus.postValue(registrationResult)
        }

    }

    class RegisterViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(repo) as T
        }
    }
}