package com.example.barber.Fragment.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedBackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedBackFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var cvFeedback : CardView

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
        var view = inflater.inflate(R.layout.fragment_feed_back, container, false)
        init(view)
        back()
        hideBottomNavigation()
        rate()
        return view
    }

    private fun rate() {
        cvFeedback.setOnClickListener(View.OnClickListener {
            var star = ratingBar.rating.toInt()
            var msg = ""
            when(star){
                1 -> {
                    msg = "Xin ch??n th??nh xin l???i qu?? kh??ch, Barber s??? c???i thi???n nh???m kh???c ph???c tr???i nghi???m n??y!"
                }
                2 ->{
                    msg = "Barber s??? c??? g???ng l??m h??i l??ng qu?? kh??ch h??n n???a !"
                }
                3 ->{
                    msg = "Ph???c v??? b???n l?? ni???m vinh h???nh c???a ch??ng t??i !"
                }
                4 ->{
                    msg = "C???m ??n b???n r???t nhi???u v?? ????nh gi?? t??ch c???c !"
                }
                5 ->{
                    msg = "Th???t tuy???t v???i, Barber s??? lu??n c???i thi???n ????? l??m b???n h??i l??ng nh???t !"
                }
            }
            var builder = AlertDialog.Builder(requireActivity())
            var view = View.inflate(requireActivity(),R.layout.dialog_comfirm,null)
            builder.setView(view)
            var title : TextView = view.findViewById(R.id.dialogTitle)
            var msgDialog : TextView = view.findViewById(R.id.dialogMsg)
            title.text = "Ph???n h???i"
            msgDialog.text = msg
            var dialog : AlertDialog = builder.create()
            var yes = view.findViewById<CardView>(R.id.cv_yes)
            var no = view.findViewById<CardView>(R.id.cv_no)
            yes.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })
            no.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })
            dialog.show()
        })
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_feedBackFragment_to_homeFragment)
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
        imvBack = view.findViewById(R.id.feedBackF_imv_back)
        cvFeedback = view.findViewById(R.id.feedBackF_cardView_rate)
        ratingBar = view.findViewById(R.id.feedBackF_ratingBar)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeedBackFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedBackFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}