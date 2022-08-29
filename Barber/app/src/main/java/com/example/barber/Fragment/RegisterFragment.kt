package com.example.barber.Fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Account
import com.example.barber.Model.User
import com.example.barber.R
import com.google.android.material.textfield.TextInputEditText
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
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var tvLogin : TextView
    private lateinit var edtUsername : TextInputEditText
    private lateinit var edtName : TextInputEditText
    private lateinit var edtPhone : TextInputEditText
    private lateinit var edtPassword : TextInputEditText
    private lateinit var edtComfirmPass : TextInputEditText
    private lateinit var edtQuestionAcc : TextInputEditText
    private lateinit var cardViewRegister : CardView

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
        var view = inflater.inflate(R.layout.fragment_register, container, false)
        init(view)
        register()
        goToLogin()
        return view
    }

    private fun register() {
        cardViewRegister.setOnClickListener(View.OnClickListener {
            var username = edtUsername.text
            var name = edtName.text
            var phone = edtPhone.text
            var password = edtPassword.text
            var comfirmPass = edtComfirmPass.text
            var questionAcc = edtQuestionAcc.text

            if (TextUtils.isEmpty(username.toString()) || TextUtils.isEmpty(name.toString()) || TextUtils.isEmpty(phone.toString())
                || TextUtils.isEmpty(password.toString()) || TextUtils.isEmpty(comfirmPass.toString()) || TextUtils.isEmpty(questionAcc.toString()))
            {
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val action = "checkUsername"
                ApiServices.create().checkUsername(username.toString(),action).enqueue(
                    object : Callback<Account>{
                        override fun onResponse(call: Call<Account>, response: Response<Account>) {
                            Toast.makeText(context, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<Account>, t: Throwable) {
                            if (password.toString().equals(comfirmPass.toString())){
                                val bundle = bundleOf(
                                    "username" to username.toString(),
                                    "name" to name.toString(),
                                    "phone" to phone.toString(),
                                    "password" to password.toString(),
                                    "questionAcc" to questionAcc.toString()
                                )
                                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                                val controller = navHostFragment?.findNavController()
                                controller?.navigate(R.id.action_registerFragment_to_verifyAccountFragment,bundle)
                            }
                            else{
                                Toast.makeText(context,"Mật khẩu phải trùng nhau", Toast.LENGTH_SHORT).show()
                            }

                        }

                    }
                )
            }
        })
    }

    private fun goToLogin(){
        tvLogin.setOnClickListener(View.OnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val controller = navHostFragment?.findNavController()
            controller?.navigate(R.id.action_registerFragment_to_loginFragment)
        })
    }

    private fun init(view: View) {
        tvLogin = view.findViewById(R.id.registerF_tv_login)
        edtUsername = view.findViewById(R.id.registerF_edt_username)
        edtName = view.findViewById(R.id.registerF_edt_name)
        edtPhone = view.findViewById(R.id.registerF_edt_phone)
        edtPassword = view.findViewById(R.id.registerF_edt_password)
        edtComfirmPass = view.findViewById(R.id.registerF_edt_comfirmPassword)
        edtQuestionAcc = view.findViewById(R.id.registerF_edt_questionPassword)
        cardViewRegister = view.findViewById(R.id.registerF_cardView_register)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}