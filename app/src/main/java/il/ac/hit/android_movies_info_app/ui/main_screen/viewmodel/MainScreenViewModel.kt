package il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.repositories.AuthRepository
import il.ac.hit.android_movies_info_app.data.repositories.firebase_implementation.AuthRepositoryFirebase
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signOut() {
        authRepository.logout()
    }
}
