package com.usefulmemo.memo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.usefulmemo.memo.domain.model.Folder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FolderDao {

    @Query("SELECT * FROM folder ORDER BY id DESC")
    fun getFolder() : Observable<List<Folder>>

    @Insert
    fun insertFolder(folder : Folder) : Completable

    @Query("DELETE FROM folder WHERE id = :id" )
    fun deleteFolder(id : Long) : Completable
}