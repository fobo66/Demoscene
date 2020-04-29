package io.github.fobo66.demoscene

import android.content.Context
import android.opengl.GLSurfaceView

class DemoSurfaceView(context: Context): GLSurfaceView(context) {

    private val renderer: DemoGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)


        renderer = DemoGLRenderer(context.resources)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }

}