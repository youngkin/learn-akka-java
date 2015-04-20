# Exercise 019: Use the Ask Pattern

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

This exercise will demonstrate how to interact with an actor from the outside by way of the `ask` pattern.

1. Change `CoffeeHouse` as follows:
    - Add the `GetStatus` case object.
    - Add the `Status` case class that extends `Msg` and has a `guestCount` parameter of type `Int`.
    - On receiving `GetStatus` respond with `Status` initialized with the current number of `Guest` actors.
2. Change `CoffeeHouseApp` as follows:
    - Handle a `StatusCommand` by asking `CoffeeHouse` to get the status.
    - Register callbacks logging the number of `Guest` actors at `info` and any failure at `error`.
    - For the `ask` timeout, use a configuration value with key `coffee-house.status-timeout`.
3. Run the `run` command to boot the `CoffeeHouseApp`, create some `Guest` actors and verify everything works as expected.
4. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
5. Run the `next` command to move to and initialize the next exercise.
