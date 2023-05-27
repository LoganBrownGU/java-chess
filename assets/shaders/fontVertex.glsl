#version 330

in vec2 position, textureCoords;

out vec2 pass_textureCoords;

uniform vec2 translation;

void main(void){
    gl_Position = vec4(position + translation * vec2(2, -2), 0, 1);
    pass_textureCoords = textureCoords;
}