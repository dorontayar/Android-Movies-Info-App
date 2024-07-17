package il.ac.hit.android_movies_info_app.utils

import il.ac.hit.android_movies_info_app.BuildConfig.TMDB_API_KEY
import il.ac.hit.android_movies_info_app.BuildConfig.YOUTUBE_API_KEY

class Constants {

    companion object {
        private const val API_VERSION = "3"
        const val BASE_URL = "https://api.themoviedb.org/$API_VERSION/"
        const val API_KEY = TMDB_API_KEY
        const val YT_API_KEY = YOUTUBE_API_KEY

        const val IMAGE_TYPE_W45 = "https://image.tmdb.org/t/p/w45/"
        const val IMAGE_TYPE_W92 = "https://image.tmdb.org/t/p/w92/"
        const val IMAGE_TYPE_W154 = "https://image.tmdb.org/t/p/w154/"
        const val IMAGE_TYPE_W185 = "https://image.tmdb.org/t/p/w185/"
        const val IMAGE_TYPE_W300 = "https://image.tmdb.org/t/p/w300/"
        const val IMAGE_TYPE_W500 = "https://image.tmdb.org/t/p/w500/"
        const val IMAGE_TYPE_ORIGINAL = "https://image.tmdb.org/t/p/original/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

        const val DEFAULT_PROFILE_IMAGE_URL = "drawable/ic_profile_placeholder"

    }
}