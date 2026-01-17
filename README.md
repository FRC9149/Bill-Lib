## FRC team 9149's year to year library

This library sparked to life as our first 3 years had rough code swerve code that kept changing. It holds everything that we use each year such as drivetrains, vision processing, controllers, etc.

---

### Installation

##### Step one
Install a release jar and move the file to the lib directory inside your workspace. (directory is not created automatically)

##### Step two
Add this line to dependencies in build.gradle
`implementation fileTree(dir: 'lib', include: '*.jar')`


```
dependencies { 
   ...
   ...
   ...
   implementation fileTree(dir: 'lib', include: '*.jar') 
}
```

Approximatly line 75.

In order to have wpilib recognize the library for autocorrect and javadocs, you have to add this line to jar in build.gradle
`from sourceSets.main.allSource`
```
jar {
    from { configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) } }
    from sourceSets.main.allSource
    ...
    ...
    ...
}
```

---

### Building the jar

When the library is ready for release, build the code with `ctrl + shift + p;  WPILIB: build robot code`

Next, copy the jar from `{workspace}/build/libs/Bill-Lib.jar`