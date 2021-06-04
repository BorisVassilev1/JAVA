# JGLUtil

JGLUtil (Java Game Library Util) is a library, which makes use of LWJGL's bindings for OpenGL and GLFW to create a simple pseudo-game-engine. It is designed to allow quick and easy creation of tech demos and for educational purposes. It can work with OpenGL shader programs and render objects on the screen and is perfect for code-lab use. 

## Features

JGLUtil allows quick and easy window, shader and mesh creation. Here is a list of currently implemented features
* Window creation and management. For now, only one window per thread is supported.
* Mesh can be imported. Currently only vertex data is included (only positions, texcoords, colors currently). Procedurally generated primitives are also available
* The rendering pipeline currently renders everything in phong shading. Materials and lights can be used. The engine was not built to have too good graphics.
* Currently the project has vertex, fragment and compute shaders. Shader creation is almost automatic as long as the program types and source codes are present.
* The ray tracing pipeline is currently under development as an example, but will probably be integrated into the the library.

## Screenshots

### Rasterization pipeline

![stanford dragons render with colored lighting](https://raw.githubusercontent.com/BorisVassilev1/JAVA/master/LWJGL%203/JGLUtil/res/image_24.png) 


### Ray Tracing
![ray-traced spheres and geometry](https://github.com/BorisVassilev1/JAVA/tree/master/LWJGL%203/JGLUtil/res/image_20.png?raw=true) 
![ray-traced spheres](https://github.com/BorisVassilev1/JAVA/tree/master/LWJGL%203/JGLUtil/res/image_7.png?raw=true)
![ray-traced sword](https://github.com/BorisVassilev1/JAVA/tree/master/LWJGL%203/JGLUtil/res/image_23.png?raw=true) 


## Getting Started

To get the project running on your device, clone the repo and open it in Eclipse as a Maven Project. There are some examples that are simultaniously used as tests in org.cdnomlqko.jglutil.examples

### Prerequisites

Required software:
* Maven
* Eclipse IDE
* JRE 8.0+ (Currently compiled with JavaSE 1.8)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/BorisVassilev1/JAVA/tree/master/LWJGL%203/JGLUtil/tags). 
I am still not using versioning because I'm changing the API basically every day. Until version tracking is needed, let the current version be 0.0.0-snapshot.

## Authors

* **Boris Vassilev** - *Initial work* - [BorisVassilev1](https://github.com/BorisVassilev1)

See also the list of [contributors](https://github.com/BorisVassilev1/JAVA/tree/master/LWJGL%203/JGLUtil/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## TODO:
* Geometry shaders - they should just work. need to be checked
* add sprite rendering support
* add textures in a sensble way
* improve object IO. Any implementation of the Mesh class should be importable and exportable... somehow
* move this todo list somewhere else. The readMe is definitely not the best place for it.

For the far far future: 
* Use libgdx's bindings for Bullet Physics