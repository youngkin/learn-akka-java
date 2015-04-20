/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.{ EventFilter, TestProbe }

class GuestSpec extends BaseAkkaSpec {

  "Sending CoffeeServed to Guest" should {
    "result in increasing the coffeeCount and log a status message at info" in {
      val guest = system.actorOf(Guest.props(system.deadLetters, new Coffee.Akkaccino))
      EventFilter.info(pattern = """.*[Ee]njoy.*1\.*""", occurrences = 1) intercept {
        guest ! new Waiter.CoffeeServed(new Coffee.Akkaccino)
      }
    }
  }

  "Sending CoffeeFinished to Guest" should {
    "result in sending ServeCoffee to Waiter" in {
      val waiter = TestProbe()
      val guest = system.actorOf(Guest.props(waiter.ref, new Coffee.Akkaccino))
      guest ! Guest.CoffeeFinished.Instance
      waiter.expectMsg(new Waiter.ServeCoffee(new Coffee.Akkaccino))
    }
  }
}

