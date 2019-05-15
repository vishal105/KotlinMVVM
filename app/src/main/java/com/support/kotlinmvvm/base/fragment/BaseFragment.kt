package com.support.kotlinmvvm.base.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.support.kotlinmvvm.base.activity.BaseActivity
import com.support.kotlinmvvm.base.viewmodel.BaseViewModel
import com.support.kotlinmvvm.extensions.*
import com.support.kotlinmvvm.utils.statelayout.StateLayout

abstract class BaseFragment<VM : BaseViewModel,
        DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) :
    Fragment() {

    private val PERMISSION_CODE = 101
    private var permissionCallBack: PermissionCallBack? = null

    open val isBackEnable: Boolean?
        get() = false

    open val toolbartitle: String?
        get() = ""

    var binding: DB? = null
        private set

    var baseActivity: BaseActivity<*, *>? = null
        private set

    private var mRootView: View? = null

    var viewModel: VM? = null

    abstract val bindingVariable: Int

    @get:LayoutRes
    abstract val layoutId: Int

    open val stateLayout: StateLayout?
        get() = null

    abstract fun initData()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = createViewModel(this)
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel?.apply {
            lifecycle.addObserver(this)
        }
        if (baseActivity != null) {
            if (baseActivity?.supportActionBar != null) {
                baseActivity?.initToolbarBack(isBackEnable)
                baseActivity?.initToolbarTitle(toolbartitle)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding?.root
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.setVariable(bindingVariable, viewModel)
        binding?.executePendingBindings()
        onCreateReference()
        initData()
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
            it?.let { it1 -> stateLayout?.infoButtonListener(it1) }
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

    fun showErrorMessage(message: String?) {
        message?.let {
            binding?.root?.apply {
                snack(it) {}
            }
        }
    }

    fun showSuccessMessage(message: String?) {
        message?.let {
            binding?.root?.apply {
                snack(it) {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.apply {
            lifecycle.removeObserver(this)
        }
        binding?.unbind()
    }

    open fun createViewModel(activity: FragmentActivity): VM {
        return ViewModelProviders.of(activity).get(mViewModelClass)
    }

    open fun createViewModel(fragment: Fragment): VM {
        return ViewModelProviders.of(fragment).get(mViewModelClass)
    }

    fun requestPermissionsIfRequired(permissions: ArrayList<String>, permissionCallBack: PermissionCallBack?) {
        this.permissionCallBack = permissionCallBack
        if (checkSelfPermissions(permissions)) {
            permissionCallBack?.permissionGranted()
        } else {
            requestAllPermissions(permissions, PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionCallBack?.permissionGranted()
                } else {
                    if (checkPermissionRationale(permissions)) {
                        permissionCallBack?.permissionDenied()
                    } else {
                        permissionCallBack?.onPermissionDisabled()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }

        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBack()
                true
            } else false
        }


    }

    open fun onBack() {
        baseActivity?.onBack()
    }

}