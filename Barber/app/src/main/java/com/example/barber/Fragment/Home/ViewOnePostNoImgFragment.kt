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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.barber.Model.Post
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewOnePostNoImgFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewOnePostNoImgFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView

    private lateinit var imvBack: ImageView
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
        val view = inflater.inflate(R.layout.fragment_view_one_post_no_img, container, false)
        init(view)
        hideNavi()
        getInfoPost()
        back()
        return view
    }

    private fun back() {
        val b = arguments
        val status = b?.getString("status")
        post = b?.get("post") as Post
        if(status.equals("fromInfo")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromInfo"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_infoFragment,bundle)
            })
        }
        if(status.equals("fromPriceListMen")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromPriceListMen"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_priceListFragment,bundle)
            })
        }
        if(status.equals("fromPriceListService")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromPriceListService"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_priceListFragment,bundle)
            })
        }
        if(status.equals("fromTrading")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromTrading"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_tradingConditionFragment,bundle)
            })
        }
        if(status.equals("fromAbout")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromAbout"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_aboutBarberFragment2,bundle)
            })
        }
        if(status.equals("fromSecu")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromSecu"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_aboutBarberFragment2,bundle)
            })
        }
        if(status.equals("fromSer")){
            imvBack.setOnClickListener(View.OnClickListener {
                var bundle = bundleOf(
                    "post" to post,
                    "status" to "fromSer"
                )
                controller.navigate(R.id.action_viewOnePostNoImgFragment_to_aboutBarberFragment2,bundle)
            })
        }
    }

    private fun getInfoPost() {
        var bundle = arguments
        Log.e("asdasdd",bundle.toString())
        if(bundle != null){
            post = bundle?.get("post") as Post
            Log.e("piost",post.tieuDe)
            tvTitle.text = post.tieuDe
            tvContent.text = post.noiDung
        }


    }
    private fun hideNavi() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.viewPostNoF_imv_back)
        tvTitle = view.findViewById(R.id.viewPostNoF_tv_title)
        tvContent = view.findViewById(R.id.viewPostNoF_tv_content)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewOnePostNoImgFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewOnePostNoImgFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}