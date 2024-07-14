package il.ac.hit.android_movies_info_app.data.repositories.auth_repository

import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.utils.Resource
import android.net.Uri

interface AuthRepository {

    suspend fun currentUser() : Resource<User>
    suspend fun login(email:String, password:String) : Resource<User>
    suspend fun createUser(
        userName:String,
        userEmail:String,
        userPhone:String,
        userLoginPass:String,
        profilePictureUri: Uri?
    ) : Resource<User>
    fun logout()
}
