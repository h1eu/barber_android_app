package com.example.barber.Model

import java.io.Serializable

class Product (
    val maSanPham : Int,
    val tenSanPham : String,
    val giaTien : Double,
    val loai : String,
    val moTa : String,
    val imgProduct : String
) : Serializable{
}