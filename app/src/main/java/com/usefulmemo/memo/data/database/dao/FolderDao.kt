package com.usefulmemo.memo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.usefulmemo.memo.domain.model.Folder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FolderDao {

    @Query("SELECT * FROM folder ORDER BY id ASC")
    fun getFolder() : Observable<List<Folder>>

    @Insert
    fun insertFolder(folder : Folder) : Completable

    @Query("DELETE FROM folder WHERE id = :id" )
    fun deleteFolder(id : Long) : Completable

    @Update
    fun updateFolder(folder : Folder) : Completable
}