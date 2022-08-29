package com.example.barber.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Interface.ClickItemHairdresserListener
import com.example.barber.Interface.ClickItemTypeProduct
import com.example.barber.R

internal class TypeProductAdapter(listTypeProduct : ArrayList<String>)
    : RecyclerView.Adapter<TypeProductAdapter.TypeProductViewHolder>() {
    private var list = listTypeProduct
    private lateinit var listener : ClickItemTypeProduct
    internal  inner class TypeProductViewHolder(itemView : View, listener: ClickItemTypeProduct) : RecyclerView.ViewHolder(itemView){
        var cvItem : CardView
        var tvItem : TextView
        init {
            cvItem = itemView.findViewById(R.id.cv_typeProduct)
            tvItem = itemView.findViewById(R.id.tv_typeProduct)
            itemView.setOnClickListener{
                listener.onItemTypeProductClick(adapterPosition,tvItem.text.toString())
            }
        }
    }

    fun setOnClickListener(listener: ClickItemTypeProduct){
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeProductViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type_product,parent,false)
        return TypeProductViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: TypeProductViewHolder, position: Int) {
        var typeProduct = list.get(position)
        holder.tvItem.text = typeProduct
    }

    override fun getItemCount(): Int {
        return list.size
    }
}