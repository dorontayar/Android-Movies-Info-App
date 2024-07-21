package il.ac.hit.android_movies_info_app.ui.restart_dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.jakewharton.processphoenix.ProcessPhoenix
import il.ac.hit.android_movies_info_app.R

class RestartDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false // Ensure the dialog cannot be canceled
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.language_change))
            .setMessage(getString(R.string.to_apply_changes_please_restart_the_app))
            .setPositiveButton(getString(R.string.restart)) { _, _ ->
                ProcessPhoenix.triggerRebirth(requireContext())
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        // Do nothing to ensure the dialog cannot be canceled
    }

    companion object {
        const val TAG = "RestartDialogFragment"

        fun show(context: Context) {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val existingFragment = fragmentManager.findFragmentByTag(TAG)
            if (existingFragment == null) {
                val dialog = RestartDialogFragment()
                dialog.show(fragmentManager, TAG)
            }
        }
    }
}
