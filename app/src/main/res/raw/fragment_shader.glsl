precision mediump float;
uniform float u_Time;
varying vec4 v_Color;
void main()
{
    vec4 color = v_Color * 0.5;

    gl_FragColor = color;
}