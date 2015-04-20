/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CoffeeHouse extends AbstractLoggingActor{

    private final FiniteDuration baristaPrepareCoffeeDuration =
        Duration.create(
            context().system().settings().config().getDuration(
                "coffee-house.barista.prepare-coffee-duration", MILLISECONDS), MILLISECONDS);

    private final FiniteDuration guestFinishCoffeeDuration =
        Duration.create(
            context().system().settings().config().getDuration(
                "coffee-house.guest.finish-coffee-duration", MILLISECONDS), MILLISECONDS);

    private final ActorRef barista =
        createBarista();

    private final ActorRef waiter =
        createWaiter();

    private final Integer caffeineLimit;

    private ConcurrentHashMap<ActorRef, Integer> guests= new
            ConcurrentHashMap<>();

    public CoffeeHouse(Integer aCaffeineLimit){
        log().debug("CoffeeHouse Open");

        caffeineLimit = aCaffeineLimit;

        receive(ReceiveBuilder.
                    match(CreateGuest.class, createGuest -> {
                                ActorRef guest = createGuest(createGuest
                                        .favoriteCoffee);
                                guests.put(guest, 0);
                            }
                    ).
                    match(ApproveCoffee.class, approveCoffee -> {
                                ActorRef guest = approveCoffee.guest;
                                Integer guestCaffeineLimit = guests.get(guest);
                                if (guestCaffeineLimit < caffeineLimit) {
                                    barista.forward(new Barista.PrepareCoffee
                                            (approveCoffee.coffee,
                                                    approveCoffee.guest), context());
                                    guests.put(guest, guestCaffeineLimit + 1);
                                }
                                else {
                                    log().info("Sorry, {}, but you have " +
                                            "reached your limit.", guest);
                                    guests.remove(guest);
                                    context().stop(guest);
                                }
                            }
                    ).
                        matchAny(this::unhandled).build()
        );
    }

    public static Props props(final Integer caffeineLimit){
        return Props.create(CoffeeHouse.class, () ->
                new CoffeeHouse(caffeineLimit) );
    }

    protected ActorRef createBarista(){
        return context().actorOf(Barista.props(baristaPrepareCoffeeDuration), "barista");
    }

    protected ActorRef createWaiter(){
        return context().actorOf(Waiter.props(self()), "waiter");
    }

    protected ActorRef createGuest(Coffee favoriteCoffee){
        ActorRef newGuest = context().actorOf(Guest.props(waiter,
                favoriteCoffee,
                guestFinishCoffeeDuration));
        return newGuest;
    }

    public static final class CreateGuest{

        public final Coffee favoriteCoffee;

        public CreateGuest(final Coffee favoriteCoffee){
            checkNotNull(favoriteCoffee, "Favorite coffee cannot be null");
            this.favoriteCoffee = favoriteCoffee;
        }

        @Override
        public String toString(){
            return "CreateGuest{favoriteCoffee=" + favoriteCoffee + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof CreateGuest) {
                CreateGuest that = (CreateGuest) o;
                return this.favoriteCoffee.equals(that.favoriteCoffee);
            }
            return false;
        }

        @Override
        public int hashCode(){
            int h = 1;
            h *= 1000003;
            h ^= favoriteCoffee.hashCode();
            return h;
        }
    }

    public static final class ApproveCoffee {

        public final Coffee coffee;
        public final ActorRef guest;

        public ApproveCoffee(final Coffee aCoffee, ActorRef aWaiter) {
            checkNotNull(aCoffee, "Coffee cannot be null");
            checkNotNull(aWaiter, "aWaiter cannot be null");
            coffee = aCoffee;
            guest = aWaiter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ApproveCoffee that = (ApproveCoffee) o;

            if (!coffee.equals(that.coffee)) return false;
            return guest.equals(that.guest);

        }

        @Override
        public int hashCode() {
            int result = coffee.hashCode();
            result = 31 * result + guest.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "ApproveCoffee{" +
                    "coffee=" + coffee +
                    ", guest=" + guest +
                    '}';
        }
    }
}
