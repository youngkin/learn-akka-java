/*
 * Copyright © 2014 Typesafe, Inc. All rights reserved.
 */

/**
 * Barista models the behavior of a real barista in a coffee shop - i.e., it makes coffee that takes
 * some time to make and it's busy while making coffee.
 */

package com.typesafe.training.coffeehouse;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * AbstractActorWithStash allows messages associated with a prior state to be stashed for later use
 * when changing behavior via "become". The ready() and busy() methods implement the behavior for the
 * two supported states a Barista can occupy.
 */
public class Barista extends AbstractActorWithStash {

    /** How long does it take to make a cup of coffee in seconds */
    private final FiniteDuration prepareCoffeeDuration;

    /** How accurate is a Barista in making the coffee requested by the customer. Expressed as a
     * number between 1 and 100 that represents the percentage of the time the Barista makes what
     * was requested by the customer.
     */
    private final int accuracy;

    /**
     * Used, in conjunction with ActorSystem.actorOf, as a factory method to
     * create a new instance of Barista with the desired accuracy and speed in making coffee.
     *
     * @return Props used as input to ActorSystem.actorOf(...)
     */
    public static Props props(FiniteDuration prepareCoffeeDuration, int accuracy){
        return Props.create(Barista.class, () ->
                new Barista(prepareCoffeeDuration, accuracy));
    }


    public Barista(FiniteDuration prepareCoffeeDuration, int aAccuracy){
        this.prepareCoffeeDuration = prepareCoffeeDuration;
        accuracy = aAccuracy;

        // Barista's start work ready to serve customers
        receive(ready());
    }

    // ------------------------------------------------------------------------------------------ //
    // ------------- [Helper methods that implement the 2 states of a Barista] ------------------ //
    // ------------------------------------------------------------------------------------------ //
    private PartialFunction<Object, BoxedUnit> ready(){
        return ReceiveBuilder.
                match(PrepareCoffee.class, prepareCoffee -> {
                    scheduleCoffeePrepared(prepareCoffee.coffee,
                            prepareCoffee.guest);
                    context().become(busy(sender()));
                }).
                matchAny(this::unhandled).build();
    }

    private PartialFunction<Object, BoxedUnit> busy(ActorRef waiter){
        return ReceiveBuilder.
                match(CoffeePrepared.class, coffeePrepared -> {
                    waiter.tell(coffeePrepared, self());
                    unstashAll();
                    context().become(ready());
                }).
                matchAny(msg -> stash()).build();
    }

    // ------------------------------------------------------------------------------------------ //
    // --------- [Helper methods that implement the basic behavior of the class.] --------------- //
    // ------------------------------------------------------------------------------------------ //

    private void scheduleCoffeePrepared(Coffee coffee, ActorRef guest){
        context().system().scheduler().scheduleOnce(prepareCoffeeDuration, self(),
                new CoffeePrepared(pickCoffee(coffee), guest), context().dispatcher(), self());
    }

    private Coffee pickCoffee(Coffee coffee){
        return new Random().nextInt(100) < accuracy ? coffee : Coffee.orderOther(coffee);
    }

    // ------------------------------- [Message definitions] ------------------------------------ //
    //
    // Message definitions must be immutable AND contain toString(), equals(), and hashCode()     //
    //                                                                                            //
    // ------------------------------------------------------------------------------------------ //

    public static final class PrepareCoffee{

        public final Coffee coffee;

        public final ActorRef guest;

        public PrepareCoffee(final Coffee coffee, final ActorRef guest){
            checkNotNull(coffee, "Coffee cannot be null");
            checkNotNull(guest, "Guest cannot be null");
            this.coffee = coffee;
            this.guest = guest;
        }

        @Override
        public String toString(){
            return "PrepareCoffee{"
                    + "coffee=" + coffee + ", "
                    + "guest=" + guest + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof PrepareCoffee) {
                PrepareCoffee that = (PrepareCoffee) o;
                return (this.coffee.equals(that.coffee))
                        && (this.guest.equals(that.guest));
            }
            return false;
        }

        @Override
        public int hashCode(){
            int h = 1;
            h *= 1000003;
            h ^= coffee.hashCode();
            h *= 1000003;
            h ^= guest.hashCode();
            return h;
        }
    }

    public static final class CoffeePrepared{

        public final Coffee coffee;

        public final ActorRef guest;

        public CoffeePrepared(final Coffee coffee, final ActorRef guest){
            checkNotNull(coffee, "Coffee cannot be null");
            checkNotNull(guest, "Guest cannot be null");
            this.coffee = coffee;
            this.guest = guest;
        }

        @Override
        public String toString(){
            return "CoffeePrepared{"
                    + "coffee=" + coffee + ", "
                    + "guest=" + guest + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof CoffeePrepared) {
                CoffeePrepared that = (CoffeePrepared) o;
                return (this.coffee.equals(that.coffee))
                        && (this.guest.equals(that.guest));
            }
            return false;
        }

        @Override
        public int hashCode(){
            int h = 1;
            h *= 1000003;
            h ^= coffee.hashCode();
            h *= 1000003;
            h ^= guest.hashCode();
            return h;
        }
    }

}
