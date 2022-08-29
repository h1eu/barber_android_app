package com.example.barber.Fragment.Store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.OrderComfirmAdapter
import com.example.barber.Adapter.OrderDestroyAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Constant
import com.example.barber.Model.ProductInBill
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
 * Use the [OrderDestroyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderDestroyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var rv : RecyclerView
    private lateinit var listDestroyOrder : ArrayList<ProductInBill>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        listDestroyOrder.clear()
        getOrder()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_destroy, container, false)
        init(view)

        return view
    }

    private fun getOrder() {
        var action = "getProductInOrder"
        var state = "destroy"
        ApiServices.create().getProductInOrder(Constant.KeyWithScreen.IdUser,state,action).enqueue(
            object : Callback<ArrayList<ProductInBill>> {
                override fun onResponse(
                    call: Call<ArrayList<ProductInBill>>,
                    response: Response<ArrayList<ProductInBill>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until listR.size){
                            listDestroyOrder.add(listR.get(i))
                        }
                        setupRv()
                    }
                }

                override fun onFailure(call: Call<ArrayList<ProductInBill>>, t: Throwable) {

                }

            }
        )
    }
    private fun setupRv(){
        val linearLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        val orderDestroyAdapter = OrderDestroyAdapter(listDestroyOrder,requireContext())
        rv.layoutManager = linearLayoutManager
        rv.adapter = orderDestroyAdapter
        orderDestroyAdapter.setOnClickListener(
            object  : OrderDestroyAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    var bundle = bundleOf(
                        "order" to listDestroyOrder.get(position),
                        "status" to "fromListOrder"
                    )
                    controller.navigate(R.id.action_listOrderFragment_to_viewOrderFragment,bundle)
                }

            }
        )

    }

    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        rv = view.findViewById(R.id.orderDestroyF_recyclerView)
        listDestroyOrder = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderDestroyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderDestroyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}