package com.usefulmemo.memo.ui.main

import androidx.lifecycle.LiveData
import com.usefulmemo.memo.MyApplication
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseViewModel
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.usecase.GetFolderUseCase
import com.usefulmemo.memo.domain.usecase.GetMemoUseCase
import com.usefulmemo.memo.utils.Constants
import com.usefulmemo.memo.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFolderUseCase: GetFolderUseCase,
    private val getMemoUseCase: GetMemoUseCase
) : BaseViewModel() {

    var folderItems = SingleLiveEvent<ArrayList<Folder>>().default(ArrayList())
    var folderName = SingleLiveEvent<String>().default(String())

    private val _memo = SingleLiveEvent<Unit>()
    private val _back = SingleLiveEvent<Unit>()
    private val _addFolder = SingleLiveEvent<Boolean>()

    val memo: LiveData<Unit> get() = _memo
    val back: LiveData<Unit> get() = _back
    val addFolder: LiveData<Boolean> get() = _addFolder

    var previousStatus = 0

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
        _addFolder.value = true
    }

    override fun writeClick() {
        val list = folderItems.value
        list?.let {
            val item = it[it.size - 2]
            getFolderUseCase.completableMemo(item, Constants.DELETE)
        }
    }

    override fun backClick() {
        _back.value = Unit
    }

    fun folderClick(folder: Folder) {
        previousStatus = Constants.FOLDER_UI

        when (folder.id.toInt()) {
            Constants.ALL_MEMO -> getAllMemo()
            Constants.MEMO -> getFolderMemo(folder.id)
            else -> getDeleteMemo()
        }

        setTitle(
            backVisible = true,
            addFolderVisible = false,
            text = folder.name
        )
    }

    fun setTitle(
        backVisible: Boolean? = null,
        addFolderVisible: Boolean? = null,
        writeVisible: Boolean? = null,
        text: String? = null
    ) {
        backVisible?.let { titleBackVisible.value = it }
        addFolderVisible?.let { titleAddFolderVisible.value = it }
        writeVisible?.let { titleWriteVisible.value = it }
        text?.let { titleText.value = it }
    }

    private fun getAllMemo() {
        getMemoUseCase.observableMemoList(Constants.ACTIVE,
            onSuccess = {
                _memo.value = Unit
            },
            onError = {
                Timber.d("timber getAllMemo error $it")
            })
    }

    private fun getDeleteMemo() {
        getMemoUseCase.observableMemoList(Constants.INACTIVE,
            onSuccess = {
                _memo.value = Unit
            },
            onError = {
                Timber.d("timber getDeleteMemo error $it")
            })
    }

    private fun getFolderMemo(folderId: Long) {
        getMemoUseCase.observableMemoFolderList(folderId,
            onSuccess = {
                _memo.value = Unit
            },
            onError = {
                Timber.d("timber getFolderMemo error $it")
            })
    }

    fun addFolderSave(){
        folderName.value?.let {
            if(it.isNotEmpty()){
                getFolderUseCase.completableMemo(Folder(0,it),Constants.INSERT)
            }
        }

        addFolderCancel()
    }

    fun addFolderCancel(){
        _addFolder.value = false
        folderName.value = ""
    }

    fun removeFolderName(){
        folderName.value = ""
    }

}