package com.example.barber.Fragment.Store

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.OrderWaitAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Bill
import com.example.barber.Model.Constant
import com.example.barber.Model.ProductInBill
import com.example.barber.Model.ProductInCart
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
 * Use the [OrderWaitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderWaitFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var rv : RecyclerView
    private lateinit var listWaitOrder : ArrayList<ProductInBill>

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
        var view = inflater.inflate(R.layout.fragment_order_wait, container, false)
        init(view)
        getData()
        return view
    }



    private fun getData() {
        var action = "getProductInOrder"
        var state = "wait"


        ApiServices.create().getProductInOrder(Constant.KeyWithScreen.IdUser,state,action).enqueue(
            object : Callback<ArrayList<ProductInBill>>{
                override fun onResponse(
                    call: Call<ArrayList<ProductInBill>>,
                    response: Response<ArrayList<ProductInBill>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until listR.size){
                            listWaitOrder.add(listR.get(i))
                        }
                        setupRv()
                    }
                }

                override fun onFailure(call: Call<ArrayList<ProductInBill>>, t: Throwable) {
                //    Log.e("loi dket",t.toString())
                }

            }
        )
    }

    private fun setupRv() {
        val linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        val orderWaitAdapter = OrderWaitAdapter(listWaitOrder,requireContext())
        rv.layoutManager = linearLayoutManager
        rv.adapter = orderWaitAdapter
        orderWaitAdapter.setOnClickListener(
            object  : OrderWaitAdapter.onItemClicklistener{
                override fun onClickView(position: Int, state : Int) {
                    if(state == 1){
                        var builder = AlertDialog.Builder(requireActivity())
                        var view = View.inflate(requireActivity(),R.layout.dialog_comfirm,null)
                        builder.setView(view)
                        var title : TextView = view.findViewById(R.id.dialogTitle)
                        var msg : TextView = view.findViewById(R.id.dialogMsg)
                        title.text = "Hu??? ????n h??ng"
                        msg.text = "B???n c?? mu???n hu??? ????n h??ng n??y ?"
                        var dialog : AlertDialog = builder.create()
                        var yes = view.findViewById<CardView>(R.id.cv_yes)
                        var no = view.findViewById<CardView>(R.id.cv_no)
                        yes.setOnClickListener(View.OnClickListener {
                            var action = "destroyOrder"
                            ApiServices.create().destroyOrder(listWaitOrder.get(position).maDonHang.toString(),action).enqueue(
                                object : Callback<Boolean>{
                                    override fun onResponse(
                                        call: Call<Boolean>,
                                        response: Response<Boolean>
                                    ) {
                                        var check = response.body()
                                        if(check != null)
                                        {
                                            if(check == true){
                                                dialog.dismiss()
                                                Toast.makeText(context, "hu??? ????n h??ng th??nh c??ng", Toast.LENGTH_SHORT).show()
                                                listWaitOrder.clear()
                                                if (listWaitOrder.size == 0){
                                                    orderWaitAdapter.notifyDataSetChanged()
                                                }
                                                getData()
                                            }
                                            else
                                                Toast.makeText(context, "Th???t b???i", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                        Toast.makeText(context, "Th???t b???i", Toast.LENGTH_SHORT).show()
                                        Log.e("error",t.toString())
                                    }

                                }
                            )
                        })
                        no.setOnClickListener(View.OnClickListener {
                            dialog.dismiss()
                        })

                        dialog.show()
                    }
                    if(state == 2){
                        var bundle = bundleOf(
                            "order" to listWaitOrder.get(position),
                            "status" to "fromListOrder"
                        )
                        controller.navigate(R.id.action_listOrderFragment_to_viewOrderFragment,bundle)
                    }
                }

            }
        )

    }

    private fun init(view : View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        rv = view.findViewById(R.id.orderWaitF_recyclerView)
        listWaitOrder = arrayListOf()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderWaitFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderWaitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}