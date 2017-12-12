package com.urgentx.recycledump

import dagger.Module
import dagger.Provides

@Module
class AppModule (val app: App) {

    @Provides
    fun provideApp(): App = app
}