package com.example.barber.Model

import java.io.Serializable

class ProductInCart (
    val maGioHang : Int,
    val maSanPham : Int,
    val tenSanPham : String,
    var soLuong : Int,
    val giaTien : Double,
    var trangThaiMua : Boolean,
    val imgProduct : String,
) : Serializable {
    fun setChecked(checked : Boolean) {
        trangThaiMua = checked
    }
}