/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.{ EventFilter, TestProbe }
import scala.concurrent.duration.DurationInt

class GuestSpec extends BaseAkkaSpec {

  "Sending CoffeeServed to Guest" should {
    "result in increasing the coffeeCount and log a status message at info" in {
      val guest = system.actorOf(Guest.props(system.deadLetters, new Coffee.Akkaccino, 100 milliseconds))
      EventFilter.info(pattern = """.*[Ee]njoy.*1\.*""", occurrences = 1) intercept {
        guest ! new Waiter.CoffeeServed(new Coffee.Akkaccino)
      }
    }
    "result in sending ServeCoffee to Waiter after finishCoffeeDuration" in {
      val waiter = TestProbe()
      val guest = createGuest(waiter)
      waiter.within(50 milliseconds, 200 milliseconds) { // The timer is not extremely accurate, relax the timing constraints.
        guest ! new Waiter.CoffeeServed(new Coffee.Akkaccino)
        waiter.expectMsg(new Waiter.ServeCoffee(new Coffee.Akkaccino, guest))
      }
    }
  }

  "Sending CoffeeFinished to Guest" should {
    "result in sending ServeCoffee to Waiter" in {
      val waiter = TestProbe()
      val guest = createGuest(waiter)
      guest ! Guest.CoffeeFinished.Instance
      waiter.expectMsg(new Waiter.ServeCoffee(new Coffee.Akkaccino, guest))
    }
  }

  def createGuest(waiter: TestProbe) = {
    val guest = system.actorOf(Guest.props(waiter.ref, new Coffee.Akkaccino, 100 milliseconds))
    waiter.expectMsg(new Waiter.ServeCoffee(new Coffee.Akkaccino, guest)) //
    // Creating Guest immediately sends Waiter.ServeCoffee
    guest
  }
}
