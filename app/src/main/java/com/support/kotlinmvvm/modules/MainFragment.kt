package com.support.kotlinmvvm.modules


import android.os.Bundle
import com.support.kotlinmvvm.BR
import com.support.kotlinmvvm.R
import com.support.kotlinmvvm.base.fragment.BaseFragment
import com.support.kotlinmvvm.databinding.FragmentMainBinding
import com.support.kotlinmvvm.modules.bottomnavigation.BottomNavigationActivity
import com.support.kotlinmvvm.utils.statelayout.StateLayout
import com.support.kotlinmvvm.utils.widgets.spinner.SpinnerBottomDialogFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment<MainFragmentViewModel,
        FragmentMainBinding>(MainFragmentViewModel::class.java) {

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_main

    override val stateLayout: StateLayout?
        get() = null

    override val toolbartitle: String?
        get() = "Demo"

    override val isBackEnable: Boolean?
        get() = true

    /*
        override fun createViewModel(activity: FragmentActivity): BottomNavigationViewModel {
        return ViewModelProviders.of(activity).get(BottomNavigationViewModel::class.java)
    }
     */

    override fun initData() {
        tvTitle?.setOnClickListener {
            val currentActivity = baseActivity as BottomNavigationActivity
            currentActivity.fragNavController.pushFragment(newInstance())
        }

        tvSpinner?.setOnClickListener {
            val list : MutableList<SpinnerItem> = arrayListOf()
            list.add(SpinnerItem("Name 1"))
            list.add(SpinnerItem("Name 2"))
            list.add(SpinnerItem("Name 3"))
            list.add(SpinnerItem("Name 4"))
            list.add(SpinnerItem("Name 5"))

            val dialog =
                SpinnerBottomDialogFragment("Select Data",list){ item,position ->
                 tvSpinner?.text = item.name
            }
            dialog.show(childFragmentManager,"Sele")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(basket: Bundle? = null) = MainFragment().apply { arguments = basket }
    }

    data class SpinnerItem(val name : String){
        override fun toString(): String {
            return name
        }
    }

}
