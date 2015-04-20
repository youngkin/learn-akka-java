# Exercise 009: Stop an Actor

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will limit the number of coffees a `Guest` consumes by setting a caffeine limit per `Guest`. When the `Guest` reaches their limit, we will stop the actor.

1. Change `CoffeeHouse` as follows:
    - Create an `ApproveCoffee` message with parameters of `coffee` type `Coffee` and `guest` type `ActorRef`.
    - Add a `caffeineLimit` parameter of type `Int`.
    - When creating the `Waiter` pass along `self` instead of `Barista`.
    - Add to the behavior, `ApproveCoffee` and look at the current `Guest` caffeineLimit as follows:
        - If less than `caffeineLimit`, send `PrepareCoffee` to the `Barista`.
        - Else log *"Sorry, {guest}, but you have reached your limit."* at `info` and stop the `Guest`.
        - Create an internal `guestCaffeineBookkeeper` that is a `ConcurrentHashMap` for tracking.
2. Change `Waiter` as follows:
    - Rename the `barista` parameter to `coffeeHouse`.
    - Change the behavior to reflect using `CoffeeHouse`.
3. Change `Guest` as follows:
    - Override the `postStop` hook to log *"Goodbye!"* at `info`.
4. Change `CoffeeHouseApp` as follows:
    - Get the caffeineLimit from configuration.
5. Run the `test` command to verify the solution works as expected.
6. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - Make sure the `Guest` actors are enjoying their coffee.
    - Make sure your `Guest` actors are stopped as expected.
7. QUIZ: Your implementation may have a hidden issue; see if you can find it!
8. Run the `next` command to advance to the next exercise.
