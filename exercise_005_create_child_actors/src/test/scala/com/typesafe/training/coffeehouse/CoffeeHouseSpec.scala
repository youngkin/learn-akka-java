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

  "Sending CreateGuest to CoffeeHouse" should {
    "result in creating a Guest" in {
      val coffeeHouse = system.actorOf(CoffeeHouse.props, "create-guest")
      coffeeHouse ! CoffeeHouse.CreateGuest.Instance
      TestProbe().expectActor("/user/create-guest/$*")
    }
  }
}
