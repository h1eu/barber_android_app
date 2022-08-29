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
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.GetProductByTypeAdapter
import com.example.barber.Adapter.ListProductAdapter
import com.example.barber.Adapter.TypeProductAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Interface.ClickItemTypeProduct
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
 * Use the [GetProductByTypeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GetProductByTypeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imgBack : ImageView
    private lateinit var tvTypeProduct : TextView
    private lateinit var recyclerViewListProduct: RecyclerView
    private lateinit var listProduct : ArrayList<Product>
    private var type = ""

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
        val view = inflater.inflate(R.layout.fragment_get_product_by_type, container, false)
        init(view)
        setTv()
        hideNaviBot()
        back()
        getProductByType()
        return view
    }


    private fun setTv() {
        var bundle = arguments
        type = bundle?.getString("tvTypeProduct").toString()
        tvTypeProduct.text = type
    }

    private fun getProductByType() {
        val action = "getProductByType"
        ApiServices.create().getProductByType(type,action).enqueue(
            object : Callback<ArrayList<Product>>{
                override fun onResponse(
                    call: Call<ArrayList<Product>>,
                    response: Response<ArrayList<Product>>
                ) {
                    var list = response.body()

                    if (list != null) {
                        for(i in 0 until list.size){
                            listProduct.add(list.get(i));
                        }
                    }
                    setupRecyclerViewGetProduct()
                }

                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                    Log.e("error",t.toString())
                }

            }
        )
    }

    private fun setupRecyclerViewGetProduct() {
        var gridLayoutManager = GridLayoutManager(requireContext(),2)
        var getProductByTypeAdapter = GetProductByTypeAdapter(listProduct,requireContext())
        recyclerViewListProduct.layoutManager = gridLayoutManager
        recyclerViewListProduct.adapter = getProductByTypeAdapter
        getProductByTypeAdapter.setOnClickListener(object : GetProductByTypeAdapter.onItemClicklistener{
            override fun onClickView(position: Int) {
                var bundle = bundleOf(
                    "Product" to listProduct.get(position),
                    "tvTypeProduct" to type,
                    "status" to "fromTypeProduct",
                )
                controller.navigate(R.id.action_getProductByTypeFragment_to_viewFullOneProductFragment,bundle)
            }
        })

    }

    private fun back() {
        imgBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_getProductByTypeFragment_to_storeFragment)
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
        imgBack = view.findViewById(R.id.getProductTypeF_imv_back)
        tvTypeProduct = view.findViewById(R.id.getProductTypeF_tv_productList)
        recyclerViewListProduct = view.findViewById(R.id.getProductTypeF_recyclerView_productList)
        listProduct = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GetProductByTypeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GetProductByTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}