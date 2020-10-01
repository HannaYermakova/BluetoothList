package by.aermakova.bluetoothlist.ui

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import by.aermakova.bluetoothlist.R
import by.aermakova.bluetoothlist.data.BluetoothDeviceModel
import by.aermakova.bluetoothlist.data.toModel
import by.aermakova.bluetoothlist.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

const val PAIRED_TYPE_TITLE = 0
const val PAIRED_TYPE = 1
const val DISCOVERED_TYPE_TITLE = 2
const val DISCOVERED_TYPE = 3

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var bluetoothAdapter: BluetoothAdapter? = null

    //    private val devicesAdapter = ItemAdapter()
    private lateinit var devicesAdapter: GenericRecyclerAdapter<BluetoothDeviceModel, BluetoothDeviceModelWrapper>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setAdapter()
        checkBluetoothEnable()
    }

    private fun setAdapter() {
        with(binding.devicesRecycler) {
            adapter = GenericRecyclerAdapter(object : GenericRecyclerAdapter.GenericContract<BluetoothDeviceModel, BluetoothDeviceModelWrapper>{
                override fun getBinding(viewType: Int): ViewDataBinding {

                }

                override fun getBR(viewType: Int): Int {

                }

            })
            val manager = LinearLayoutManager(this@MainActivity)
            layoutManager = manager
        }
    }

    private fun checkLocationEnabled() {
        val locationRequest = LocationRequest.create()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)

        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            registerDiscoveryReceiver()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this@MainActivity, REQUEST_LOCATION)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }

/*        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            val dialog = LocationSettingDialog {
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    REQUEST_LOCATION
                )
            }
            dialog.show(supportFragmentManager, TAG)
        } else {
            registerDiscoveryReceiver()
        }*/
    }

    private fun checkBluetoothEnable() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter?.isEnabled == false) {
            startActivityForResult(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                REQUEST_ENABLE_BT
            )
        } else {
            findPairedDevices()
            checkLocationPermission()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let { viewModel.updatePaired(device.toModel(DISCOVERED_TYPE)) }
                }
            }
        }
    }

    private fun registerDiscoveryReceiver() {
        bluetoothAdapter?.startDiscovery()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    private fun findPairedDevices() {
        val pairedDevices = bluetoothAdapter?.bondedDevices
        val listPaired = pairedDevices?.map { device -> device.toModel(PAIRED_TYPE) }
        listPaired?.let { viewModel.updatePaired(listPaired) }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter?.cancelDiscovery()
        unregisterReceiver(receiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationEnabled()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BT -> {
                if (resultCode == Activity.RESULT_OK) {
                    findPairedDevices()
                    checkLocationPermission()
                } else finish()
            }
            REQUEST_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    registerDiscoveryReceiver()
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        } else
            checkLocationEnabled()
    }

    companion object {
        private const val REQUEST_LOCATION = 123
        private const val TAG = "dialog_tag"
        private const val PERMISSION_REQUEST_LOCATION = 654
        private const val REQUEST_ENABLE_BT = 258
    }
}

abstract class BluetoothDeviceModelWrapper(
    data: BluetoothDeviceModel
) : GenericItemWrapper<BluetoothDeviceModel>(data)