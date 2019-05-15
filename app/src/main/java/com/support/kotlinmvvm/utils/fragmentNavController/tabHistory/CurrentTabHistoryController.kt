package com.support.kotlinmvvm.utils.fragmentNavController.tabHistory

import android.os.Bundle
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavPopController
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavTransactionOptions

class CurrentTabHistoryController(fragNavPopController: FragNavPopController) : BaseFragNavTabHistoryController(fragNavPopController) {

    @Throws(UnsupportedOperationException::class)
    override fun popFragments(popDepth: Int,
                              transactionOptions: FragNavTransactionOptions?): Boolean {
        return fragNavPopController.tryPopFragments(popDepth, transactionOptions) > 0
    }

    override fun switchTab(index: Int) {}

    override fun onSaveInstanceState(outState: Bundle) {}

    override fun restoreFromBundle(savedInstanceState: Bundle?) {}
}