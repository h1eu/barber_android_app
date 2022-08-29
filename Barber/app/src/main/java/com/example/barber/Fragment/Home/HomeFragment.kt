package com.example.barber.Fragment.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.PostImgAdapter
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Constant
import com.example.barber.Model.LoadingDialog
import com.example.barber.Model.Post
import com.example.barber.Model.User
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var constraintLayoutBarberMeeting: ConstraintLayout
    private lateinit var constraintLayoutPriceList: ConstraintLayout
    private lateinit var constraintLayoutFeedBack: ConstraintLayout
    private lateinit var constraintLayoutHairdresser: ConstraintLayout
    private lateinit var constraintLayoutInfo: ConstraintLayout
    private lateinit var constraintLayoutHistoreMeeting: ConstraintLayout
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var tvNameAcc : TextView
    private lateinit var listPost : ArrayList<Post>
    private lateinit var rv : RecyclerView
    private var typePost = "Bài viết phổ biến"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        getUser()

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        init(view)
        unHideBottomNavigation()
        onBackPress()
        getPost()
        goToBarberMeeting(navHostFragment,controller)
        goToPriceList(navHostFragment,controller)
        goToFeedBack(navHostFragment,controller)
        goToInfo(navHostFragment,controller)
        goToHistoryMeeting(navHostFragment,controller)
        goToHairdresser(navHostFragment,controller)
        return view
    }

    private fun getPost() {
        var action = "getPost"
        ApiServices.create().getPost(typePost,action).enqueue(
            object : Callback<ArrayList<Post>>{
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until listR.size){
                            listPost.add(listR.get(i))
                        }
                        setUpRV()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.e("khoing ket noi duoc",t.toString())
                }

            }
        )
    }

    private fun setUpRV() {
        val gridLayoutManager = GridLayoutManager(requireContext(),2)
        val postImgAdapter = PostImgAdapter(listPost,typePost,requireContext())
        rv.layoutManager = gridLayoutManager
        rv.adapter = postImgAdapter
        postImgAdapter.setOnClickListener(
            object : PostImgAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    val bundle = bundleOf(
                        "post" to listPost.get(position),
                        "status" to "fromHome"
                    )
                    controller.navigate(R.id.action_homeFragment_to_viewOnePostFragment,bundle)
                }

            }
        )
    }

    private fun unHideBottomNavigation() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.isVisible = true

    }

    override fun onResume() {
        super.onResume()
        tvNameAcc.text = Constant.KeyWithScreen.nameUser
    }

    private fun getUser() {
        val bundle = arguments
        val username = bundle?.getString("username")
        val action = "getUser"
        ApiServices.create().getUser(username.toString(),action).enqueue(
            object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    var user = response.body()
                    if (user != null) {
                        Constant.KeyWithScreen.nameUser = user.hoVaTen
                        Constant.KeyWithScreen.IdUser = user.maNguoiDung.toString()
                        Constant.KeyWithScreen.idAcc = user.maTaiKhoanUser.toString()
                        Constant.KeyWithScreen.phoneUser = user.soDienThoai.toString()
                        tvNameAcc.text = Constant.KeyWithScreen.nameUser
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                }

            }
        )
    }


    private fun goToHairdresser(navHostFragment: NavHostFragment, controller: NavController) {
        constraintLayoutHairdresser.setOnClickListener(View.OnClickListener {
            this.controller?.navigate(R.id.action_homeFragment_to_hairdresserListFragment)
        })
    }

    private fun goToHistoryMeeting(navHostFragment: NavHostFragment, controller: NavController) {
        constraintLayoutHistoreMeeting.setOnClickListener(View.OnClickListener {
            this.controller?.navigate(R.id.action_homeFragment_to_historyMeetingFragment)
        })
    }

    private fun goToInfo(navHostFragment: NavHostFragment, controller: NavController) {
        constraintLayoutInfo.setOnClickListener(View.OnClickListener {
            this.controller?.navigate(R.id.action_homeFragment_to_infoFragment)
        })
    }

    private fun onBackPress() {
       val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finish()
       }

    }

    private fun goToFeedBack(navHostFragment: NavHostFragment, controller: NavController) {
        constraintLayoutFeedBack.setOnClickListener(View.OnClickListener {
            this.controller?.navigate(R.id.action_homeFragment_to_feedBackFragment)
        })
    }

    private fun goToPriceList(navHostFragment: NavHostFragment, controller: NavController) {
        constraintLayoutPriceList.setOnClickListener(View.OnClickListener {
            this.controller?.navigate(R.id.action_homeFragment_to_priceListFragment)
        })
    }

    private fun goToBarberMeeting(navHostFragment: NavHostFragment, controller: NavController) {
        constraintLayoutBarberMeeting.setOnClickListener(View.OnClickListener {
            this.controller?.navigate(R.id.action_homeFragment_to_barberMeetingFragment)
        })
    }


    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        constraintLayoutBarberMeeting = view.findViewById(R.id.homeF_layout_barberMeeting)
        constraintLayoutPriceList = view.findViewById(R.id.homeF_layout_priceList)
        constraintLayoutFeedBack = view.findViewById(R.id.homeF_layout_feedback)
        constraintLayoutHairdresser = view.findViewById(R.id.homeF_layout_hairdresser)
        constraintLayoutInfo = view.findViewById(R.id.homeF_layout_info)
        constraintLayoutHistoreMeeting = view.findViewById(R.id.homeF_layout_historyMeeting)
        tvNameAcc = view.findViewById(R.id.homeF_tv_account)
        listPost = arrayListOf()
        rv = view.findViewById(R.id.homeF_recyclerView)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


