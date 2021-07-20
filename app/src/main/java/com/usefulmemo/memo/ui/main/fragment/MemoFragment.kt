package com.usefulmemo.memo.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseFragment
import com.usefulmemo.memo.databinding.FragmentMemoBinding
import com.usefulmemo.memo.ui.main.FolderListAdapter
import com.usefulmemo.memo.ui.main.MainViewModel
import com.usefulmemo.memo.ui.main.MemoListAdapter
import com.usefulmemo.memo.utils.BindingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoFragment  : BaseFragment<FragmentMemoBinding, MainViewModel>(R.layout.fragment_memo) {
    override val viewModel by activityViewModels<MainViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BindingAdapter.setMemoListAdapter(
            binding.recyclerviewMemo,
            MemoListAdapter(viewModel)
        )
    }
}