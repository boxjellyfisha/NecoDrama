package com.kellyhong.necodrama.arch

import android.app.Application
import com.facebook.stetho.Stetho
import com.kellyhong.necodrama.di.dataModule
import com.kellyhong.necodrama.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Main Application
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        // start Koin context
        startKoin {
            androidContext(this@MyApplication)
            androidLogger()
            modules(dataModule, viewModelModule)
        }
    }
}
