#version 400 core

in vec3 position;
in vec3 inColor;

out vec4 color;

void main() {
	gl_Position = vec4(position, 1.0);
	color = vec4(inColor,1.0);
}