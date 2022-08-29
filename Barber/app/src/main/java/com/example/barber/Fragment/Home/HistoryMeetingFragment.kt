package com.example.barber.Fragment.Home

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.HistoryMeetingAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.BarberMeeting
import com.example.barber.Model.Constant
import com.example.barber.Model.Store
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
 * Use the [HistoryMeetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryMeetingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var recyclerViewHistoryMeeting: RecyclerView
    private lateinit var listHistoreMeeting : ArrayList<BarberMeeting>
    private lateinit var imvBack : ImageView


    private var sdf : SimpleDateFormat = SimpleDateFormat("dd/MM/YYYY")
    private var currentDay = sdf.format(object : Date() {})

    private var stf : SimpleDateFormat = SimpleDateFormat("HH:mm")
    private var currentTime : String = stf.format(object : Date(){})

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
        val view = inflater.inflate(R.layout.fragment_history_meeting, container, false)
        init(view)
        back()
        hideBottomNavigation()
        getListHistoryMeeting()
        return view
    }

    private fun getListHistoryMeeting() {
        val action = "getHistoryMeeting"
        ApiServices.create().getListHistoryMeeting(Constant.KeyWithScreen.IdUser,action).enqueue(
            object : Callback<ArrayList<BarberMeeting>> {
                override fun onResponse(
                    call: Call<ArrayList<BarberMeeting>>,
                    response: Response<ArrayList<BarberMeeting>>
                ) {
                    var list  = response.body()!!
                    var listR = list.asReversed()
                    for(i in 0 until listR.size){
                        listHistoreMeeting.add(listR.get(i))
                    }
                    setupRecyclerView()
                }

                override fun onFailure(call: Call<ArrayList<BarberMeeting>>, t: Throwable) {

                }

            }
        )
    }

    private fun setupRecyclerView() {
        var historyMeetingAdapter = HistoryMeetingAdapter(listHistoreMeeting,currentDay,currentTime)
        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recyclerViewHistoryMeeting.layoutManager = linearLayoutManager
        recyclerViewHistoryMeeting.adapter = historyMeetingAdapter
        historyMeetingAdapter.setOnClickListener(
            object : HistoryMeetingAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    var builder = AlertDialog.Builder(requireActivity())
                    var view = View.inflate(requireActivity(),R.layout.dialog_comfirm,null)
                    builder.setView(view)
                    var title : TextView = view.findViewById(R.id.dialogTitle)
                    var msg : TextView = view.findViewById(R.id.dialogMsg)
                    title.text = "Huỷ lịch cắt tóc"
                    msg.text = "Bạn có muốn huỷ lịch cắt tóc ?"
                    var dialog : AlertDialog = builder.create()
                    var yes = view.findViewById<CardView>(R.id.cv_yes)
                    var no = view.findViewById<CardView>(R.id.cv_no)
                    yes.setOnClickListener(View.OnClickListener {
                        var action = "cancelMeeting"
                        ApiServices.create().cancelMeeting(listHistoreMeeting.get(position).maLichCatToc.toString(),action).enqueue(
                            object : Callback<Boolean>{
                                override fun onResponse(
                                    call: Call<Boolean>,
                                    response: Response<Boolean>
                                ) {
                                    var check = response.body()
                                    if(check != null){
                                        if(check == true){
                                            dialog.dismiss()
                                            Toast.makeText(context, "Huỷ lịch thành công", Toast.LENGTH_SHORT).show()
                                            listHistoreMeeting.clear()
                                            getListHistoryMeeting()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<Boolean>, t: Throwable) {

                                }

                            }
                        )
                    })
                    no.setOnClickListener(View.OnClickListener {
                        dialog.dismiss()
                    })

                    dialog.show()
                }

            })
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_historyMeetingFragment_to_homeFragment)
        })
    }

    private fun hideBottomNavigation() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.historyMeetingF_imv_back)
        recyclerViewHistoryMeeting = view.findViewById(R.id.historyMeetingF_recyclerView)
        listHistoreMeeting = arrayListOf()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryMeetingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryMeetingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}