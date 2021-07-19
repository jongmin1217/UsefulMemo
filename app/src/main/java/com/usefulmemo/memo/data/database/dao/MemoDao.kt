package com.usefulmemo.memo.data.database.dao

import androidx.room.*
import com.usefulmemo.memo.domain.model.Memo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo ORDER BY regDate DESC")
    fun getAllMemo() : Observable<List<Memo>>

    @Query("SELECT * FROM memo WHERE folderId = :folderId ORDER BY regDate DESC")
    fun getFolderMemo(folderId : Long) : Observable<List<Memo>>

    @Query("SELECT * FROM memo WHERE id = :memoId")
    fun getMemo(memoId : Long) : Single<Memo>

    @Insert
    fun insertMemo(memo : Memo) : Completable

    @Update
    fun updateMemo(memo: Memo) : Completable

    @Query("DELETE FROM memo WHERE id = :id" )
    fun deleteMemo(id : Long) : Completable

    @Query("DELETE FROM memo WHERE folderId = :folderId" )
    fun deleteFolderMemo(folderId : Long) : Completable
}