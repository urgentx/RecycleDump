package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject


class VMInjectionFactory <out Type: ViewModel> @Inject constructor(val vm: Lazy<Type>): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(p0: Class<T>): T {
        return vm as T
    }
}