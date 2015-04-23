/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

/**
 * As a rule of thumb, everything that can be made immutable should be made immutable. Only allow
 * mutable state where absolutely necessary. Even then, always make copies of any mutable fields
 * before returning them via accessors.
 */
package com.typesafe.training.coffeehouse;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.FromConfig;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * AbstractLoggingActors have the built-in ability to log by simply using the log() function.
 * NOTE: extending this class prevents the extension of other classes such as the AbstractActorWithStash.
 */
public class CoffeeHouse extends AbstractLoggingActor{

    /* baristaPrepareCoffeeDuration, guestFinishCoffeeDuration, accuracy, and
     * maxComplaintCount are examples of obtaining config information from
     * the resources/application.config file.
     */
    private final FiniteDuration baristaPrepareCoffeeDuration =
        Duration.create(
            context().system().settings().config().getDuration(
                "coffee-house.barista.prepare-coffee-duration", MILLISECONDS), MILLISECONDS);

    private final FiniteDuration guestFinishCoffeeDuration =
        Duration.create(
            context().system().settings().config().getDuration(
                "coffee-house.guest.finish-coffee-duration", MILLISECONDS), MILLISECONDS);

    private final int accuracy =
            context().system().settings().config().getInt("coffee-house.barista.accuracy");

    private final int maxComplaintCount =
            context().system().settings().config().getInt("coffee-house.waiter.max-complaint-count");

    /**
     *  barista & waiter are examples of supervised actors.
     */
    private final ActorRef barista =
        createBarista();

    private final ActorRef waiter =
        createWaiter();

    /**
     * Used to keep track of current guests and their associated caffeine intake.
     */
    private final Map<ActorRef, Integer> guestCaffeineBookkeeper = new ConcurrentHashMap<>();

    private final int caffeineLimit;

    /**
     * Defines the strategies used to manage failures in CoffeeHouse's various
     * child actors.
     */
    private final SupervisorStrategy strategy =
            new OneForOneStrategy(false, DeciderBuilder.
                    /**
                     * Stops the Guest actor from drinking too much caffeine.
                     */
                    match(Guest.CaffeineException.class, e -> SupervisorStrategy.stop()).
                    /**
                     * This is an example of the supervisor taking an
                     * explicit alternative action based on the failure of a
                     * child actor. In this case it steps in for the failed
                     * actor, the Waiter, to continue processing a guest's
                     * order for a coffee.
                     */
                    match(Waiter.FrustratedException.class,
                            (Waiter.FrustratedException e) -> {
                                barista.tell(new Barista.PrepareCoffee(e.coffee, e.guest),
                                        sender());
                                return SupervisorStrategy.restart();
                            }).
                    matchAny(e -> SupervisorStrategy.restart()).
                    build()
            );

