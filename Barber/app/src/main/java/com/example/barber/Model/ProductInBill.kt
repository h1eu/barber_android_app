package com.example.barber.Model

import java.io.Serializable

class ProductInBill(
    val maDonHang : Int,
    val maSanPham : Int,
    val tenSanPham : String,
    var soLuong : Int,
    val giaTien : Double,
    var trangThaiDatHang : String,
    val imgProduct : String,
) : Serializable {

}