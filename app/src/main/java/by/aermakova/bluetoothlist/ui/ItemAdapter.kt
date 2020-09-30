package by.aermakova.bluetoothlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.bluetoothlist.R
import by.aermakova.bluetoothlist.data.BluetoothDeviceModel
import by.aermakova.bluetoothlist.databinding.BluetoothDeviceItemBinding
import by.aermakova.bluetoothlist.databinding.TitleLayoutBinding
import java.util.ArrayList

class ItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemsList = arrayListOf<BluetoothDeviceModel>()

    fun updateData(list: List<BluetoothDeviceModel>) {
        val arrayList = ArrayList<BluetoothDeviceModel>(list).apply {
            add(BluetoothDeviceModel("", "", PAIRED_TYPE_TITLE))
            add(BluetoothDeviceModel("", "", DISCOVERED_TYPE_TITLE))
            sortBy { it.type }
        }
        val diffResult = DiffUtil.calculateDiff(ItemDiffUtil(itemsList, arrayList))
        setData(arrayList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setData(list: List<BluetoothDeviceModel>) {
        itemsList.clear()
        itemsList.addAll(list)
    }

    class ItemViewHolder(val binding: BluetoothDeviceItemBinding) : RecyclerView.ViewHolder(binding.root)

    class TitleViewHolder(val binding: TitleLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            PAIRED_TYPE -> {
                val binding: BluetoothDeviceItemBinding = DataBindingUtil.inflate(inflater, R.layout.bluetooth_device_item, parent, false)
                ItemViewHolder(binding)
            }
            DISCOVERED_TYPE -> {
                val binding: BluetoothDeviceItemBinding = DataBindingUtil.inflate(inflater, R.layout.bluetooth_device_item, parent, false)
                ItemViewHolder(binding)
            }
            else -> {
                val binding: TitleLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.title_layout, parent, false)
                TitleViewHolder(binding)
            }

        }
    }

    override fun getItemCount() = itemsList.size

    override fun getItemViewType(position: Int): Int {
        return itemsList[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList[position]
        when (holder.itemViewType) {
            PAIRED_TYPE   -> (holder as ItemViewHolder).binding.bluetoothDevice = item
            DISCOVERED_TYPE -> (holder as ItemViewHolder).binding.bluetoothDevice = item
            else -> (holder as TitleViewHolder).binding.titleDevices = item
        }
    }
}

class ItemDiffUtil(
    private val oldList: List<BluetoothDeviceModel>,
    private val newList: List<BluetoothDeviceModel>
) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].type == newList[newItemPosition].type

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }
}