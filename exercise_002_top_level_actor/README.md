# Exercise 002: Create a Top Level Actor

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will make `CoffeeHouse` a top-level actor and implement some configuration properties.

1. In the `CoffeeHouse` actor:
    - Implement a `static Props` factory using `akka.actor.Props`.
    - In the constructor, Log *"CoffeeHouse Open"* at `debug`.
2. In `CoffeeHouseApp` create a top-level `CoffeeHouse` actor:
    - Use `system.actorOf` with the `CoffeeHouse.props`.
    - Name the actor *"coffee-house"*.
3. Create a configuration file that does the following:
    - Set the Akka logging level to `debug`.
    - Turn on logging of `unhandled` messages.
    - Use the `Slf4jLogger`.
4. Run the `run` command to boot the `CoffeeHouseApp` and verify *"CoffeeHouse Open"* is logged.
5. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
6. Run the `next` command to move to and initialize the next exercise.
