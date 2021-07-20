package com.usefulmemo.memo.utils

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

class KeyboardVisibilityUtils (
    private val window: Window,
    private val onShowKeyboard: (() -> Unit)? = null,
    private val onHideKeyboard: (() -> Unit)? = null
) {

    private val minKeyboardHeightPX = 150

    private val windowVisibleDisplayFrame = Rect()
    private var lastVisibleDecorViewHeight: Int = 0


    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
        val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

        if (lastVisibleDecorViewHeight != 0) {
            if (lastVisibleDecorViewHeight > visibleDecorViewHeight + minKeyboardHeightPX) {
                onShowKeyboard?.invoke()
            } else if (lastVisibleDecorViewHeight + minKeyboardHeightPX < visibleDecorViewHeight) {
                onHideKeyboard?.invoke()
            }
        }
        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }

    init {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun detachKeyboardListeners() {
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }
}