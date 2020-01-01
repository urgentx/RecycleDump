package com.urgentx.recycledump.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import kotlin.reflect.KProperty0

/**
 *  Usage example:
 *
 *      @Inject
 *      lateinit var mFactory: VMInjectionFactory<MyViewModel>
 */
//class VMInjectionFactory <out Type: ViewModel> @Inject constructor(val vm: Lazy<Type>): ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(p0: Class<T>): T {
//        return vm.getValue(p0, KProperty0(p0))
//    }
//}