#version 330

in vec4 outColor;
in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform float textureWeight;
uniform float colorWeight;

layout (std140) uniform Matrices {
	mat4 projectionMatrix;
	mat4 viewMatrix;
	mat4 cameraWorldMatrix;
};


struct PointLight {
	float intensity;
	vec3 position;
	vec3 color;
};

struct DirectionalLight {
	float intensity;
	vec3 direction;
	vec3 color;
};

struct AmbientLight {
	float intensity;
 	vec3 color;
};

float attenuationExponent = 2.0;

struct Material {
	sampler2D texture;
	vec3 albedo_color;
	bool use_texture;
};

layout(std140) uniform PointLights {
	PointLight pointLights[];
};

layout(std140) uniform AmbientLights{
	AmbientLight ambientLigths[];
};

layout(std140) uniform DirectionLights {
	DirectionalLight directionalLights[];
};

uniform vec3 lightPosition;

vec3 calcLight(PointLight light, in vec3 fragmentColor, in vec3 position, in vec3 normal, in float attenuationExp) {
	vec3 toLight = light.position - position;
	float dist = length(toLight);
	
	
	float diffuse = 0, specular = 0;
	vec3 diffuseColor, specularColor;
	{
		diffuse = max(dot(normalize(toLight), normal), 0.0);
		
		diffuseColor = diffuse * fragmentColor * light.color * light.intensity;
	}
	
	{
		mat4 cameraWorldMatrix = inverse(viewMatrix);
		vec3 cameraPos = cameraWorldMatrix[3].xyz;
		vec3 toCamera = normalize(cameraPos - position);
		vec3 fromLight = normalize(position - light.position);
		
		float face_hit = dot(fromLight, normal);
		vec3 reflected = normalize(reflect(fromLight, normal));
		
		specular = max(dot(toCamera, reflected), 0.0) * float(face_hit < 0);
		
		float specularPower = 7.0; // not good!!!
		
		specular = pow(specular, specularPower);
		
		specularColor = specular * fragmentColor * light.color * light.intensity;
	}
	
	float att = pow(dist, attenuationExponent);
	
	float diffuseEffect = 1.0; 
	float specularEffect = 1.0;
	
	return (diffuseColor * diffuseEffect + specularColor * specularEffect) / att;
}

vec3 calcLight(DirectionalLight light, in vec3 fragmentColor, in vec3 position, in vec3 normal) {
	
	vec3 toLight = - light.direction;
	
	float diffuse = max(dot(toLight, normal), 0.0);
	
	
	return vec3(0);
	
}

void main()
{
	vec3 color = vec3(1.0);
	PointLight light = PointLight(30.0, lightPosition, vec3(1.0, 1.0, 1.0));
	
	fragColor = vec4(calcLight(light, color, mvVertexPos, mvVertexNormal, 2.0), 1.0);
}