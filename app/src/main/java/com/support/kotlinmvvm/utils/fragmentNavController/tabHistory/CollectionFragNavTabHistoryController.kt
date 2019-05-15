package com.support.kotlinmvvm.utils.fragmentNavController.tabHistory

import android.os.Bundle
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavPopController
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavSwitchController
import com.support.kotlinmvvm.utils.fragmentNavController.FragNavTransactionOptions
import java.util.*

abstract class CollectionFragNavTabHistoryController(fragNavPopController: FragNavPopController,
                                                     private val fragNavSwitchController: FragNavSwitchController
) : BaseFragNavTabHistoryController(fragNavPopController) {

    internal abstract val collectionSize: Int

    internal abstract val andRemoveIndex: Int

    internal abstract var history: ArrayList<Int>

    @Throws(UnsupportedOperationException::class)
    override fun popFragments(popDepth: Int,
                              transactionOptions: FragNavTransactionOptions?): Boolean {
        var localDepth = popDepth
        var changed = false
        var switched: Boolean
        do {
            switched = false
            val count = fragNavPopController.tryPopFragments(localDepth, transactionOptions)
            if (count > 0) {
                changed = true
                switched = true
                localDepth -= count
            } else if (collectionSize > 1) {
                fragNavSwitchController.switchTab(andRemoveIndex, transactionOptions)
                localDepth--
                changed = true
                switched = true
            }
        } while (localDepth > 0 && switched)
        return changed
    }

    override fun restoreFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            return
        }
        val arrayList = savedInstanceState.getIntegerArrayList(EXTRA_STACK_HISTORY)
        if (arrayList != null) {
            history = arrayList
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val history = history
        if (history.isEmpty()) {
            return
        }
        outState.putIntegerArrayList(EXTRA_STACK_HISTORY, history)
    }

    companion object {
        private const val EXTRA_STACK_HISTORY = "EXTRA_STACK_HISTORY"
    }
}