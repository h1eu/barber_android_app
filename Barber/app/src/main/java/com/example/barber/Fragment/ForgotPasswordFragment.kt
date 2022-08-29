package com.example.barber.Fragment

import android.os.Bundle
import android.text.TextUtils
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
import com.example.barber.Api.ApiServices
import com.example.barber.Model.LoadingDialog
import com.example.barber.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvLogin : TextView
    private lateinit var tvForgotPass : TextView
    private lateinit var tvResetPass : TextView
    private lateinit var edtUsername : TextInputEditText
    private lateinit var edtQuestionPass : TextInputEditText
    private lateinit var edtPassword : TextInputEditText
    private lateinit var edtComfirmPass : TextInputEditText
    private lateinit var layoutUsername : TextInputLayout
    private lateinit var layoutQuestionPass : TextInputLayout
    private lateinit var layoutPassword : TextInputLayout
    private lateinit var layoutComfirmPassword : TextInputLayout
    private lateinit var cardViewCheck : CardView
    private lateinit var cardViewComfirm : CardView
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
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        init(view)
        checkAccount()
        goToLogin()
        return view
    }

    private fun checkAccount() {
        cardViewCheck.setOnClickListener(View.OnClickListener {
            val loadingDialog = LoadingDialog(requireActivity())
            val username = edtUsername.text
            val questionPass = edtQuestionPass.text
            if (TextUtils.isEmpty(username.toString()) || TextUtils.isEmpty(questionPass.toString()) ){
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            }
            else{
                loadingDialog.startLoading()
                val action = "checkUserForgotPass"
                ApiServices.create().checkUserForgotPass(username.toString(),questionPass.toString(),action).enqueue(
                    object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val responseResult = response.body()
                            if(responseResult.equals("false")){
                                Toast.makeText(context, "Sai thông tin", Toast.LENGTH_SHORT).show()
                                loadingDialog.dismissDialog()
                            }
                            else if(responseResult.equals("true")){
                                Toast.makeText(context, "Đặt lại mật khẩu", Toast.LENGTH_SHORT).show()
                                loadingDialog.dismissDialog()
                                tvForgotPass.visibility = View.INVISIBLE
                                layoutUsername.visibility = View.INVISIBLE
                                layoutQuestionPass.visibility = View.INVISIBLE
                                cardViewCheck.visibility = View.INVISIBLE
                                tvResetPass.visibility = View.VISIBLE
                                layoutPassword.visibility = View.VISIBLE
                                layoutComfirmPassword.visibility = View.VISIBLE
                                cardViewComfirm.visibility = View.VISIBLE

                                cardViewComfirm.setOnClickListener(View.OnClickListener {
                                    val password = edtPassword.text
                                    val comfirmPassword = edtComfirmPass.text
                                    if(TextUtils.isEmpty(password.toString()) || TextUtils.isEmpty(comfirmPassword.toString())){
                                        Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
                                        }
                                        else{
                                            if(password.toString().equals(comfirmPassword.toString())){
                                                loadingDialog.startLoading()
                                                val action = "resetPassword"
                                                ApiServices.create().resetPassword(username.toString(),password.toString(),action).enqueue(
                                                    object : Callback<Unit>{
                                                        override fun onResponse(
                                                            call: Call<Unit>,
                                                            response: Response<Unit>
                                                        ) {
                                                            Toast.makeText(context,"Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                                            loadingDialog.dismissDialog()
                                                        }

                                                        override fun onFailure(
                                                            call: Call<Unit>,
                                                            t: Throwable
                                                        ) {
                                                            Toast.makeText(context,"Lỗi", Toast.LENGTH_SHORT).show()
                                                            loadingDialog.dismissDialog()
                                                        }

                                                    }
                                                )
                                            }
                                            else
                                            {
                                                Toast.makeText(context,"Mật khẩu phải trùng nhau", Toast.LENGTH_SHORT).show()
                                                loadingDialog.dismissDialog()
                                            }

                                }
                                })
                            }

                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            loadingDialog.dismissDialog()
                            Toast.makeText(context, "tài khoản không tồn tại", Toast.LENGTH_SHORT).show()
                        }

                    }
                )
            }
        })
    }

    private fun goToLogin() {
        tvLogin.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        })

    }

    private fun init(v : View){
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        tvLogin = v.findViewById(R.id.forgotPassF_tv_login)
        tvForgotPass = v.findViewById(R.id.forgotPassF_tv)
        tvResetPass = v.findViewById(R.id.forgotPassF_tv_resetpass)
        edtUsername = v.findViewById(R.id.forgotPassF_edt_username)
        edtQuestionPass = v.findViewById(R.id.forgotPassF_edt_questionPass)
        edtPassword = v.findViewById(R.id.forgotPassF_edt_password)
        edtComfirmPass = v.findViewById(R.id.forgotPassF_edt_comfirmPassword)
        cardViewCheck = v.findViewById(R.id.forgotPassF_cardView_check)
        cardViewComfirm = v.findViewById(R.id.forgotPassF_cardView_comfirm)
        layoutUsername = v.findViewById(R.id.forgotPassF_edt_usernameLayout)
        layoutQuestionPass = v.findViewById(R.id.forgotPassF_edt_questionPassLayout)
        layoutPassword = v.findViewById(R.id.forgotPassF_edt_passNewLayout)
        layoutComfirmPassword = v.findViewById(R.id.forgotPassF_edt_comfirmPassNewLayout)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}