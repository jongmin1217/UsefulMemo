package com.usefulmemo.memo.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
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
import com.usefulmemo.memo.ui.main.fragment.WriteFragment
import com.usefulmemo.memo.utils.Constants
import com.usefulmemo.memo.utils.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel by viewModels<MainViewModel>()

    private val folderFragment = FolderFragment()
    private val memoFragment by lazy { MemoFragment() }
    private val writeFragment by lazy { WriteFragment() }
    private val addFolderDialogFragment by lazy { AddFolderDialogFragment() }

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
                previousFragmentCheck()
            })

            write.observe(this@MainActivity, {
                nextFragment(writeFragment)
            })

            closeKeyboard.observe(this@MainActivity, {
                closeKeyboard()
            })

            addFolder.observe(this@MainActivity, { folder ->
                if(folder != null){
                    addFolderDialogFragment.folder = folder
                    addFolderDialogFragment.show(supportFragmentManager, Constants.ADD_FOLDER)
                }
                else addFolderDialogFragment.dismiss()
            })
        }
    }

    private fun closeKeyboard() {
        this.currentFocus?.let {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, folderFragment).addToBackStack(folderFragment.toString()).commit()
    }

    private fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.in_right,
            R.anim.out_left,
            R.anim.in_left,
            R.anim.out_right
        ).replace(binding.frameLayout.id, fragment).addToBackStack(fragment.toString()).commit()
    }

    private fun previousFragmentCheck() {
        for (fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                when(fragment){
                    is FolderFragment -> finish()
                    is MemoFragment -> {
                        memoFragment.binding.scrollView.smoothScrollTo(0, 0)

                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                GlobalScope.launch(Dispatchers.Main){
                                    viewModel.clearMemoObserve()
                                    removeFragment(fragment)
                                }


                            }
                        }, 1000)
//                        viewModel.clearMemoObserve()
//                        removeFragment(fragment)
                        break
                    }
                    else -> {
                        viewModel.emptyMemoCheck()
                        removeFragment(fragment)
                        break
                    }
                }
            }
        }
    }


    private fun removeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.popBackStack()

        initTitle()
    }

    private fun initTitle() {
        with(supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-2).toString()){
            when{
                contains(Constants.FOLDER_FRAGMENT) -> {
                    viewModel.setTitle(
                        backVisible = false,
                        addFolderVisible = true,
                        writeVisible = true,
                        text = MyApplication.mInstance.getString(R.string.folder)
                    )
                }
                contains(Constants.MEMO_FRAGMENT) -> {
                    viewModel.setTitle(writeVisible = true)
                }
            }
        }
    }

    override fun onBackPressed() {
        previousFragmentCheck()
    }
}