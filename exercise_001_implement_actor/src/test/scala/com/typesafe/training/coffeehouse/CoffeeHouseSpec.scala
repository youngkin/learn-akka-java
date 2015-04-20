/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.actor.Props
import akka.testkit.EventFilter

class CoffeeHouseSpec extends BaseAkkaSpec {

  "Sending a message to CoffeeHouse" should {
    "result in logging a 'coffee brewing' message at info" in {
      val coffeeHouse = system.actorOf(Props(new CoffeeHouse))
      EventFilter.info(pattern = ".*[Cc]offee.*", occurrences = 1) intercept {
        coffeeHouse ! "Brew Coffee"
      }
    }
  }
}
