package com.usefulmemo.memo.domain.repository

import com.usefulmemo.memo.domain.model.Memo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MemoRepository {
    fun getAllMemo() : Observable<List<Memo>>
    fun getFolderMemo(folderId : Long) : Observable<List<Memo>>
    fun getMemo(memoId : Long) : Single<Memo>
    fun insertMemo(memo : Memo) : Completable
    fun updateMemo(memo: Memo) : Completable
    fun deleteMemo(id : Long) : Completable
    fun deleteFolderMemo(folderId : Long) : Completable
    fun getDeleteMemo() : Observable<List<Memo>>
}