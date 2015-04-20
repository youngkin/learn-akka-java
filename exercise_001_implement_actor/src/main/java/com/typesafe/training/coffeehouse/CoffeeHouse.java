package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by uyounri on 4/16/15.
 */
public class CoffeeHouse extends AbstractLoggingActor {
    public CoffeeHouse() {
        receive(ReceiveBuilder.
                        matchAny(o -> log().info("Coffee Brewing")).build()
        );
    }
}
