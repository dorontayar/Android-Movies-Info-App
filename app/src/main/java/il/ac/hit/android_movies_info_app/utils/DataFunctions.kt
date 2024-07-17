package il.ac.hit.android_movies_info_app.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun <T,A> performFetchingAndSaving(localDbFetch: () -> LiveData<T>,
                                    remoteDbFetch: suspend () ->Resource<A>,
                                    localDbSave: suspend (A) -> Unit) : LiveData<Resource<T>> =

    liveData(Dispatchers.IO) {

        emit(Resource.loading())

        val source = localDbFetch().map { Resource.success(it) }
        emitSource(source)

        val fetchResource = remoteDbFetch()

        if(fetchResource.status is Success)
            localDbSave(fetchResource.status.data!!)

        else if(fetchResource.status is Error){
            emit(Resource.error(fetchResource.status.message))
            emitSource(source)
        }
    }

fun <T> performFetching(remoteDbFetch: suspend () -> Resource<T>): LiveData<Resource<T>> =

    liveData(Dispatchers.IO) {

        emit(Resource.loading())

        val fetchResource = remoteDbFetch()

        if (fetchResource.status is Success) {
            emit(Resource.success(fetchResource.status.data!!))
        } else if (fetchResource.status is Error) {
            emit(Resource.error(fetchResource.status.message))
        }
}
fun <T> performFetchingFromDb(dbFetch: suspend () -> LiveData<T>): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {

        emit(Resource.loading())

        val source = dbFetch().map { Resource.success(it) }
        emitSource(source)
    }

fun <A> performSaving(
    localDbSave: suspend (A) -> Unit,
    data: A
): LiveData<Resource<Unit>> =

    liveData(Dispatchers.IO) {

    emit(Resource.loading())

    try {
        localDbSave(data)
        emit(Resource.success(Unit))
    } catch (e: Exception) {
        emit(Resource.error(e.message ?: "Error saving data"))
    }
}

fun performDeletion(deleteAction: suspend () -> Unit): LiveData<Resource<Unit>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())

        try {
            withContext(Dispatchers.IO) {
                deleteAction()
            }
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Error deleting data"))
        }
    }
