package il.ac.hit.android_movies_info_app.ui

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import il.ac.hit.android_movies_info_app.R
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var previousLocale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previousLocale = Locale.getDefault()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val currentLocale = newConfig.locales[0]
        if (currentLocale != previousLocale) {
            previousLocale = currentLocale
            RestartDialogFragment.show(this)
        }
    }

    override fun onResume() {
        super.onResume()
        val currentLocale = Locale.getDefault()
        if (currentLocale != previousLocale) {
            RestartDialogFragment.show(this)
        }
    }
}
