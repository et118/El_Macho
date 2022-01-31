# El Mama
## Info
This is a Discord bot framework made with [discord4j](https://discord4j.com/) that makes it easy to create bots with commands and events, hence the name El **Mama**.
It uses gradle 7.3.3 and java 17 and lower.
### Features
- An easily modifiable codebase (I hope)
- Built-in command and event system
- Built-in info command to easily show your users how commands work and their prefixes
- Gradle tasks for running and building jar files
- A loving mother that will allow you to make whatever child you want... I mean a framework that will allow you to make whatever bot you want
### Built in commands and events
- Ping - Basic ping command that makes the bot return with the message "Pong"
- Info - Shows info about commands and events such as if they are enabled or not and description
### TODO
- Docs about how to create commands and events
- Permission System
- Change group name to rootProject.name in build.gradle

<br>

## Setup
This tutorial uses **Intellij** as an IDE but it should technically work for any other IDE using **Gradle**. But no promises though ;)
### Download repository
Open a terminal and locate to the folder where you want to download the repository. Then type in the following command:
```
git clone https://github.com/et118/El-Mama.git
```
### Open in IDE
Navigate into `El-Mama` and open the `build.gradle` file with Intellij. Let Intellij do its thing and index the gradle project. When it's done indexing you should be able to modify and run the code.
## Running code
When you have modified the code to your liking you can easily run it using gradle. One way of doing it is by typing the following command in the terminal:
<br><br>
*Note: Make sure to replace the content within the brackets with your Discord bot token*
```
./gradlew run --args="[TOKEN HERE]"
```
Another way of running the code is by using Intellij:s built-in run configurations.
To create a run configuration you must first navigate to the right side of Intellij.
There you should see a Gradle tab sticking out. 
Click on it and expand the group named `el mama`. Right click on the `run` task and select `Modify Run Configurations`. 
Add the following in the arguments field:
<br><br>
*Note: Make sure to replace the content within the brackets with your Discord bot token*
```
run --args="[TOKEN HERE]"
```
Then select apply and close the window. Now you can run your bot simply by double clicking the run task in the Gradle sidebar.
Once the task has been run once you should also see it among Intellij:s run configurations in the upper right corner.
If you have selected the gradle task in the run configuration window you should be able to run it using the shortcut `Shift + f10`.

## Generating executable Jar
When you want to release your bot you can easily compile an executable jar file using Gradle.
One way of doing it is by typing the following command into the terminal.
```
./gradlew fatJar
```
Another way of doing it is by double clicking the `fatJar` task in the Gradle sidebar. 
Both of these methods will generate an executable jar in `PROJECT_DIR/out`. 
This jar file will take a Discord bot token as an argument when run.
To run the executable jar file type in the following command:
<br><br>
*Note: Make sure to replace the content within the brackets with your Discord bot token*
```
java -jar "El Mama-1.0.jar" [TOKEN HERE]
```
