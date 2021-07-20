package com.usefulmemo.memo.ui.main.fragment

import android.os.Bundle
import android.text.Selection
import android.view.View
import androidx.fragment.app.activityViewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseBottomDialog
import com.usefulmemo.memo.databinding.DialogAddFolderBinding
import com.usefulmemo.memo.ui.main.MainActivity
import com.usefulmemo.memo.ui.main.MainViewModel

class AddFolderDialogFragment : BaseBottomDialog<DialogAddFolderBinding,MainViewModel>(R.layout.dialog_add_folder) {
    override val viewModel by activityViewModels<MainViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }

}