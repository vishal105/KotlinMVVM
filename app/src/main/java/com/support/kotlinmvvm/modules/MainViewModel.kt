package com.support.kotlinmvvm.modules

import android.app.Application
import android.os.Handler
import com.support.kotlinmvvm.base.viewmodel.BaseViewModel

class MainViewModel constructor(app: Application)
    : BaseViewModel(app) {

    init {

    }

    fun data(){
        loading(false)

        Handler().postDelayed({
            networkError {
                empty()
                showSuccess("Success...")
            }
        },3000)
    }

    fun empty(){
        loading(true)

        Handler().postDelayed({
            emptyView()
        },3000)
    }
}