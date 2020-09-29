package by.aermakova.bluetoothlist.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import by.aermakova.bluetoothlist.R


class LocationSettingDialog(private val listener: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_name)
            .setPositiveButton(
                R.string.ok
            ) { _, _ ->
                listener.invoke()
                dismiss()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
            .setMessage(R.string.dialog_message_text)
        return dialog.create()
    }
}