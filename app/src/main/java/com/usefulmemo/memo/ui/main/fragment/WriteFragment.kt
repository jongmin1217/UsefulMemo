package com.usefulmemo.memo.ui.main.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Selection
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseFragment
import com.usefulmemo.memo.databinding.FragmentWriteBinding
import com.usefulmemo.memo.ui.main.MainViewModel
import com.usefulmemo.memo.utils.KeyboardVisibilityUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WriteFragment : BaseFragment<FragmentWriteBinding, MainViewModel>(R.layout.fragment_write) {
    override val viewModel by activityViewModels<MainViewModel>()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
        initListener()
    }

    override fun onDestroy() {
        viewModel.emptyMemoCheck()
        super.onDestroy()
    }

    private fun initLayout(){
        viewModel.setTitle(
            backVisible = true,
            addFolderVisible = false,
            writeVisible = false
        )
    }

    private fun initListener() {
        activity?.let {
            keyboardVisibilityUtils = KeyboardVisibilityUtils(it.window,
                onShowKeyboard = {
                    if (viewModel.uiStatus.selectMemo != null) viewModel.titleCompleteVisible.value = true
                }, onHideKeyboard = {
                    if (viewModel.uiStatus.selectMemo != null) viewModel.titleCompleteVisible.value = false
                })
        }
    }
}