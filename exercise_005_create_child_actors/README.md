# Exercise 005: Create Child Actor

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will use `CoffeeHouse` to create `Guest` as a child actor.

1. Create the `Guest` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Create a `static Props` factory for `Guest`.
    - Implement the behavior as *empty*.
    - *HINT*: `this::unhandled`.
2. Change `CoffeeHouse` as follows:
    - Add `CreateGuest` message as a `public final static class` that is an `Instance`.
    - Create a `private guest` actor without a *name*.
    - Use a `createGuest` factory method that creates a `Guest`.
    - Upon receiving the `CreateGuest` message call the factory method.
3. Change `CoffeeHouseApp` as follows:
    - Remove the anonymous actor.
    - When handling `Command.Guest`, send `CreateGuest.Instance` to `CoffeeHouse`.
    - Make sure you account for the creating of 1 or more `Guest` actors.
4. Turn on `lifecycle` debugging.
5. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - *HINT*: Enter `5 g` or `5 guest` to create five `Guest` actors. If you omit the count, `1` is used by default.
6. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
7. Run the `next` command to move to and initialize the next exercise.
