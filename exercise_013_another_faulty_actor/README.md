# Exercise 013: Another Faulty Actor

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will introduce another faulty actor in the form of our `Barista` where sometimes they make the wrong coffee. When this happens, the `Guest` will complain and reorder. If the `Waiter` receives too many complaints, he will become frustrated.

1. Change the `Barista` as follows:
    - Add an `accuracy` parameter of type `Int` expressing a percentage.
    - Get a random `Int` value less than 100.
    - If the random `Int` is less than `accuracy`, prepare the correct `Coffee` otherwise prepare a wrong one.
2. Change the `Waiter` as follows:
    - Add a `Complaint` message with parameter `coffee` of type `Coffee`.
    - Add a `FrustratedException` case object extending `IllegalStateException` to the companion object.
    - Add a `barista` parameter of type `ActorRef` and a `maxComplaintCount` parameter of type `Int`.
    - Keep track of the number of `Complaint` messages received.
    - If more `Complaint` messages arrive than the `maxComplaintCount`, throw a `FrustratedException`, else send `PrepareCoffee` to the `Barista`.
3. Change the `Guest` as follows:
    - On receiving the wrong `Coffee`, send a `Complaint` to the `Waiter`.
    - Log at `info` when receiving the wrong `Coffee` (ie. *"Expected a {}, but got a {}!"*)
    - Which argument needs to be given for `coffee`?
4. Change `CoffeeHouse` as follows:
    - For `accuracy` use a configuration value with key `coffee-house.barista.accuracy`.
    - For `maxComplaintCount` use a configuration value with key `coffee-house.waiter.max-complaint-count`.
    - Don't forget to use the new parameters when creating the `Barista` and `Waiter`.
5. Run the `run` command to boot the `CoffeeHouseApp` and verify:
    - Create a `Guest` and see what happens when the `Waiter` gets frustrated.
    - *HINT*: You might need to use small `accuracy` and `maxComplainCount` values.
6. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
7. Run the `next` command to move to and initialize the next exercise.
