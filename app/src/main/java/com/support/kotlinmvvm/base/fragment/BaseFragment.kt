package com.support.kotlinmvvm.base.fragment

import android.content.Context
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
import com.support.kotlinmvvm.extensions.observe
import com.support.kotlinmvvm.extensions.snack
import com.support.kotlinmvvm.utils.statelayout.StateLayout


abstract class BaseFragment<VM : BaseViewModel,
        DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) :
    Fragment() {

    var binding: DB? = null
        private set
    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null

    var viewModel: VM? = null

    abstract val bindingVariable: Int

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun getStateLayout(): StateLayout?

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

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }

        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBack()
                    true
                } else false
            }
        })
    }

    open fun onBack() {
        baseActivity?.onBack()
    }

}