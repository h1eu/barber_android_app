package com.example.barber.Fragment

import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Account
import com.example.barber.Model.Constant
import com.example.barber.Model.LoadingDialog
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var tvRegister : TextView
    private lateinit var cardViewLogin : CardView
    private lateinit var edtUsername : TextInputEditText
    private lateinit var edtPassword : TextInputEditText
    private lateinit var tvForgotPass : TextView
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        init(view)
        register()
        gotoForgotPassword()
        login()
        onBack()
        hideBot()
        return view
    }

    private fun hideBot() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun onBack() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun login() {
        cardViewLogin.setOnClickListener(View.OnClickListener {
            var loadingDialog = LoadingDialog(requireActivity())
            var tenTaiKhoan = edtUsername.text
            var matKhau = edtPassword.text
            if (TextUtils.isEmpty(tenTaiKhoan.toString()) || TextUtils.isEmpty(matKhau.toString()))
            {
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            }
            else{
                loadingDialog.startLoading()
                var action = "login"
                ApiServices.create().login(tenTaiKhoan.toString(),matKhau.toString(),action)
                    .enqueue(
                        object : Callback<String>{
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                val responApi = response.body()!!
                                if (responApi.equals("true")){
                                    val bundle = bundleOf(
                                        "username" to tenTaiKhoan.toString()
                                    )
                                    Constant.KeyWithScreen.username = edtUsername.text.toString()
                                    loadingDialog.dismissDialog()
                                    controller?.navigate(R.id.action_loginFragment_to_homeFragment,bundle)
                                }
                                else if (responApi.equals("false")){
                                    Toast.makeText(context, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                                    loadingDialog.dismissDialog()
                                }
                            }
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                loadingDialog.dismissDialog()
                                Log.e("ket noi loi",t.toString())
                            }

                        }
                    )
            }
        })
    }

    private fun gotoForgotPassword() {
        tvForgotPass.setOnClickListener(View.OnClickListener {
            controller?.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        })
    }

    private fun register() {
        tvRegister.setOnClickListener(View.OnClickListener {
            controller?.navigate(R.id.action_loginFragment_to_registerFragment)
        })
    }

    private fun init(view : View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        tvRegister = view.findViewById(R.id.register)
        cardViewLogin = view.findViewById(R.id.loginF_cardView_login)
        edtUsername = view.findViewById(R.id.loginF_edt_username)
        edtPassword = view.findViewById(R.id.loginF_edt_password)
        tvForgotPass = view.findViewById(R.id.loginF_tv_forgotPass)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}