# Exercise 008: Keeping Actors Busy

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will introduce a `Barista` actor who specializes in making our fine caffeinated beverages and will keep our other actors busy.

1. Create the `Barista` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Add a `prepareCoffeeDuration` parameter of type `FiniteDuration`.
    - Define a `Props` factory.
    - Create a `PrepareCoffee` message with parameters of `coffee` type `Coffee` and `guest` type `ActorRef`.
    - Create a `CoffeePrepared` message with parameters of `coffee` type `Coffee` and `guest` type `ActorRef`.
    - Define the behavior as when `PrepareCoffee(coffee, guest)` is received:
        - Busily prepare coffee for `prepareCoffeeDuration`.
        - Respond with `CoffeePrepared(coffee, guest)` to the sender.
        - *HINT*: Use `Thread.sleep` to simulate being busy.
        - *NOTE*: Never use `Thread.sleep` in production as it blocks.
2. Change `Waiter` as follows:
    - Add a reference to the `Barista` actor.
    - Instead of serving coffee immediately, defer to the `Barista` for preparation.
3. Change `CoffeeHouse` as follows:
    - Create a `private barista` actor with name *"barista"*.
    - Use a `createBarista` factory method.
    - For `prepareCoffeeDuration`, use a configuration value with key `coffee-house.barista.prepare-coffee-duration`.
4. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - Make sure the `Guest` actors are enjoying their coffee.
5. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
6. Run the `next` command to move to and initialize the next exercise.
