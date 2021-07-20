package com.usefulmemo.memo.data.repository

import com.usefulmemo.memo.data.datasource.local.MemoLocalDataSource
import com.usefulmemo.memo.domain.model.Memo
import com.usefulmemo.memo.domain.repository.MemoRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val dataSource: MemoLocalDataSource
) : MemoRepository {
    override fun getAllMemo(): Observable<List<Memo>> = dataSource.getAllMemo()
    override fun getFolderMemo(folderId : Long) : Observable<List<Memo>> = dataSource.getFolderMemo(folderId)
    override fun getMemo(memoId: Long): Single<Memo> = dataSource.getMemo(memoId)
    override fun insertMemo(memo : Memo) : Single<Long> = dataSource.insertMemo(memo)
    override fun updateMemo(memo: Memo) : Completable = dataSource.updateMemo(memo)
    override fun deleteMemo(id : Long) : Completable = dataSource.deleteMemo(id)
    override fun deleteFolderMemo(folderId : Long) : Completable = dataSource.deleteFolderMemo(folderId)
    override fun getDeleteMemo() : Observable<List<Memo>> = dataSource.getDeleteMemo()
}