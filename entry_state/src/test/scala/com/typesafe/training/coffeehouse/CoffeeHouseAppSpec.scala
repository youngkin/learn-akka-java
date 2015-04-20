/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import org.scalactic.ConversionCheckedTripleEquals
import scala.collection.JavaConversions._

class CoffeeHouseAppSpec extends BaseSpec with ConversionCheckedTripleEquals {

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
}
