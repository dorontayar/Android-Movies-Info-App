package il.ac.hit.android_movies_info_app.data.model.favorite_movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.BelongsToCollection

class BelongsToCollectionConverter {
    @TypeConverter
    fun fromBelongsToCollection(collection: BelongsToCollection?): String? {
        if (collection == null) {
            return null
        }
        val gson = Gson()
        val type = BelongsToCollection::class.java
        return gson.toJson(collection, type)
    }

    @TypeConverter
    fun toBelongsToCollection(collectionString: String?): BelongsToCollection? {
        if (collectionString == null) {
            return null
        }
        val gson = Gson()
        val type = BelongsToCollection::class.java
        return gson.fromJson(collectionString, type)
    }
}
