package com.example.barber.Fragment.Search

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
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var rvjob : RecyclerView
    private lateinit var rvblog : RecyclerView
    private lateinit var jobNext : LinearLayout
    private lateinit var blogNext : LinearLayout
    private lateinit var listPostjob : ArrayList<Post>
    private lateinit var listblog : ArrayList<Post>
    private var typeJob = "Tuyển dụng"
    private var typeBlog = "Blog"

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
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        init(view)
        unhideBottom()
        getPost()
        goJob()
        onBackPress()
        goBlog()
        return view
    }

    private fun onBackPress() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }
    private fun goJob() {
        jobNext.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf(
                "listPost" to listPostjob,
                "status" to "fromSearch"
            )
            controller.navigate(R.id.action_searchFragment_to_jobFragment,bundle)
        })
    }

    private fun goBlog() {
        blogNext.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf(
                "listPost" to listblog,
                "status" to "fromSearch"
            )
            controller.navigate(R.id.action_searchFragment_to_blogFragment,bundle)
        })
    }
    private fun getPost() {
        var action = "getPost"
        ApiServices.create().getPost(typeBlog,action).enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until 2){
                            listblog.add(listR.get(i))
                        }
                        setUpRVBlog()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.e("khoing ket noi duoc",t.toString())
                }

            }
        )
        ApiServices.create().getPost(typeJob,action).enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    var list = response.body()
                    if(list != null){
                        var listR = list.asReversed()
                        for(i in 0 until 2){
                            listPostjob.add(listR.get(i))
                        }
                        setUpRVJob()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.e("khoing ket noi duoc",t.toString())
                }

            }
        )
    }

    private fun setUpRVJob() {
        val linearLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        val postImgAdapter = PostImgAdapter(listPostjob,typeJob,requireContext())
        rvjob.layoutManager = linearLayoutManager
        rvjob.adapter = postImgAdapter
        postImgAdapter.setOnClickListener(
            object : PostImgAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    val bundle = bundleOf(
                        "post" to listPostjob.get(position),
                        "status" to "fromSearch"
                    )
                    controller.navigate(R.id.action_searchFragment_to_viewOnePostFragment,bundle)
                }

            }
        )
    }

    private fun setUpRVBlog() {
        val linearLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        val postImgAdapter = PostImgAdapter(listblog,typeBlog,requireContext())
        rvblog.layoutManager = linearLayoutManager
        rvblog.adapter = postImgAdapter
        postImgAdapter.setOnClickListener(
            object : PostImgAdapter.onItemClicklistener{
                override fun onClickView(position: Int) {
                    val bundle = bundleOf(
                        "post" to listblog.get(position),
                        "status" to "fromSearch"
                    )
                    controller.navigate(R.id.action_searchFragment_to_viewOnePostFragment,bundle)
                }

            }
        )
    }

    private fun unhideBottom() {
        bottomNavigationView.visibility = View.VISIBLE
    }
    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        rvblog = view.findViewById(R.id.searchF_recyclerView_blog)
        rvjob = view.findViewById(R.id.searchF_recyclerView_job)
        blogNext = view.findViewById(R.id.searchF_layout_lookbook_next)
        jobNext = view.findViewById(R.id.searchF_layout_job_next)
        listblog = arrayListOf()
        listPostjob = arrayListOf()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}