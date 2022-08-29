package com.example.barber.Fragment.Home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.barber.Model.Post
import com.example.barber.Model.Product
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewOnePostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewOnePostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView

    private lateinit var imvBack: ImageView
    private lateinit var imvPost: ImageView
    private lateinit var tvTitle : TextView
    private lateinit var tvContent : TextView
    private lateinit var post : Post

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
        val view = inflater.inflate(R.layout.fragment_view_one_post, container, false)
        init(view)
        hideNavi()
        back()
        getInFoPost()

        return view
    }



    private fun back() {
        val b = arguments
        val status = b?.getString("status")
        post = b?.get("post") as Post
        if(status.equals("fromHome")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromHome"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_homeFragment,bundle)
            })
        }

        if(status.equals("fromKnowLedge")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromKnowLedge"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_knowledgeFragment,bundle)
            })
        }
        if(status.equals("fromYN")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromYN"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_youNeedFragment,bundle)
            })
        }
        if(status.equals("fromHT")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromHT"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_hairTrendingFragment,bundle)
            })
        }
        if(status.equals("fromSearch")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromSearch"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_searchFragment,bundle)
            })
        }
        if(status.equals("fromJob")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromJob"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_jobFragment,bundle)
            })
        }
        if(status.equals("fromBlog")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromBlog"
                )
                controller.navigate(R.id.action_viewOnePostFragment_to_blogFragment,bundle)
            })
        }


    }

    private fun getInFoPost() {
        var bundle = arguments
        Log.e("asdasdd",bundle.toString())
        if(bundle != null){
            post = bundle?.get("post") as Post
            Log.e("piost",post.tieuDe)
            tvTitle.text = post.tieuDe
            tvContent.text = post.noiDung
            if(TextUtils.isEmpty(post.imgPost)){
                imvPost.visibility = View.INVISIBLE
            }
            else{
                Glide.with(requireContext()).load(post.imgPost).into(imvPost)
            }

        }


    }

    private fun hideNavi() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)

        imvBack = view.findViewById(R.id.viewPostF_imv_back)
        imvPost = view.findViewById(R.id.viewPostF_img)
        tvTitle = view.findViewById(R.id.viewPostF_tv_title)
        tvContent = view.findViewById(R.id.viewPostF_tv_content)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewOnePostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewOnePostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}