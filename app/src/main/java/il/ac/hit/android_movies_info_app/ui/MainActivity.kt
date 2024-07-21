package il.ac.hit.android_movies_info_app.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.processphoenix.ProcessPhoenix
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
            showRestartDialog(this)
        }
    }

    private fun showRestartDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.language_change))
            .setCancelable(false)
            .setMessage(getString(R.string.to_apply_changes_please_restart_the_app))
            .setPositiveButton(getString(R.string.restart)) { _, _ ->
                ProcessPhoenix.triggerRebirth(context)
            }
            .show()
    }
}