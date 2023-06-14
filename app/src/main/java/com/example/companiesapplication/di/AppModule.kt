package com.example.companiesapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.example.companiesapplication.domian.use_cases.AddNewItem
import com.example.companiesapplication.domian.use_cases.CompanySearchUseCase
import com.example.companiesapplication.domian.use_cases.DeleteItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun provideAddItem(): AddNewItem {
        return AddNewItem()
    }

    @Singleton
    @Provides
    fun provideDeleteItemCase() = DeleteItemUseCase()
    @Singleton
    @Provides
    fun provideSearchCase() = CompanySearchUseCase()
}