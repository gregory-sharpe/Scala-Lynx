file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 913
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
text:
```scala
package lynxcats.Poker
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
  case NoAdditionalBets(pot: Pot) extends potStates, bettableStates
  case BetInProgress(pot: Pot) extends potStates, bettableStates
}

case class Pot(
    value: potSize,
    Players: Players,
    minBet: potSize,
    smallBlind: potSize = 2
)
//IDEA https://www.youtube.com/watch?v=_QZan0Yq5Dc

object Pot {
  def empty(players: Players) = Pot(0,players,0,2)
  def nextPlayer():State[potStates,Player] = ???
  def addSmallBlind(
      sb: bet,
      player: Player
  ): IndexedStateT[Eval, NoSmallBlind.ty[e@@], NoBigBlind, Player] = IndexedStateT{NoSmallBlind(pot)=>

  }

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
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2576)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:94)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner