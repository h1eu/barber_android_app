package com.example.barber.Fragment.User

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.barber.Api.ApiServices
import com.example.barber.Model.Constant
import com.example.barber.Model.LoadingDialog
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePassFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var edtPasswordOld : TextInputEditText
    private lateinit var edtPasswordNew : TextInputEditText
    private lateinit var edtPwNewComfirm : TextInputEditText
    private lateinit var cardViewSave : CardView


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
        val view = inflater.inflate(R.layout.fragment_change_pass, container, false)
        init(view)
        hideNavigation()
        back()
        changePassword()
        return view
    }

    private fun changePassword() {
        cardViewSave.setOnClickListener(View.OnClickListener {
            val dialog = LoadingDialog(requireActivity())
            val oldPass = edtPasswordOld.text.toString()
            val newPass = edtPasswordNew.text.toString()
            val comfirmPass = edtPwNewComfirm.text.toString()
            if(TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(comfirmPass)){
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            }
            else {
                dialog.startLoading()
                val action = "changePassword"
                ApiServices.create().changePassword(Constant.KeyWithScreen.idAcc,oldPass,newPass,action).enqueue(
                    object : Callback<String>{
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val rs = response.body()
                            if (rs != null){
                                if (rs.equals("false")){
                                    Toast.makeText(context, "mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show()
                                    dialog.dismissDialog()
                                }
                                else if (rs.equals("true")){
                                    if (newPass.equals(comfirmPass)){
                                        Toast.makeText(context, "đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                        dialog.dismissDialog()
                                    }
                                    else{
                                        Toast.makeText(context, "Xác nhận mật khẩu thất bại", Toast.LENGTH_SHORT).show()
                                        dialog.dismissDialog()
                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {

                        }

                    }
                )

            }
        })
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_changePassFragment_to_userFragment)
        })
    }

    private fun hideNavigation() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.changePassF_imv_back)
        edtPasswordOld = view.findViewById(R.id.changePassF_edt_passOld)
        edtPasswordNew = view.findViewById(R.id.changePassF_edt_passNew)
        edtPwNewComfirm = view.findViewById(R.id.changePassF_edt_passNewComfirm)
        cardViewSave = view.findViewById(R.id.changePassF_cardView_save)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePassFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangePassFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}