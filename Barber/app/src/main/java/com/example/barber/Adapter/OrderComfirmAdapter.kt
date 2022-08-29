package com.example.barber.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Model.ProductInBill
import com.example.barber.R
import java.text.DecimalFormat

internal class OrderComfirmAdapter(listProduct : ArrayList<ProductInBill>, context : Context)
    : RecyclerView.Adapter<OrderComfirmAdapter.OrderComfirmViewHolder>() {
    private var list = listProduct
    private var mContext = context
    private lateinit var listener : OrderComfirmAdapter.onItemClicklistener
    internal inner class OrderComfirmViewHolder(itemView : View, listener : OrderComfirmAdapter.onItemClicklistener)
        : RecyclerView.ViewHolder(itemView){
        var imv : ImageView
        var imvDestroy : ImageView
        var tvState : TextView
        var tvName : TextView
        var tvPrice : TextView
        var tvTotalPrice : TextView
        var tvTotalProduct : TextView
        var layout : ConstraintLayout
        init {
            imv = itemView.findViewById(R.id.orderStateItemImv)
            imvDestroy = itemView.findViewById(R.id.orderStateItem_imvDes)
            tvName = itemView.findViewById(R.id.orderStateItem_nameProduct)
            tvPrice = itemView.findViewById(R.id.orderStateItem_priceProduct)
            tvState = itemView.findViewById(R.id.orderState_tv_state)
            tvTotalPrice = itemView.findViewById(R.id.orderStateItem_totalPrice)
            tvTotalProduct = itemView.findViewById(R.id.orderStateItem_countProduct)
            layout = itemView.findViewById(R.id.item_layout)
            itemView.setOnClickListener{
                listener.onClickView(adapterPosition)
            }


        }
    }
    interface onItemClicklistener{
        fun onClickView(position : Int)
    }

    fun setOnClickListener(listener: OrderComfirmAdapter.onItemClicklistener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderComfirmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_state,parent,false)
        return OrderComfirmViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: OrderComfirmViewHolder, position: Int) {
        var p = list.get(position)
        holder.tvState.text = "Đã xác nhận"
        holder.tvName.text = p.tenSanPham
        var giaTien = p.giaTien
        var tt : Double = p.giaTien * p.soLuong.toDouble()
        var decimalFormat = DecimalFormat("###,###,###")
        var price = decimalFormat.format(giaTien)
        var totalPrice = decimalFormat.format(tt)
        holder.tvPrice.text = "Giá tiền: " + price.toString() + " vnd"
        holder.tvTotalPrice.text = "Tồng tiền: "+ totalPrice.toString() + " vnd"
        holder.tvTotalProduct.text = p.soLuong.toString() + " sản phẩm"
        Glide.with(mContext).load(p.imgProduct).into(holder.imv)
        holder.imvDestroy.visibility = View.INVISIBLE
        holder.layout.setOnClickListener{
            listener.onClickView(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}