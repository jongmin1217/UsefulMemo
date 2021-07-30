package com.usefulmemo.memo.ui.main.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Selection
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseBottomDialog
import com.usefulmemo.memo.databinding.DialogAddFolderBinding
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.ui.main.MainActivity
import com.usefulmemo.memo.ui.main.MainViewModel
import timber.log.Timber

class AddFolderDialogFragment(private val folder : Folder) : BaseBottomDialog<DialogAddFolderBinding,MainViewModel>(R.layout.dialog_add_folder) {
    override val viewModel by activityViewModels<MainViewModel>()


    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = if(folder.id == 0.toLong()) resources.getString(R.string.add_folder)
        else resources.getString(R.string.edit_folder)

        viewModel.folderName.value = folder.name

        initListener()
    }

    private fun initListener(){
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvSave.setOnClickListener {
            viewModel.addFolderSave(folder.id)
            dismiss()
        }

        binding.ivRemove.setOnClickListener {
            viewModel.folderName.value = ""
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        viewModel.folderName.value = ""
    }

}