#version 460

//layout (location = 0) in vec3 position;
//layout (location = 1) in vec3 normal;
//layout (location = 2) in vec3 texCoord;
//layout (location = 3) in vec2 color;
#include <rendering.glincl>

out vec4 outColor;
out vec2 outTexCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

//the current object rendered
uniform mat4 worldMatrix;

//the camera
//layout (std140) uniform Matrices {
//	mat4 projectionMatrix;
//	mat4 viewMatrix;
//	mat4 cameraWorldMatrix;
//};

void main() {
	vec4 mvPos = worldMatrix * vec4(position, 1.0);
	
	gl_Position = projectionMatrix * viewMatrix * mvPos;
	
	outColor = color;
	outTexCoord = texCoord;
	mvVertexNormal = normalize(worldMatrix * vec4(normal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}