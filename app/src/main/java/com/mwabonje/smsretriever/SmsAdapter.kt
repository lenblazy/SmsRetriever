package com.mwabonje.smsretriever

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mwabonje.smsretriever.databinding.AdapterSmsBinding

class SmsAdapter(
    private val list: MutableList<String>,
) : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {

    fun setSmsList(updatedList: List<String>) {
        val diffResult = DiffUtil.calculateDiff(CharactersDiffCallback(list, updatedList))
        list.clear()
        list.addAll(updatedList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    class ViewHolder(private val binding: AdapterSmsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.apply {
                tvName.text = item
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterSmsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class CharactersDiffCallback(
    private val oldList: List<String>,
    private val newList: List<String>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}
