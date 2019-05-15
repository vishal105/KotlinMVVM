package com.support.kotlinmvvm.utils.fragmentNavController

interface FragNavSwitchController {
    fun switchTab(@FragNavController.TabIndex index: Int, transactionOptions: FragNavTransactionOptions?)
}