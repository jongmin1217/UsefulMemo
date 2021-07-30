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
                nextFragment(MemoFragment())
            })

            back.observe(this@MainActivity, {
                previousFragmentCheck()
            })

            write.observe(this@MainActivity, {
                nextFragment(WriteFragment())
            })

            closeKeyboard.observe(this@MainActivity, {
                closeKeyboard()
            })

            addFolder.observe(this@MainActivity, { folder ->
                AddFolderDialogFragment(folder).show(supportFragmentManager, Constants.ADD_FOLDER)
            })
        }
    }

    private fun closeKeyboard() {
        this.currentFocus?.let {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, folderFragment)
            .addToBackStack(folderFragment.toString()).commit()
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
        when (val fragment = supportFragmentManager.fragments[0]) {
            is FolderFragment -> finish()
            else -> removeFragment(fragment)
        }
    }


    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        previousFragmentCheck()
    }
}