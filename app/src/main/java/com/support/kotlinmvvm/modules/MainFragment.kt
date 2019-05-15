package com.support.kotlinmvvm.modules


import android.os.Bundle
import com.support.kotlinmvvm.BR
import com.support.kotlinmvvm.R
import com.support.kotlinmvvm.base.fragment.BaseFragment
import com.support.kotlinmvvm.databinding.FragmentMainBinding
import com.support.kotlinmvvm.modules.bottomnavigation.BottomNavigationActivity
import com.support.kotlinmvvm.utils.statelayout.StateLayout
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment<MainFragmentViewModel,
        FragmentMainBinding>(MainFragmentViewModel::class.java) {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main

    override fun getStateLayout(): StateLayout? {
        return null
    }

    override fun initData() {
        tvTitle?.setOnClickListener {
            val currentActivity = baseActivity as BottomNavigationActivity
            currentActivity.fragNavController.pushFragment(newInstance())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(basket: Bundle? = null) = MainFragment().apply { arguments = basket }
    }

}