    /**
     * Accessor method for the supervision strategy.
     * @return SupervisorStrategy
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    public CoffeeHouse(int caffeineLimit){
        log().debug("CoffeeHouse Open");
        this.caffeineLimit = caffeineLimit;

        /**
         * Defines the various actions to take for a given message. Except for the 2nd match clause,
         * which takes a predicate that further restricts the match, the first parameter is the type
         * of the message and the 2nd (or 3rd) parameter is a function that implements the desired
         * behavior.
         */
        receive(ReceiveBuilder.
                match(CreateGuest.class, createGuest -> {
                    final ActorRef guest = createGuest(createGuest.favoriteCoffee, createGuest.caffeineLimit);
                    addGuestToBookkeeper(guest);
                    context().watch(guest);
                }).
                /**
                 * I don't understand this::coffeeApproved. It looks like a reference to a function
                 * that takes a parameter of type ApproveCoffe.
                 */
                match(ApproveCoffee.class, this::coffeeApproved, approveCoffee ->
                        barista.forward(new Barista.PrepareCoffee(approveCoffee.coffee, approveCoffee.guest), context())
                ).
                match(ApproveCoffee.class, approveCoffee -> {
                    log().info("Sorry, {}, but you have reached your limit.", approveCoffee.guest.path().name());
                    context().stop(approveCoffee.guest);
                }).
                /**
                 * Received from the akka actor system when an actor gets
                 * terminated. In this case it's assumed that the terminated
                 * actor is a Guest.
                 */
                match(Terminated.class, terminated -> {
                    log().info("Thanks, {}, for being our guest!", terminated.getActor());
                    removeGuestFromBookkeeper(terminated.getActor());
                }).
                /**
                 * "unhandled" results in the message being sent to the dead-letter queue.
                 */
                matchAny(this::unhandled).build()
        );
    }

    /**
     * Used, in conjunction with ActorSystem.actorOf, as a factory method to
     * create a new instance of CoffeeHouse.
     *
     * @param caffeineLimit A constructor parameter used to specify how much
     *                      caffeine a Guest is allowed to consume.
     * @return Props used as input to ActorSystem.actorOf(...)
     */
    public static Props props(int caffeineLimit){
        return Props.create(CoffeeHouse.class, () -> new CoffeeHouse(caffeineLimit));
    }

    // ------------------------------------------------------------------------------------------ //
    // --------- [Helper methods that implement the basic behavior of the class.] --------------- //
    // ------------------------------------------------------------------------------------------ //
    private boolean coffeeApproved(ApproveCoffee approveCoffee){
        final int guestCaffeineCount = guestCaffeineBookkeeper.get(approveCoffee.guest);
        if (guestCaffeineCount < caffeineLimit) {
            guestCaffeineBookkeeper.put(approveCoffee.guest, guestCaffeineCount + 1);
            return true;
        }
        return false;
    }

    private void addGuestToBookkeeper(ActorRef guest){
        guestCaffeineBookkeeper.put(guest, 0);
        log().debug("Guest {} added to bookkeeper", guest);
    }

    private void removeGuestFromBookkeeper(ActorRef guest){
        guestCaffeineBookkeeper.remove(guest);
        log().debug("Removed guest {} from bookkeeper", guest);
    }



    // ------------------------------------------------------------------------------------------ //
    // ------- [Factory methods that create the various actors supervised by this class] -------- //
    // ------------------------------------------------------------------------------------------ //
    protected ActorRef createBarista(){
        return context().actorOf(FromConfig.getInstance().
                props(Barista.props(baristaPrepareCoffeeDuration,
                        accuracy)), "barista");
    }

    protected ActorRef createWaiter(){
        return context().actorOf(Waiter.props(self(), barista, maxComplaintCount), "waiter");
    }

    protected ActorRef createGuest(Coffee favoriteCoffee, int caffeineLimit){
        return context().actorOf(Guest.props(waiter, favoriteCoffee, guestFinishCoffeeDuration, caffeineLimit));
    }



    // ------------------------------- [Message definitions] ------------------------------------ //
    //
    // Message definitions must be immutable AND contain toString(), equals(), and hashCode()     //
    //                                                                                            //
    // ------------------------------------------------------------------------------------------ //
    public static final class CreateGuest{

        public final Coffee favoriteCoffee;

        public final int caffeineLimit;

        public CreateGuest(final Coffee favoriteCoffee, final int caffeineLimit){
            checkNotNull(favoriteCoffee, "Favorite coffee cannot be null");
            this.favoriteCoffee = favoriteCoffee;
            this.caffeineLimit = caffeineLimit;
        }

        @Override
        public String toString(){
            return "CreateGuest{"
                + "favoriteCoffee=" + favoriteCoffee + ", "
                + "caffeineLimit=" + caffeineLimit + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof CreateGuest) {
                CreateGuest that = (CreateGuest) o;
                return (this.favoriteCoffee.equals(that.favoriteCoffee))
                    && (this.caffeineLimit == that.caffeineLimit);
            }
            return false;
        }

        @Override
        public int hashCode(){
            int h = 1;
            h *= 1000003;
            h ^= favoriteCoffee.hashCode();
            h *= 1000003;
            h ^= caffeineLimit;
            return h;
        }
    }

    public static final class ApproveCoffee{

        public final Coffee coffee;

        public final ActorRef guest;

        public ApproveCoffee(final Coffee coffee, final ActorRef guest){
            checkNotNull(coffee, "Coffee cannot be null");
            checkNotNull(guest, "Guest cannot be null");
            this.coffee = coffee;
            this.guest = guest;
        }

        @Override
        public String toString(){
            return "ApproveCoffee{"
                + "coffee=" + coffee + ", "
                + "guest=" + guest + "}";
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (o instanceof ApproveCoffee) {
                ApproveCoffee that = (ApproveCoffee) o;
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
