# JGLUtil

JGLUtil (Java Game Library Util) is a library, which makes use of LWJGL's bindings for OpenGL and GLFW to create a simple pseudo-game-engine. It is designed to allow quick and easy creation of tech demos and for educational purposes. It can work with OpenGL shader programs and render objects on the screen and is perfect for code-lab use. 

## Getting Started

To get the project running on your device, clone the repo and open it in Eclipse as a Maven Project. There are some examples that are simultaniously used as tests in org.cdnomlqko.jglutil.examples

### Prerequisites

Required software:
* Maven
* Eclipse IDE
* JRE 8.0+ (Currently compiled with JDK 1.8)

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
* Geometry shaders - they should just work.
* make something that ignores lights
* add sprite rendering support
* move this todo list somewhere else. The readme is definitely not the best place for it.

For the far far future: 
* add support for mesh generation through shaders - it might not be needed though
* Use libgdx's bindings for Bullet Physics