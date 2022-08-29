package com.example.barber.Fragment.User.Support

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.PostNoImgAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Post
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
 * Use the [ServicesBarberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServicesBarberFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var listInfoPost : ArrayList<Post>
    private lateinit var rv : RecyclerView
    private lateinit var imvBack : ImageView
    private var type = "Cam kết dịch vụ"

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
        val  view =  inflater.inflate(R.layout.fragment_services_barber, container, false)
        init(view)
        back()
        hideBottom()
        getPost()
        return view
    }

    private fun hideBottom() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun getPost() {
        var action = "getPost"

        ApiServices.create().getPost(type,action).enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    var list = response.body()
                    if(list != null){
                        for(i in 0 until list.size){
                            listInfoPost.add(list.get(i))
                        }
                        setUpRV()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.e("loi ket noi",t.toString())
                }

            }
        )
    }

    private fun setUpRV() {
        var linearLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        var postNoImgAdapter = PostNoImgAdapter(listInfoPost,type)
        rv.layoutManager = linearLayoutManager
        rv.adapter = postNoImgAdapter
        postNoImgAdapter.setOnClickListener(object : PostNoImgAdapter.onItemClicklistener{
            override fun onClickView(position: Int) {
                val bundle = bundleOf(
                    "post" to listInfoPost.get(position),
                    "status" to "fromSer"
                )
                controller.navigate(R.id.action_servicesBarberFragment_to_viewOnePostNoImgFragment,bundle)
            }

        })

    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_servicesBarberFragment_to_supportFragment)
        })
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        listInfoPost = arrayListOf()
        rv = view.findViewById(R.id.servicesBarberF_recyclerView)
        imvBack = view.findViewById(R.id.servicesBarberF_imv_back)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ServicesBarberFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServicesBarberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}