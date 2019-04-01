// Author:
// Title:
#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform float u_time;

const vec3 camera = vec3(0.5, 0.5, -2.0);

const vec4 sp1   = vec4(0.500, 0.500, 2.000, 0.400);
const vec4 sp2   = vec4(2.000,2.026,3.526,1.400);
vec3 light = vec3(sin(u_time),0.507,cos(u_time)*2.);
//(sp1.xyz+sp2.xyz)/2.;
//vec3(1.0,1.0,45.0);


float DE(vec3 point) {
    //if(point.y > -0.01 && point.y < 0.0) return 0.;
    return min(
        min(length(point - sp1.xyz) - sp1.w,
        length(point - sp2.xyz) - sp2.w),
        length(point.y) - 0.1
    );
}

float angleBetween(vec3 v1, vec3 v2) {
    return acos(dot(normalize(v1), normalize(v2))) / 3.14;
}

const float EPS = 0.05;
vec3 normal(vec3 p) {
    return normalize(vec3(
        DE(vec3(p.x + EPS, p.y, p.z)) - DE(vec3(p.x - EPS, p.y, p.z)),
        DE(vec3(p.x, p.y + EPS, p.z)) - DE(vec3(p.x, p.y - EPS, p.z)),
        DE(vec3(p.x, p.y, p.z + EPS)) - DE(vec3(p.x, p.y, p.z - EPS))
    ));
}

void main() {
    vec3 st = vec3(gl_FragCoord.xy/u_resolution.xy, 0.0);
    vec3 dir = normalize(st - camera);
    vec3 currPoint = st;
    
    float ang = angleBetween(dir, light);
    float coef = 1.;
    //gl_FragColor = vec4(1.-ang, 1.-ang, 1.-ang, 1.0);
    
    for(int step = 0;step < 500;step ++) {
        float dist = DE(currPoint);
        
        if(dist < 0.06) {
            vec3 normala = normal(currPoint);
            dir = reflect(dir, normala);
            ang = angleBetween(dir, light);
            coef *= 0.7;
            float color = coef * (1.0 - ang);
            gl_FragColor = vec4(color, color, color, 1.0);
        }
        
        currPoint += dir * dist / 20.;
        
    }
}