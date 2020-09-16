#version 330

layout (location = 0) in vec3 position;
layout (location = 2) in vec2 texCoord;

out vec2 outTexCoord;

void main() {
	gl_Position =  vec4(position.xy, 0.0, 1.0);
	outTexCoord = (position.xy + vec2(1.0))/ 2;
	outTexCoord.y = 1 - outTexCoord.y;
}