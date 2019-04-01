#version 430 core

in vec4 outColor;
in vec2 outTexCoord;
out vec4 fragColor;

//uniform sampler2D texture_sampler;
//uniform vec3 ray00;
//uniform vec3 ray01;
//uniform vec3 ray10;
//uniform vec3 ray11;
//uniform vec3 vertexArray[];
//uniform ivec3 indicesArray[];

float lerp2f(float a , float b, float k)
{
	return (a * k + b * (1 - k));
}
vec3 lerp2vec(vec3 a, vec3 b, float k)
{
	return vec3(lerp2f(a.x, b.x, k), lerp2f(a.y, b.y, k), lerp2f(a.z, b.z, k));
}
vec3 lerp4vec(vec3 a, vec3 b, vec3 c, vec3 d, float x, float y)
{
	vec3 vecAB = lerp2vec(a, b, x);
	vec3 vecCD = lerp2vec(c, d, x);
	return lerp2vec(vecAB, vecCD, y);
}

vec3 intersectRayTriangle(vec3 rayOrigin, vec3 rayVector, vec3 vertex0, vec3 vertex1, vec3 vertex2)
{
		float EPSILON = 0.00000000001f;
	    vec3 edge1, edge2, h, s, q;
	    float a,f,u,v;
	    edge1 = vertex1 - vertex0;
	    edge2 = vertex2 - vertex0;
	    h = cross(rayVector, edge2);
	    a = dot(edge1,h);
	    if(a > -EPSILON && a < EPSILON)
	    	return vec3(0.0,0.0,0.0);
	    f = 1/a;
	    s = rayOrigin - vertex0;
	    u = f * dot(s, h);
	    if (u < 0.0 || u > 1.0)
	        return vec3(0.0,0.0,0.0);
	    q = cross(s, edge1);
	    v = f * dot(rayVector, q);
	    if (v < 0.0 || u + v > 1.0)
	        return vec3(0.0,0.0,0.0);
	    float t = f * dot(edge2, q);
	    if(t > EPSILON)
	    {
	    	return rayOrigin + rayVector*t;
	    }
	    else
	    {
	    	return vec3(0.0,0.0,0.0);
	    }
}

void main()
{
	//ivec3 a1 = indicesArray[0];
	//vec3 a2 = vertexArray[0];
    //fragColor = (texture(texture_sampler, outTexCoord) + outColor)/2;
    //vec2 screenCoord = gl_FragCoord.xy / vec2(800,600);
    //vec3 closestIntersectionPoint;
   
    //vec3 intPoint = intersectRayTriangle(vec3(0.0,0.0,0.0), lerp4vec(ray00,ray01,ray10,ray11, screenCoord.x, screenCoord.y), vec3(-0.5,-0.5,10.0), vec3(-0.5,0.5,10.0),vec3(0.5,-0.5,10.0));
    
    //vec3 col = vec3(1.0,1.0,1.0);
    //fragColor = vec4(col * step(-length(intPoint) - 10,-0.00001) ,1.0);
    //fragColor = vec4(1.0,1.0,1.0,1.0);
    fragColor = outColor;
}

