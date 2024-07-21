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
    private lateinit var previousLanguage: String
    private var previousOrientation: Int = Configuration.ORIENTATION_UNDEFINED
    private var restartDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previousLanguage = Locale.getDefault().language
        previousOrientation = resources.configuration.orientation
    }



    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentOrientation = newConfig.orientation
        if (currentOrientation != previousOrientation) {
            previousOrientation = currentOrientation
            if (restartDialog?.isShowing == true) {

                val currentLanguage = newConfig.locales[0].language
                if (currentLanguage != previousLanguage) {
                    previousLanguage = currentLanguage
                    showRestartDialog(this)
                }
            }

        }else{
            val currentLanguage = newConfig.locales[0].language
            if (currentLanguage != previousLanguage) {
                previousLanguage = currentLanguage
                showRestartDialog(this)
            }
        }
    }

    private fun showRestartDialog(context: Context) {
        if (restartDialog?.isShowing == true) {
            restartDialog?.dismiss()
            restartDialog = null
        }
        else {
            restartDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.language_change))
                .setCancelable(false)
                .setMessage(getString(R.string.to_apply_changes_please_restart_the_app))
                .setPositiveButton(getString(R.string.restart)) { _, _ ->
                    ProcessPhoenix.triggerRebirth(context)
                }
                .show()
        }
    }
}