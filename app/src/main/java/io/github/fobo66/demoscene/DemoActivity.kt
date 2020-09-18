package io.github.fobo66.demoscene

import androidx.appcompat.app.AppCompatActivity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DemoActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        glSurfaceView = DemoSurfaceView(this)

        setContentView(glSurfaceView)
    }

    override fun onStart() {
        super.onStart()
        glSurfaceView.onResume()
    }

    override fun onStop() {
        super.onStop()
        glSurfaceView.onPause()
    }
}