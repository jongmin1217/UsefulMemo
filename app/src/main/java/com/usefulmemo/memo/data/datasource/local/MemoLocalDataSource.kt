package com.usefulmemo.memo.data.datasource.local

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.usefulmemo.memo.data.database.dao.FolderDao
import com.usefulmemo.memo.data.database.dao.MemoDao
import com.usefulmemo.memo.domain.model.Memo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MemoLocalDataSource @Inject constructor(
    private val local : MemoDao
){
    fun getAllMemo() : Observable<List<Memo>> = local.getAllMemo()
    fun getFolderMemo(folderId : Long) : Observable<List<Memo>> = local.getFolderMemo(folderId)
    fun getMemo(memoId : Long) : Single<Memo> = local.getMemo(memoId)
    fun insertMemo(memo : Memo) : Completable = local.insertMemo(memo)
    fun updateMemo(memo: Memo) : Completable = local.updateMemo(memo)
    fun deleteMemo(id : Long) : Completable = local.deleteMemo(id)
    fun deleteFolderMemo(folderId : Long) : Completable = local.deleteFolderMemo(folderId)
}