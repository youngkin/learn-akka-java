/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.TestProbe

class WaiterSpec extends BaseAkkaSpec {

  "Sending ServeCoffee to Waiter" should {
    "result in sending a CoffeeServed response to sender" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val waiter = system.actorOf(Waiter.props)
      waiter ! new Waiter.ServeCoffee(new Coffee.Akkaccino)
      sender.expectMsg(new Waiter.CoffeeServed(new Coffee.Akkaccino))
    }
  }
}
