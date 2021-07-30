#version 460

#include <rendering.glincl>

in vec4 outColor;
in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

void main() {
	//fragColor = vec4(vec3(gl_FragCoord.z),1);
	fragColor = vec4(vec3(1. / gl_FragCoord.w),1);
}