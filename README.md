# android-svg-code-render

## Introduction

The purpose of this project is creating a (Java based) tool for converting SVG formatted vector graphic files into Java source code which can be used in Android apps.
The generated files are rendering the content of the SVG file directly into a Canvas object under Android, thus eliminating the rather slow and resource consuming interpretation of the SVG file structure.

Original idea is coming from a blog post from **Bartosz Wesołowski**:
http://androiddreamrevised.blogspot.co.nz/2014/06/transforming-svg-images-into-android.html

There are similar projects already for desktop Java, but I have not found one yet which generates Java code that can be used directly on Android without any external library.

## How to use it

The tool can be run from command line using standard Java JAR execution:

```
java -jar android-svg-code-render <inputfile.svg> [-p <package name>] [-c <class name>] [-o <outputfile.java>] [-t <template file>] [-rt <text replacement file>]
```

Arguments for the executable are:
* **inputfile.svg** (required) - SVG formatted input file path/name (compressed files are not supported at the moment)
* **-aos &lt;api version&gt;** (optional) - minimum Android API version for rendered code
* **-p &lt;package name&gt;** (optional) - package name in the output file (default: "vector_render")
* **-c &lt;class name&gt;** (optional) - name of the embedding class for the render method (default: "VectorRender")
* **-o &lt;outputfile.java&gt;** (optional) - output file path/name (default: "VectorRender_*inputfilename*")
* **-t &lt;template file&gt;** (optional) - template Java source file which will be used for generating the output (default: built-in template)
* **-rt &lt;text replacements file&gt;** (optional) - file name for a list of text elements in the SVG file which must be replaced by a Java variable (method parameter) in the output, list consists of lines with text element followed by variable name delimited by equals sign (default: none)
* **-rc &lt;color replacements file&gt;** (optional) - file name for a list of color elements in the SVG file which must be replaced by a Java variable (method parameter) in the output, list consists of lines with color element (format: #AARRGGBB) followed by variable name delimited by equals sign (default: none)
* **-tfp &lt;typeface parameter name&gt;** (optional) - use specified parameter name as ```Typeface``` for text rendering instead of default typeface instances.
 
### Output file

The output will be a Java source file which can be used directly in your Android app without any additional library and without the original source SVG file itself.
The file contains only the relevant operations which are necessary for drawing the SVG file to a Canvas object. (More or less, some optimizations are still needed, see issue #57.)
In the built-in template there will be one static method, called `render`. It takes three arguments: the target canvas, the width and the height of the output (bounding box) in pixels.

### Template file

The exact format of the embedding class and the method header can be specified through a template file.
An example template file is included in the *templates* folder which changes the output to create a `Picture` instance out of the vectors instead of rendering the vectors to a `Canvas`. (Which can be used as a `PictureDrawable` input for any standard Android UI component.)

The template file can be used for very tricky things. For example I added an interface implementation to the embedding class, so I am able to call the render method in a generic way for all vector rendering objects I have in my app.

But you can also tweak the canvas by applying a `Matrix` instance on it which is created out of the passed in arguments, so you can scale, rotate, mirror or pretty much do whatever you want with your vectors while they are rendered.

I am pretty sure that there are more imaginable developers out there than me who can use the template in various ways for many purpose.

The format of the template file is pretty loose: it must be a text file with four `%s` string literals in it. The order of the literals are strict (at the moment) and refer to the following parameters:

1. package name
2. the imports for the used classes
3. class name
4. list of constants, including two `float` constants, called `WIDTH` and `HEIGHT`, which contain the width and the height of the source SVG document
5. list of fields for all Android class instances which are allocated for the rendering
6. the body of the field initialization code
7. the body of the rendering code

The only constraint around the rendering code is it expects a local variable: the target `Canvas` instance, named as `canvas`. Everything else is free-form in the template.
(See related issue #58.)

### How to replace static texts or colors by dynamic variables

Sometimes it is very handy to be able to render text or color dynamically as part of the vector output. By specifying a _replacement file_ parameter either for texts or for colors it is possible to change specific static items in the output into Java variables or parameters.
For example adding this line into a text file which is used as text replacement configuration:
```
player_name=playerName
```

Will change any texts in the SVG input file which reads "player_name" into a Java variable in the rendering which is called ```playerName```.
All you need to do is passing a parameter to the render method as ```String playerName``` and you can change the rendered text dynamically every time when you call the render method. Easy as.

Changing colors into dynamic variables is similarly simple, for example this color replacement configuration:
```
#ffff0000=backgroundColor
```

Will change any colors in the SVG input file which is plain red (ARGB code is 0xFFFF0000) into a Java variable in the rendering which is called "```backgroundColor```".
Pass a parameter to the render method as ```int backgroundColor``` and you can specify the color right before rendering.

At the end of the SVG file processing any not used text and color replacements will trigger a warning log to the standard output.

## About text rendering

Rendering text as part of the SVG file is rather limited in the generated render method. Unfortunately, it is depending on the calculation of the width of the text object, this calculation is not simulated at the moment.
Your text might be fine though, but certain distortions and transformations might not work properly.

In case you need to change the rendered text dynamically then read the previous section about How to replace static texts.

Sometimes the alignment of the text is off, or the text is rendered to a wrong position. The alignment can be corrected by using non-standard "text-align" style parameter on the text instance inside the SVG. This style is specified by certain editors in saved files, for example by Inkscape is providing the alignment style.

Specified font set for texts in the SVG file will be ignored by the render method. Resolving a font set would be pretty heavy operation and would be depending on your implementation also.
Instead, you can specify your own Android ```Typeface``` instance as a parameter to the render method and set up your font handling in any way you like.
All you need to do is: use the -tfp command line parameter and pass a ```Typeface``` parameter name of your choice to the render method. Also set up the same parameter in your template file. The provided ```Typeface``` parameter will be used for each text rendering then, regardless what was specified in the SVG file.

## Why would this be useful to anybody?

This tool is not useful for generic SVG rendering. Obviously, if your SVG files are not static (for example downloaded from somewhere) then you won't be able to convert them before building your app.

On the other hand, if you need a flexible way of rendering complex vectors (for example a UI for a game, like in my case) and you are concerned about the speed because there are plenty of files to render then this is the solution you might be looking for.
Before I converted all the SVG files to Java code I had to add a *"Loading"* screen to my game because it took a few seconds to render the vectors into bitmaps before I was able to use them on the screen. Now, there is no need for that screen any more, the rendering is blazingly fast. (No wonder, there is no need for interpreting a massive XML structure.)

Other benefits of using Java code over SVG file (or other vector format):

* It consumes significantly less memory: no need to load the SVG file, the SVG parser eats up lots of memory, tons of useless objects were created while the image is rendered simply because the render library cannot predict the need for the various drawing classes. All of these problems are eliminated by the static rendering code.
* You can change the rendering code manually or automatically in any way you like. It is just a Java source code after all.
* You can add hooks into the code for passing in dynamic data (like replacing colours or text at rendering time).
* It is nearly impossible to extract the original file from your compiled app (so, nobody will steal your precious SVG images).
* Have I mentioned that it is FAST? You can even render the vectors in real time... Zoom in/out, rotate, scale UI dynamically, support any random screen resolution or aspect ratio: this is all possible without quality loss.
* Potentially vector rendering is consuming much less memory than bitmaps (I haven't done any benchmarking, and it depends on the actual file, but it is highly possible).

### Why not to use it...

There are downsides too... Keep them in mind!

The rendering code increases the size of the executable significantly, sometimes extremely. It might mean that your app runs slowly in general because the code base is significantly larger and the execution runs out of the buffer too often. (Although, this is more likely just a theoretical problem, I have not experienced such issues.)

Compiling of your code base will be much slower, the compiler must chew through all the vector rendering classes. Dexing will slow down too.
There is a workaround I have figured out for this issue: you can put all the rendering classes into a separate library (or into a module in Android Studio/Gradle) and include it in pre-compiled form. This won't slow down the app compiling every time.

If you want to update the vector files then it is pretty much not possible without updating the app binary itself. While using SVG files you can implement a downloading process and replace the files even one-by-one whenever you want to.

As it seems the generated code increases the size of the APK file more than the original SVG files. I haven't done any scientific experiments or benchmarking on this issue and it probably depends on the actual file content too, so this is just a mere warning.

Since the files are converted into Java code, all files are contributing into the infamous Dex file method count ([see 64k reference limit](https://developer.android.com/studio/build/multidex.html)). Usually one SVG will be turned into a few methods only (due to the required method chaining it cannot be solved in one method only usually), so it is not a big deal, but if you had lots of files then it can make the situation worse for your code base.

## Compatibility

The tool is not compatible with all SVG files, certain features are not supported.
Obviously certain SVG features, like interactivity is out of scope for this tool, also there might be difficulties with some SVG formats saved from certain programs.
For some details please see Unresolved issues below.

Generated code is aiming to be compatible with the minimum Android API version and any later version. See minimum Android API version setting.
Not all Android graphical methods are hardware accelerated on all Android versions.
A warning is printed to the standard output when the rendered code contains OS method calls which are not hardware accelerated on the specified minimum API version.
For more information see the [official documentation](https://developer.android.com/guide/topics/graphics/hardware-accel) .

## Unresolved issues

There are some issues which needed to be addressed sooner or later:

* Probably not all SVG files will be converted successfully. If you find any files which are working with the original androidsvg library (see below), but doesn't work from the converted format or you get an exception while running the tool then get in touch with me.
* Text rendering is most likely not working properly for every text distortions, I haven't tried it too much, but the rendering seems to be depending on the calculation of the width of the rendered text. Have a look at the About text rendering section.
* The outputted code is not optimized too deeply. Some optimization was done by eliminating useless objects, but still an awful lot of unnecessary `Paint` and `Matrix` instances are created with no good reason. The original library is a bit too hasty with cloning these instances, probably it will be possible to eliminate these later on to save some memory and increase the performance even further. (See issue #57.)
* Embedded and external bitmaps are converted into static byte array and copied into the output source code. This method might work for small bitmaps, but large files will cause some trouble most likely. You might need to rewrite the code manually which loads the image to get it from some external file inside your app.

## Original source
This project was built on the source code of the great androidsvg library from **Paul LeBeau**.
**Big thanks to him!**

You can find the project page for the original library here:
https://github.com/BigBadaboom/androidsvg

Currently integrated version is v1.3

## Code license
android-svg-code-render is licensed under the [**Apache License v2.0**](http://www.apache.org/licenses/LICENSE-2.0).
