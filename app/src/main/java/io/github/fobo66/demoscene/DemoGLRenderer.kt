package io.github.fobo66.demoscene

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class DemoGLRenderer(resources: Resources) : GLSurfaceView.Renderer {

    private lateinit var fragmentShader: String
    private lateinit var vertexShader: String

    private var colorHandle: Int = 0
    private var positionHandle: Int = 0
    private var mvpMatrixHandle: Int = 0
    private var timeHandle: Int = 0

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private val modelMatrix = FloatArray(16)

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private val viewMatrix = FloatArray(16)

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport.  */
    private val projectionMatrix = FloatArray(16)

    /** Allocate storage for the final combined matrix. This will be passed into the shader program.  */
    private val mvpMatrix = FloatArray(16)

    /** Store our model data in a float buffer.  */
    private val triangle1Vertices: FloatBuffer

    /** How many bytes per float.  */
    private val mBytesPerFloat = 4

    /** How many elements per vertex.  */
    private val mStrideBytes = 7 * mBytesPerFloat

    /** Offset of the position data.  */
    private val mPositionOffset = 0

    /** Size of the position data in elements.  */
    private val mPositionDataSize = 3

    /** Offset of the color data.  */
    private val mColorOffset = 3

    /** Size of the color data in elements.  */
    private val mColorDataSize = 4

    init {
        // This triangle is red, green, and blue.
        val triangle1VerticesData: FloatArray = floatArrayOf(
            // X, Y, Z,
            // R, G, B, A
            -0.5987875f, -0.25f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.5f, -0.25765f, 0.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f
        )


        // Initialize the buffers.
        triangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.size * mBytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()


        triangle1Vertices.put(triangle1VerticesData).position(0)

        resources.openRawResource(R.raw.vertex_shader).use {
            vertexShader = it.bufferedReader().readText()
        }

        resources.openRawResource(R.raw.fragment_shader).use {
            fragmentShader = it.bufferedReader().readText()
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        // Do a complete rotation every 10 seconds.

        // Do a complete rotation every 10 seconds.
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = 360.0f / 10000.0f * time.toInt()

        // Draw the triangle facing straight on.

        // Draw the triangle facing straight on.
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f)
        drawTriangle(triangle1Vertices)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f

        Matrix.frustumM(projectionMatrix, 0, left, ratio, bottom, top, near, far)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Position the eye behind the origin.
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = 1.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        var vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)

        if (vertexShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader)

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle)

            // Get the compilation status.
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle)
                vertexShaderHandle = 0
            }
        }

        if (vertexShaderHandle == 0) {
            throw RuntimeException("Failed to create vertex shader")
        }

        // Load in the fragment shader shader.

        // Load in the fragment shader shader.
        var fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader)

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle)

            // Get the compilation status.
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle)
                fragmentShaderHandle = 0
            }
        }

        if (fragmentShaderHandle == 0) {
            throw java.lang.RuntimeException("Error creating fragment shader.")
        }

        // Create a program object and store the handle to it.

        // Create a program object and store the handle to it.
        var programHandle = GLES20.glCreateProgram()

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle)

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle)

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position")
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color")

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle)

            // Get the link status.
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }

        if (programHandle == 0) {
            throw RuntimeException("Error creating program.")
        }

        // Set program handles. These will later be used to pass in values to the program.
        mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix")
        timeHandle = GLES20.glGetUniformLocation(programHandle, "u_Time")
        positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position")
        colorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color")

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle)
    }


    /**
     * Draws a triangle from the given vertex data.
     *
     * @param aTriangleBuffer The buffer containing the vertex data.
     */
    private fun drawTriangle(aTriangleBuffer: FloatBuffer) {
        // Pass in the position information
        aTriangleBuffer.position(mPositionOffset)
        GLES20.glVertexAttribPointer(
            positionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
            mStrideBytes, aTriangleBuffer
        )
        GLES20.glEnableVertexAttribArray(positionHandle)

        // Pass in the color information
        aTriangleBuffer.position(mColorOffset)
        GLES20.glVertexAttribPointer(
            colorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
            mStrideBytes, aTriangleBuffer
        )
        GLES20.glEnableVertexAttribArray(colorHandle)

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniform1f(timeHandle, SystemClock.uptimeMillis().toFloat())
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }
}
