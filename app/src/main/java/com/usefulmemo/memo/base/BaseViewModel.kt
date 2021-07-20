package com.usefulmemo.memo.base

import androidx.lifecycle.ViewModel
import com.usefulmemo.memo.MyApplication
import com.usefulmemo.memo.R
import com.usefulmemo.memo.utils.SingleLiveEvent

open class BaseViewModel : ViewModel() {

    var titleText = SingleLiveEvent<String>().default(MyApplication.mInstance.getString(R.string.folder))
    var titleWriteVisible = SingleLiveEvent<Boolean>().default(true)
    var titleAddFolderVisible = SingleLiveEvent<Boolean>().default(true)
    var titleBackVisible = SingleLiveEvent<Boolean>().default(false)
    var titleCompleteVisible = SingleLiveEvent<Boolean>().default(false)

    fun <T : Any?> SingleLiveEvent<T>.default(initialValue: T) = apply { setValue(initialValue) }

    open fun addFolderClick(){}
    open fun writeClick(){}
    open fun backClick(){}
    open fun completeClick(){}
}