file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
### file%3A%2F%2F%2FC%3A%2FUsers%2Fgrego%2FOneDrive%2FScala-Lynx%2Flynxcats%2Fsrc%2Fmain%2Fscala%2FLynx%2Forg%2Flynxcats%2FPoker%2FPot.scala:13: error: illegal start of definition type
type players = List[Player]
^

occurred in the presentation compiler.

action parameters:
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
text:
```scala
package lynxcats.Poker
import lynxcats.Poker.Player
import scala.util.Random
import cats.data.State
import cats.data.IndexedStateT
import cats.Eval
import potStates.*
import cats.data.StateT
import cats.instances.unit
import cats.data.IndexedState


type players = List[Player]
type potSize = Int; type bet = Int
// make into a sealed trait instead
sealed trait bettableStates
// pattern 1

enum potStates{
  case NoSmallBlind(pot: Pot)
  case NoBigBlind(pot: Pot)
  case NoAdditionalBets(pot: Pot) extends potStates, bettableStates
  case BetInProgress(pot: Pot) extends potStates, bettableStates
}
case class Pot(
    value: Int,
    minBet: Int,
    smallBlind: Int = 2
)
//IDEA https://www.youtube.com/watch?v=_QZan0Yq5Dcs

object Pot {

  val empty = Pot(0,0,2)
  val emptySmallBlind = NoSmallBlind(empty)
  val emptyNoAdditionalBets = NoAdditionalBets(empty)
  def addSmallBlind(
      player: Player
  ): IndexedStateT[Eval, NoSmallBlind, NoBigBlind, Player] = IndexedState{case  NoSmallBlind(pot) =>
    val updatedPlayer = Player.bet(pot.smallBlind).runS(player).value
    val updatedPotAmount = Player.bet(pot.smallBlind).runA(player).value + pot.value
    val updatedPot = pot.copy(value = updatedPotAmount)
    (NoBigBlind(updatedPot),updatedPlayer)
  }

  def addBigBlind(
      player: Player
  ): IndexedStateT[Eval, NoBigBlind, NoAdditionalBets, Player] = IndexedState{ case NoBigBlind(pot) => 
    val bigBlind = pot.smallBlind*2
    val updatedPlayer = Player.bet(bigBlind).runS(player).value
    val updatedPotAmount2 = Player.bet(bigBlind).runA(player).value + pot.value
    val updatedPot = pot.copy(value = updatedPotAmount,minBet = bigBlind)
    (NoAdditionalBets(updatedPot),updatedPlayer)
  }
  def fold(player: Player): StateT[Eval, bettableStates, Player] = IndexedState{state=>
    val updatedPlayer = Player.fold.runS(player).value
    state match
        case NoAdditionalBets(pot) => 
            (NoAdditionalBets(pot),updatedPlayer)
        case BetInProgress(pot) =>
            (BetInProgress(pot),updatedPlayer)
  }
  def bet(
      player: Player,
      betAmount: bet
  ): IndexedStateT[Eval,bettableStates, BetInProgress, Player] = IndexedState{state=>
    val updatedPlayer = Player.bet(betAmount).runS(player).value
    //  can you nicely extract pot without pattern matching on the state 
    val pot = state match
        case NoAdditionalBets(pot) => pot
        case BetInProgress(pot) =>pot 
    val updatedPotAmount:Int =  Player.bet(betAmount).runA(player).value + pot.value
    val updatedPot = pot.copy(value = updatedPotAmount,minBet = betAmount)
    (BetInProgress(updatedPot),updatedPlayer)  
  }
  def check(player: Player): StateT[Eval, NoAdditionalBets, Player] = State{case NoAdditionalBets(pot)=>
    val updatedPlayer = Player.check.runS(player).value
    (NoAdditionalBets(pot),updatedPlayer)
  }
  def finishRound(): StateT[Eval, bettableStates, List[potStates]] = ???
    
}

```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4464)
	scala.meta.internal.parsers.ScalametaParser.bracelessPackageStats$1(ScalametaParser.scala:4681)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$11(ScalametaParser.scala:4692)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:368)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$10(ScalametaParser.scala:4692)
	scala.meta.internal.parsers.ScalametaParser.tryParse(ScalametaParser.scala:216)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$1(ScalametaParser.scala:4684)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.batchSource(ScalametaParser.scala:4652)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$source$1(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.source(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.entrypointSource(ScalametaParser.scala:4650)
	scala.meta.internal.parsers.ScalametaParser.parseSourceImpl(ScalametaParser.scala:135)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$parseSource$1(ScalametaParser.scala:132)
	scala.meta.internal.parsers.ScalametaParser.parseRuleAfterBOF(ScalametaParser.scala:59)
	scala.meta.internal.parsers.ScalametaParser.parseRule(ScalametaParser.scala:54)
	scala.meta.internal.parsers.ScalametaParser.parseSource(ScalametaParser.scala:132)
	scala.meta.parsers.Parse$.$anonfun$parseSource$1(Parse.scala:29)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:36)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:25)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:17)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:206)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:356)
```
#### Short summary: 

file%3A%2F%2F%2FC%3A%2FUsers%2Fgrego%2FOneDrive%2FScala-Lynx%2Flynxcats%2Fsrc%2Fmain%2Fscala%2FLynx%2Forg%2Flynxcats%2FPoker%2FPot.scala:13: error: illegal start of definition type
type players = List[Player]
^