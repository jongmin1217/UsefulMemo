package com.usefulmemo.memo.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo
import com.usefulmemo.memo.ui.main.FolderListAdapter
import com.usefulmemo.memo.ui.main.MemoListAdapter

object BindingAdapter {

    @BindingAdapter("viewClick")
    @JvmStatic
    fun viewClick(view: View, listener: View.OnClickListener) {
        view.setOnClickListener {
            it.isClickable = false
            it.run {
                postDelayed({ it.isClickable = true }, 500)
                listener.onClick(it)
            }
        }
    }

    @BindingAdapter("marginStart")
    @JvmStatic
    fun marginStart(view: View, dp : Float) {
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> { marginStart = dp.toInt() }
    }

    @BindingAdapter("setFolderListAdapter")
    @JvmStatic
    fun setFolderListAdapter(view: RecyclerView, adapter : FolderListAdapter) {
        if(!adapter.hasObservers()) adapter.setHasStableIds(true)

        val lm = LinearLayoutManager(view.context)

        view.layoutManager = lm
        view.adapter = adapter

        val animator = view.itemAnimator
        if(animator is SimpleItemAnimator){
            animator.supportsChangeAnimations = false
        }
    }

    @BindingAdapter("setFolderItems")
    @JvmStatic
    fun setFolderItems(view: RecyclerView, items : ArrayList<Folder>) {
        view.adapter?.let {
            (it as FolderListAdapter).items = items
        }
    }

    @BindingAdapter("setMemoListAdapter")
    @JvmStatic
    fun setMemoListAdapter(view: RecyclerView, adapter : MemoListAdapter) {
        if(!adapter.hasObservers()) adapter.setHasStableIds(true)

        val lm = LinearLayoutManager(view.context)

        view.layoutManager = lm
        view.adapter = adapter

        val animator = view.itemAnimator
        if(animator is SimpleItemAnimator){
            animator.supportsChangeAnimations = false
        }
    }

    @BindingAdapter("setMemoItems")
    @JvmStatic
    fun setMemoItems(view: RecyclerView, items : ArrayList<Memo>) {
        view.adapter?.let {
            (it as MemoListAdapter).items = items
        }
    }
}