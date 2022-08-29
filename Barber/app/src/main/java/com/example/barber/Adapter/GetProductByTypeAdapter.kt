package com.example.barber.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Model.Product
import com.example.barber.R
import java.text.DecimalFormat

internal class GetProductByTypeAdapter(listTypeProduct : ArrayList<Product>, context : Context) :
    RecyclerView.Adapter<GetProductByTypeAdapter.GetProductByTypeViewHolder>() {
    private var list = listTypeProduct
    private var mContext = context
    private lateinit var listener : GetProductByTypeAdapter.onItemClicklistener

    internal  inner class GetProductByTypeViewHolder(itemView: View,listener: onItemClicklistener) : RecyclerView.ViewHolder(itemView) {
        var cvItem: CardView
        var tvNameItem: TextView
        var tvPriceItem: TextView
        var imgItem: ImageView

        init {
            cvItem = itemView.findViewById(R.id.item_cardView)
            tvNameItem = itemView.findViewById(R.id.item_tv_productName)
            tvPriceItem = itemView.findViewById(R.id.item_tv_productPrice)
            imgItem = itemView.findViewById(R.id.item_imv)
            itemView.setOnClickListener{
                listener.onClickView(adapterPosition)
            }
        }
    }
    interface onItemClicklistener{
        fun onClickView(position : Int)
    }
    fun setOnClickListener(listener: GetProductByTypeAdapter.onItemClicklistener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetProductByTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return GetProductByTypeViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: GetProductByTypeViewHolder, position: Int) {
        var product = list.get(position)
        holder.tvNameItem.text = product.tenSanPham
        var decimalFormat = DecimalFormat("###,###,###")
        var price = decimalFormat.format(product.giaTien)
        holder.tvPriceItem.text = price.toString() + " vnd"
        Glide.with(mContext).load(product.imgProduct).into(holder.imgItem)
        holder.cvItem.setOnClickListener{
            listener.onClickView(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}