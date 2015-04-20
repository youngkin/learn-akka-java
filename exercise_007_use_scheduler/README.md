# Exercise 007: Use Scheduler

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will implement the Akka `Scheduler` to simulate the `Guest` ordering more `Coffee`.

1. Change `Guest` as follows:
    - Add a `finishCoffeeDuration` parameter of type `scala.concurrent.duration.FiniteDuration`.
    - Change the behavior on receiving `CoffeeServed` to schedule the sending of `CoffeeFinished` to the `Guest`.
    - QUIZ: What other changes are needed for the guest to enjoy their coffee?
2. Change `CoffeeHouse` as follows:
    - Adjust the code for creating a new `Guest`.
    - For `finishCoffeeDuration`, use a configuration value with key `coffee-house.guest.finish-coffee-duration`.
    - To get the configuration value, use the `getDuration` method on `context.system.settings.config`.
3. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - Make sure the `Guest` actors are enjoying their coffee.
    - *HINT*: Enter g {coffee} or guest {coffee} where {coffee} has to be the first letter of one of the defined coffees (`a`, `m`, or `c`). If you omit {coffee}, `Akkaccino` will be used by default.
4. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
5. Run the `next` command to move to and initialize the next exercise.
