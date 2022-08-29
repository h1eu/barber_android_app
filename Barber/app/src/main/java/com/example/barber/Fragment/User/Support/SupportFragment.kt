package com.example.barber.Fragment.User.Support

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SupportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SupportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var cvService: CardView
    private lateinit var cvAboutBarber: CardView
    private lateinit var cvTrading: CardView
    private lateinit var cvSecu: CardView
    private lateinit var imvBack: ImageView

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
        val view = inflater.inflate(R.layout.fragment_support, container, false)
        init(view)
        hideNavi()
        goService()
        goAbout()
        goSecu()
        goTrading()
        back()
        return view
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_supportFragment_to_userFragment)
        })
    }

    private fun goAbout() {
        cvAboutBarber.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_supportFragment_to_aboutBarberFragment2)
        })
    }

    private fun goSecu() {
        cvSecu.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_supportFragment_to_infoSecurityFragment)
        })
    }

    private fun goService() {
        cvService.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_supportFragment_to_servicesBarberFragment)
        })
    }

    private fun goTrading() {
        cvTrading.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_supportFragment_to_tradingConditionFragment)
        })
    }

    private fun hideNavi() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        cvService = view.findViewById(R.id.supportF_cardView_servicesBarber)
        cvSecu = view.findViewById(R.id.supportF_cardView_infoSecurity)
        cvTrading = view.findViewById(R.id.supportF_cardView_tradingCondition)
        cvAboutBarber = view.findViewById(R.id.supportF_cardView_aboutBarber)
        imvBack = view.findViewById(R.id.supportF_imv_back)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SupportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SupportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}