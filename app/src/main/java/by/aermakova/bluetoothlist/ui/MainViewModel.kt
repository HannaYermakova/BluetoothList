package by.aermakova.bluetoothlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.aermakova.bluetoothlist.data.BluetoothDeviceModel

class MainViewModel : ViewModel() {

    private val _listPaired = mutableSetOf<BluetoothDeviceModel>()
    private val _listPairedLD = MutableLiveData<MutableList<BluetoothDeviceModel>>()
    val listPaired: LiveData<MutableList<BluetoothDeviceModel>>
        get() = _listPairedLD

    fun updatePaired(devices: List<BluetoothDeviceModel>) {
        _listPaired.addAll(devices)
        _listPairedLD.value = convertSetToList()
    }

    fun updatePaired(device: BluetoothDeviceModel) {
        _listPaired.add(device)
        _listPairedLD.value = convertSetToList()
    }

    private fun convertSetToList() : MutableList<BluetoothDeviceModel>{
        return arrayListOf<BluetoothDeviceModel>().apply {
            addAll(_listPaired)
        }
    }
}