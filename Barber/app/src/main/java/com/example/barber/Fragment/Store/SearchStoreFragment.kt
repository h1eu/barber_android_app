package com.example.barber.Fragment.Store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.ListProductAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Interface.ClickItemProduct
import com.example.barber.Model.Product
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchStoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchStoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var nameSearch : TextView
    private lateinit var tvTitle : TextView
    private lateinit var rv : RecyclerView
    private lateinit var listProduct : ArrayList<Product>
    private var name = ""

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
        val view = inflater.inflate(R.layout.fragment_search_store, container, false)
        init(view)
        hideBot()
        getName()
        back()
        return view
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            var bundle = arguments
            var product = bundle?.get("Product")
            var b = bundleOf(
                "Product" to product,
                "status" to "Store"
            )
            controller.navigate(R.id.action_searchStoreFragment_to_storeFragment,b)
        })
    }

    private fun getName() {
        var bundle = arguments
        if(bundle!= null){
            name = bundle.get("searchName") as String
            nameSearch.text = name.toString()
            getProduct(name)
        }
    }

    private fun getProduct(name: String) {
        var action = "searchProduct"
        ApiServices.create().searchProduct(name,action).enqueue(
            object : Callback<ArrayList<Product>>{
                override fun onResponse(
                    call: Call<ArrayList<Product>>,
                    response: Response<ArrayList<Product>>
                ) {
                    var list = response.body()
                    if(list != null){
                        if(list.size > 0){
                            for(i in 0 until list.size){
                                listProduct.add(list.get(i))
                                setupRV()
                            }
                        }
                        else tvTitle.text = "Không có sản phẩm"
                    } else tvTitle.text = "Không có sản phẩm"
                }

                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                    Log.e("asdasd",t.toString())
                }

            }
        )
    }

    private fun setupRV() {
        var gridLayoutManager = GridLayoutManager(requireContext(),2)
        var productAdapter = ListProductAdapter(listProduct,requireContext())
        rv.layoutManager = gridLayoutManager
        rv.adapter = productAdapter
        productAdapter.setOnClickListener(
            object : ClickItemProduct {
                override fun onItemProductClick(position: Int) {
                    var bundle = bundleOf(
                        "Product" to listProduct.get(position),
                        "searchName" to name,
                        "status" to "fromSearch",
                    )
                    controller.navigate(R.id.action_searchStoreFragment_to_viewFullOneProductFragment,bundle)
                }

            }
        )
    }

    private fun hideBot() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.searchStoreF_imv_back)
        nameSearch = view.findViewById(R.id.searchStoreF_edt_search)
        tvTitle = view.findViewById(R.id.storeF_tv_productList)
        rv = view.findViewById(R.id.storeF_recyclerView_productList)
        listProduct = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchStoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchStoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}