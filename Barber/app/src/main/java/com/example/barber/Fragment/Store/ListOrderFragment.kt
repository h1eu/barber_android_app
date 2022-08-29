package com.example.barber.Fragment.Store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
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
        val view = inflater.inflate(R.layout.fragment_list_order, container, false)
        init(view)
        hideNaviBot()
        setupTabLayout()
        back()
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_listOrderFragment_to_storeFragment)
        })
    }

    private fun setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
        prepareViewPager()
    }


    private fun hideNaviBot() {
        bottomNavigationView.setupWithNavController(controller!!)
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun prepareViewPager() {
        var orderFragment  = OrderWaitFragment()
        var orderComfirmFragment = OrderComfirmFragment()
        var orderDestroyFragment = OrderDestroyFragment()
        var adapter = ViewPagerAdapterListOrder(parentFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.addFragment(orderFragment,"Chờ xác nhận")
        adapter.addFragment(orderComfirmFragment,"Đã xác nhận")
        adapter.addFragment(orderDestroyFragment,"Đã huỷ")
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
              //  Log.e("page duoch chon",position.toString())
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
               // Log.e("page duoch chon",state.toString())
            }

        })
    }


    private fun init(view: View) {
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        tabLayout = view.findViewById(R.id.listOrderF_tablayout)
        viewPager = view.findViewById(R.id.listOrderF_viewPager)
        imvBack = view.findViewById(R.id.listOrderF_imv_back)
    }

    class ViewPagerAdapterListOrder(fm : FragmentManager,behavior: Int): FragmentStatePagerAdapter(fm,behavior){
        var fragmentArrayList : ArrayList<Fragment> = arrayListOf()
        var titleArrayList : ArrayList<String> = arrayListOf()
        public fun addFragment(fragment: Fragment, title : String){
            fragmentArrayList.add(fragment)
            titleArrayList.add(title)
        }
        override fun getCount(): Int {
            return fragmentArrayList.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return super.getItemPosition(`object`)
            return POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            return fragmentArrayList.get(position)
        }


        override fun getPageTitle(position: Int): CharSequence? {
            return titleArrayList.get(position)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}