package il.ac.hit.android_movies_info_app.utils

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.error(e.message ?: "An unknown Error Occurred")
    }
}