package com.urgentx.recycledump

import android.app.Application
import com.urgentx.recycledump.di.AppComponent
import com.urgentx.recycledump.di.AppModule
import com.urgentx.recycledump.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this)) //Deprecated because we haven't used the component yet.
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    fun appComponent() : AppComponent = appComponent
}