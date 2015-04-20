/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.EventFilter

class CoffeeHouseSpec extends BaseAkkaSpec {

  "Creating CoffeeHouse" should {
    "result in logging a status message at debug" in {
      EventFilter.debug(pattern = ".*[Oo]pen.*", occurrences = 1) intercept {
        system.actorOf(CoffeeHouse.props)
      }
    }
  }

  "Sending a message to CoffeeHouse" should {
    "result in logging a 'coffee brewing' message at info" in {
      val coffeeHouse = system.actorOf(CoffeeHouse.props)
      EventFilter.info(pattern = ".*[Cc]offee.*", occurrences = 1) intercept {
        coffeeHouse ! "Brew Coffee"
      }
    }
  }
}

