package com.example.barber.Adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Api.ApiServices
import com.example.barber.Interface.ClickItemHairdresserListener
import com.example.barber.Interface.ClickItemHourListener
import com.example.barber.MainActivity
import com.example.barber.Model.BarberMeeting
import com.example.barber.Model.Constant
import com.example.barber.Model.LoadingDialog
import com.example.barber.Model.ProductInCart
import com.example.barber.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

internal class ProductInCartAdapter(listProductInCart : ArrayList<ProductInCart>,context : Context)
    : RecyclerView.Adapter<ProductInCartAdapter.ProductInCartViewHolder>() {
    private var mContext = context
    private var list = listProductInCart
    private lateinit var listener : onItemClicklistener

    internal inner class ProductInCartViewHolder(itemView : View, listener: onItemClicklistener) : RecyclerView.ViewHolder(itemView){
        var cb : CheckBox
        var imv : ImageView
        var imvMinus : ImageView
        var imvAdd : ImageView
        var imvDele : ImageView
        var tvPName : TextView
        var tvPPrice : TextView
        var tvPTotal : TextView
        init {
            cb = itemView.findViewById(R.id.cartItem_cb)
            imv = itemView.findViewById(R.id.cartItemImv)
            tvPName = itemView.findViewById(R.id.cartItem_nameProduct)
            tvPPrice = itemView.findViewById(R.id.cartItem_priceProduct)
            imvMinus = itemView.findViewById(R.id.minusProduct)
            imvAdd = itemView.findViewById(R.id.addProduct)
            tvPTotal = itemView.findViewById(R.id.totalProduct)
            imvDele = itemView.findViewById(R.id.deleteProduct)
            itemView.setOnClickListener{
                listener.onClickView(adapterPosition,0)
            }

        }

    }


    interface onItemClicklistener{
        fun onClickView(position : Int,status : Int)
        fun onCheckBoxClick(position: Int, checked : Boolean)
    }


    fun setOnClickListener(listener: onItemClicklistener){
        this.listener = listener
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInCartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return ProductInCartViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ProductInCartViewHolder, position: Int) {

        var pIC = list.get(position)
        holder.tvPName.text = pIC.tenSanPham
        holder.tvPTotal.text = pIC.soLuong.toString()
        var tt : Double = pIC.giaTien * pIC.soLuong.toDouble()
        var decimalFormat = DecimalFormat("###,###,###")
        var price = decimalFormat.format(tt)
        holder.tvPPrice.text = price.toString() + " vnd"
        Glide.with(mContext).load(pIC.imgProduct).into(holder.imv)
        holder.cb.isChecked =  pIC.trangThaiMua

        holder.imvAdd.setOnClickListener{
            listener.onClickView(position,1)
        }

        holder.imvMinus.setOnClickListener{
            listener.onClickView(position,2)
        }

        holder.cb.setOnClickListener{
            listener.onCheckBoxClick(position,holder.cb.isChecked)
        }

        holder.imvDele.setOnClickListener{
            listener.onClickView(position,3)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



}



