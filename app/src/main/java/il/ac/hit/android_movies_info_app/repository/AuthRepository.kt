package il.ac.hit.android_movies_info_app.repository

import il.ac.hit.android_movies_info_app.model.User
import il.ac.hit.android_movies_info_app.util.Resource

interface AuthRepository {

    suspend fun currentUser() : Resource<User>
    suspend fun login(email:String, password:String) : Resource<User>
    suspend fun createUser(userName:String,
                           userEmail:String,
                           userPhone:String,
                           userLoginPass:String) : Resource<User>
    fun logout()
}