package com.example.barber.Fragment.Store

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.JsonReader
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.ProductInOrderAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.*
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var rv : RecyclerView
    private lateinit var tvTotalPrice : TextView
    private lateinit var tvAdd : TextView
    private lateinit var tvPhone : TextView
    private lateinit var tvThanhToan : TextView
    private lateinit var cvAddUser : CardView
    private lateinit var cvAddEdit : CardView
    private lateinit var cvOrder : CardView
    private lateinit var listProduct : ArrayList<ProductInCart>
    private var totalPrice : ArrayList<Double> = arrayListOf()
    private var tPrice = 0.0
    private var add = ""
    private var thanhToan = "Thanh toán khi nhận hàng"
    private lateinit var product: Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        init(view)
        hideNaviBot()
        back()
        getUser()
        getlistProduct()
        setupRV()
        setAddress()
        setInfo()
        order()
        return view
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            var x = arguments
            product = x?.get("Product") as Product
            var status = x?.get("status")
            if(status!!.equals("fromCart")){
                var bundle = bundleOf(
                    "Product" to product,
                    "status" to "Store"
                )
                controller.navigate(R.id.action_orderFragment_to_cartFragment,bundle)
            }
            if(status!!.equals("view")){
                var bundle = bundleOf(
                    "Product" to product,
                    "status" to "fromView"
                )
                controller.navigate(R.id.action_orderFragment_to_cartFragment,bundle)
            }

        })
    }

    private fun order() {
        var loadingDialog = LoadingDialog(requireActivity())
        cvOrder.setOnClickListener(View.OnClickListener {
            var add = tvAdd.text.toString()
            var tt = tvThanhToan.text.toString()
            var ph = tvPhone.text.toString()
            if(TextUtils.isEmpty(add) || add.equals(" ") || listProduct.size == 0){
                Toast.makeText(context, "Thiếu thông tin", Toast.LENGTH_SHORT).show()
            }
            else{
                var action = "order"
                var action2 = "deleteProductAfterOrder"
                var action3 = "addProductInBill"

                for(i in 0 until listProduct.size) {
                    var x = listProduct.get(i).giaTien * listProduct.get(i).soLuong
                    ApiServices.create().order(
                        x.toString(),add, tt, ph,
                        listProduct.get(i).maSanPham.toString(),
                        Constant.KeyWithScreen.IdUser,
                        action
                    ).enqueue(
                        object : Callback<Boolean> {
                            override fun onResponse(
                                call: Call<Boolean>,
                                response: Response<Boolean>
                            ) {
                                var check = response.body()
                                if (check != null) {
                                    if (check == true) {
                                        ApiServices.create().addProductInBill(
                                            Constant.KeyWithScreen.IdUser,
                                            listProduct.get(i).maSanPham.toString(),
                                            listProduct.get(i).tenSanPham,
                                            listProduct.get(i).giaTien.toString(),
                                            listProduct.get(i).soLuong.toString(),
                                            listProduct.get(i).imgProduct,
                                            action3
                                        ).enqueue(
                                            object : Callback<Boolean> {
                                                override fun onResponse(
                                                    call: Call<Boolean>,
                                                    response: Response<Boolean>
                                                ) {
                                                    var check = response.body()
                                                    if (check != null) {
                                                        if (check == true) {
                                                            Log.e("them  ", "thanh cong" )
                                                            ApiServices.create().deleteProductAfterOrder(Constant.KeyWithScreen.IdUser,listProduct.get(i).maSanPham.toString(),action2).enqueue(
                                                                object : Callback<Boolean>{
                                                                    override fun onResponse(
                                                                        call: Call<Boolean>,
                                                                        response: Response<Boolean>
                                                                    ){
                                                                        var check = response.body()
                                                                        if (check != null){
                                                                            if (check == true){
                                                                                Log.e("xoa trong gio hang tai vi tri",i.toString())
                                                                                if(i == listProduct.size - 1){
                                                                                    controller.navigate(R.id.action_orderFragment_to_listOrderFragment)
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                                                        Log.e("xoa loi tai vi tri : ", i.toString() + " loi la: " + t.toString())
                                                                    }

                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                                    Log.e("them loisadsdasd: ", " so " + i.toString() + " " + t.toString())

                                                }

                                            }
                                        )
                                    }

                                }
                            }

                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                Log.e("tao loi: ", t.toString())
                            }

                        }
                    )
                }

            }
        })
    }

    private fun setInfo() {
        tvThanhToan.text = "Thanh toán khi nhận hàng"
        tvPhone.text = Constant.KeyWithScreen.phoneUser
    }

    private fun getUser() {
        val action = "getUser"
        ApiServices.create().getUser(Constant.KeyWithScreen.username,action).enqueue(
            object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    var user = response.body()
                    if(user != null){
                        add = user.diaChi
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {

                }

            }
        )
    }

    private fun setAddress() {
        cvAddEdit.setOnClickListener(View.OnClickListener {
            var builder = AlertDialog.Builder(requireActivity())
            var view = View.inflate(requireActivity(),R.layout.dialog_comfirm,null)
            builder.setView(view)
            var title : TextView = view.findViewById(R.id.dialogTitle)
            var msg : TextView = view.findViewById(R.id.dialogMsg)
            var editText : EditText = view.findViewById(R.id.dialog_edt)
            title.text = "Thêm địa chỉ"
            msg.visibility = View.INVISIBLE
            editText.visibility = View.VISIBLE
            var dialog : AlertDialog = builder.create()
            var yes = view.findViewById<CardView>(R.id.cv_yes)
            var no = view.findViewById<CardView>(R.id.cv_no)
            yes.setOnClickListener(View.OnClickListener {
                tvAdd.text = editText.text
                dialog.dismiss()
            })
            no.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })
            dialog.show()
        })
        cvAddUser.setOnClickListener(View.OnClickListener {
            if(add.equals(" ")){
                Toast.makeText(context, "Vui lòng thay đổi địa chỉ", Toast.LENGTH_SHORT).show()
            }
            else{
                tvAdd.text = add
            }

        })
    }

    private fun setupRV() {
        val linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        val productInOrderAdapter = ProductInOrderAdapter(listProduct,requireContext())
        rv.layoutManager = linearLayoutManager
        rv.adapter = productInOrderAdapter

    }

    private fun getlistProduct() {
        val bundle = arguments
        listProduct = bundle?.get("listProductOrder") as ArrayList<ProductInCart>
        for(i in 0 until listProduct.size){
            totalPrice.add(listProduct.get(i).giaTien * listProduct.get(i).soLuong)
            tPrice += listProduct.get(i).giaTien * listProduct.get(i).soLuong
        }
        var decimalFormat = DecimalFormat("###,###,###")
        var price = decimalFormat.format(tPrice)
        tvTotalPrice.text = "Thành tiền : ${price} vnd"
    }

    private fun hideNaviBot() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.orderF_imv_back)
        rv = view.findViewById(R.id.orderF_recyclerView_product)
        cvAddEdit = view.findViewById(R.id.orderF_cardView_addEdit)
        cvAddUser = view.findViewById(R.id.orderF_cardView_add)
        cvOrder = view.findViewById(R.id.orderF_cardView_order)
        tvTotalPrice = view.findViewById(R.id.orderF_tv_totalCost)
        tvPhone = view.findViewById(R.id.orderF_tv_phone)
        tvThanhToan = view.findViewById(R.id.orderF_tv_thanhToan)
        tvAdd = view.findViewById(R.id.orderF_tv_add)
        listProduct = arrayListOf()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}