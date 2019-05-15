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

    open val stateLayout: StateLayout?
        get() = null

    open val isBackEnable: Boolean?
        get() = false

    open val toolbartitle: String?
        get() = ""

    open val toolbar: Toolbar?
        get() = null

    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = createViewModel(this)
        super.onCreate(savedInstanceState)
        binding
        initToolbar(toolbar)
        viewModel?.apply {
            lifecycle.addObserver(this)
        }
        onCreateReference()
        initData()
    }

    fun initToolbar(toolbar: Toolbar?) {
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            initToolbarBack(isBackEnable)
            initToolbarTitle(toolbartitle)
        }
    }

    fun initToolbarBack(isEnable: Boolean?) {
        isEnable?.let {
            this.supportActionBar?.setHomeButtonEnabled(it)
            this.supportActionBar?.setDisplayHomeAsUpEnabled(isEnable)
        }

    }

    fun initToolbarTitle(title: String?) {
        if (this.supportActionBar != null && title?.isNotEmpty() == true) {
            this.supportActionBar?.title = title
            this.toolbar?.title = title
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let { lifecycle.removeObserver(it) }
        binding.unbind()
    }

    private fun onCreateReference() {
        viewModel?.isLoading?.observe(this) {
            if (it == false) {
               stateLayout?.loading()
            } else {
               stateLayout?.loadingWithContent()
            }
        }

        viewModel?.networkError?.observe(this) {
            it?.let { it1 ->stateLayout?.infoButtonListener(it1) }
        }

        viewModel?.infoStateview?.observe(this) {
           stateLayout?.showInfo(it)
        }

        viewModel?.infoEmptyView?.observe(this) {
           stateLayout?.showEmpty(it)
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