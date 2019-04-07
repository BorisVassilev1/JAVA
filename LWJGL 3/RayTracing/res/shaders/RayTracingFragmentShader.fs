#version 440

in vec4 outColor;
in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 vertexArray[8];
uniform ivec3 indicesArray[12];

uniform vec3 ray00;
uniform vec3 ray10;
uniform vec3 ray01;
uniform vec3 ray11;

float lerp2f(float a , float b, float k)
{
	return (a * k + b * (1 - k));
}
vec3 lerp2v(vec3 a, vec3 b, float k)
{
	return vec3(lerp2f(a.x, b.x, k), lerp2f(a.y, b.y, k), lerp2f(a.z, b.z, k));
}

vec3 lerp4v(vec3 a, vec3 b, vec3 c, vec3 d, float x, float y)
{
	vec3 vecAB = lerp2v(a, b, x);
	vec3 vecCD = lerp2v(c, d, x);
	return lerp2v(vecAB, vecCD, y);
}

vec3 rayToTriangle(vec3 rayOrigin, vec3 rayVector, vec3 v0, vec3 v1, vec3 v2)
{
	float EPSILON = 0.00000000001f;
	vec3 vertex0 = v0;
    vec3 vertex1 = v1;  
    vec3 vertex2 = v2;
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
    	return rayOrigin + rayVector * t;
    	//System.out.println( outIntersectionPoint);
    }
    else
    {
    	return vec3(0.0,0.0,0.0);
    }
}

vec3 getFromVertexArray(int id)
{
	for(int i = 0; i < 8; i ++)
	{
		if(i == id){
			return vertexArray[i];
		}
	}
	return vec3(0.0,0.0,0.0);
}
ivec3 getFromIndicesArray(int id)
{
	for(int i = 0; i < 12; i ++)
	{
		if(i == id){
			return indicesArray[i];
		}
	}
	return ivec3(1,1,1);
}

void main()
{
	vec3 a = vertexArray[0];
	ivec3 b = indicesArray[0];
	
	vec3 intersectionPoint;
	vec3 closestIntersectionPoint = vec3(1000.0,1000.0,1000.0);
	vec3 st = vec3(gl_FragCoord.xy/vec2(800,600), 0.0);
	vec3 direction = lerp4v(ray00,ray10, ray01, ray11, st.x,st.y);
	
	for(int i = 0; i < 12; i ++)
	{
		//intersectionPoint = rayToTriangle(vec3(0.0,0.0,0.0), direction, getFromVertexArray(getFromIndicesArray(i).x), getFromVertexArray(getFromIndicesArray(i).y), getFromVertexArray(getFromIndicesArray(i).z));
		intersectionPoint = rayToTriangle(vec3(0.0,0.0,0.0), direction, vertexArray[indicesArray[i].x], vertexArray[indicesArray[i].y], vertexArray[indicesArray[i].z]);
		//intersectionPoint = rayToTriangle(vec3(0.0,0.0,0.0), direction, vec3(-1.0,-1.0,-10.0), vec3(1.0,-1.0,-10.0), vec3(1.0,1.0,-10.0));
		if(length(intersectionPoint) != 0 && length(intersectionPoint) < length(closestIntersectionPoint))
		{
			closestIntersectionPoint = intersectionPoint;
		}
	}
	float col = 1.0-length(closestIntersectionPoint)/ 100;
	fragColor = vec4(col, col, col, 1.0);
    //fragColor = (texture(texture_sampler, outTexCoord) + outColor)/2;
}