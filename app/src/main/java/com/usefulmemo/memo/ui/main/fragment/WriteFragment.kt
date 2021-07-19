package com.usefulmemo.memo.ui.main.fragment

import androidx.fragment.app.activityViewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseFragment
import com.usefulmemo.memo.databinding.FragmentWriteBinding
import com.usefulmemo.memo.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteFragment  : BaseFragment<FragmentWriteBinding, MainViewModel>(R.layout.fragment_write) {
    override val viewModel by activityViewModels<MainViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }
}