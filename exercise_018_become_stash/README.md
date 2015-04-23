# Exercise 018: Changing Actor Behavior

## Overview
The major classes that comprise this application are:

1. **CoffeeHouseApp** - The main entry point for the application. It provides the bootstrapping necessary to get the application into a running state.
2. **CoffeeHouse** - This is the main controller for the entire application. It creates and controls all the other actors in the application.
3. **Barista** - Implements the behavior associated with a coffee house barista who is responsible for making the coffee requested by guests.
4. **Guest** - Implements the behavior associated with Guests, or customers, who consume the coffee offerings of the Coffee House.
5. **Waiter** - Implements the behavior associated with a Waiter that accepts orders from Guests, forwards those orders to the Barista, and serves the completed coffee drink to the guest.

The **Barista** is only human, and as such, makes mistakes from time to time. Mistakes result in the **Guest** getting a coffee drink that they didn't order. When this happens the **Guest** complains and eventually gets the coffee drink that they actually ordered. 

The **CoffeeHouse** is a place where **Guests** congregate, **Waiters** and **Baristas** are employees, and implements the policies of the business (e.g., **Guests** can only consume so much coffee/caffeine).

## Eclipse Reminder (Only if your IDE is Eclipse based)

If you are using *Eclipse* as your IDE, you will need to import this exercise. **Attention**: It is **crucial** that you **complete this exercise** before running `next`.

When importing an exercise into *Eclipse* make sure you always do the following:

1. Close *all open tabs* in Eclipse.
2. Collapse the *current and completed* exercise project folder.
3. Select `File > Import` to run the `Import Wizard` and import the next exercise.

## Instructions

In this exercise, we will demonstrate through the use of `become` and `stash` to modify actor

1. Re-implement the `Barista` actors behavior as a finite state machine:
    - Do not use the `busy` method.
    - Use `become`, `stash` and the `scheduler`.
2. Run the `run` command to boot the `CoffeeHouseApp`, create some `Guest` actors and verify everything works as expected.
3. Run the `test` command to verify the solution works as expected.
    - *Remember* to make sure all tests for this exercise pass before running `next`.
4. Run the `next` command to move to and initialize the next exercise.
