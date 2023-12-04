file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

action parameters:
offset: 889
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
text:
```scala
package Lynx.org.lynxcats.Poker
import Lynx.org.lynxcats.Poker.*
import Lynx.org.lynxcats.Poker.Poker.* //{CardValue,Suit,HandRankings}
import scala.util.Random
import cats.data.State
import cats.data.IndexedStateT
import cats.Eval
import potStates.*
import cats.data.StateT
import cats.instances.unit
type Players = List[Player]
type potSize = Int; type bet = Int
// make into a sealed trait instead
sealed trait bettableStates
// pattern 1

enum potStates{
  case NoSmallBlind(pot: Pot)
  case NoBigBlind(pot: Pot)
  case NoAdditionalBets(pot: Pot) extends potStates2, bettableStates
  case BetInProgress(pot: Pot) extends potStates2, bettableStates
}

// pattern 2
case class Pot(
    value: potSize,
    Players: Players,
    minBet: potSize,
    smallBlind: potSize = 2
){}
//dw
//IDEA https://www.youtube.com/watch?v=_QZan0Yq5Dc
object Pot {
  
  def empty(players: Players) = Pot(0,@@)
  def addSmallBlind(
      sb: bet,
      player: Player
  ): IndexedStateT[Eval, NoSmallBlind, NoBigBlind, Player] = ???
  def addBigBlind(
      bb: bet,
      player: Player
  ): IndexedStateT[Eval, NoBigBlind, NoAdditionalBets, Player] = ???
  def fold(player: Player): StateT[Eval, bettableStates, Player] = ???
  def bet(
      player: Player,
      bet: bet
  ): IndexedStateT[Eval, bettableStates, BetInProgress, Player] = ???
  def raise(
      player: Player,
      bet: bet
  ): IndexedStateT[Eval, bettableStates, BetInProgress, Player] = ???
  def AllIn(
      player: Player,
      bet: bet
  ): IndexedStateT[Eval, bettableStates, BetInProgress, Player] = ???
  def check(player: Player): StateT[Eval, NoAdditionalBets, Player] = ???
  def finishRound(): StateT[Eval, bettableStates, List[potStates]] = ???

}

```



#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:131)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:128)
	scala.collection.immutable.List.apply(List.scala:79)
	dotty.tools.dotc.util.Signatures$.countParams(Signatures.scala:501)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:186)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:94)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:63)
	scala.meta.internal.pc.MetalsSignatures$.signatures(MetalsSignatures.scala:17)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:51)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0