package com.usefulmemo.memo.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity <B : ViewDataBinding, VM : BaseViewModel>(@LayoutRes val layoutId: Int) : AppCompatActivity() {

    abstract val viewModel: VM
    lateinit var binding: B

    abstract fun initBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

        initBinding()
        observe()
    }

    open fun observe(){}
}