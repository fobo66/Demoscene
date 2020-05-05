precision mediump float;
uniform float u_Time;
varying vec4 v_Color;
void main()
{
    float color = 0.5 + 0.5*sin(u_Time);

    gl_FragColor = vec4(v_Color.r + color, v_Color.g + color, v_Color.b - color, v_Color.a);
}