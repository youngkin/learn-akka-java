/*
 * Copyright © 2014 Typesafe, Inc. All rights reserved.
 */

package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * AbstractLoggingActors have the built-in ability to log by simply using the log() function.
 * NOTE: extending this class prevents the extension of other classes such as the AbstractActorWithStash.
 */
public class Waiter extends AbstractLoggingActor{

    private final ActorRef barista;
    private final int maxComplaints;
    private ActorRef coffeeHouse;
    private int complaintCount = 0;

    public Waiter(ActorRef coffeeHouse, ActorRef aBarista, int aMaxComplaints){
        this.coffeeHouse = coffeeHouse;
        barista = aBarista;
        maxComplaints = aMaxComplaints;

        receive(ReceiveBuilder.
                match(ServeCoffee.class, serveCoffee ->
                        this.coffeeHouse.tell(new CoffeeHouse.ApproveCoffee(serveCoffee.coffee, sender()), self())
                ).
                match(Complaint.class, complaint -> {
                    complaintCount++;
                    if (complaintCount > maxComplaints) {
                        throw new FrustratedException(complaint.coffee, sender());
                    }
                    else {
                        barista.tell(new Barista.PrepareCoffee(
                                complaint.coffee, sender()), self());
                    }
                }).
                match(Barista.CoffeePrepared.class, coffeePrepared ->
                                coffeePrepared.guest.tell(new CoffeeServed(coffeePrepared.coffee), self())
                ).
                matchAny(this::unhandled).build()
        );
    }

    public static Props props(ActorRef coffeeHouse, ActorRef barista, int maxComplaints){
        return Props.create(Waiter.class, () -> new Waiter(coffeeHouse,
                barista, maxComplaints));
    }

    // ------------------------------- [Message definitions] ------------------------------------ //
    //
    // Message definitions must be immutable AND contain toString(), equals(), and hashCode()     //
    //                                                                                            //
    // ------------------------------------------------------------------------------------------ //

    public static final class ServeCoffee{

        public final Coffee coffee;

        public ServeCoffee(final Coffee coffee){
            checkNotNull(coffee, "Coffee cannot be null");
            this.coffee = coffee;
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

    public static final class Complaint {
        public final Coffee coffee;

        public Complaint(final Coffee aCoffee) {
            checkNotNull(aCoffee);
            coffee = aCoffee;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Complaint complaint = (Complaint) o;

            return coffee.equals(complaint.coffee);

        }

        @Override
        public int hashCode() {
            return coffee.hashCode();
        }

        @Override
        public String toString() {
            return "Complaint{" +
                    "coffee=" + coffee +
                    '}';
        }
    }

    public static final class FrustratedException extends
            IllegalStateException {

        public final Coffee coffee;
        public final ActorRef guest;

        private FrustratedException() {
            super();
            coffee = null;
            guest = null;
        }

        public FrustratedException(Coffee aCoffee, ActorRef aGuest) {
            super();
            coffee = aCoffee;
            guest = aGuest;
        }

    }
}
