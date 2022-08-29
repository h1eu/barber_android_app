package com.example.barber.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Model.ProductInCart
import com.example.barber.R
import java.text.DecimalFormat

class ProductInOrderAdapter(listProduct : ArrayList<ProductInCart>, context : Context) :
    RecyclerView.Adapter<ProductInOrderAdapter.ProductInOrderViewHolder>() {
    private var list = listProduct
    private var mContext = context
    class ProductInOrderViewHolder(itemview : View): RecyclerView.ViewHolder(itemview){
        var imgProduct : ImageView
        var tvNameP : TextView
        var tvPriceP : TextView
        var tvCountP : TextView
        var tvTotalPriceP : TextView

        init {
            imgProduct = itemview.findViewById(R.id.orderItemImv)
            tvNameP = itemview.findViewById(R.id.orderItem_nameProduct)
            tvPriceP = itemview.findViewById(R.id.orderItem_priceProduct)
            tvCountP = itemview.findViewById(R.id.orderItem_countProduct)
            tvTotalPriceP = itemview.findViewById(R.id.orderItem_totalPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_in_order,parent,false)
        return ProductInOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductInOrderViewHolder, position: Int) {
        var product = list.get(position)
        holder.tvNameP.text = product.tenSanPham
        var giaTien : Double = product.giaTien
        var decimalFormat = DecimalFormat("###,###,###")
        var priceP = decimalFormat.format(giaTien)
        holder.tvPriceP.text = "Giá : ${giaTien} vnd"
        holder.tvCountP.text = "( ${product.soLuong} sản phẩm)"
        var tongTien : Double = product.giaTien * product.soLuong.toDouble()
        var price = decimalFormat.format(tongTien)
        holder.tvTotalPriceP.text = "Giá : ${price} vnd"
        Glide.with(mContext).load(product.imgProduct).into(holder.imgProduct)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}