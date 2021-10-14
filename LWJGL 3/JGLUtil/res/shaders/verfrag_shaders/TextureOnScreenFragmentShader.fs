#version 430

#include <rendering.glincl>

in vec2 outTexCoord;

out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = texture(texture_sampler, outTexCoord);
}