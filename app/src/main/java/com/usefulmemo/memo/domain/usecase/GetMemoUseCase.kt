package com.usefulmemo.memo.domain.usecase

import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo
import com.usefulmemo.memo.domain.repository.MemoRepository
import com.usefulmemo.memo.domain.usecase.base.UseCase
import com.usefulmemo.memo.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class GetMemoUseCase @Inject constructor(private val repository: MemoRepository) : UseCase() {

    fun singleMemo(
        memoId: Long,
        onSuccess: ((t: Memo) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            repository.getMemo(memoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe(onSuccess, onError)
        )
    }

    fun observableMemoList(
        onSuccess: ((t: List<Memo>) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            repository.getAllMemo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe(onSuccess, onError)
        )
    }

    fun observableMemoFolderList(
        folderId: Long,
        onSuccess: ((t: List<Memo>) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            repository.getFolderMemo(folderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe(onSuccess, onError)
        )
    }

    fun completableMemo(
        memo: Memo,
        type: Int,
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            when (type) {
                Constants.INSERT -> repository.insertMemo(memo)
                Constants.UPDATE -> repository.updateMemo(memo)
                else -> repository.deleteMemo(memo.id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe()
        )
    }

    fun deleteFolderMemo(
        folder : Folder,
        onFinished: () -> Unit = {}
    ) {
        addDisposable(
            repository.deleteFolderMemo(folder.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe()
        )
    }
}