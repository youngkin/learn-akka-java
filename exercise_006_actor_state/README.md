# Exercise 006: Actor State

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will implement state by tracking a `Guest` actor's favorite `Coffee`.

1. Create the `Waiter` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Define a `static Props` factory.
    - Create a `ServeCoffee` message with parameter `coffee` of type `Coffee`.
    - Create a `CoffeeServed` message with parameter `coffee` of type `Coffee`.
    - Define the behavior as when `ServeCoffee(coffee)` is received, respond with `CoffeeServed(coffee)` to the sender.
2. Change `Guest` as follows:
    - Create a `CoffeeFinished.Instance` message.
    - Add a `waiter` parameter of type `ActorRef`.
    - Add a `favoriteCoffee` parameter of type `Coffee`.
    - Add a local mutable `coffeeCount` field of type `Int`.
    - Define the behavior as:
        - When `CoffeeServed(coffee)` is received:
            - Increase the `coffeeCount` by one.
            - Log *"Enjoying my {coffeeCount} yummy {coffee}!"* at `info`.
        - When `CoffeeFinished` is received, respond with `ServeCoffee(favoriteCoffee)` to `waiter`.
3. Change `CoffeeHouse` as follows:
    - Create a `private waiter` actor with name *"waiter"*.
    - Use a `createWaiter` factory method.
    - Add `favoriteCoffee` parameter of type `Coffee` to the `CreateGuest` message.
    - Update the `createGuest` factory method to account for the `waiter` and `favoriteCoffee` parameters.
4. Change `CoffeeHouseApp` to account for the `favoriteCoffee` parameter required by the `CreateGuest` message.
5. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - *HINT*: Enter g {coffee} or guest {coffee} where {coffee} has to be the first letter of one of the defined coffees (`a`, `m`, or `c`). If you omit {coffee}, `Akkaccino` will be used by default.
6. QUIZ: Why don't you see any log messages from `Guest` actors enjoying their coffee?
7. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
8. Run the `next` command to move to and initialize the next exercise.
