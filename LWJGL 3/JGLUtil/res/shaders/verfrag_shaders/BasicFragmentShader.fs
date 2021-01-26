#version 460

#include <rendering.glincl>

in vec4 outColor;
in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

uniform sampler2D texture_sampler;

vec3 calcLight(Light light, in vec3 position, in vec3 normal, in float attenuationExp, Material mat) {
	vec3 lightPosition = (light.transform * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
	vec3 lightForward = (light.transform * vec4(0.0, 0.0, 1.0, 0.0)).xyz;
	
	if(light.type == 0)
		return light.color * mat.color * light.intensity;
	
	vec3 toLight = lightPosition - position;
	if(light.type == 1)
		toLight = -lightForward;
	
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
		vec3 fromLight = -toLight;
		
		float face_hit = dot(fromLight, normal);
		vec3 reflected = normalize(reflect(fromLight, normal));
		
		specular = max(dot(toCamera, reflected), 0.0) * float(face_hit < 0);
		
		specular = pow(specular, mat.specular_exponent);
		
		specularColor = specular * light.color * light.intensity;
	}
	
	float att;
	if(light.type == 2)
		att = pow(dist, attenuationExp);
	else
		att = 1.0;
	
	return (diffuseColor * mat.diffuse_power + specularColor * mat.specular_power) / att;
}

void main()
{
	vec3 light = vec3(0.0, 0.0, 0.0);
	for(int i = 0; i < lights.length(); i ++) {
		light += calcLight(lights[i], mvVertexPos, mvVertexNormal, 2.0, materials[material_index]);
	}
	
	fragColor = vec4(light, 1.0);
}