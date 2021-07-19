package com.usefulmemo.memo.di

import com.usefulmemo.memo.data.datasource.local.FolderLocalDataSource
import com.usefulmemo.memo.data.datasource.local.MemoLocalDataSource
import com.usefulmemo.memo.data.repository.FolderRepositoryImpl
import com.usefulmemo.memo.data.repository.MemoRepositoryImpl
import com.usefulmemo.memo.domain.repository.FolderRepository
import com.usefulmemo.memo.domain.repository.MemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMemoRepository(
        memoLocalDataSource : MemoLocalDataSource
    ) : MemoRepository {
        return MemoRepositoryImpl(memoLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideFolderRepository(
        folderLocalDataSource : FolderLocalDataSource
    ) : FolderRepository {
        return FolderRepositoryImpl(folderLocalDataSource)
    }

}