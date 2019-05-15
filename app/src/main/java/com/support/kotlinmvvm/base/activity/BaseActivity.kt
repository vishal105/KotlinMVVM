package com.support.kotlinmvvm.base.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.support.kotlinmvvm.base.viewmodel.BaseViewModel
import com.support.kotlinmvvm.extensions.observe
import com.support.kotlinmvvm.extensions.snack
import com.support.kotlinmvvm.utils.statelayout.StateLayout

abstract class BaseActivity<VM : BaseViewModel,
        DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : AppCompatActivity() {

    val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
    }

    var viewModel: VM? = null

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun getStateLayout(): StateLayout?

    abstract fun getToolBar(): Toolbar?

    abstract fun getToolBarTitle(): String?

    open fun isBackEnable() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = createViewModel(this)
        super.onCreate(savedInstanceState)
        binding
        viewModel?.apply {
            lifecycle.addObserver(this)
        }
        onCreateReference()
        initData()
    }

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let { lifecycle.removeObserver(it) }
        binding.unbind()
    }

    private fun onCreateReference() {
        viewModel?.isLoading?.observe(this) {
            if (it == false) {
                getStateLayout()?.loading()
            } else {
                getStateLayout()?.loadingWithContent()
            }
        }

        viewModel?.networkError?.observe(this) {
            it?.let { it1 -> getStateLayout()?.infoButtonListener(it1) }
        }

        viewModel?.infoStateview?.observe(this) {
            getStateLayout()?.showInfo(it)
        }

        viewModel?.infoEmptyView?.observe(this) {
            getStateLayout()?.showEmpty(it)
        }

        viewModel?.errorMessage?.observe(this) {
            showErrorMessage(it)
        }

        viewModel?.successMessage?.observe(this) {
            showSuccessMessage(it)
        }
    }

    open fun onBack() {
        onBackPressed()
    }

    open fun createViewModel(activity: FragmentActivity): VM {
        return ViewModelProviders.of(activity).get(mViewModelClass)
    }

    open fun createViewModel(fragment: Fragment): VM {
        return ViewModelProviders.of(fragment).get(mViewModelClass)
    }

    fun showErrorMessage(message: String?) {
        message?.let {
            binding.root.apply {
                snack(it) {}
            }
        }
    }

    fun showSuccessMessage(message: String?) {
        message?.let {
            binding.root.apply {
                snack(it) {}
            }
        }
    }

    fun setToolBar(title: String?, isBackEnable: Boolean) {
        if (getToolBar() != null) {
            setSupportActionBar(getToolBar())
            supportActionBar?.title = title
            supportActionBar?.setHomeButtonEnabled(isBackEnable)
            supportActionBar?.setDisplayHomeAsUpEnabled(isBackEnable)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}