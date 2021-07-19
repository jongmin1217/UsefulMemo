package com.usefulmemo.memo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.usefulmemo.memo.data.database.dao.FolderDao
import com.usefulmemo.memo.data.database.dao.MemoDao
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo

@Database(entities = [Memo::class, Folder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun memoDao() : MemoDao
    abstract fun folderDao() : FolderDao
}