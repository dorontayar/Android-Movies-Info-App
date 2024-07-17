package il.ac.hit.android_movies_info_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApp:Application() {
    override fun onCreate() {
        super.onCreate()

    }
}