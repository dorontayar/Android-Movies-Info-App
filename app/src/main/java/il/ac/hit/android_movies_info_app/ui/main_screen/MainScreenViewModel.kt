package il.ac.hit.android_movies_info_app.ui.main_screen


import androidx.lifecycle.*
import il.ac.hit.android_movies_info_app.repositories.AuthRepository

class MainScreenViewModel(private val authRep: AuthRepository) : ViewModel() {

    fun signOut() {
        authRep.logout()
    }

    class MainScreenViewModelFactory(val authRepo:AuthRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainScreenViewModel(authRepo) as T
        }
    }
}