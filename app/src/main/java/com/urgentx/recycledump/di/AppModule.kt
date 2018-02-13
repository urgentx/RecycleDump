package com.urgentx.recycledump.di

import com.urgentx.recycledump.App
import dagger.Module
import dagger.Provides

@Module
class AppModule (val app: App) {

    @Provides
    fun provideApp(): App = app

}