package il.ac.hit.android_movies_info_app.ui.main_screen.viewmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _bottomNavigationVisibility = MutableLiveData<Boolean>(true)

    val bottomNavigationVisibility: MutableLiveData<Boolean>
        get() = _bottomNavigationVisibility

    fun signOut() {
        authRepository.logout()
    }
    fun setBottomNavigationVisibility(isVisible:Boolean){
        Log.w("MainScreenViewModel","Changing bottom nav visibility")
        _bottomNavigationVisibility.value = isVisible
    }
}
