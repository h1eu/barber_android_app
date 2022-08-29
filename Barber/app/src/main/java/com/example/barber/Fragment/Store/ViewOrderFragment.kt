package com.example.barber.Fragment.Store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Bill
import com.example.barber.Model.ProductInBill
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
 * Use the [ViewOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var rvProductInOrder : RecyclerView
    private lateinit var tvAdd : TextView
    private lateinit var tvPhone : TextView
    private lateinit var tvTt : TextView
    private lateinit var tvTotalPrice : TextView
    private lateinit var tvState : TextView
    private lateinit var imgProduct : ImageView
    private lateinit var tvNameP : TextView
    private lateinit var tvPriceP : TextView
    private lateinit var tvCountP : TextView

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
        val view = inflater.inflate(R.layout.fragment_view_order, container, false)
        init(view)
        hideNavi()
        back()
        getData()
        return view
    }

    private fun getData() {
        var bundle = arguments
        if(bundle != null){
            var action = "getOrder"
            var order = bundle.get("order") as ProductInBill
            ApiServices.create().getOrder(order.maDonHang.toString(),action).enqueue(
                object : Callback<ArrayList<Bill>>{
                    override fun onResponse(
                        call: Call<ArrayList<Bill>>,
                        response: Response<ArrayList<Bill>>
                    ) {
                        tvNameP.text = order.tenSanPham
                        var decimalFormat = DecimalFormat("###,###,###")
                        var price = decimalFormat.format(order.giaTien)
                        tvPriceP.text = "Giá tiền: " + price.toString() + " vnd"
                        tvCountP.text = order.soLuong.toString() + " sản phẩm"
                        Glide.with(requireContext()).load(order.imgProduct).into(imgProduct)
                        if(order.trangThaiDatHang.equals("wait")){
                            tvState.text = "Chờ xác nhận"
                        }
                        if(order.trangThaiDatHang.equals("comfirm")){
                            tvState.text = "Đã xác nhận"
                        }
                        if(order.trangThaiDatHang.equals("destroy")){
                            tvState.text = "Đã huỷ"
                        }
                        var list = response.body()
                        if(list != null){
                            tvAdd.text = list.get(0).diaChiGiaoHang
                            tvPhone.text = list.get(0).soDienThoaiNhanHang
                            tvTt.text = list.get(0).phuongThucThanhToan
                            var ttPrice = decimalFormat.format(list.get(0).tongTien)
                            tvTotalPrice.text = "Tổng tiền: " + ttPrice.toString() + " vnd"
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Bill>>, t: Throwable) {
                        Log.e("loi ",t.toString())
                    }

                }
            )
        }

    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_viewOrderFragment_to_listOrderFragment)
        })
    }

    private fun hideNavi() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.viewOrderF_imv_back)
        tvAdd = view.findViewById(R.id.viewOrderF_tv_add)
        tvPhone = view.findViewById(R.id.viewOrderF_tv_phone)
        tvTt = view.findViewById(R.id.viewOrderF_tv_thanhToan)
        tvTotalPrice = view.findViewById(R.id.viewOrderF_tv_totalPrice)
        tvState = view.findViewById(R.id.orderState_tv_state)
        imgProduct = view.findViewById(R.id.orderItemImv)
        tvNameP = view.findViewById(R.id.orderItem_nameProduct)
        tvCountP = view.findViewById(R.id.orderItem_countProduct)
        tvPriceP = view.findViewById(R.id.orderItem_priceProduct)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}