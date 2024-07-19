package il.ac.hit.android_movies_info_app.data.remote_db


import il.ac.hit.android_movies_info_app.HiltApp
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T>
            getResult(call : suspend () -> Response<T>) : Resource<T> {

        try {
            val result  = call()
            if(result.isSuccessful) {
                val body = result.body()
                if(body != null) return  Resource.success(body)
            }
            return Resource.error(
                HiltApp.getContext()?.getString(R.string.network_call_has_failed_for_the_following_reason) +
                    "${result.message()} ${result.code()}")
        }catch (e : Exception) {
            return Resource.error(
                HiltApp.getContext()?.getString(R.string.network_call_has_failed_for_the_following_reason2)
                    + (e.localizedMessage ?: e.toString()))
        }
    }
}