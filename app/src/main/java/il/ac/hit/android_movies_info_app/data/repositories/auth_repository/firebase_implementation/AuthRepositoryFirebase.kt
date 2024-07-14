package il.ac.hit.android_movies_info_app.data.repositories.auth_repository.firebase_implementation

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import il.ac.hit.android_movies_info_app.data.model.User
import il.ac.hit.android_movies_info_app.data.repositories.auth_repository.AuthRepository
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.DEFAULT_PROFILE_IMAGE_URL
import il.ac.hit.android_movies_info_app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import il.ac.hit.android_movies_info_app.utils.safeCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryFirebase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AuthRepository {

    private val userRef = firestore.collection("user")

    override suspend fun currentUser(): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val user = userRef.document(firebaseAuth.currentUser!!.uid).get().await().toObject(
                    User::class.java)
                Resource.success(user!!)
            }
        }
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = userRef.document(result.user?.uid!!).get().await().toObject(User::class.java)!!
                Resource.success(user)
            }
        }
    }

    override suspend fun createUser(
        userName: String,
        userEmail: String,
        userPhone: String,
        userLoginPass: String,
        profilePictureUri: Uri?
    ): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmail, userLoginPass).await()
                val userId = registrationResult.user?.uid!!

                // Uploading profile picture to FirebaseStorage to obtain the URL for FireStore
                var profilePictureUrl: String? = null
                profilePictureUri?.let { uri ->
                    val profilePictureRef = storage.reference.child("profile_pictures/$userId.jpg")
                    profilePictureRef.putFile(uri).await()
                    profilePictureUrl = profilePictureRef.downloadUrl.await().toString()
                }

                val finalProfilePictureUrl = profilePictureUrl ?: DEFAULT_PROFILE_IMAGE_URL

                val newUser = User(userName, userEmail, userPhone, finalProfilePictureUrl)


                userRef.document(userId).set(newUser).await()
                Resource.success(newUser)
            }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
