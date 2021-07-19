package com.usefulmemo.memo.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseFragment
import com.usefulmemo.memo.databinding.FragmentFolderBinding
import com.usefulmemo.memo.ui.main.FolderListAdapter
import com.usefulmemo.memo.ui.main.MainViewModel
import com.usefulmemo.memo.utils.BindingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderFragment : BaseFragment<FragmentFolderBinding,MainViewModel>(R.layout.fragment_folder) {
    override val viewModel by activityViewModels<MainViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BindingAdapter.setFolderListAdapter(
            binding.recyclerviewFolder,
            FolderListAdapter(viewModel)
        )
    }
}