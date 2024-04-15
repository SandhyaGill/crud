package com.sandhya.crud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandhya.crud.databinding.RvItemBinding

class RecyclerViewAdapter(var rvList: MutableList<Item>, var onClickInterface: OnClickInterface) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(var binding : RvItemBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     return ViewHolder(RvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return rvList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.apply {
                var item = rvList[position]
                tvName.text = rvList[position].name
                tvDate.text = rvList[position].date
                itemView.setOnClickListener{
                    onClickInterface.onClick(item)
                }
                tvDelete.setOnClickListener{
                    onClickInterface.onDeleteClick(item)
                }
            }
        }
    }

    fun addItem(item: Item) {
        rvList.add(item)
        notifyItemInserted(rvList.size - 1)
    }

    fun updateItem(position: Int, item: Item) {
        rvList[position] = item
        notifyItemChanged(position)
    }
}