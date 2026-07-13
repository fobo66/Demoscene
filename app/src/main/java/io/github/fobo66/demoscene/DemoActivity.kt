package io.github.fobo66.demoscene

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class DemoActivity : ComponentActivity() {

    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

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