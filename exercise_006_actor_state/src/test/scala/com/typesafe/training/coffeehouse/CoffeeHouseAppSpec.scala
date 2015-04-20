/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.TestProbe

import org.scalactic.ConversionCheckedTripleEquals
import scala.collection.JavaConversions._

class CoffeeHouseAppSpec extends BaseAkkaSpec with ConversionCheckedTripleEquals {

  import CoffeeHouseApp._

  "Calling argsToOpts" should {
    "return the correct opts for the given args" in {
      argsToOpts(List("a=1", "b", "-Dc=2")) should ===(Map("a" -> "1", "-Dc" -> "2"))
    }
  }

  "Calling applySystemProperties" should {
    "apply the system properties for the given opts" in {
      System.setProperty("c", "")
      applySystemProperties(Map("a" -> "1", "-Dc" -> "2"))
      System.getProperty("c") should ===("2")
    }
  }

  "Creating CoffeeHouseApp" should {
    "result in creating a top-level actor named 'coffee-house'" in {
      new CoffeeHouseApp(system)
      TestProbe().expectActor("/user/coffee-house")
    }
  }

  "Calling createGuest" should {
    "result in sending CreateGuest to CoffeeHouse count number of times" in {
      val probe = TestProbe()
      new CoffeeHouseApp(system) {
        createGuest(2, new Coffee.Akkaccino, Int.MaxValue)
        override def createCoffeeHouse() = probe.ref
      }
      probe.receiveN(2) shouldEqual List.fill(2)(new CoffeeHouse.CreateGuest(new Coffee.Akkaccino))
    }
  }
}
