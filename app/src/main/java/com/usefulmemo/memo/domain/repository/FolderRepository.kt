package com.usefulmemo.memo.domain.repository

import com.usefulmemo.memo.domain.model.Folder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FolderRepository {
    fun getFolder() : Observable<List<Folder>>
    fun insertFolder(folder : Folder) : Completable
    fun deleteFolder(id : Long) : Completable
}