package com.example.barber.Fragment.Home

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.barber.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentPagerAdapter as FragmentPagerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PriceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PriceListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var controller : NavController
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var imvBack : ImageView

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
        val view = inflater.inflate(R.layout.fragment_price_list, container, false)
        init(view)
        setupTabLayout()
        back()
        hideBottom()
        return view
    }

    private fun back() {
        imvBack.setOnClickListener(View.OnClickListener {
            controller.navigate(R.id.action_priceListFragment_to_homeFragment)
        })
    }

    private fun hideBottom() {
        bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
        prepareViewPager(viewPager)
    }

    private fun prepareViewPager(viewPager: ViewPager) {
        var priceListMenFragment = PriceListMenFragment()
        var priceListWomenFragment = PriceListWomenFragment()
        var adapterViewPager : ViewPagerAdapterPriceList = ViewPagerAdapterPriceList(parentFragmentManager,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapterViewPager.addFragment(priceListMenFragment,"Nam")
        adapterViewPager.addFragment(priceListWomenFragment,"Dịch vụ khác")
        viewPager.adapter = adapterViewPager

    }

    private fun init(view: View) {
        imvBack = view.findViewById(R.id.priceListF_imv_back)
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment?.findNavController()!!
        bottomNavigationView = requireActivity().findViewById(R.id.navigationBottom)
        tabLayout = view.findViewById(R.id.priceListF_tablayout)
        viewPager = view.findViewById(R.id.priceListF_viewPager)
    }

    private class ViewPagerAdapterPriceList(fm: FragmentManager, behavior : Int) :  FragmentStatePagerAdapter(fm,behavior) {
        var fragmentArrayList : ArrayList<Fragment> = arrayListOf()
        var titleArrayList : ArrayList<String> = arrayListOf()
        public fun addFragment(fragment: Fragment, title : String){
            fragmentArrayList.add(fragment)
            titleArrayList.add(title)
        }
        override fun getCount(): Int {
            return fragmentArrayList.size
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
         * @return A new instance of fragment PriceListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PriceListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}