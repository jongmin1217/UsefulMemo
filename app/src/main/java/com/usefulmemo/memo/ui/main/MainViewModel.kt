package com.usefulmemo.memo.ui.main

import androidx.lifecycle.LiveData
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

    private val _memo = SingleLiveEvent<Unit>()
    private val _back = SingleLiveEvent<Unit>()
    private val _write = SingleLiveEvent<Unit>()
    private val _closeKeyboard = SingleLiveEvent<Unit>()
    private val _addFolder = SingleLiveEvent<Boolean>()

    val memo: LiveData<Unit> get() = _memo
    val back: LiveData<Unit> get() = _back
    val write: LiveData<Unit> get() = _write
    val closeKeyboard: LiveData<Unit> get() = _closeKeyboard
    val addFolder: LiveData<Boolean> get() = _addFolder

    var editFolderInfo: Folder? = null

    private var selectFolderId: Long = Constants.MEMO.toLong()
    var memoItems = SingleLiveEvent<ArrayList<Memo>>().default(ArrayList())


    private var selectMemo: Memo? = null

    private var timer = Timer()

    var memoText: String by Delegates.observable("") { _, old, new ->
        if (old != new) {
            selectMemo?.let {
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

    override fun addFolderClick() {
        editFolderInfo = null
        _addFolder.value = true
    }

    override fun writeClick() {
        selectMemo =
            Memo(0, "", Util.getUnixTime(), true, if (selectFolderId < 1) 0 else selectFolderId)
        memoText = ""

        selectMemo?.let { memo ->
            getMemoUseCase.singleInsert(memo,
                onSuccess = {
                    memo.id = it
                },
                onError = {
                    Timber.d("timber insert error $it")
                }
            )
            goWrite()
        }

    }

    override fun backClick() {
        _back.value = Unit
    }

    override fun completeClick() {
        _closeKeyboard.value = Unit
    }

    fun folderClick(folder: Folder) {
        when (folder.id.toInt()) {
            Constants.ALL_MEMO -> getAllMemo()
            Constants.DELETE_MEMO -> getDeleteMemo()
            else -> getFolderMemo(folder.id)
        }

        setTitle(
            backVisible = true,
            addFolderVisible = false,
            text = folder.name
        )

    }

    fun memoClick(memo: Memo) {
        memoText = memo.text
        selectMemo = memo
        goWrite()
    }

    private fun goWrite() {
        setTitle(
            backVisible = true,
            addFolderVisible = false,
            writeVisible = false
        )
        _write.value = Unit
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

    private fun getAllMemo() {
        var firstLoad = true
        getMemoUseCase.observableMemoList(Constants.ACTIVE,
            onSuccess = {
                selectFolderId = Constants.ALL_MEMO.toLong()
                memoItems.value = it as ArrayList<Memo>
                if (firstLoad) {
                    _memo.value = Unit
                    firstLoad = false
                }
            },
            onError = {
                Timber.d("timber getAllMemo error $it")
            })
    }

    private fun getDeleteMemo() {
        var firstLoad = true
        getMemoUseCase.observableMemoList(Constants.INACTIVE,
            onSuccess = {
                selectFolderId = Constants.DELETE_MEMO.toLong()
                memoItems.value = it as ArrayList<Memo>
                if (firstLoad) {
                    _memo.value = Unit
                    firstLoad = false
                }
            },
            onError = {
                Timber.d("timber getDeleteMemo error $it")
            })
    }

    private fun getFolderMemo(folderId: Long) {
        var firstLoad = true
        getMemoUseCase.observableMemoFolderList(folderId,
            onSuccess = {
                selectFolderId = folderId
                memoItems.value = it as ArrayList<Memo>
                if (firstLoad) {
                    _memo.value = Unit
                    firstLoad = false
                }
            },
            onError = {
                Timber.d("timber getFolderMemo error $it")
            })
    }

    fun addFolderSave() {
        folderName.value?.let {
            if (it.isNotEmpty()) {
                getFolderUseCase.completableMemo(
                    Folder(
                        if (editFolderInfo == null) 0 else editFolderInfo?.id ?: 0, it
                    ), if (editFolderInfo == null) Constants.INSERT else Constants.UPDATE
                )
                addFolderCancel()
            }
        }
    }

    fun addFolderCancel() {
        _addFolder.value = false
        folderName.value = ""
    }

    fun removeFolderName() {
        folderName.value = ""
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
        editFolderInfo = folder
        folderName.value = folder.name
        _addFolder.value = true
    }

    fun clearMemoObserve() {
        getMemoUseCase.clearDisposable()
    }

    fun emptyMemoCheck() {
        selectMemo?.let {
            if (it.text.isEmpty()) {
                getMemoUseCase.completableMemo(it, Constants.DELETE)
            }
        }

        selectMemo = null
    }
}