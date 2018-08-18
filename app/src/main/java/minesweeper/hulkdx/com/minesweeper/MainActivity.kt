package minesweeper.hulkdx.com.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Extra params for windows:
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Create and start game:
        game = Game(this)

        // set content to MainView
        setContentView(game?.getMainView())
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO remove resources here: 
    }
}
