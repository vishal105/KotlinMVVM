package com.support.kotlinmvvm.utils.fragmentNavController

interface FragNavPopController {
    @Throws(UnsupportedOperationException::class)
    fun tryPopFragments(popDepth: Int, transactionOptions: FragNavTransactionOptions?): Int
}