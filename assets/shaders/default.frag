#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_resolution; // added varying

uniform sampler2D u_texture;
uniform float u_time; // added uniform

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    // apply the bloom effect
    vec4 bloom = vec4(0.0);
    for (int i = 0; i < 5; i++) {
        vec2 offset = vec2(cos(u_time * 2.0 + float(i)), sin(u_time * 2.0 + float(i))) * 0.005;
        bloom += texture2D(u_texture, v_texCoords + offset) * 0.2;
    }

    // combine the original color with the bloom
    gl_FragColor = color + bloom;
}
