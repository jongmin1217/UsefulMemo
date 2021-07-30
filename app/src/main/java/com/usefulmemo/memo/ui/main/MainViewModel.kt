package com.usefulmemo.memo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.usefulmemo.memo.MyApplication
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseViewModel
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo
import com.usefulmemo.memo.domain.usecase.GetFolderUseCase
import com.usefulmemo.memo.domain.usecase.GetMemoUseCase
import com.usefulmemo.memo.utils.Constants
import com.usefulmemo.memo.utils.SingleLiveEvent
import com.usefulmemo.memo.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFolderUseCase: GetFolderUseCase,
    private val getMemoUseCase: GetMemoUseCase
) : BaseViewModel() {

    var folderItems = SingleLiveEvent<ArrayList<Folder>>().default(ArrayList())
    var folderName = SingleLiveEvent<String>().default(String())
    var memoItems = SingleLiveEvent<ArrayList<Memo>>().default(ArrayList())

    private val _memo = SingleLiveEvent<Unit>()
    private val _back = SingleLiveEvent<Unit>()
    private val _write = SingleLiveEvent<Memo>()
    private val _closeKeyboard = SingleLiveEvent<Unit>()
    private val _addFolder = SingleLiveEvent<Folder?>()

    val memo: LiveData<Unit> get() = _memo
    val back: LiveData<Unit> get() = _back
    val write: LiveData<Memo> get() = _write
    val closeKeyboard: LiveData<Unit> get() = _closeKeyboard
    val addFolder: LiveData<Folder?> get() = _addFolder

    val uiStatus = UiStatus()

    var selectFolderId: Long = Constants.MEMO.toLong()

    private var timer = Timer()

    var memoText: String by Delegates.observable("") { _, old, new ->
        if (old != new) {
            uiStatus.selectMemo?.let {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        it.apply {
                            text = new
                            regDate = Util.getUnixTime()
                        }
                        getMemoUseCase.completableMemo(it, Constants.UPDATE)
                    }
                }, 400)
            }
        }
    }


    init {
        getFolderUseCase.observableFolderList(
            onSuccess = {
                val list = (it as ArrayList<Folder>).apply {
                    add(
                        0,
                        Folder(
                            Constants.MEMO.toLong(),
                            MyApplication.mInstance.resources.getString(R.string.memo)
                        )
                    )
                    add(
                        0,
                        Folder(
                            Constants.ALL_MEMO.toLong(),
                            MyApplication.mInstance.resources.getString(R.string.all_memo)
                        )
                    )
                    add(
                        Folder(
                            Constants.DELETE_MEMO.toLong(),
                            MyApplication.mInstance.resources.getString(R.string.delete_memo)
                        )
                    )
                }

                folderItems.value = list
            },
            onError = {
                Timber.d("timber folder list error $it")
            }
        )
    }

    fun setTitle(
        backVisible: Boolean? = null,
        addFolderVisible: Boolean? = null,
        writeVisible: Boolean? = null,
        completeVisible: Boolean? = null,
        text: String? = null
    ) {
        backVisible?.let { titleBackVisible.value = it }
        addFolderVisible?.let { titleAddFolderVisible.value = it }
        writeVisible?.let { titleWriteVisible.value = it }
        completeVisible?.let { titleCompleteVisible.value = it }
        text?.let { titleText.value = it }
    }

    override fun addFolderClick() {
        _addFolder.value = null
    }

    fun writeClick(folderId : Long) {
        val addMemo =
            Memo(0, "", Util.getUnixTime(), true, if (selectFolderId < 1) 0 else selectFolderId)

        getMemoUseCase.singleInsert(addMemo,
            onSuccess = {
                addMemo.id = it
                _write.value = addMemo
            },
            onError = {
                Timber.d("timber insert error $it")
            }
        )

    }

    override fun backClick() {
        _back.value = Unit
    }

    override fun completeClick() {
        _closeKeyboard.value = Unit
    }

    fun folderClick(folder: Folder) {
        selectFolderId = folder.id

        getMemo(folder.id)
        _memo.value = Unit
        setTitle(
            backVisible = true,
            addFolderVisible = false,
            text = folder.name
        )

    }

    fun memoClick(memo: Memo) {
        _write.value = memo
    }


    private fun getMemo(folderId: Long) {
        if (folderId < 0) {
            getMemoUseCase.observableMemoList(if (folderId.toInt() == Constants.ALL_MEMO) Constants.ACTIVE else Constants.INACTIVE,
                onSuccess = {
                    memoItems.value = it as ArrayList<Memo>
                },
                onError = {
                    Timber.d("timber $it")
                })
        } else {
            getMemoUseCase.observableMemoFolderList(folderId,
                onSuccess = {
                    memoItems.value = it as ArrayList<Memo>
                },
                onError = {
                    Timber.d("timber $it")
                })
        }

    }

    fun addFolderSave(folderId: Long) {
        folderName.value?.let {
            if (it.isNotEmpty()) {
                getFolderUseCase.completableMemo(
                    Folder(folderId, it),
                    if (folderId == 0.toLong()) Constants.INSERT else Constants.UPDATE
                )
            }
        }
    }

    fun deleteFolder(folder: Folder) {
        getFolderUseCase.completableMemo(folder, Constants.DELETE)
        getMemoUseCase.deleteFolderMemo(folder)
    }

    fun deleteMemo(memo: Memo) {
        if (selectFolderId == Constants.DELETE_MEMO.toLong()) getMemoUseCase.completableMemo(
            memo,
            Constants.DELETE
        )
        else getMemoUseCase.completableMemo(memo.apply { active = false }, Constants.UPDATE)
    }

    fun editFolder(folder: Folder) {
        _addFolder.value = folder
    }

    fun clearMemoObserve() {
        getMemoUseCase.clearDisposable()
    }

    fun emptyMemoCheck() {
        uiStatus.selectMemo?.let {
            if (it.text.isEmpty()) {
                getMemoUseCase.completableMemo(it, Constants.DELETE)
            }
        }

        uiStatus.selectMemo = null

        titleCompleteVisible.value?.let {
            if (it) titleCompleteVisible.value = false
        }
    }

    data class UiStatus(
        var selectMemo: Memo? = null
    )
}