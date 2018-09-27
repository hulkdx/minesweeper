package minesweeper.hulkdx.com.minesweeper.views

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import minesweeper.hulkdx.com.minesweeper.Game

class MainView: SurfaceView {

    companion object {
        const val TAG = "MainView"
    }

    constructor(context: Context)
            : super(context) {
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}