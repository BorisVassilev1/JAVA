#version 460

#include <rendering.glincl>

out vec2 outTexCoord;

void main() {
	gl_Position =  vec4(position.xy, 0.0, 1.0);
	outTexCoord = (position.xy + vec2(1.0))/ 2;
}