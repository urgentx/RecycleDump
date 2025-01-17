package com.urgentx.recycledump.di

import com.urgentx.recycledump.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun inject(app: App)

}