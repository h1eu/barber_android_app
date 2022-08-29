package com.example.barber.Model

import java.io.Serializable

class Post
    (
    val maBaiViet : Int,
    val chuyenMuc : String,
    val tieuDe : String,
    val noiDung : String,
    val imgPost : String,
    val idImgPost : String,
    val maAdmin: Int
    ) : Serializable
{
}