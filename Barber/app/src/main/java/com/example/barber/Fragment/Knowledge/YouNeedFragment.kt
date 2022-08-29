package com.example.barber.Fragment.Knowledge

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
 * Use the [YouNeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YouNeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private var typePost = "Bạn cần biết"
    private lateinit var listPost : ArrayList<Post>
    private lateinit var imvBack : ImageView
    private lateinit var rv : RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_you_need, container, false)
        init(view)
        hideNavi()
        back()
        getPost()
        return view
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_youNeedFragment_to_knowledgeFragment)
        })
    }

    private fun getPost() {
        var action = "getPost"
        ApiServices.create().getPost(typePost,action).enqueue(
            object : Callback<ArrayList<Post>> {
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

            })
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
                        "status" to "fromYN"
                    )
                    controller.navigate(R.id.action_youNeedFragment_to_viewOnePostFragment,bundle)
                }

            }
        )
    }

    private fun hideNavi() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        listPost = arrayListOf()
        imvBack = view.findViewById(R.id.youNeedF_imv_back)
        rv = view.findViewById(R.id.youNeedF_recyclerView)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YouNeedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YouNeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}