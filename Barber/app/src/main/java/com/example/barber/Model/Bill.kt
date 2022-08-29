package com.example.barber.Model

import java.io.Serializable

class Bill(
    val maDonHang : Int,
    val tongTien : Double,
    val diaChiGiaoHang : String,
    val tinhTrangDonHang : String,
    val phuongThucThanhToan : String,
    val soDienThoaiNhanHang : String,
    val maSanPham : String,
    val maNguoiDung : String
) : Serializable{
}