package com.example.foodrecipe.repository

import com.example.foodrecipe.dataSource.LocalDataSource
import com.example.foodrecipe.dataSource.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
    ) {
    val remote = remoteDataSource
    val local = localDataSource
}