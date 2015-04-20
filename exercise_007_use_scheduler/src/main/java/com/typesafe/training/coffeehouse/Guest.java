package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import scala.concurrent.duration.FiniteDuration;


/**
 * Created by uyounri on 4/16/15.
 */
public class Guest extends AbstractLoggingActor {

    private final ActorRef waiter;
    private final Coffee   favoriteCoffee;
    private int coffeeCount = 0;

    private final FiniteDuration finishCoffeeDuration;

    public static Props props(final ActorRef waiter, final Coffee favoriteCoffee, FiniteDuration guestFinishCoffeeDuration){
        return Props.create(Guest.class, () -> new Guest(waiter,
                favoriteCoffee, guestFinishCoffeeDuration));
    }

    public Guest(ActorRef waiter, Coffee favoriteCoffee, FiniteDuration
            guestFinishCoffeeDuration) {
        this.waiter = waiter;
        this.favoriteCoffee = favoriteCoffee;
        this.finishCoffeeDuration = guestFinishCoffeeDuration;

        this.waiter.tell(
                new Waiter.ServeCoffee(this.favoriteCoffee), self() );

        receive(ReceiveBuilder.
                        match(Waiter.CoffeeServed.class, coffee ->
                        { coffeeCount++;
                            log().info("Enjoying my {} yummy " +
                                    "{}!", coffeeCount, coffee.coffee);
                            scheduleCoffeeFinished();
                        }).
                        match(CoffeeFinished.class, coffeeFinished ->
                            this.waiter.tell(new Waiter.ServeCoffee
                                    (this.favoriteCoffee), self() )).
                        matchAny(this::unhandled).build()
        );
    }

    public static final class CoffeeFinished {
        public static final CoffeeFinished Instance =
                new CoffeeFinished();

        private CoffeeFinished(){
        }

    }

    private void scheduleCoffeeFinished() {
        context().system().scheduler().scheduleOnce(finishCoffeeDuration,
                self(),CoffeeFinished.Instance, context().dispatcher(), self());
    }
}
