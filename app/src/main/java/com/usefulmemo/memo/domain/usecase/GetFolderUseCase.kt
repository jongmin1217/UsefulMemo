package com.usefulmemo.memo.domain.usecase

import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo
import com.usefulmemo.memo.domain.repository.FolderRepository
import com.usefulmemo.memo.domain.usecase.base.UseCase
import com.usefulmemo.memo.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetFolderUseCase @Inject constructor(private val repository: FolderRepository) : UseCase() {

    fun observableFolderList(
        onSuccess: ((t: List<Folder>) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            repository.getFolder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe(onSuccess, onError)
        )
    }

    fun completableMemo(
        folder: Folder,
        type: Int,
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            when (type) {
                Constants.INSERT -> repository.insertFolder(folder)
                else -> repository.deleteFolder(folder.id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe()
        )
    }

}