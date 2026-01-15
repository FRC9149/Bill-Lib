#This section is one of the most important to update.

Most of this code is just the default swerve example from wpilib. This is why the naming conventions and style are completely different from the rest of the library.

The gyroscope was written from scratch. I made it an interface, so when using it in code, you don't need to change which gyro you are using each year. All you have to do is input the correct gyro into the constructor. If we end up switching things like motors, gear ratios, etc. We should use this method to implement those as well.

#When using this code:
    - The wheels turn slowly, so strafing is jank at first
    - The PID controllers are crap, so the wheels oscilate (a lot at times)
    - Other issues I forgot about (sorry I wrote this a year after I stopped updating it)


#When updating this code
    I am not 100% sure, but I believe the code starts in SweveSubsytem which holds the gyro, 4 modules, configurations, and handles the interaction with the rest of the code. Essentially, this is the spine of the nervous system where everything meets back up. Start here and figure out where to branch off too.

    Create documentation. While writing this I was very confused. I became less confused but didn't write anything down, so looking back on it makes me even more confused.