#version 430

#include <rendering.glincl>

out vec2 outTexCoord;

void main() {
	gl_Position =  vec4(position.xy, 0.0, 1.0);
	outTexCoord = (vec2(position.x, -position.y) + vec2(1.0))/ 2;
}