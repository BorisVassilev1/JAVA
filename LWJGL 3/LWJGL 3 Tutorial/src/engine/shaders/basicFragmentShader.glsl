#version 150

in vec2 passTextCoords;

out vec4 fragColor;

uniform sampler2D sampler;

void main(void) {
	fragColor = texture(sampler, passTextCoords);
}