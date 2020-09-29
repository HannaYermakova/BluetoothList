package by.aermakova.bluetoothlist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.bluetoothlist.R
import by.aermakova.bluetoothlist.data.BluetoothDeviceModel
import by.aermakova.bluetoothlist.databinding.BluetoothDeviceItemBinding
import java.util.ArrayList

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val itemsList = arrayListOf<BluetoothDeviceModel>()

    fun updateData(list: List<BluetoothDeviceModel>) {
        val arrayList = ArrayList<BluetoothDeviceModel>(list).apply { sortedBy { it.type } }
        val diffResult = DiffUtil.calculateDiff(ItemDiffUtil(itemsList, arrayList))
        setData(arrayList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setData(list: List<BluetoothDeviceModel>) {
        itemsList.clear()
        itemsList.addAll(list)
    }

    class ItemViewHolder(val binding: BluetoothDeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: BluetoothDeviceItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.bluetooth_device_item, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.bluetoothDevice = itemsList[position]
        if (checkTitle(position)) {
            holder.binding.devicesTitle.visibility = View.VISIBLE
        } else {
            holder.binding.devicesTitle.visibility = View.GONE
        }
    }

    private fun checkTitle(position: Int): Boolean {
        return with(itemsList) {
            (position == 0 && itemsList[position].type == PAIRED_TYPE) ||
                    (position in 1 until size
                            && itemsList[position - 1].type == PAIRED_TYPE
                            && itemsList[position].type == DISCOVERED_TYPE)
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