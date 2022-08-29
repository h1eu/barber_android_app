package com.example.barber.Model

import com.google.gson.annotations.SerializedName

class Account(
    @SerializedName("maTaiKhoan")val maTaiKhoan : Int,
    @SerializedName("tenTaiKhoan")val tenTaiKhoan : String,
    @SerializedName("matKhau")val matKhau : String,
    @SerializedName("vaiTro")val vaiTro : String)
{

}