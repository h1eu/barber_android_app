package com.example.barber.Fragment.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.HairdresserListAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Interface.ClickItemHairdresserListener
import com.example.barber.Model.Hairdresser
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
 * Use the [HairdresserListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HairdresserListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var imgBack : ImageView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var listHairdresser : ArrayList<Hairdresser>

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hairdresser_list, container, false)
        init(view)
        getListHairdress()
        backtoHome()
        hideBottomNavigation()
        return view
    }

    private fun setupRecycler(listHairdresser : ArrayList<Hairdresser>) {
        var hairdresserListAdapter = HairdresserListAdapter(listHairdresser,requireContext())
        var linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = hairdresserListAdapter
    }

    private fun getListHairdress() {
        val action = "getAllHairdresser"
        ApiServices.create().getAllHairdresser(action).enqueue(
            object : Callback<ArrayList<Hairdresser>>{
                override fun onResponse(
                    call: Call<ArrayList<Hairdresser>>,
                    response: Response<ArrayList<Hairdresser>>
                ) {
                    listHairdresser = response.body()!!
                    setupRecycler(listHairdresser)
                }

                override fun onFailure(call: Call<ArrayList<Hairdresser>>, t: Throwable) {
                    Log.e("aaaaaaaaaaaaaa","Loi")
                }

            }
        )
    }

    private fun hideBottomNavigation() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun backtoHome() {
        imgBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_hairdresserListFragment_to_homeFragment)
        })
    }

    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imgBack = view.findViewById(R.id.hairdresserF_imv_back)
        recyclerView = view.findViewById(R.id.hairdresserF_recyclerView)
        listHairdresser = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HairdresserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HairdresserListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}