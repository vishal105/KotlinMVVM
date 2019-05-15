package com.support.kotlinmvvm.base.viewmodel

import android.app.Application
import android.os.Message
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.support.kotlinmvvm.utils.statelayout.StateLayout

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    val networkErrorStateInfo = StateLayout.StateInfo(
        null,
        "Network error",
        "Please check your connection!",
        "Retry"
    )

    val emptyStateInfo = StateLayout.StateInfo(
        null,
        "Empty",
        "No data found.",
        "",
        StateLayout.State.EMPTY
    )

    val infoEmptyView = MutableLiveData<StateLayout.StateInfo>()
    val isLoading = MutableLiveData<Boolean>()
    val networkError = MutableLiveData<() -> Unit>()
    val infoStateview = MutableLiveData<StateLayout.StateInfo>()

    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()

    fun loading(withContent: Boolean = false) {
        isLoading.value = withContent
    }

    fun emptyView(stateInfo: StateLayout.StateInfo = emptyStateInfo) {
        infoEmptyView.value = stateInfo
    }

    fun networkError(block: () -> Unit) {
        infoView(networkErrorStateInfo)
        networkError.value = block
    }

    fun infoView(stateInfo: StateLayout.StateInfo) {
        infoStateview.value = stateInfo
    }

    fun showError(message: String) {
        errorMessage.value = message
    }

    fun showSuccess(message: String) {
        successMessage.value = message
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStart() {

    }

}
