package com.example.companiesapplication.di

import com.example.companiesapplication.domian.Repository
import com.example.companiesapplication.domian.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {


    @Binds
    @Singleton
    abstract fun provideRepo(repo:RepositoryImpl):Repository
}