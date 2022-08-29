package com.example.barber.Fragment.User

import android.graphics.Color
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
import com.example.barber.Model.User
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
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
 * Use the [InfoAccFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoAccFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView
    private lateinit var username : TextInputEditText
    private lateinit var name : TextInputEditText
    private lateinit var phone : TextInputEditText
    private lateinit var address : TextInputEditText
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
        val view = inflater.inflate(R.layout.fragment_info_acc, container, false)
        init(view)
        hideNavigation()
        back()
        getInfo()
        changeInfo()
        return view
    }

    private fun changeInfo() {
        cardViewSave.setOnClickListener(View.OnClickListener {
            val dialog = LoadingDialog(requireActivity())
            val nameUser = name.text.toString()
            val phoneUser = phone.text.toString()
            val addressUser = address.text.toString()
            if(TextUtils.isEmpty(nameUser) || TextUtils.isEmpty(phoneUser) || TextUtils.isEmpty(addressUser)){
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            }
            else {
                dialog.startLoading()
                val action = "changeInfoUser"
                ApiServices.create().changeInfoUser(Constant.KeyWithScreen.IdUser,nameUser,phoneUser,addressUser,action).enqueue(
                    object : Callback<Unit>{
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Toast.makeText(context,"Đổi thành công", Toast.LENGTH_SHORT).show()
                            dialog.dismissDialog()
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {

                        }

                    }
                )
            }
        })

    }

    private fun getUser(){
        val action = "getUser"
        ApiServices.create().getUser(Constant.KeyWithScreen.username,action).enqueue(
            object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val user = response.body()
                    if(user != null){
                        name.hint = user.hoVaTen
                        phone.hint = user.soDienThoai
                        address.hint = user.diaChi
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                }

            }
        )
    }

    private fun getInfo() {
        username.hint = Constant.KeyWithScreen.username
        username.setHintTextColor(Color.BLACK)
        username.isEnabled = false
        getUser()
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_infoAccFragment_to_userFragment)
        })
    }

    private fun hideNavigation() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        imvBack = view.findViewById(R.id.infoAccF_imv_back)
        username = view.findViewById(R.id.infoAccF_edt_username)
        name = view.findViewById(R.id.infoAccF_edt_name)
        phone = view.findViewById(R.id.infoAccF_edt_phone)
        address = view.findViewById(R.id.infoAccF_edt_address)
        cardViewSave = view.findViewById(R.id.infoAccF_cardView_save)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoAccFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoAccFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}