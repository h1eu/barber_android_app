package com.example.barber.Adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Model.Store
import com.example.barber.R

class StoreListAdapter(listStore : ArrayList<Store>) : RecyclerView.Adapter<StoreListAdapter.StoreListViewHolder>() {
    private var list = listStore
    class StoreListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvTitle : TextView
        var tvAddress : TextView
        init {
            tvTitle = itemView.findViewById(R.id.item_store_title)
            tvAddress = itemView.findViewById(R.id.item_store_address)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store_in_store_list,parent,false)
        return StoreListViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreListViewHolder, position: Int) {
        var store = list.get(position)
        holder.tvAddress.text = store.diaChi
        var index = holder.tvAddress.text.indexOf(",")
        var title = holder.tvAddress.text.substring(0,index)
        holder.tvTitle.text = "Barber ${title}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

}