package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by uyounri on 4/16/15.
 */
public class Guest extends AbstractLoggingActor {

    public static Props props(){
        return Props.create(Guest.class, Guest::new);
    }

    public Guest() {
        receive(ReceiveBuilder.
                        matchAny(this::unhandled).build()
        );
    }
}
