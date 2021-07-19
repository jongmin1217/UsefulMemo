package com.usefulmemo.memo.ui.main

import android.provider.SyncStateContract
import androidx.lifecycle.MutableLiveData
import com.usefulmemo.memo.base.BaseViewModel
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo
import com.usefulmemo.memo.domain.usecase.GetFolderUseCase
import com.usefulmemo.memo.domain.usecase.GetMemoUseCase
import com.usefulmemo.memo.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFolderUseCase: GetFolderUseCase,
    private val getMemoUseCase: GetMemoUseCase
) : BaseViewModel() {

    val test = MutableLiveData<String>()

    fun getResult() {
        getFolderUseCase.observableFolderList(
            onSuccess = {
                Timber.d("timberMsg getFolderUseCase success $it")
            },
            onError = {
                Timber.d("timberMsg getFolderUseCase error $it")
            },
            onFinished = {
                Timber.d("timberMsg getFolderUseCase finish")
            }
        )

        getMemoUseCase.observableMemoList(
            onSuccess = {
                Timber.d("timberMsg observableMemoList success $it")
            },
            onError = {
                Timber.d("timberMsg observableMemoList error $it")
            }
        )

        getMemoUseCase.singleMemo(1,
            onSuccess = {
                Timber.d("timberMsg singleMemo success $it")
            },
            onError = {
                Timber.d("timberMsg singleMemo error $it")
            },
            onFinished = {
                Timber.d("timberMsg singleMemo finish")
            }
        )
    }

    fun test() {
        getMemoUseCase.completableMemo(Memo(0, "fd", 123, true, 0), Constants.INSERT)
    }

    fun test2() {
        getMemoUseCase.clearDisposable()
        getMemoUseCase.observableMemoFolderList(0,
            onSuccess = {
                Timber.d("timberMsg observableMemoFolderList success $it")
            },
            onError = {
                Timber.d("timberMsg observableMemoFolderList error $it")
            },
            onFinished = {
                Timber.d("timberMsg observableMemoFolderList finish")
            }
        )
    }

    fun test3() {
        getFolderUseCase.completableMemo(Folder(0, "ds"), Constants.INSERT)
    }

}