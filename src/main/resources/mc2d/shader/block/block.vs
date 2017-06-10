#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColor;

out vec3 color;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

void main() {
	gl_Position = worldMatrix * projectionMatrix * vec4(position, 1.0);
	color = inColor;
}