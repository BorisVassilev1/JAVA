#version 430

#include <rendering.glincl>

in vec4 outColor;
in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

void main() {
	fragColor = vec4(1,1,1,1);
}