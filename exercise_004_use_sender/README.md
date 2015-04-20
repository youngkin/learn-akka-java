# Exercise 004: Use the Sender

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will use the `implicit` sender to respond from `CoffeeHouse`.

1. Change `CoffeeHouse` as follows:
    - Instead of logging, respond to the `sender()` with a *"Coffee Brewing"* message.
2. In `CoffeeHouseApp` create an anonymous actor:
    - Use `static Props` to create the actor.
    - Extend `AbstractLoggingActor`.
    - In the constructor send `CoffeeHouse` a *"Brew Coffee"* message.
    - In the constructor, define the behavior using `ReceiveBuilder`.
    - Upon receiving a message, log the message at `info`.
3. Run the `test` command to verify the solution works as expected.
4. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged.
    - *"Coffee Brewing"* is logged.
5. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
6. Run the `next` command to move to and initialize the next exercise.
