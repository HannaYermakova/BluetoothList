package by.aermakova.bluetoothlist.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class GenericItemWrapper<Item>(
    protected val data: Item
) {
    abstract fun getType(): Int
    abstract fun getLayout(): Int
    abstract fun getVarId(): Int
}

class GenericRecyclerAdapter<Item, Wrapper : GenericItemWrapper<Item>>(private val contract: GenericContract<Item, Wrapper>) :
    RecyclerView.Adapter<GenericRecyclerAdapter.GenericItemViewHolder<Wrapper>>() {

    class GenericItemViewHolder<Item>(
        private val binding: ViewDataBinding,
        private val varId: Int
    ) :
        RecyclerView.ViewHolder(binding.root), Binder<Item> {
        override fun bind(data: Item) {
            binding.setVariable(varId, data)
        }
    }

    internal interface Binder<Item> {
        fun bind(data: Item)
    }

    fun updateData(list: List<Wrapper>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    private val itemList = arrayListOf<Wrapper>()

    override fun getItemViewType(position: Int): Int = itemList[position].getType()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericItemViewHolder<Wrapper> {
/*        val inflater = LayoutInflater.from(parent.context)
        val layout: Int = contract.getLayoutByViewType(viewType)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layout, parent, false)
        val br = contract.getBRbyItemType(viewType)
        return GenericItemViewHolder(binding, br)*/
        return create(viewType)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: GenericItemViewHolder<Wrapper>, position: Int) {
        holder.bind(itemList[position])
    }

    fun <Wrapper> create(viewType: Int): GenericItemViewHolder<Wrapper> {
        return GenericItemViewHolder(contract.getBinding(viewType), contract.getBR(viewType))
}

interface GenericContract<Item, Wrapper : GenericItemWrapper<Item>> {
    fun getBinding(viewType: Int): ViewDataBinding
    fun getBR( viewType: Int): Int
}}

