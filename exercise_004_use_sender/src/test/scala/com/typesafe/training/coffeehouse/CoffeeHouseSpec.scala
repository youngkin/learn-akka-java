/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.{ EventFilter, TestProbe }

class CoffeeHouseSpec extends BaseAkkaSpec {

  "Creating CoffeeHouse" should {
    "result in logging a status message at debug" in {
      EventFilter.debug(pattern = ".*[Oo]pen.*", occurrences = 1) intercept {
        system.actorOf(CoffeeHouse.props)
      }
    }
  }

  "Sending a message to CoffeeHouse" should {
    "result in sending a 'coffee brewing' message as response" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val coffeeHouse = system.actorOf(CoffeeHouse.props)
      coffeeHouse ! "Brew Coffee"
      sender.expectMsgPF() { case message if message.toString matches ".*[Cc]offee.*" =>() }
    }
  }
}
