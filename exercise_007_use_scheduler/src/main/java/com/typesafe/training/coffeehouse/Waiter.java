package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by uyounri on 4/16/15.
 *
 * Get rid of warning messages about use of default template!!!
 */
public class Waiter extends AbstractLoggingActor {
    public static Props props(){
        return Props.create(Waiter.class, Waiter::new);
    }

    public Waiter() {
        receive(ReceiveBuilder.
                        match(ServeCoffee.class, serveCoffee ->
                                sender().tell(new CoffeeServed(serveCoffee
                                                .coffee),
                                        self()) ).
                        matchAny(this::unhandled).build()
        );
    }


    public static final class ServeCoffee{

        final public Coffee coffee;

        public ServeCoffee(final Coffee coffee){
            this.coffee = coffee;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ServeCoffee that = (ServeCoffee) o;

            return coffee.equals(that.coffee);

        }

        @Override
        public int hashCode() {
            return coffee.hashCode();
        }

        @Override
        public String toString() {
            return "ServeCoffee{" +
                    "coffee=" + coffee +
                    '}';
        }
    }

    public static final class CoffeeServed{

        final public Coffee coffee;

        public CoffeeServed(final Coffee coffee){
            this.coffee = coffee;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoffeeServed that = (CoffeeServed) o;

            return coffee.equals(that.coffee);

        }

        @Override
        public int hashCode() {
            return coffee.hashCode();
        }

        @Override
        public String toString() {
            return "CoffeeServed{" +
                    "coffee=" + coffee +
                    '}';
        }
    }
}
