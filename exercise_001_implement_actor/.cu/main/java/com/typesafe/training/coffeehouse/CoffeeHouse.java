/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;

public class CoffeeHouse extends AbstractLoggingActor{

    public CoffeeHouse(){
        receive(ReceiveBuilder.
                matchAny(o -> log().info("Coffee Brewing")).build()
        );
    }
}
