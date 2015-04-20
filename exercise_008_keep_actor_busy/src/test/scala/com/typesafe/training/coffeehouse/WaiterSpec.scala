/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.TestProbe

class WaiterSpec extends BaseAkkaSpec {

  "Sending ServeCoffee to Waiter" should {
    "result in sending PrepareCoffee to Barista" in {
      val barista = TestProbe()
      val guest = TestProbe()
      implicit val _ = guest.ref
      val waiter = system.actorOf(Waiter.props(barista.ref))
      waiter ! new Waiter.ServeCoffee(new Coffee.Akkaccino)
      barista.expectMsg(new Barista.PrepareCoffee(new Coffee.Akkaccino, guest.ref))
    }
  }
}
