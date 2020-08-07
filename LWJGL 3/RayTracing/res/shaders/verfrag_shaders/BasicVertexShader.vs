#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec2 texCoord;

out vec4 outColor;
out vec2 outTexCoord;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * worldMatrix * vec4(position, 1.0);
	
	outColor = vec4(inColor,1.0);
	outTexCoord = texCoord;
}