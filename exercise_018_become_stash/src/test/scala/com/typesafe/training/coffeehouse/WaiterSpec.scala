/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.{ EventFilter, TestProbe }

class WaiterSpec extends BaseAkkaSpec {

  "Sending ServeCoffee to Waiter" should {
    "result in sending ApproveCoffee to CoffeeHouse" in {
      val coffeeHouse = TestProbe()
      val guest = TestProbe()
      implicit val _ = guest.ref
      val waiter = system.actorOf(Waiter.props(coffeeHouse.ref, system.deadLetters, Int.MaxValue))
      waiter ! new Waiter.ServeCoffee(new Coffee.Akkaccino)
      coffeeHouse.expectMsg(new CoffeeHouse.ApproveCoffee(new Coffee.Akkaccino, guest.ref))
    }
  }

  "Sending Complaint to Waiter" should {
    "result in sending PrepareCoffee to Barista" in {
      val barista = TestProbe()
      val guest = TestProbe()
      implicit val _ = guest.ref
      val waiter = system.actorOf(Waiter.props(system.deadLetters, barista.ref, 1))
      waiter ! new Waiter.Complaint(new Coffee.Akkaccino)
      barista.expectMsg(new Barista.PrepareCoffee(new Coffee.Akkaccino, guest.ref))
    }
    "result in a FrustratedException if maxComplaintCount exceeded" in {
      val waiter = system.actorOf(Waiter.props(system.deadLetters, system.deadLetters, 0))
      EventFilter[Waiter.FrustratedException](occurrences = 1) intercept {
        waiter ! new Waiter.Complaint(new Coffee.Akkaccino)
      }
    }
  }
}
