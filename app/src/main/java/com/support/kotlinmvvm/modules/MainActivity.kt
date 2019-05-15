package com.support.kotlinmvvm.modules

import androidx.appcompat.widget.Toolbar
import com.support.kotlinmvvm.R
import com.support.kotlinmvvm.base.activity.BaseActivity
import com.support.kotlinmvvm.databinding.ActivityMainBinding
import com.support.kotlinmvvm.utils.statelayout.StateLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel,
        ActivityMainBinding>(MainViewModel::class.java) {
    override fun getToolBar(): Toolbar? {
        return null
    }

    override fun getToolBarTitle(): String? {
       return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getStateLayout(): StateLayout? {
        return stateMain
    }

    override fun initData() {
        viewModel?.data()
    }

}
