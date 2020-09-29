package by.aermakova.bluetoothlist.data

import android.bluetooth.BluetoothDevice

data class BluetoothDeviceModel(
    val title: String,
    val hardwareAddress: String,
    val type : Int
)

fun BluetoothDevice.toModel(type: Int) : BluetoothDeviceModel{
    return BluetoothDeviceModel(name?: "No name", address?: "No address", type)
}