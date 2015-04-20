/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.actor.ActorDSL._
import akka.testkit.{ EventFilter, TestProbe }

class CoffeeHouseSpec extends BaseAkkaSpec {

  "Creating CoffeeHouse" should {
    "result in logging a status message at debug" in {
      EventFilter.debug(pattern = ".*[Oo]pen.*", occurrences = 1) intercept {
        system.actorOf(CoffeeHouse.props(Int.MaxValue), "coffee-house")
      }
    }
    "result in creating a child actor with the name 'barista'" in {
      system.actorOf(CoffeeHouse.props(Int.MaxValue), "create-barista")
      TestProbe().expectActor("/user/create-barista/barista")
    }
    "result in creating a child actor with the name 'waiter'" in {
      system.actorOf(CoffeeHouse.props(Int.MaxValue), "create-waiter")
      TestProbe().expectActor("/user/create-waiter/waiter")
    }
  }

  "Sending CreateGuest to CoffeeHouse" should {
    "result in creating a Guest" in {
      val coffeeHouse = system.actorOf(CoffeeHouse.props(Int.MaxValue), "create-guest")
      coffeeHouse ! new CoffeeHouse.CreateGuest(new Coffee.Akkaccino)
      TestProbe().expectActor("/user/create-guest/$*")
    }
  }

  "Sending ApproveCoffee to CoffeeHouse" should {
    "result in forwarding PrepareCoffee to Barista if caffeineLimit not yet reached" in {
      val barista = TestProbe()
      val coffeeHouse =
        actor("prepare-coffee")(new CoffeeHouse(Int.MaxValue) {
          override def createBarista() = barista.ref
        })
      coffeeHouse ! new CoffeeHouse.CreateGuest(new Coffee.Akkaccino)
      val guest = TestProbe().expectActor("/user/prepare-coffee/$*")
      coffeeHouse ! new CoffeeHouse.ApproveCoffee(new Coffee.Akkaccino, guest)
      barista.expectMsg(new Barista.PrepareCoffee(new Coffee.Akkaccino, guest))
    }
    "result in logging a status message at info if caffeineLimit reached" in {
      val coffeeHouse = system.actorOf(CoffeeHouse.props(1), "caffeine-limit")
      coffeeHouse ! new CoffeeHouse.CreateGuest(new Coffee.Akkaccino)
      val guest = TestProbe().expectActor("/user/caffeine-limit/$*")
      EventFilter.info(pattern = ".*[Ss]orry.*", occurrences = 1) intercept {
        coffeeHouse ! new CoffeeHouse.ApproveCoffee(new Coffee.Akkaccino, guest)
      }
    }
    "result in stopping the Guest if caffeineLimit reached" in {
      val probe = TestProbe()
      val coffeeHouse = system.actorOf(CoffeeHouse.props(1), "guest-terminated")
      coffeeHouse ! new CoffeeHouse.CreateGuest(new Coffee.Akkaccino)
      val guest = TestProbe().expectActor("/user/guest-terminated/$*")
      probe.watch(guest)
      coffeeHouse ! new CoffeeHouse.ApproveCoffee(new Coffee.Akkaccino, guest)
      probe.expectTerminated(guest)
    }
  }

  "On termination of Guest, CoffeeHouse" should {
    "remove the guest from the caffeineLimit bookkeeper" in {
      val coffeeHouse = system.actorOf(CoffeeHouse.props(1), "guest-removed")
      coffeeHouse ! new CoffeeHouse.CreateGuest(new Coffee.Akkaccino)
      val guest = TestProbe().expectActor("/user/guest-removed/$*")
      EventFilter.debug(pattern = ".*[Rr]emoved.*", occurrences = 1) intercept {
        coffeeHouse ! new CoffeeHouse.ApproveCoffee(new Coffee.Akkaccino, guest)
      }
    }
  }
}
