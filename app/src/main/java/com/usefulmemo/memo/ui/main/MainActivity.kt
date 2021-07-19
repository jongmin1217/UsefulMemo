package com.usefulmemo.memo.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseActivity
import com.usefulmemo.memo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(R.layout.activity_main) {
    override val viewModel by viewModels<MainViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getResult()
    }

}