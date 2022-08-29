package com.example.barber.Fragment.Knowledge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.PostImgAdapter
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
 * Use the [KnowledgeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KnowledgeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var rvYouNeed : RecyclerView
    private lateinit var rvHairTrending : RecyclerView
    private lateinit var youNeedNext : LinearLayout
    private lateinit var hairTredingNext : LinearLayout
    private lateinit var listPostYouNeed : ArrayList<Post>
    private lateinit var listHairTrending : ArrayList<Post>
    private var typeYN = "Bạn cần biết"
    private var typeHT = "Xu hướng hot nhất"


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
        var view = inflater.inflate(R.layout.fragment_knowledge, container, false)
        init(view)
        unhideBottom()
        getPost()
        goYouNeed()
        onBackPress()
        goHairTrending()
        return view
    }

    private fun onBackPress() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }
    private fun goHairTrending() {
        hairTredingNext.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf(
                "listPost" to listHairTrending,
                "status" to "fromKnowLedge"
            )
            controller.navigate(R.id.action_knowledgeFragment_to_hairTrendingFragment,bundle)
        })
    }

    private fun goYouNeed() {
        youNeedNext.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf(
                "listPost" to listPostYouNeed,
                "status" to "fromKnowLedge"
            )
            controller.navigate(R.id.action_knowledgeFragment_to_youNeedFragment,bundle)
        })
    }

    private fun getPost() {
        var action = "getPost"
        ApiServices.create().getPost(typeYN,action).enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until 2 ){
                            listPostYouNeed.add(listR.get(i))
                        }
                        setUpRVYN()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.e("khoing ket noi duoc",t.toString())
                }

            }
        )
        ApiServices.create().getPost(typeHT,action).enqueue(
            object : Callback<ArrayList<Post>>{
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until 2 ){
                            listHairTrending.add(listR.get(i))
                        }
                        setUpRVHT()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.e("khoing ket noi duoc",t.toString())
                }

            }
        )
    }

    private fun setUpRVHT() {
        val linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val postImgAdapter = PostImgAdapter(listHairTrending,typeHT,requireContext())
        rvHairTrending.layoutManager = linearLayoutManager
        rvHairTrending.adapter = postImgAdapter
        postImgAdapter.setOnClickListener(
            object : PostImgAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    val bundle = bundleOf(
                        "post" to listHairTrending.get(position),
                        "status" to "fromKnowLedge"
                    )
                    controller.navigate(R.id.action_knowledgeFragment_to_viewOnePostFragment,bundle)
                }

            }
        )
    }

    private fun setUpRVYN() {
        val linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val postImgAdapter = PostImgAdapter(listPostYouNeed,typeHT,requireContext())
        rvYouNeed.layoutManager = linearLayoutManager
        rvYouNeed.adapter = postImgAdapter
        postImgAdapter.setOnClickListener(
            object : PostImgAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    val bundle = bundleOf(
                        "post" to listPostYouNeed.get(position),
                        "status" to "fromKnowLedge"
                    )
                    controller.navigate(R.id.action_knowledgeFragment_to_viewOnePostFragment,bundle)
                }

            }
        )
    }

    private fun unhideBottom() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        rvYouNeed = view.findViewById(R.id.knowleadgeF_recyclerView_youNeed)
        rvHairTrending = view.findViewById(R.id.knowleadgeF_recyclerView_hairTrending)
        youNeedNext = view.findViewById(R.id.knowledgeF_layout_youNeed_next)
        hairTredingNext = view.findViewById(R.id.knowledgeF_layout_hairTrending_next)
        listPostYouNeed = arrayListOf()
        listHairTrending = arrayListOf()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KnowledgeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KnowledgeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}