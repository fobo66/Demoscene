package io.github.fobo66.demoscene

import androidx.appcompat.app.AppCompatActivity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DemoActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        glSurfaceView = DemoSurfaceView(this)

        setContentView(glSurfaceView)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
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