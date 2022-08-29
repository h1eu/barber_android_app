package com.example.barber.Adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Interface.ClickItemProduct
import com.example.barber.Interface.ClickItemTypeProduct
import com.example.barber.Model.Product
import com.example.barber.R
import java.text.DecimalFormat

class ListProductAdapter(listProduct : ArrayList<Product>, context : Context) :
    RecyclerView.Adapter<ListProductAdapter.ListProductAdapterViewHolder>() {
    private var mContext = context
    private var list = listProduct
    private lateinit var listener : ClickItemProduct
    class ListProductAdapterViewHolder(itemView : View, listener: ClickItemProduct) : RecyclerView.ViewHolder(itemView){
        var cvItem : CardView
        var tvNameItem : TextView
        var tvPriceItem : TextView
        var imgItem : ImageView
        init {
            cvItem = itemView.findViewById(R.id.item_cardView)
            tvNameItem = itemView.findViewById(R.id.item_tv_productName)
            tvPriceItem = itemView.findViewById(R.id.item_tv_productPrice)
            imgItem = itemView.findViewById(R.id.item_imv)
            itemView.setOnClickListener{
                listener.onItemProductClick(adapterPosition)
            }
        }

    }

    fun setOnClickListener(listener: ClickItemProduct){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListProductAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return ListProductAdapterViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ListProductAdapterViewHolder, position: Int) {
        var product = list.get(position)
        holder.tvNameItem.text = product.tenSanPham
        var decimalFormat = DecimalFormat("###,###,###")
        var price = decimalFormat.format(product.giaTien)
        holder.tvPriceItem.text = price.toString() + " vnd"
        Glide.with(mContext).load(product.imgProduct).into(holder.imgItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}