package com.support.kotlinmvvm.modules.bottomnavigation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.support.kotlinmvvm.R
import com.support.kotlinmvvm.base.activity.BaseActivity
import com.support.kotlinmvvm.databinding.ActivityBottomNavigationBinding
import com.support.kotlinmvvm.modules.MainFragment
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavController
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavLogger
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavSwitchController
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavTransactionOptions
import com.support.kotlinmvvm.utils.fragmentNavController.tabHistory.UniqueTabHistoryStrategy
import com.support.kotlinmvvm.utils.statelayout.StateLayout
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class BottomNavigationActivity : BaseActivity<BottomNavigationViewModel,
        ActivityBottomNavigationBinding>(BottomNavigationViewModel::class.java), FragNavController.TransactionListener,
    FragNavController.RootFragmentListener {

    override val numberOfRootFragments: Int
        get() = 3

    private val INDEX_TAB1 = FragNavController.TAB1
    private val INDEX_TAB2 = FragNavController.TAB2
    private val INDEX_TAB3 = FragNavController.TAB3

    val fragNavController: FragNavController = FragNavController(supportFragmentManager, R.id.container)

    override fun getToolBar(): Toolbar? {
        return null
    }

    override fun getToolBarTitle(): String? {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_bottom_navigation
    }

    override fun getStateLayout(): StateLayout? {
        return stateBottomNavigation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragNavController.apply {
            transactionListener = this@BottomNavigationActivity
            rootFragmentListener = this@BottomNavigationActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {
                    Log.e("error fragment", message, throwable)
                }
            }

            //defaultTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right).build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    when (index) {
                        0 -> mBottomNav.selectedItemId = R.id.action_tab1
                        1 -> mBottomNav.selectedItemId = R.id.action_tab2
                        2 -> mBottomNav.selectedItemId = R.id.action_tab3
                        else -> {

                        }
                    }
                }
            })
        }

        fragNavController.initialize(INDEX_TAB1, savedInstanceState)

        val initial = savedInstanceState == null
        if (initial) {
            mBottomNav.selectedItemId = R.id.action_tab1
        }
    }

    override fun initData() {

        mBottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_tab1 -> {
                    fragNavController.switchTab(INDEX_TAB1)
                    true
                }
                R.id.action_tab2 -> {
                    fragNavController.switchTab(INDEX_TAB2)
                    true
                }
                R.id.action_tab3 -> {
                    fragNavController.switchTab(INDEX_TAB3)
                    true
                }
                else -> {
                    true
                }
            }
        }

    }

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            INDEX_TAB1 -> return MainFragment.newInstance()
            INDEX_TAB2 -> return MainFragment.newInstance()
            INDEX_TAB3 -> return MainFragment.newInstance()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        // If we have a backstack, show the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(fragNavController.isRootFragment.not())
    }


    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(fragNavController.isRootFragment.not())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }

    override fun onBack() {
        if (fragNavController.popFragment().not()) {
            super.onBack()
        }
    }
}