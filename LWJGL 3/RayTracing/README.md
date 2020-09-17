# RayTracing

Implements a rimple Ray Tracer in Java with OpenGL (LWJGL). I'm in the process of making this a library that compiles and runs compute shaders and can visualize their inputs and outputs.

## Getting Started

To get the project running on your device, clone the repo and open it in Eclipse as a Maven Project. Might be possible in other environments, but I don't know how.

### Prerequisites

Required software:
* Maven
* Eclipse IDE
* JRE 8.0+ (can work with older versions, but it is not tested)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 
I am still not using versioning bcs I have no idea of it and I'm changing the API basically every day...

## Authors

* **Boris Vassilev** - *Initial work* - [BorisVassilev1](https://github.com/BorisVassilev1)

See also the list of [contributors](https://github.com/BorisVassilev1/JAVA/tree/master/LWJGL%203/RayTracing/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## TODO: 
* Think if utils.Loader is needed.
* Instanced rendering might be needed some day. A Scene object may be a solution here. 
* Fix rendering. Normals are needed. Indexed system for normals, vertex color, UVs. Adequate implementation of materials. - working on it
* Object3d contains only position and mesh. Something like GameObject is needed, with Transformation, Material, etc separated.
* Light and shadows. smooth shading - working on it
* Geometry shaders - they should just work.
* Make more examples while doing all of this! -> CubeExample does not have lights
* Make a Scene obect to store everything.
* materials
* everything should be passed to the shaders through UBOs. Make automatic parsing to FloatBuffer. 
* **RENAME THE PROJECT TO SOMETHING MORE FITTING TO IT'S CURRENT PURPOSE AS A LIBRARY**

For the far far future: 
* Should mesh generation be done with a geometry shader smh, or in a compute shader?
* Use libgdx's bindings for Bullet Physics