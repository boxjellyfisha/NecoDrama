package com.kellyhong.necodrama.di

import com.kellyhong.necodrama.MainActivity
import com.kellyhong.necodrama.db.AppPreference
import com.kellyhong.necodrama.db.drama.DramaDatabase
import com.kellyhong.necodrama.http.DramaService
import com.kellyhong.necodrama.model.MainDataSource
import com.kellyhong.necodrama.model.MainLocal
import com.kellyhong.necodrama.model.MainRemote
import com.kellyhong.necodrama.model.MainRepository
import com.kellyhong.necodrama.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single { DramaDatabase.getInstance(androidContext()) }
    single { AppPreference(androidContext()) }
    single<MainDataSource.Local> { MainLocal(get()) }
    single<MainDataSource.Remote> { MainRemote() }
    single<MainDataSource> { MainRepository(get(), get()) }
}

val viewModelModule = module {
    scope<MainActivity> {
        viewModel { MainViewModel(get()) }
    }
}