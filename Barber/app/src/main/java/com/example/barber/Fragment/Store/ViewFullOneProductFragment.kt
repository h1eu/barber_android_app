package com.example.barber.Fragment.Store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Constant
import com.example.barber.Model.Product
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
 * Use the [ViewFullOneProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFullOneProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imgBack : ImageView
    private lateinit var imgCart : ImageView
    private lateinit var imgProduct : ImageView
    private lateinit var tvProductPrice : TextView
    private lateinit var tvProductName : TextView
    private lateinit var tvProductDes : TextView
    private lateinit var layoutAddCart : ConstraintLayout

    private var soLuong = 1


    private lateinit var product : Product

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
        val view = inflater.inflate(R.layout.fragment_view_full_one_product, container, false)
        init(view)
        hideNaviBot()
        back()
        viewProduct()
        addCart()
        gotoCart()
        return view
    }


    private fun back() {
        val bundle = arguments
        var status = bundle?.getString("status")
        product = bundle?.get("Product") as Product
        if (status == "fromStore"){
            imgBack.setOnClickListener(View.OnClickListener {
                controller.navigate(R.id.action_viewFullOneProductFragment_to_storeFragment)
            })
        }
        if (status == "fromTypeProduct"){
            imgBack.setOnClickListener(View.OnClickListener {
                var type = bundle.get("tvTypeProduct")
                var b = bundleOf(
                    "Product" to product,
                    "tvTypeProduct" to type,
                    "status" to "fromTypeProduct"
                )
                controller.navigate(R.id.action_viewFullOneProductFragment_to_getProductByTypeFragment,bundle)
            })
        }
        if (status == "Store"){
            imgBack.setOnClickListener(View.OnClickListener {
                var b = bundleOf(
                    "Product" to product,
                    "status" to "Store"
                )
                controller.navigate(R.id.action_viewFullOneProductFragment_to_storeFragment,b)
            })
        }
        if (status == "fromSearch"){
            imgBack.setOnClickListener(View.OnClickListener {
                var search = bundle.get("searchName")
                var b = bundleOf(
                    "Product" to product,
                    "searchName" to search,
                    "status" to "fromStore"
                )
                controller.navigate(R.id.action_viewFullOneProductFragment_to_searchStoreFragment,bundle)
            })
        }


    }


    private fun gotoCart() {
        imgCart.setOnClickListener(View.OnClickListener {
            Log.e("product view",product.maSanPham.toString())
            var bundle = bundleOf(
                "Product" to product,
                "status" to "fromView"
            )
            controller.navigate(R.id.action_viewFullOneProductFragment_to_cartFragment,bundle)
        })

    }

    private fun addCart() {
        layoutAddCart.setOnClickListener(View.OnClickListener {
            val action = "addProductToCart"
            ApiServices.create().addProductToCart(Constant.KeyWithScreen.IdUser,product.maSanPham.toString(),
                    product.tenSanPham,soLuong.toString(),product.giaTien.toString(),product.imgProduct,action).enqueue(
                object : Callback<Boolean>{
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        val check = response.body()
                        if(check != null){
                            if(check == true){
                                Toast.makeText(requireContext(), "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(context, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Log.e("error",t.toString())
                    }

                }
            )
        })
    }


    private fun viewProduct() {
        var bundle = arguments
        if(bundle != null){
            product = bundle?.get("Product") as Product
            var decimalFormat = DecimalFormat("###,###,###")
            var price = decimalFormat.format(product.giaTien)
            tvProductPrice.text = price.toString() + " vnd"
            tvProductName.text = product.tenSanPham
            tvProductDes.text = product.moTa
            Glide.with(requireContext()).load(product.imgProduct).into(imgProduct)
        }


    }

    private fun hideNaviBot() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imgBack = view.findViewById(R.id.viewProductF_imv_back)
        imgCart = view.findViewById(R.id.viewProductF_imv_cart)
        imgProduct = view.findViewById(R.id.viewProductF_imv_product)
        tvProductPrice = view.findViewById(R.id.viewProductF_tv_productPrice)
        tvProductName = view.findViewById(R.id.viewProductF_tv_productName)
        tvProductDes = view.findViewById(R.id.viewProductF_tv_productDes)
        layoutAddCart = view.findViewById(R.id.viewProductF_layout_addCart)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewFullOneProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewFullOneProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}