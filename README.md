# android-svg-code-render

## What is the meaning of this!?

The purpose of this project is creating a (Java based) tool for converting SVG formatted vector graphic files into Java source code.
The generated files can be used for rendering the content of the SVG file directly into a Canvas object under Android, thus eliminating the rather slow and resouce consuming interpretation of the SVG file structure.

Original idea is coming from a blog post from **Bartosz Wesołowski**:
http://androiddreamrevised.blogspot.co.nz/2014/06/transforming-svg-images-into-android.html

There are similar projects already for desktop Java, but I have not found one yet which generates Java code that can be used directly on Android without any external library.

## How to use it

The tool can be run from command line using standard Java JAR execution:

```
java -jar android-svg-code-render inputfile.svg <package name> <class name> <outputfile.java> <template file>
```

Arguments for the executable are:
* **inputfile.svg** (required) - SVG formatted input file path/name (compressed files are not supported at the moment)
* **package name** (optional) - package name in the output file (default: "vector_render")
* **class name** (optional) - name of the embedding class for the render method (default: "VectorRender")
* **outputfile.java** (optional) - output file path/name (default: "VectorRender_*inputfilename*")
* **template file** (optional) - template Java source file which will be used for generating the output (default: built-in template)
 
In the current release the argument parsing is rather rudamentary, if you want to specify an optional argument then you have to specify the arguments before that desired one. The order of the arguments cannot change.

### Output file

The output will be a Java source file which can be used directly in your Android app without any additional library and without the source SVG file itself.
The file contains only the relevant operations which are necessary for drawing the SVG file to a Canvas object.
In the built-in template there will be one static method, called `render`. It takes three arguments: the target canvas, the width and the height of the output (bounding box) in pixels.

### Template file

The exact format of the embedding class and the method header can be specified through a template file.
An example template file is included in the *templates* folder which changes the output to create a `Picture` instance out of the vectors instead of rendering the vectors to a `Canvas`. (Which can be used as a `PictureDrawable` input for any standard Android UI component.)

The template file can be used for very tricky things. For example I added an interface implementation to the embedding class, so I am able to call the render method in a generic way for all the vector rendering objects I have in my app.

But you can also tweak the canvas by applying a `Matrix` instance on it which is created out of the passed in arguments, so you can scale, rotate, mirror or pretty much do whatever you want with your vectors while they are rendered.

I am pretty sure that there are more imaginable developers out there than me who can use the template in various ways for many purpose.

The format of the template file is pretty loose: it must be a text file with four `%s` string literals in it. The order of the literals are strict (at the moment) and refer to the following parameters:

1. package name
2. the imports for the used classes
2. class name
3. the body of the rendering code

The only constraint around the rendering code that it expects a local variable: the target `Canvas` instance, named as `canvas`. Everything else is free-form in the template.

## Why would this be useful to anybody?

This tool is not useful for everybody and for any random SVG rendering. Obviously, if your SVG files are not static (for example downloaded from somewhere) then you won't be able to convert them before building your app.

On the other hand, if you need a flexible way of rendering complex vectors (for example a UI for a game, like in my case) and you are concerned about the speed because there are plenty of files to render then this is the solution you might be looking for.
Before I converted all the SVG files to Java code I had to add a *"Loading"* screen to my game because it took a few seconds to render the vectors into bitmaps. Now, there is no need for that screen anymore, the rendering is blazingly fast. (No wonder, there is no need for interpreting a massive XML structure.)

Other benefits of using Java code over SVG file (or other vector format):

* You can change the rendering code manually or automatically in any way you like.
* You can add hooks into the code for passing in dynamic data (like replacing colors or text at rendering time).
* It is nearly impossible to extract the original file out of your code (so, nobody will steal your precious SVG images).
* Have I mentioned that it is FAST? You can even render the vectors in real time... zoom in/out, rotate, dynamic scaling UI, supporting any random screen resolution, this is all possible without quality loss.
* Probably vector rendering is consuming much less memory than bitmaps (I haven't done any benchmarking, and it depends on the actual file, but it is highly possible).

### Why not to use...

There are downsides too... Keep them in mind!

The rendering code increases the size of the executable significantly, sometimes extremely. It might mean that your app runs slowly in general because the code base is significantly larger and the execution runs out of the buffer too often. (Although, this is more likely just a theoretical problem, I have not experienced such issues.)

Compiling of your codebase will be much slower, the compiler must chew through all the vector rendering classes. Dexing will slow down too.

If you want to update the vector files then it is pretty much not possible without updating the app binary itself. While using SVG files you can implement a downloading process and replace the files even one-by-one whenever you want to.

As it seems the generated code increases the size of the APK file more than the original SVG files. I haven't done any scientific experiments on this issue and it probably depends on the actual file content too, so this is just a mere warning.


## Unresolved issues

There are some issues which needed to be addressed sooner or later:

* Probably not all SVG files will be converted successfully. If you find any files which are working with the original androidsvg library (see below), but doesn't work from the converted format or you get an exception while running the tool then get in touch with me.
* Text rendering is most likely not working properly, I haven't tried too much, but it seems to be depending on the calculation of the width of the rendered text. This part is not implemented yet, don't be surprised too much if your vector file looks funny with text in it.
* There is a hard limit built in into Java: the compiled bytecode of one method cannot be larger than 64kB. If your SVG file is elaborate enough then you might end up with hitting this limit. (You will see the error when you try to compile the outputted class.) This problem can be workarounded manually by splitting the rendering method into multiple methods. You can daisy-chain the methods by calling the next method from the current one and pass in some needed local variables with `Paint` and `Matrix` instances. This issue will be addressed in a later release.
* The outputted code is not optimized too deeply. Some optimization was done by eliminating useless objects, but still an awful lot of unnecessary `Paint` and `Matrix` instances are created with no good reason. The original library is a bit too hasty with cloning these instances, probably it will be possible to eliminate these later on to save some memory and increase the performance even further.
* It is not possible to get the pixel size of the original SVG file. This will be addressed also later on.

## Original source
This project was built on the source code of the great androidsvg library from **Paul LeBeau**.
**Big thanks to him!**

You can find the project page for the original library here (while Google keeps it alive):
http://code.google.com/p/androidsvg

## Code license
android-svg-code-render is licensed under the [**Apache License v2.0**](http://www.apache.org/licenses/LICENSE-2.0).
