# Exercise 001: Implement an Actor

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will implement the `CoffeeHouse` actor with logging.

1. Create the `CoffeeHouse` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Extend `AbstractLoggingActor`.
    - Import `akka.japi.pf.ReceiveBuilder`.
    - Implement a constructor for `CoffeeHouse`.
    - In the constructor, define the initial behavior using `ReceiveBuilder`.
    - Handle any message by logging *"Coffee Brewing"* at `info`.
2. Run the `run` command to verify the main class `CoffeeHouseApp` boots up as expected.
3. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
4. Run the `next` command to move to and initialize the next exercise.
