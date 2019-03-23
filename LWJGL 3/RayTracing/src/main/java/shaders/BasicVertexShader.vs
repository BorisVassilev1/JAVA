#version 400 core

in vec3 vertices;

out vec4 color;

void main()
{
	gl_position = vec4(vertices, 1.0);
	color = vec4(vertices.x + 0.5, vertices.y + 0.5, 1.0);
}