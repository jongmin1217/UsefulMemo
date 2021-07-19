package com.usefulmemo.memo.data.datasource.local

import com.usefulmemo.memo.data.database.dao.FolderDao
import com.usefulmemo.memo.domain.model.Folder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FolderLocalDataSource @Inject constructor(
    private val local : FolderDao
){
    fun getFolder() : Observable<List<Folder>> = local.getFolder()
    fun insertFolder(folder : Folder) : Completable = local.insertFolder(folder)
    fun deleteFolder(id : Long) : Completable = local.deleteFolder(id)
    fun updateFolder(folder : Folder) : Completable = local.updateFolder(folder)
}