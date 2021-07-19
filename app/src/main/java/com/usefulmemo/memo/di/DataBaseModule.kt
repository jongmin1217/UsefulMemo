package com.usefulmemo.memo.di

import android.app.Application
import androidx.room.Room
import com.usefulmemo.memo.BuildConfig
import com.usefulmemo.memo.data.database.AppDatabase
import com.usefulmemo.memo.data.database.dao.FolderDao
import com.usefulmemo.memo.data.database.dao.MemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun provideUsefulMemoDB(application : Application) : AppDatabase{
        return Room.databaseBuilder(application,AppDatabase::class.java, BuildConfig.ROOM_NAME).build()
    }

    @Provides
    @Singleton
    fun provideMemoDao(database: AppDatabase) : MemoDao{
        return database.memoDao()
    }


    @Provides
    @Singleton
    fun provideFolderDao(database: AppDatabase) : FolderDao{
        return database.folderDao()
    }

}