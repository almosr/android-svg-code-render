# android-svg-code-render

## What is the meaning of this!?

The purpose of this project is creating a (Java based) tool for converting SVG formatted vector graphic files into Java source code.
The generated files can be used for rendering the content of the SVG file directly to a Canvas object under Android, thus eliminating the rather slow and resouce consuming interpretation of the SVG file.

Original idea is coming from a blog post from **Bartosz Wesołowski**:
http://androiddreamrevised.blogspot.co.nz/2014/06/transforming-svg-images-into-android.html

There are similar projects already for desktop Java, but I have not found one yet which generates Java code that can be used directly on Android without any external library.

## Original source
This project was built on the source code of the great androidsvg library from **Paul LeBeau**.
**Big thanks to him!**

You can find the project page for the original library here (while Google keeps it alive):
http://code.google.com/p/androidsvg

## Code license
android-svg-code-render is licensed under the [**Apache License v2.0**](http://www.apache.org/licenses/LICENSE-2.0).
