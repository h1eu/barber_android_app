package com.example.barber.Fragment.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.StoreListAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Store
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
 * Use the [StoreListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var listStore : ArrayList<Store>

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
        val view = inflater.inflate(R.layout.fragment_store_list, container, false)
        init(view)
        hideNavigation()
        back()
        getListStore()
        return view
    }

    private fun setupRecyclerView(listStore: ArrayList<Store>) {
        val linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        val storeListAdapter = StoreListAdapter(listStore)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = storeListAdapter
    }

    private fun getListStore() {
        val action = "getAllStore"
        ApiServices.create().getAllStore(action).enqueue(
            object : Callback<ArrayList<Store>>{
                override fun onResponse(
                    call: Call<ArrayList<Store>>,
                    response: Response<ArrayList<Store>>
                ) {
                    listStore = response.body()!!
                    setupRecyclerView(listStore)
                }

                override fun onFailure(call: Call<ArrayList<Store>>, t: Throwable) {

                }

            }
        )
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_storeListFragment_to_userFragment)
        })
    }

    private fun hideNavigation() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.storeListF_imv_back)
        recyclerView = view.findViewById(R.id.storeListF_recyclerView)
        listStore = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoreListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}