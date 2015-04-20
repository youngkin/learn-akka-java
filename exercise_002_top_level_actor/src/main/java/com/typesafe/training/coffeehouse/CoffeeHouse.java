package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by uyounri on 4/16/15.
 */


public class CoffeeHouse extends AbstractLoggingActor {

    public static Props props(){
        return Props.create(CoffeeHouse.class, CoffeeHouse::new);
    }

    public CoffeeHouse() {
        log().debug("Coffee House Open");
        receive(ReceiveBuilder.
                        matchAny(o -> log().info("Coffee Brewing")).build()
        );
    }
}
