## FRC team 9149's year to year library

This library sparked to life as our first 3 years had rough code swerve code that kept changing. It holds everything that we use each year such as drivetrains, vision processing, controllers, etc.

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