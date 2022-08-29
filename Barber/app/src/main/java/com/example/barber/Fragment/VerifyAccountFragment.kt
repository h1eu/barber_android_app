package com.example.barber.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.barber.Api.ApiServices
import com.example.barber.Model.User
import com.example.barber.R
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyAccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvLoading : TextView
    private lateinit var tvRs : TextView
    private lateinit var aniLoading : LottieAnimationView
    private lateinit var aniRs : LottieAnimationView
    private lateinit var cardViewLogin : CardView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController

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
        val view = inflater.inflate(R.layout.fragment_verify_account, container, false)
        init(view)
        checkRegister()
        handle()
        return view
    }

    private fun checkRegister() {
        verifyAcc()
    }

    private fun verifyAcc() {
            val bundle = arguments
            val username = bundle?.getString("username")
            val password = bundle?.getString("password")
            val name = bundle?.getString("name")
            val phone = bundle?.getString("phone")
            val questionAcc = bundle?.getString("questionAcc")

            val action = "register"
            ApiServices.create().registerUser(username.toString(),password.toString(),name.toString(),phone.toString(),questionAcc.toString(),action).enqueue(
                object  : Callback<Unit> {
                    override fun onResponse(
                        call: Call<Unit>,
                        response: Response<Unit>
                    ) {
                        MainScope().launch {
                            hideAnimation()
                        }
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.e("error register",t.toString())
                        Toast.makeText(context, "Lỗi đăng ký", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }

    private fun handle(){
        cardViewLogin.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_verifyAccountFragment_to_loginFragment)
        })
    }

    private suspend fun hideAnimation(){
        delay(2000)
        tvLoading.visibility = View.INVISIBLE
        aniLoading.visibility = View.INVISIBLE
        aniRs.visibility = View.VISIBLE
        tvRs.visibility = View.VISIBLE
        cardViewLogin.visibility = View.VISIBLE
    }

    private fun init(view : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        tvLoading = view.findViewById(R.id.verifyAccF_tv_loading)
        tvRs = view.findViewById(R.id.verifyAccF_tv_rs)
        aniLoading = view.findViewById(R.id.verifyAccF_loading)
        aniRs = view.findViewById(R.id.verifyAccF_rs)
        cardViewLogin = view.findViewById(R.id.verifyAccF_cardView_login)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}