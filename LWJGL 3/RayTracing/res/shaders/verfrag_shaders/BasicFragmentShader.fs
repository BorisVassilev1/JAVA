#version 460

#include <rendering.glincl>

in vec4 outColor;
in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

uniform sampler2D texture_sampler;

float attenuationExponent = 2.0;

uniform vec3 lightPosition;

vec3 calcLight(Light light, in vec3 position, in vec3 normal, in float attenuationExp, Material mat) {
	vec3 lightPosition = (light.transform * vec4(0.0,0.0,0.0,1.0)).xyz;
	
	vec3 toLight = lightPosition - position;
	float dist = length(toLight);
	
	
	float diffuse = 0, specular = 0;
	vec3 diffuseColor, specularColor;
	{
		diffuse = max(dot(normalize(toLight), normal), 0.0);
		
		diffuseColor = diffuse * mat.color * light.color * light.intensity;
	}
	
	{
		vec3 cameraPos = cameraWorldMatrix[3].xyz;
		vec3 toCamera = normalize(cameraPos - position);
		vec3 fromLight = normalize(position - lightPosition);
		
		float face_hit = dot(fromLight, normal);
		vec3 reflected = normalize(reflect(fromLight, normal));
		
		specular = max(dot(toCamera, reflected), 0.0) * float(face_hit < 0);
		
		float specularPower = 20.0; // not good!!!
		
		specular = pow(specular, mat.specular_exponent);
		
		specularColor = specular * light.color * light.intensity;
	}
	
	float att = pow(dist, attenuationExponent);
	
	return (diffuseColor * mat.diffuse_power + specularColor * mat.specular_power) / att;
}

vec3 calcLight(DirectionalLight light, in vec3 fragmentColor, in vec3 position, in vec3 normal) {
	
	vec3 toLight = - light.direction;
	
	float diffuse = max(dot(toLight, normal), 0.0);
	
	
	return vec3(0);
}

void main()
{	
	//Light light = lights[0];
	vec3 light = vec3(0);
	for(int i = 0; i < lights.length(); i ++) {
		light += calcLight(lights[i], mvVertexPos, mvVertexNormal, 2.0, materials[material_index]);
	}
	
	fragColor = vec4(light, 1.0);
}