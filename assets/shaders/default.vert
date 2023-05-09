attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_resolution; // added uniform

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_resolution; // added varying

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    v_resolution = u_resolution; // pass the value to the fragment shader
    gl_Position = u_projTrans * a_position;
}
