## This section is one of the most important to update inside the library.

Most of this code is just the default swerve example that I slowly started to update. This is why the naming conventions and styles are inconsistent. This also means I had no idea what I was doing when I was writting this. I was just trying to figure out what each piece did and plug in what our team was using. I will try to add comments as to what these features do. This way we can build on the library instead of restarting each year.

Lots of things in here are modular. The gyroscope is an interface, meaning you can create a class which implements it. This means in the library you can just put "I want the rotation the robot is at" then you import what gyroscope you are using and pass it in as a variable. This removes the need to go back and change each line whenever you change hardware and you also don't have to pass in 1,000,000 variables. SwerveConfig is the same way but it is a record instead of a interface. 
### Overall:
When writing the code, please try to keep things modular, as this means it's easily expanded apun and you can change things w/o updating the entire codebase.

## When using this code:
 - The wheels turn slowly, so strafing is jank at first
- The PID controllers are crap, so the wheels oscilate (a lot at times)
- Other issues I forgot about (sorry I wrote this a year after I stopped updating it)


## When updating this code
- The code starts in SweveSubsytem which holds the gyro, 4 modules, configurations, and handles the interaction with the rest of the code. Essentially, this is the spine of the nervous system where everything meets back up. Start here and figure out where to branch off too.

- Create documentation. While writing this I was very confused. I became less confused but didn't write anything down, so looking back on it makes me even more confused.

- When I create projects, I have classes use PascalCase and variables use camelCase. Please try to stick to this
- Please add documentation so we can build off the code in future years.