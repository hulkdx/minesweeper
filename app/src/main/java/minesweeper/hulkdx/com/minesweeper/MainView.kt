package minesweeper.hulkdx.com.minesweeper

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.View

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/07/2018.
 */
class MainView: SurfaceView {
    
    constructor(context: Context)
            : super(context) {
        init(context, null)
    }
    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }
    @RequiresApi(21)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }
    private fun init(context: Context, attrs: AttributeSet?) {
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }
}