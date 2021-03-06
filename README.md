
# Fast Track to Akka with Java

## Coffee House

Welcome to the Akka Coffee House. Here you will delight in one of three caffeinated concoctions: `Akkaccino`, `MochaPlay` and `CaffeScala` and learn the basics of Akka with Java along the way. To achieve this, you will work through the series of exercises organized by topic as laid out in the Fast Track to Akka with Java slide deck. Make sure you have the deck handy as you will want to refer to it for guidance.

## Prerequisites

This course is best suited for individuals that have knowledge of Java. You will also need access to the Internet and a computer with the following software installed:

- JVM 1.8 or higher
- [Scala 2.11](http://www.scala-lang.org/documentation/) or higher
- [sbt 13.7](http://www.scala-sbt.org/0.13/docs/index.html) or higher
- A Unix compatible shell

## How Students will Use Courseware

This courseware follows a [multi-project sbt](http://www.scala-sbt.org/0.13/tutorial/Multi-Project.html) structure that contains the base code needed for working through the exercises. During each exercise, you will move to a new project, add code to solve the problem set, and when done will have a great reference for the basics of Akka. Central to each exercise is the `CoffeeHouseApp` main class that you will modify and run to show you how things are progressing. We also include completed tests that you will run at the end of each exercise, so you can be sure your solution is correct.

### Sbt

This project does not provide a GUI so you will rely on an interactive `sbt` session for feedback. If you are new to [sbt](http://www.scala-sbt.org/documentation.html), that is okay, but there is a couple of commands you want to familiarize yourself with.

To load and start a `sbt` session, make sure you are in the `fttas-coffee-house` directory and type `sbt` in your terminal window.

`$ sbt`

#### Clean

To clean your current exercise, run the `clean` command from your `sbt` session. This will delete all generated files in the `target` directory.

`[run the man command] [001] some_exercise > clean`

#### Compile

To compile your current exercise, run the `compile` command from your `sbt` session. This will compile the source in the `src/main/java` directory.

`[run the man command] [001] some_exercise > compile`

#### Reload

To reload your `sbt` session, run the `reload` command from your `sbt` session. This reloads the build definitions, `build.sbt`, and `project/*.sbt` files. Reloading is a requirement if you change the build definition files.

`[run the man command] [001] some_exercise > reload`

#### Man

To view the instructions for the current exercises README.md in your terminal window, run the `man` command from your `sbt` session. If you are using an IDE, you can also open up the exercises README.md file in your workspace. You will notice that the prompt for each exercise contains a reminder `[run the man command]`.

`[run the man command] [001] some_exercise > man`

#### Test

To test your current exercise, run the `test` command from your `sbt` session. This will compile and run all tests for the current exercise. Automated tests are your safeguard and validate whether or not you have completed the exercise successfully and are ready to move on.

`[run the man command] [001] some_exercise > test`

#### Next

To move to and initialize the next exercise, you will run the `next` command from your `sbt` session. The initialization process is known as `grooming` the project and occurs only once for each exercise. In addition to `grooming` the exercise, the `eclipse` project files will be generated.

*Attention*: It is *critical* that you *complete* the current exercise *before* moving to the next one. The only way to be sure, is by running the `test` command which is the *final step* of every exercise. The `test` command will confirm whether or not you have successfully completed the exercise and are ready to move on.

`[run the man command] [001] some_exercise > next`


#### Run

As part of each exercise, you will run the `run` command to bootstrap the main class `CoffeeHouseApp`. This will start the application for the *current* exercise that you will interact with.

`[run the man command] [001] some_exercise > run`

#### Prev

To go to the previous exercise, run the `prev` command.

`[run the man command] [001] some_exercise > prev`

### Eclipse (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import each new project into Eclipse after running `next`. The instructions for each exercise contain a reminder to import the current exercise. **Attention**: Starting in `exercise_001_implement_actor` and beyond, it is **crucial** that you **complete the current exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

Additionally, you to make sure you have the following preferences checked under `Preferences > General > Workspace`:

1. [x] Build automatically
2. [x] Refresh using native hooks or polling
3. [x] Refresh on access
4. [x] Save automatically on build

# Exercises

1. When you run `sbt` for the first time, you will be in the `[run the man command] base >` project.
2. The *first* thing you will always do is run the `man` command and *read* the instructions for the current exercise.
3. If you are using and IDE like *Eclipse* or *Intellij* now would be a good time to import the `CoffeeHouse` project.
    - If you are using *Eclipse* you will first need to run the `eclipse` command. This is the only time you will need to do so.
    - If you are using *Eclipse* after running `eclipse` and importing, you will see two projects, `common` and `entry_state`.
    - If you are using *Intellij* you will import all projects `common` through `exercise_019_use_ask_pattern`.
4. You will notice a nice reminder *[run the man command]* as part of the shell prompt in each exercise.
5. At this point we are ready to move to the next exercise so run the `next` command.
6. This will forward you to the `[run the man command] common >` project.
7. Enjoy! Have fun and welcome to the world of `Akka with Java`.
