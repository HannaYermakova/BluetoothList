package by.aermakova.bluetoothlist.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import by.aermakova.bluetoothlist.R
import by.aermakova.bluetoothlist.data.BluetoothDeviceModel
import by.aermakova.bluetoothlist.ui.PAIRED_TYPE

@BindingAdapter("app:group_title")
fun setTitleOfGroup(text: TextView, deviceModel: BluetoothDeviceModel?) {
    if (deviceModel != null) {
        text.text = when (deviceModel.type) {
            PAIRED_TYPE -> text.resources.getText(R.string.paired_devices)
            else -> text.resources.getText(R.string.discovered_devices)
        }
    }
}