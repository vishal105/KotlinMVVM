package com.support.kotlinmvvm.utils.fragmentNavController.tabHistory

import com.support.kotlinmvvm.utils.fragmentNavController.FragNavSwitchController

sealed class NavigationStrategy

class CurrentTabStrategy : NavigationStrategy()

class UnlimitedTabHistoryStrategy(val fragNavSwitchController: FragNavSwitchController) : NavigationStrategy()

class UniqueTabHistoryStrategy(val fragNavSwitchController: FragNavSwitchController) : NavigationStrategy()