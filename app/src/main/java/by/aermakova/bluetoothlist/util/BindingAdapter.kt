package by.aermakova.bluetoothlist.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.bluetoothlist.R
import by.aermakova.bluetoothlist.data.BluetoothDeviceModel
import by.aermakova.bluetoothlist.ui.BluetoothDeviceModelWrapper
import by.aermakova.bluetoothlist.ui.GenericRecyclerAdapter
import by.aermakova.bluetoothlist.ui.ItemAdapter
import by.aermakova.bluetoothlist.ui.PAIRED_TYPE_TITLE

@BindingAdapter("app:group_title")
fun setTitleOfGroup(text: TextView, deviceModel: BluetoothDeviceModel?) {
    deviceModel?.let {
        text.text = when (deviceModel.type) {
            PAIRED_TYPE_TITLE -> text.resources.getText(R.string.paired_devices)
            else -> text.resources.getText(R.string.discovered_devices)
        }
    }
}

@BindingAdapter("app:items")
fun setListItems(recyclerView: RecyclerView, listItems: List<BluetoothDeviceModel>?) {
    listItems?.let {

//        (recyclerView.adapter as? ItemAdapter)?.updateData(listItems)
        (recyclerView.adapter as? GenericRecyclerAdapter<BluetoothDeviceModel, BluetoothDeviceModelWrapper>)?.updateData(listItems)
    }
}