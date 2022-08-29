package com.example.barber.Fragment.User

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.barber.Model.Constant
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var cardViewInfoAcc : CardView
    private lateinit var cardViewChangePass: CardView
    private lateinit var cardViewSupport : CardView
    private lateinit var cardViewStoreList : CardView
    private lateinit var cardViewLogout : CardView
    private lateinit var tvNameUser : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        tvNameUser.text = Constant.KeyWithScreen.nameUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        init(view)
        unHideBottomNavigation()
        infoUser()
        storeList()
        changePassword()
        support()
        logout()
        onBackPress()
        return view
    }



    private fun logout() {
        cardViewLogout.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_userFragment_to_loginFragment)
        })
    }

    private fun unHideBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun support() {
        cardViewSupport.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_userFragment_to_supportFragment)
        })
    }

    private fun changePassword() {
        cardViewChangePass.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_userFragment_to_changePassFragment)
        })
    }

    private fun storeList() {
        cardViewStoreList.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_userFragment_to_storeListFragment)
        })
    }

    private fun infoUser() {
        cardViewInfoAcc.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_userFragment_to_infoAccFragment)
        })
    }

    private fun onBackPress() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }


    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        tvNameUser = view.findViewById(R.id.userF_tv_account)
        cardViewInfoAcc = view.findViewById(R.id.userF_cardView_infoAccount)
        cardViewLogout = view.findViewById(R.id.userF_cardView_logout)
        cardViewSupport = view.findViewById(R.id.userF_cardView_support)
        cardViewChangePass = view.findViewById(R.id.userF_cardView_changePass)
        cardViewStoreList = view.findViewById(R.id.userF_cardView_storeList)
      //  tvNameUser.text = Constant.KeyWithScreen.nameUser
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}