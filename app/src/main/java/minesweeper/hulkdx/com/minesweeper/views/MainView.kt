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

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/07/2018.
 */
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