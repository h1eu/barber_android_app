package com.example.barber.Fragment.Store

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.ListProductAdapter
import com.example.barber.Adapter.TypeProductAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Interface.ClickItemProduct
import com.example.barber.Interface.ClickItemTypeProduct
import com.example.barber.Model.Product
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var rvTypeProduct : RecyclerView
    private lateinit var listTypeProduct : ArrayList<String>
    private lateinit var rvProduct : RecyclerView
    private lateinit var imvCart : ImageView
    private lateinit var imvOrder : ImageView
    private lateinit var imvSearch : ImageView
    private lateinit var listProduct : ArrayList<Product>
    private lateinit var edtNameSearch : TextInputEditText
    private lateinit var product: Product


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
        val view = inflater.inflate(R.layout.fragment_store, container, false)
        init(view)
        unHideNaviBot()
        goCart()
        getAllTypeProduct()
        getAllProduct()
        goOrderList()
        search()
        onBackPress()
        return view
    }

    private fun search() {

        imvSearch.setOnClickListener(View.OnClickListener {
            var search = edtNameSearch.text.toString()
            if(TextUtils.isEmpty(search)){
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            }
            else {
                product = Product(-1,"",-1.0,"","","")
                var bundle = bundleOf(
                    "Product" to product,
                    "searchName" to search,
                    "status" to "Store"
                )
                controller.navigate(R.id.action_storeFragment_to_searchStoreFragment,bundle)
            }
        })
    }

    private fun goOrderList() {
        imvOrder.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_storeFragment_to_listOrderFragment)
        })
    }

    private fun onBackPress() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }


    private fun goCart() {
        imvCart.setOnClickListener(View.OnClickListener {
            product = Product(-1,"",-1.0,"","","")
            var bundle = bundleOf(
                "Product" to product,
                "status" to "Store"
            )
            controller.navigate(R.id.action_storeFragment_to_cartFragment,bundle)
        })
    }

    private fun unHideNaviBot() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun getAllProduct() {

        var action = "getAllProduct"
        ApiServices.create().getAllProduct(action).enqueue(
            object : Callback<ArrayList<Product>>{
                override fun onResponse(
                    call: Call<ArrayList<Product>>,
                    response: Response<ArrayList<Product>>
                ) {
                    var list = response.body()
                    if (list != null) {
                        var listR = list.asReversed()
                        for(i in 0 until listR.size){
                            listProduct.add(listR.get(i))
                        }
                    }
                    setupRecyclerViewProduct()
                }

                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                    Log.e("ss",t.toString())
                }

            }
        )
    }

    private fun setupRecyclerViewProduct() {
        var gridLayoutManager = GridLayoutManager(requireContext(),2)
        var productAdapter = ListProductAdapter(listProduct,requireContext())
        rvProduct.layoutManager = gridLayoutManager
        rvProduct.adapter = productAdapter
        productAdapter.setOnClickListener(
            object : ClickItemProduct{
                override fun onItemProductClick(position: Int) {
                    var bundle = bundleOf(
                        "Product" to listProduct.get(position),
                        "status" to "Store",
                    )
                    controller.navigate(R.id.action_storeFragment_to_viewFullOneProductFragment,bundle)
                }

            }
        )
    }

    private fun getAllTypeProduct() {

        var action = "getAllTypeProduct"
        ApiServices.create().getAllTypeProduct(action).enqueue(
            object : Callback<ArrayList<String>>{
                override fun onResponse(
                    call: Call<ArrayList<String>>,
                    response: Response<ArrayList<String>>
                ) {
                    var list = response.body()

                    if (list != null) {
                        for(i in 0 until list.size){
                            listTypeProduct.add(list.get(i));
                        }
                    }
                    setupRecyclerViewTypeProduct()
                }

                override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                    Log.e("ss",t.toString())
                }

            }
        )

    }
    private fun setupRecyclerViewTypeProduct() {
        var linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        var typeProductAdapter = TypeProductAdapter(listTypeProduct)
        rvTypeProduct.layoutManager = linearLayoutManager
        rvTypeProduct.adapter = typeProductAdapter
        typeProductAdapter.setOnClickListener(
            object : ClickItemTypeProduct{
                override fun onItemTypeProductClick(position: Int, typeProduct: String) {
                    var bundle = bundleOf(
                        "tvTypeProduct" to typeProduct,
                        "position" to position
                    )
                    controller.navigate(R.id.action_storeFragment_to_getProductByTypeFragment,bundle)
                }

            }
        )
    }

    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        rvTypeProduct = view.findViewById(R.id.storeF_recyclerView_productList)
        rvProduct = view.findViewById(R.id.storeF_recyclerView_product)
        imvCart = view.findViewById(R.id.storeF_cart)
        imvOrder = view.findViewById(R.id.storeF_bill)
        imvSearch = view.findViewById(R.id.storeF_search)
        edtNameSearch = view.findViewById(R.id.storeF_edt_search)
        listProduct = arrayListOf()
        listTypeProduct = arrayListOf()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


