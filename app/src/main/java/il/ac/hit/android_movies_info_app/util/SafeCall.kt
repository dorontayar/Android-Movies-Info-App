import il.ac.hit.android_movies_info_app.model.User
import il.ac.hit.android_movies_info_app.util.Resource

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}