#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord;
uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    vec4 attackColor = vec4(0.0, 1.0, 0.0, 1.0); // Green color for attack
    gl_FragColor = texColor * attackColor * v_color;
}