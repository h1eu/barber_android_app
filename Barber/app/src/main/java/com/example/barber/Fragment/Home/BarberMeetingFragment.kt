package com.example.barber.Fragment.Home

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.HairdresserBarberMeetingAdapter
import com.example.barber.Adapter.HourBarberMeetingAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Interface.ClickItemHairdresserListener
import com.example.barber.Interface.ClickItemHourListener
import com.example.barber.Model.*
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BarberMeetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarberMeetingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var recyclerViewHour : RecyclerView
    private lateinit var recyclerViewHairdresser : RecyclerView
    private lateinit var imvPickDay : ImageView
    private lateinit var tvDayPicked : TextView
    private lateinit var autoCompleteTextViewListStore: AutoCompleteTextView
    private lateinit var cardViewComfirm: CardView
    private lateinit var imvBack : ImageView
    private lateinit var tvPickHour : TextView
    private lateinit var adapterStringListStore : ArrayAdapter<String>
    private lateinit var arraylistStore : ArrayList<Store>
    private lateinit var arraylistHairdresser : ArrayList<Hairdresser>
    private lateinit var addressArrayList: ArrayList<String>
    private lateinit var hourUsedList : ArrayList<String>
    private var oldPostionHour  = -1
    private var oldPostionHairdresser = -1
    private var oldStore = ""

    private var daySelected = ""
    private var hourSelected = ""
    private var nameHairdresser = ""
    private var addressStoreSelected = ""
    private var idHairdresserSelected = ""

    private var sdf : SimpleDateFormat = SimpleDateFormat("dd/MM/YYYY")
    private var currentDay = sdf.format(object : Date() {})

    private var stf : SimpleDateFormat = SimpleDateFormat("HH:mm")
    private var currentTime : String = stf.format(object : Date(){})



    private lateinit var hourArrayList : ArrayList<String>
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
        val view = inflater.inflate(R.layout.fragment_barber_meeting, container, false)
        init(view)
        back()
