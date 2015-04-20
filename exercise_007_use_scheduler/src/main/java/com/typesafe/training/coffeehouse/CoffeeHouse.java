package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by uyounri on 4/16/15.
 *
 * xxxx
 */


public class CoffeeHouse extends AbstractLoggingActor {

    private final ActorRef waiter;

    private final FiniteDuration guestFinishCoffeeDuration =
            Duration.create(context().system().settings().config()
                    .getDuration("coffee-house.guest.finish-coffee-duration",
                            TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);

    public static Props props(){
        return Props.create(CoffeeHouse.class, CoffeeHouse::new);
    }

    public CoffeeHouse() {
        log().debug("Coffee House Open");
        waiter = createWaiter();

        receive(ReceiveBuilder.
                        match(CreateGuest.class, createGuest ->
                                createGuest(createGuest.favoriteCoffee, guestFinishCoffeeDuration)).
                        matchAny(this::unhandled).build()
        );
    }

    private ActorRef createWaiter() {
        return context().actorOf(Waiter.props(), "waiter");
    }

    private ActorRef createGuest(Coffee favoriteCoffee, FiniteDuration guestFinishCoffeeDuration) {
        return context().actorOf(Guest.props(waiter, favoriteCoffee, guestFinishCoffeeDuration));
    }

    public static final class CreateGuest{

        final public Coffee favoriteCoffee;

        public CreateGuest(final Coffee coffee){
            favoriteCoffee = coffee;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CreateGuest that = (CreateGuest) o;

            return favoriteCoffee.equals(that.favoriteCoffee);

        }

        @Override
        public int hashCode() {
            return favoriteCoffee.hashCode();
        }

        @Override
        public String toString() {
            return "CreateGuest{" +
                    "favoriteCoffee=" + favoriteCoffee +
                    '}';
        }
    }

}
