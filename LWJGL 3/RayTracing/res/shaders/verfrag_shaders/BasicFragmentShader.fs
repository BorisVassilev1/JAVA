#version 330

in vec4 outColor;
in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = (texture(texture_sampler, outTexCoord) + outColor)/2;
}