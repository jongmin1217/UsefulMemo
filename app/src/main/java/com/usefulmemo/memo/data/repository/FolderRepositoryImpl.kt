package com.usefulmemo.memo.data.repository

import com.usefulmemo.memo.data.datasource.local.FolderLocalDataSource
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.repository.FolderRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val dataSource: FolderLocalDataSource
) : FolderRepository{
    override fun getFolder(): Observable<List<Folder>> = dataSource.getFolder()
    override fun insertFolder(folder : Folder) : Completable = dataSource.insertFolder(folder)
    override fun deleteFolder(id : Long) : Completable = dataSource.deleteFolder(id)
}