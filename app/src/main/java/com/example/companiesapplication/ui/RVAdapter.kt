package com.example.companiesapplication.ui

import android.graphics.Color
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.companiesapplication.R
import com.example.companiesapplication.shared.extention_funtions.SetSpanIndex.setSpanIndex
import com.example.companiesapplication.shared.models.ItemModel


class RVAdapter(


    private val items:MutableList<ItemModel>,
    private val onItemClick:(ItemModel)->Unit,
    private val onLongPress:(ItemModel)->Unit

) : RecyclerView.Adapter<RVAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     return MyViewHolder(
         LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent, false)

     )
    }



    fun addItem(item: ItemModel){

        notifyItemInserted(items.size - 1)
    }
    fun updateItem(position: Int){
        notifyItemChanged(position)
    }
    fun deleteItem(index:Int){
        notifyItemRemoved(index)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val item = items[position]

        holder.status.text = item.status
        holder.number.text = "${item.id}"
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        if (item.isHighLighted){
            try {
                val spanIndex = item.name.setSpanIndex(item.textToHighLight)
                val str = SpannableString(item.name)
                str.setSpan(BackgroundColorSpan(Color.CYAN), spanIndex.startIndex, spanIndex.endIndex, 0)
                holder.name.text = str
            }catch (e:Exception){
                Log.e("span", e.message, e)
            }

        }else{
            holder.name.text = item.name
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
        val status: TextView = itemView.findViewById(R.id.status)
        val number: TextView = itemView.findViewById(R.id.number)

    }


}