//        Log.e("gio",currentTime)
//        Log.e("ngay",currentDay)
        hideBottomNavigation()
        pickStore()
        pickDay()
        pickHour()
        selected()
        return view
    }




    private fun hideBottomNavigation() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }


    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_barberMeetingFragment_to_homeFragment)
        })
    }

    private fun selected() {
        val loadingDialog = LoadingDialog(requireActivity())
        cardViewComfirm.setOnClickListener(View.OnClickListener {
            loadingDialog.startLoading()
            if(TextUtils.isEmpty(daySelected) || TextUtils.isEmpty(hourSelected) || TextUtils.isEmpty(addressStoreSelected)
                || TextUtils.isEmpty(idHairdresserSelected) || TextUtils.isEmpty(nameHairdresser)){
                Toast.makeText(context, "Vui lòng chọn đủ thông tin", Toast.LENGTH_SHORT).show()
                loadingDialog.dismissDialog()
            }
            else{
                val action = "createBarberMeeting"
                ApiServices.create().createBarberMeeting( Constant.KeyWithScreen.IdUser.toString(),idHairdresserSelected,nameHairdresser,daySelected,hourSelected,addressStoreSelected,action).enqueue(
                    object : Callback<Unit>{
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            loadingDialog.dismissDialog()
                            controller.navigate(R.id.action_barberMeetingFragment_to_historyMeetingFragment)
                        }
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.e("error",t.toString())
                            loadingDialog.dismissDialog()
                        }
                    }
                )
            }
        })

}

    private fun getAllStore() {
        val action = "getAllStore"
        ApiServices.create().getAllStore(action).enqueue(
            object : Callback<ArrayList<Store>>{
                override fun onResponse(
                    call: Call<ArrayList<Store>>,
                    response: Response<ArrayList<Store>>
                ) {
                    arraylistStore = response.body()!!
                    for(i in 0 until arraylistStore.size){
                        addressArrayList.add(arraylistStore.get(i).diaChi)
                    }
                }
                override fun onFailure(call: Call<ArrayList<Store>>, t: Throwable) {
                }
            }
        )
    }

    private fun pickStore() {
        getAllStore()
        adapterStringListStore = ArrayAdapter<String>(requireContext(),R.layout.item_store,addressArrayList)
        autoCompleteTextViewListStore.setAdapter(adapterStringListStore)

        autoCompleteTextViewListStore.setOnItemClickListener(
            object  : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                recyclerViewHour.visibility = View.INVISIBLE
                var maCuaHang = arraylistStore.get(pos).maCuaHang.toString()
                addressStoreSelected = arraylistStore.get(pos).diaChi
                var action = "getHairdresser"
                ApiServices.create().getHaidresser(maCuaHang,action).enqueue(
                    object : Callback<ArrayList<Hairdresser>>{
                        override fun onResponse(
                            call: Call<ArrayList<Hairdresser>>,
                            response: Response<ArrayList<Hairdresser>>
                        ) {
                            arraylistHairdresser = response.body()!!
                            oldStore = autoCompleteTextViewListStore.text.toString()
                            Log.e("store",autoCompleteTextViewListStore.text.toString())

                            setupRecycerViewHairdresser(arraylistHairdresser)
                        }
                        override fun onFailure(call: Call<ArrayList<Hairdresser>>, t: Throwable) {

                        }
                    }
                )
            }
        })



    }

    private fun setupRecycerViewHairdresser(listHairdresserName : ArrayList<Hairdresser>) {

        var hairdresserBarberMeetingAdapter = HairdresserBarberMeetingAdapter(listHairdresserName,requireContext())
        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recyclerViewHairdresser.layoutManager = linearLayoutManager
        recyclerViewHairdresser.adapter = hairdresserBarberMeetingAdapter
        hairdresserBarberMeetingAdapter.setOnClickListener(
            object : ClickItemHairdresserListener{
                override fun onItemHairdresserClick(position: Int) {
                    recyclerViewHour.visibility = View.INVISIBLE
                    idHairdresserSelected = arraylistHairdresser.get(position).maThoCatToc.toString()
                    nameHairdresser = arraylistHairdresser.get(position).tenThoCatToc
                    recyclerViewHairdresser.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<ConstraintLayout>(R.id.item_layout).setBackgroundResource(R.drawable.item_hairdresser_selected)
                    if (oldPostionHairdresser != position){
                        if(oldPostionHairdresser != -1){
                            recyclerViewHairdresser.findViewHolderForAdapterPosition(oldPostionHairdresser)!!.itemView.findViewById<ConstraintLayout>(R.id.item_layout).setBackgroundColor(Color.WHITE)
                        }
                        oldPostionHairdresser = position
                    }
                }



            }
        )
    }


    private fun pickDay() {
        var calendar = Calendar.getInstance()
        var year     = calendar.get(Calendar.YEAR)
        var month    = calendar.get(Calendar.MONTH) + 1
        var day      = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog =  DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
            var monthT = mMonth+1
            tvDayPicked.text =  "Ngày ${if (mDay > 9) mDay else "0" + mDay} tháng ${if (monthT > 9) mDay else "0" + monthT} năm ${mYear}"
            var selected = "${if (mDay > 9) mDay else "0" + mDay}/${if (monthT > 9) mDay else "0" + monthT}/${mYear}"
            daySelected = selected
        },year,month,day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        calendar.add(Calendar.DATE,2)
        datePickerDialog.datePicker.init(year,month-1,day,null)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        tvDayPicked.setOnClickListener(View.OnClickListener {
           datePickerDialog.show()
            recyclerViewHour.visibility = View.INVISIBLE
        })
    }

    private fun pickHour() {
        tvPickHour.setOnClickListener(View.OnClickListener {
            recyclerViewHour.visibility = View.VISIBLE
            checkHour()
        })
    }

    private fun checkHour() {
        val action = "checkHour"
        val status = "true"
//        Log.e("idtho",idHairdresserSelected.toString())
//        Log.e("dic chi",addressStoreSelected.toString())
//        Log.e("day", this.daySelected.toString())
        ApiServices.create().checkHour(idHairdresserSelected,addressStoreSelected,
            this.daySelected,status,action).enqueue(
            object : Callback<ArrayList<BarberMeeting>>{
                override fun onResponse(
                    call: Call<ArrayList<BarberMeeting>>,
                    response: Response<ArrayList<BarberMeeting>>
                ) {
                    var list = response.body()
                    if(list != null){
                        for(i in 0 until  list.size){
                            hourUsedList.add(list.get(i).gio)
                        }
                    }
                    setupRecycerViewHour()

                }
                override fun onFailure(call: Call<ArrayList<BarberMeeting>>, t: Throwable) {

                }

            }
        )
    }


    private fun setupRecycerViewHour() {
        hourArrayList = arrayListOf(
            "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30"
        )
        var hourBarberMeetingAdapter = HourBarberMeetingAdapter(hourArrayList,hourUsedList,currentDay,currentTime,daySelected)
        var gridLayoutManager  = GridLayoutManager(context,5)
        recyclerViewHour.layoutManager = gridLayoutManager
        recyclerViewHour.adapter = hourBarberMeetingAdapter

        hourBarberMeetingAdapter.setOnClickListener(
            object : ClickItemHourListener{
                override fun onItemHourClick(position: Int, hour : String) {
                    hourSelected = hour
                    Log.e("gio ",hour)
                    recyclerViewHour.findViewHolderForAdapterPosition(position)!!.itemView.findViewById<TextView>(R.id.tv_hour).setBackgroundColor(Color.rgb(255,204,51))
                    if (oldPostionHour != position){
                        if(oldPostionHour != -1){
                            recyclerViewHour.findViewHolderForAdapterPosition(oldPostionHour)!!.itemView.findViewById<TextView>(R.id.tv_hour).setBackgroundColor(Color.WHITE)
                        }
                        oldPostionHour = position
                    }
                }
            }
        )

    }


    private fun init(view: View) {
        hourArrayList = arrayListOf(  )
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvPickDay = view.findViewById(R.id.barberMeetingF_imv_pickDay)
        tvDayPicked = view.findViewById(R.id.barberMeetingF_tv_dayPicked)
        recyclerViewHour = view.findViewById(R.id.barberMeetingF_recyclerView_time)
        autoCompleteTextViewListStore = view.findViewById(R.id.barberMeetingF_autoText_pickStore)
        recyclerViewHairdresser = view.findViewById(R.id.barberMeetingF_recyclerView_hairdresserList)
        cardViewComfirm = view.findViewById(R.id.barberMeetingFcardView_meeting)
        imvBack = view.findViewById(R.id.barberMeetingF_imv_back)
        tvPickHour = view.findViewById(R.id.barberMeetingF_tv_hourPicked)
        arraylistStore = arrayListOf()
        addressArrayList = arrayListOf()
        arraylistHairdresser = arrayListOf()
        hourUsedList = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BarberMeetingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BarberMeetingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}


