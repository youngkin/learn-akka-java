/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.TestProbe
import scala.concurrent.duration.DurationInt

class BaristaSpec extends BaseAkkaSpec {

  "Sending PrepareCoffee to Barista" should {
    "result in sending a CoffeePrepared response after prepareCoffeeDuration" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val barista = system.actorOf(Barista.props(100 milliseconds))
      sender.within(50 milliseconds, 1000 milliseconds) { // busy is innccurate, so we relax the timing constraints.
        barista ! new Barista.PrepareCoffee(new Coffee.Akkaccino, system.deadLetters)
        sender.expectMsg(new Barista.CoffeePrepared(new Coffee.Akkaccino, system.deadLetters))
      }
    }
  }
}
