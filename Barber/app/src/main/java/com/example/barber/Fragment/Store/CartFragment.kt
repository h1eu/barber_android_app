package com.example.barber.Fragment.Store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.ProductInCartAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Constant
import com.example.barber.Model.LoadingDialog
import com.example.barber.Model.Product
import com.example.barber.Model.ProductInCart
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imgBack : ImageView
    private lateinit var cardVOrder : CardView
    private lateinit var listProductInCart: ArrayList<ProductInCart>
    private lateinit var rv : RecyclerView
    private lateinit var product : Product
    private lateinit var cb : CheckBox
    private lateinit var listProductOrder : ArrayList<ProductInCart>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        init(view)
        hideNaviBot()
        back()
        getInfoCart()
        order()
        return view
    }

    override fun onResume() {
        super.onResume()
        cb.isChecked = false
    }

    private fun back() {
        val bundle = arguments
        val status = bundle?.getString("status")
        product = bundle?.get("Product") as Product
        if(status.equals("fromView")){
            imgBack.setOnClickListener(View.OnClickListener {
                val bundle = bundleOf(
                    "Product" to product,
                    "status" to "fromStore"
                )
                controller.navigate(R.id.action_cartFragment_to_viewFullOneProductFragment,bundle)
            })
        }
        if (status.equals("Store")){
            imgBack.setOnClickListener(View.OnClickListener {
                val bundle = bundleOf(
                    "Product" to product,
                    "status" to "Store"
                )
                controller.navigate(R.id.action_cartFragment_to_storeFragment,bundle)
            })
        }


    }



    private fun order() {
        var flag = 0
        cardVOrder.setOnClickListener(View.OnClickListener {
            for(i in 0 until listProductInCart.size){
                if(listProductInCart.get(i).trangThaiMua == true){
                    flag = 1
                    listProductOrder.add(listProductInCart.get(i))
                }
            }
            if(flag == 0){
                Toast.makeText(context, "Bạn chưa chọn sản phẩm nào", Toast.LENGTH_SHORT).show()
            }
            else{
                val x = arguments
                var status = x?.get("status")
                var product = x?.get("Product") as Product
                Log.e("product cart ",product.maSanPham.toString())
                if(status!!.equals("Store")){
                    val bundle = bundleOf(
                        "status" to "fromCart",
                        "Product" to product,
                        "listProductOrder" to listProductOrder
                    )
                    controller.navigate(R.id.action_cartFragment_to_orderFragment,bundle)
                }

                if(status!!.equals("fromView")){
                    val bundle = bundleOf(
                        "status" to "view",
                        "Product" to product,
                        "listProductOrder" to listProductOrder
                    )
                    controller.navigate(R.id.action_cartFragment_to_orderFragment,bundle)
                }

            }
        })
        listProductOrder.clear()


    }

    private fun getInfoCart() {
        val action = "getInfoCart"
        ApiServices.create().getInfoCart(Constant.KeyWithScreen.IdUser,action).enqueue(
            object : Callback<ArrayList<ProductInCart>>{
                override fun onResponse(
                    call: Call<ArrayList<ProductInCart>>,
                    response: Response<ArrayList<ProductInCart>>
                ) {
                    val rs = response.body()
                    if(rs != null){
                   //     var list = rs.asReversed()
                        for (i in 0 until rs.size){
                            listProductInCart.add(rs.get(i))
                        }
                        cb.setOnClickListener(View.OnClickListener {
                            for (i in 0 until listProductInCart.size){
                                listProductInCart.get(i).setChecked(cb.isChecked)
                            }
                            setupRecyclerView()
                        })
                        setupRecyclerView()
                    }
                    else{
                        cardVOrder.isEnabled = false
                    }
                }
                override fun onFailure(call: Call<ArrayList<ProductInCart>>, t: Throwable) {
                    Log.e("errore",t.toString())
                }

            }
        )
    }

    private fun setupRecyclerView() {
        var linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        var productInCartAdapter = ProductInCartAdapter(listProductInCart,requireContext())
        rv.layoutManager = linearLayoutManager
        rv.adapter = productInCartAdapter
        productInCartAdapter.setOnClickListener(object : ProductInCartAdapter.onItemClicklistener{
                override fun onClickView(position: Int, status : Int) {
                    var pIC = listProductInCart.get(position)
                    var soL = pIC.soLuong
                    if(status == 3){
                        var builder = AlertDialog.Builder(requireActivity())
                        var view = View.inflate(requireActivity(),R.layout.dialog_comfirm,null)
                        builder.setView(view)
                        var title : TextView = view.findViewById(R.id.dialogTitle)
                        var msg : TextView = view.findViewById(R.id.dialogMsg)
                        title.text = "Xoá sản phẩm khỏi giỏ hàng"
                        msg.text = "Bạn có muốn xoá sản phẩm khỏi giỏ hàng không ?"
                        var dialog : AlertDialog = builder.create()
                        var yes = view.findViewById<CardView>(R.id.cv_yes)
                        var no = view.findViewById<CardView>(R.id.cv_no)
                        yes.setOnClickListener(View.OnClickListener {
                            var action = "deleteProductInCart"
                            ApiServices.create().deleteProductInCart(Constant.KeyWithScreen.IdUser,pIC.maSanPham.toString(),action).enqueue(
                                object : Callback<Boolean>{
                                    override fun onResponse(
                                        call: Call<Boolean>,
                                        response: Response<Boolean>
                                    ) {
                                        var check = response.body()
                                        if(check != null){
                                            if(check == true){
                                                dialog.dismiss()
                                                Toast.makeText(context, "Xoá sản phẩm khỏi giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                                                listProductInCart.clear()
                                                if (listProductInCart.size == 0){
                                                    productInCartAdapter.notifyDataSetChanged()
                                                }
                                                getInfoCart()
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                        Log.e("xoa gio hang that bai",t.toString())
                                    }

                                }
                            )

                        })
                        no.setOnClickListener(View.OnClickListener {
                            dialog.dismiss()
                        })

                        dialog.show()
                    }
                    else if(status == 2){
                        if(soL > 1){
                            var soLuongNew = soL - 1
                            pIC.soLuong = soLuongNew
                        }
                    }
                    else if (status == 1){
                        var soLuongNew = soL + 1
                        pIC.soLuong = soLuongNew
                    }
                    var action = "updateCart"
                    ApiServices.create().updateCart(Constant.KeyWithScreen.IdUser,pIC.maSanPham.toString(),pIC.soLuong.toString(),action).enqueue(
                        object : Callback<Boolean>{
                            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                var check = response.body()
                                if(check != null){
                                    if(check == true){
                                        rv.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<TextView>(R.id.totalProduct).text = pIC.soLuong.toString()
                                        var tongTien : Double = pIC.giaTien * pIC.soLuong.toDouble()
                                        var decimalFormat = DecimalFormat("###,###,###")
                                        var price = decimalFormat.format(tongTien)
                                        rv.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<TextView>(R.id.cartItem_priceProduct).text = price.toString()  + " vnd"
                                    }
                                }
                            }
                            override fun onFailure(call: Call<Boolean>, t: Throwable) {

                                Log.e("error",t.toString())
                            }

                        }
                    )
            }

            override fun onCheckBoxClick(position: Int, checked: Boolean) {
                Log.e("statte",position.toString()+ "   " + checked.toString())
                listProductInCart.get(position).trangThaiMua = checked
                if(checked == false){
                    cb.isChecked = false
                }
            }

        })
    }

    private fun hideNaviBot() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }


    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imgBack = view.findViewById(R.id.cartF_imv_back)
        rv = view.findViewById(R.id.cartF_rv)
        cardVOrder = view.findViewById(R.id.cartF_cardView_order)
        listProductInCart = arrayListOf()
        cb = view.findViewById(R.id.cartF_cb)
        listProductOrder = arrayListOf()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}