package com.usefulmemo.memo.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.usefulmemo.memo.MyApplication
import com.usefulmemo.memo.R
import com.usefulmemo.memo.base.BaseActivity
import com.usefulmemo.memo.base.BaseFragment
import com.usefulmemo.memo.databinding.ActivityMainBinding
import com.usefulmemo.memo.ui.main.fragment.AddFolderDialogFragment
import com.usefulmemo.memo.ui.main.fragment.FolderFragment
import com.usefulmemo.memo.ui.main.fragment.MemoFragment
import com.usefulmemo.memo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel by viewModels<MainViewModel>()

    private val folderFragment = FolderFragment()
    private val memoFragment by lazy { MemoFragment() }
    private val addFolderDialogFragment by lazy { AddFolderDialogFragment() }

    private var time: Long = 0

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFragment()
    }

    override fun observe() {
        super.observe()

        with(viewModel) {
            memo.observe(this@MainActivity, {
                nextFragment(memoFragment)
            })

            back.observe(this@MainActivity, {
                removeFragment()
            })

            addFolder.observe(this@MainActivity, { show ->
                if(show) addFolderDialogFragment.show(supportFragmentManager, Constants.ADD_FOLDER)
                else addFolderDialogFragment.dismiss()
            })
        }
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, folderFragment).commit()
    }

    private fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.in_right,
            R.anim.out_left,
            R.anim.in_left,
            R.anim.out_right
        ).replace(binding.frameLayout.id, fragment).addToBackStack(null).commit()
    }

    private fun removeFragment() {
        for (fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                if (fragment is FolderFragment) doubleTabBack()
                else {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                    supportFragmentManager.popBackStack()

                    initTitle()
                    break
                }
            }
        }
    }

    private fun initTitle() {
        if(viewModel.previousStatus == Constants.FOLDER_UI){
            viewModel.setTitle(
                backVisible = false,
                addFolderVisible = true,
                writeVisible = true,
                text = MyApplication.mInstance.getString(R.string.folder)
            )
        }
    }

    override fun onBackPressed() {
        removeFragment()
    }

    private fun doubleTabBack() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(this, resources.getString(R.string.double_tab), Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - time < 2000) {
            super.onBackPressed()
            finish()
            exitProcess(0)
        }
    }

}