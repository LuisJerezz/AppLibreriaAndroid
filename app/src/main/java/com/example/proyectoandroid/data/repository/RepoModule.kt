package com.example.proyectoandroid.data.repository

import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
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
    abstract fun bindUserRepository(
        repoImp: InMemoryUserRepository
    ) : UserRepositoryInterface
}