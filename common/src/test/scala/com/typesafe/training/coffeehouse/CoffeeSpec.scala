/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import com.google.common.collect.ImmutableSet

class CoffeeSpec extends BaseSpec {

  import Coffee._

  "coffees" should {
    "contain Akkaccino, CaffeJava and MochaPlay" in {
      COFFEES should ===(ImmutableSet.of(new Akkaccino(), new CaffeJava(), new MochaPlay()))
    }
  }

  "Calling order" should {
    "create the correct Beverage for a known code" in {
      order("A") should ===(new Akkaccino())
      order("a") should ===(new Akkaccino())
      order("C") should ===(new CaffeJava())
      order("c") should ===(new CaffeJava())
      order("M") should ===(new MochaPlay())
      order("m") should ===(new MochaPlay())
    }
    "throw an IllegalArgumentException for an unknown code" in {
      an[IllegalArgumentException] should be thrownBy order("1")
    }
  }

  "Calling orderOther" should {
    "return an other Coffee than the given one" in {
      forAll(COFFEES) { coffee => orderOther(coffee) should !==(coffee) }
    }
  }
}
