package com.example.barber.Api

import com.example.barber.Model.*
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.InputStream
import java.io.InputStreamReader
import java.util.jar.JarEntry


interface ApiServices {
    companion object{
        var BASE_URL = "http://192.168.56.1"
       // var BASE_URL = "http://192.168.43.158"
     //   var BASE_URL = "http://45.77.170.42"
      //  var BASE_URL = "http://192.168.1.100"
        var gson = GsonBuilder().setLenient().create()
        fun create() : ApiServices {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }
    @FormUrlEncoded
    @POST("/barberapi/")
    fun login(
        @Field("tenTaiKhoan") tenTaiKhoan: String,
        @Field("matKhau") matKhau: String,
        @Field("action") action : String
        ) : Call<String>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun checkUsername(
        @Field("tenTaiKhoan") tenTaiKhoan: String,
        @Field("action") action : String
    ) : Call<Account>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getUser(
        @Field("tenTaiKhoan") tenTaiKhoan: String,
        @Field("action") action : String
    ) : Call<User>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun registerUser(
        @Field("tenTaiKhoan") tenTaiKhoan: String,
        @Field("matKhau") matKhau: String,
        @Field("hoVaTen") hoVaTen: String,
        @Field("soDienThoai") soDienThoai: String,
        @Field("cauHoiMatKhau") cauHoiMatKhau: String,
        @Field("action") action : String
    ) : Call<Unit>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun checkUserForgotPass(
        @Field("tenTaiKhoan") tenTaiKhoan: String,
        @Field("cauHoiMatKhau") cauHoiMatKhau: String,
        @Field("action") action : String
    ) : Call<String>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun resetPassword(
        @Field("tenTaiKhoan") tenTaiKhoan: String,
        @Field("matKhau") matKhau: String,
        @Field("action") action : String
    ) : Call<Unit>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getAllStore(
        @Field("action") action : String
    ) : Call<ArrayList<Store>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getHaidresser(
        @Field("maCuaHang") maCuaHang : String,
        @Field("action") action : String
    ) : Call<ArrayList<Hairdresser>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getAllHairdresser(
        @Field("action") action : String
    ) : Call<ArrayList<Hairdresser>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun createBarberMeeting(
        @Field("maNguoiDung") maNguoiDung: String,
        @Field("maThoCatToc") maThoCatToc: String,
        @Field("tenThoCatToc") tenThoCatToc: String,
        @Field("ngay") ngay : String,
        @Field("gio") gio : String,
        @Field("diaChiCuaHang") diaChiCuaHang: String,
        @Field("action") action : String
    ) : Call<Unit>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getListHistoryMeeting(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("action") action : String
    ) : Call<ArrayList<BarberMeeting>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun changeInfoUser(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("hoVaTen") hoVaTen : String,
        @Field("soDienThoai") soDienThoai : String,
        @Field("diaChi") diaChi : String,
        @Field("action") action : String
    ) : Call<Unit>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun changePassword(
        @Field("maTaiKhoan") maTaiKhoan : String,
        @Field("matKhauOld") matKhauOld : String,
        @Field("matKhauNew") matKhauNew : String,
        @Field("action") action : String
    ) : Call<String>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun checkHour(
        @Field("maThoCatToc") maThoCatToc : String,
        @Field("diaChiCuaHang") diaChiCuaHang : String,
        @Field("ngay") ngay : String,
        @Field("tinhTrangLichHen") tinhTrangLichHen : String,
        @Field("action") action : String
    ) : Call<ArrayList<BarberMeeting>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getAllTypeProduct(
        @Field("action") action : String
    ) : Call<ArrayList<String>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getAllProduct(
        @Field("action") action : String
    ) : Call<ArrayList<Product>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getProductByType(
        @Field("loai") loai : String,
        @Field("action") action : String
    ) : Call<ArrayList<Product>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun addProductToCart(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("maSanPham") maSanPham : String,
        @Field("tenSanPham") tenSanPham : String,
        @Field("soLuong") soLuong : String,
        @Field("giaTien") giaTien : String,
        @Field("imgProduct") imgProduct : String,
        @Field("action") action : String
    ) : Call<Boolean>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getInfoCart(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("action") action : String
    ) : Call<ArrayList<ProductInCart>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun cancelMeeting(
        @Field("maLichCatToc") maLichCatToc : String,
        @Field("action") action : String
    ) : Call<Boolean>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun updateCart(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("maSanPham") maSanPham : String,
        @Field("soLuong") soLuong : String,
        @Field("action") action : String
    ) : Call<Boolean>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun order(
        @Field("tongTien") tongTien : String,
        @Field("diaChiGiaoHang") diaChiGiaoHang : String,
        @Field("phuongThucThanhToan") phuongThucThanhToan : String,
        @Field("soDienThoaiNhanHang") soDienThoaiNhanHang : String,
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("maSanPham") maSanPham : String,
        @Field("action") action : String
    ) : Call<Boolean>



    @FormUrlEncoded
    @POST("/barberapi/")
    fun deleteProductAfterOrder(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("maSanPham") maSanPham : String,
        @Field("action") action : String
    ) : Call<Boolean>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun addProductInBill(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("maSanPham") maSanPham : String,
        @Field("tenSanPham") tenSanPham : String,
        @Field("giaTien") giaTien : String,
        @Field("soLuong") soLuong : String,
        @Field("imgProduct") imgProduct : String,
        @Field("action") action : String
    ) : Call<Boolean>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun deleteProductInCart(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("maSanPham") maSanPham : String,
        @Field("action") action : String
    ) : Call<Boolean>


    @FormUrlEncoded
    @POST("/barberapi/")
    fun getProductInOrder(
        @Field("maNguoiDung") maNguoiDung : String,
        @Field("trangThaiDatHang") trangThaiDatHang : String,
        @Field("action") action : String
    ) : Call<ArrayList<ProductInBill>>


    @FormUrlEncoded
    @POST("/barberapi/")
    fun getPost(
        @Field("chuyenMuc") chuyenMuc : String,
        @Field("action") action : String
    ) : Call<ArrayList<Post>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getOnePost(
        @Field("maBaiViet") maBaiViet : String,
        @Field("action") action : String
    ) : Call<Post>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun getOrder(
        @Field("maDonHang") maDonHang : String,
        @Field("action") action : String
    ) : Call<ArrayList<Bill>>


    @FormUrlEncoded
    @POST("/barberapi/")
    fun searchProduct(
        @Field("tenSanPham") tenSanPham : String,
        @Field("action") action : String
    ) : Call<ArrayList<Product>>

    @FormUrlEncoded
    @POST("/barberapi/")
    fun destroyOrder(
        @Field("maDonHang") maDonHang : String,
        @Field("action") action : String
    ) : Call<Boolean>




}