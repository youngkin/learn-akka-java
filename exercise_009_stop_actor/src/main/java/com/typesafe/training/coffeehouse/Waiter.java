/*
 * Copyright © 2014 Typesafe, Inc. All rights reserved.
 */

package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class Waiter extends AbstractLoggingActor{

    private ActorRef coffeeHouse;

    public Waiter(ActorRef aCoffeeHouse){
        this.coffeeHouse = aCoffeeHouse;

        receive(ReceiveBuilder.
                        match(ServeCoffee.class, serveCoffee ->
                                        coffeeHouse.tell(
                                                new CoffeeHouse.ApproveCoffee(
                                                        serveCoffee.coffee,
                                                        serveCoffee.guest),
                                                self())
                        ).
                        match(Barista.CoffeePrepared.class, coffeePrepared ->
                                        coffeePrepared.guest.tell(new CoffeeServed(coffeePrepared.coffee), self())
                        ).
                        matchAny(this::unhandled).build()
        );
    }

    public static Props props(ActorRef coffeeHouse){
        return Props.create(Waiter.class, () -> new Waiter(coffeeHouse));
    }

    public static final class ServeCoffee{

        public final Coffee coffee;
        public final ActorRef guest;

        public ServeCoffee(final Coffee aCoffee, final ActorRef aGuest){
            checkNotNull(aCoffee, "Coffee cannot be null");
            coffee = aCoffee;
            guest = aGuest;
        }

        @Override
        public String toString(){
            return "ServeCoffee{coffee=" + coffee + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof ServeCoffee) {
                ServeCoffee that = (ServeCoffee) o;
                return this.coffee.equals(that.coffee);
            }
            return false;
        }

        @Override
        public int hashCode(){
            int h = 1;
            h *= 1000003;
            h ^= coffee.hashCode();
            return h;
        }
    }

    public static final class CoffeeServed{

        public final Coffee coffee;

        public CoffeeServed(final Coffee coffee){
            checkNotNull(coffee, "Coffee cannot be null");
            this.coffee = coffee;
        }

        @Override
        public String toString(){
            return "CoffeeServed{coffee=" + coffee + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof CoffeeServed) {
                CoffeeServed that = (CoffeeServed) o;
                return this.coffee.equals(that.coffee);
            }
            return false;
        }

        @Override
        public int hashCode(){
            int h = 1;
            h *= 1000003;
            h ^= coffee.hashCode();
            return h;
        }
    }
}
