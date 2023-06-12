package com.example.companiesapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.companiesapplication.shared.ItemModel
import com.example.companiesapplication.R

class RV_Adapter(


    private val items:MutableList<ItemModel>,
    private val onItemClick:(ItemModel)->Unit,
    private val onLongPress:(ItemModel)->Unit

) : RecyclerView.Adapter<RV_Adapter.MyViewHolder>() {






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     return MyViewHolder(
         LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent, false)

     )
    }


    fun addItem(item: ItemModel){
//        items.add(item)
//
        notifyItemInserted(items.size - 1)
    }
    fun updateItem(position: Int){
//        items[position] = item
        notifyItemChanged(position)
    }
    fun deleteItem(index:Int){
        notifyItemRemoved(index)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val item = items[position]
        holder.name.text = item.name
        holder.status.text = item.status
        holder.number.text = "${item.id}"
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            onLongPress(item)
            true
        }

    }

    override fun getItemCount(): Int {
       return items.size
    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
       val name: TextView = itemView.findViewById(R.id.name)
        val status = itemView.findViewById<TextView>(R.id.status)
        val number = itemView.findViewById<TextView>(R.id.number)


    }



}