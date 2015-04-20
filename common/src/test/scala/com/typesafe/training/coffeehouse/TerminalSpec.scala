/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

//import com.typesafe.training.coffeehouse.Coffee._
//import com.typesafe.training.coffeehouse.Terminal._
//import com.typesafe.training.coffeehouse.TerminalCommand._

class TerminalSpec extends BaseSpec with Terminal {

  "Calling Command.apply" should {
    "create the correct CreateGuest command for the given input" in {
      Terminal.create("guest") should ===(new TerminalCommand.Guest(1, new Coffee.Akkaccino, Int.MaxValue))
      Terminal.create("2 g") should ===(new TerminalCommand.Guest(2, new Coffee.Akkaccino, Int.MaxValue))
      Terminal.create("g m") should ===(new TerminalCommand.Guest(1, new Coffee.MochaPlay, Int.MaxValue))
      Terminal.create("g 1") should ===(new TerminalCommand.Guest(1, new Coffee.Akkaccino, 1))
      Terminal.create("2 g m 1") should ===(new TerminalCommand.Guest(2, new Coffee.MochaPlay, 1))
    }
    "create the GetStatus command for the given input" in {
      Terminal.create("status") should ===(TerminalCommand.Status.Instance)
      Terminal.create("s") should ===(TerminalCommand.Status.Instance)
    }
    "create the Quit command for the given input" in {
      Terminal.create("quit") should ===(TerminalCommand.Quit.Instance)
      Terminal.create("q") should ===(TerminalCommand.Quit.Instance)
    }
    "create the Unknown command for illegal input" in {
      Terminal.create("foo") should ===(new TerminalCommand.Unknown("foo"))
    }
  }
}
