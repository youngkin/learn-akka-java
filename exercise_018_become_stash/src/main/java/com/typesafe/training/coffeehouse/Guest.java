/*
 * Copyright © 2014 Typesafe, Inc. All rights reserved.
 */

package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.FiniteDuration;

/**
 * AbstractLoggingActors have the built-in ability to log by simply using the log() function.
 * NOTE: extending this class prevents the extension of other classes such as the AbstractActorWithStash.
 */
public class Guest extends AbstractLoggingActor{

    private final ActorRef waiter;

    private final Coffee favoriteCoffee;

    private final FiniteDuration finishCoffeeDuration;

    private final int caffeineLimit;

    private int coffeeCount = 0;

    public Guest(ActorRef waiter, Coffee favoriteCoffee, FiniteDuration finishCoffeeDuration, int caffeineLimit){
        this.waiter = waiter;
        this.favoriteCoffee = favoriteCoffee;
        this.finishCoffeeDuration = finishCoffeeDuration;
        this.caffeineLimit = caffeineLimit;
        orderFavoriteCoffee();

        receive(ReceiveBuilder.
                match(Waiter.CoffeeServed.class, coffeeServed -> {
                    if (! coffeeServed.coffee.equals(favoriteCoffee)) {
                        log().info("Expected a {}, but got a {}!",
                                favoriteCoffee, coffeeServed.coffee);
                        waiter.tell(new Waiter.Complaint(favoriteCoffee), self());
                    }
                    else {
                        coffeeCount++;
                        log().info("Enjoying my {} yummy {}!", coffeeCount, coffeeServed.coffee);
                        scheduleCoffeeFinished();
                    }
                }).
                match(CoffeeFinished.class, coffeeFinished -> coffeeCount > this.caffeineLimit, coffeeFinished -> {
                    throw new CaffeineException();
                }).
                match(CoffeeFinished.class, coffeeFinished ->
                        orderFavoriteCoffee()
                ).
                matchAny(this::unhandled).build()
        );
    }

    public static Props props(final ActorRef waiter, final Coffee favoriteCoffee,
        final FiniteDuration finishCoffeeDuration, final int caffeineLimit){
        return Props.create(Guest.class,
            () -> new Guest(waiter, favoriteCoffee, finishCoffeeDuration, caffeineLimit));
    }

    // ------------------------------------------------------------------------------------------ //
    // --------- [Helper methods that implement the basic behavior of the class.] --------------- //
    // ------------------------------------------------------------------------------------------ //

    private void scheduleCoffeeFinished(){
        context().system().scheduler().scheduleOnce(finishCoffeeDuration, self(),
                CoffeeFinished.Instance, context().dispatcher(), self());
    }

    private void orderFavoriteCoffee(){
        waiter.tell(new Waiter.ServeCoffee(favoriteCoffee), self());
    }


    // ------------------------------------------------------------------------------------------ //
    // ---------------------------- [Actor lifecycle methods] ----------------------------------- //
    // ------------------------------------------------------------------------------------------ //

    @Override
    public void postStop(){
        log().info("Goodbye!");
    }

    public static final class CaffeineException extends IllegalStateException{
        static final long serialVersionUID = 1;

        public CaffeineException(){
            super("Too much caffeine!");
        }
    }

    // ------------------------------- [Message definitions] ------------------------------------ //
    //
    // Message definitions must be immutable AND contain toString(), equals(), and hashCode()     //
    //                                                                                            //
    // ------------------------------------------------------------------------------------------ //

    public static final class CoffeeFinished{

        public static final CoffeeFinished Instance = new CoffeeFinished();

        private CoffeeFinished(){
        }
    }
}
