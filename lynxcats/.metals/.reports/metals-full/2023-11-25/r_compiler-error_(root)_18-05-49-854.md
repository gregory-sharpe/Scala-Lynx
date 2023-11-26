file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

action parameters:
offset: 1070
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
text:
```scala
package Lynx.org.lynxcats.Poker
import Lynx.org.lynxcats.Poker.*
import scala.util.Random
import cats.data.State
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.data.IndexedStateT
import Lynx.org.lynxcats.Poker.*
import Lynx.org.lynxcats.Poker.Poker.*
import CardValue.*
import Suit.*
import HandRankings.*
import Action.*
import cats.Eval
import potStates.*
type bettableStates = NoAdditionalBets.type |Betted.type |Raised.type 
enum potStates{
    case MissingSmallBlind
    case MissingBigBlind
    case NoAdditionalBets 
    case Betted(amount:Int)
    case Raised(amount:Int)//unnescecary just testing stuff
}
case class Pot(value: Int, players: List[players],state:potStates)
object Pot {
  def folded:IndexedStateT[Eval,bettableStates,bettableStates,Unit] = IndexedStateT.set(Betted)
  def Check(pot: Pot, player: Player) = ???
  def Bet(pot: Pot, player: Player) = ???
  def Raise(pot: Pot, player: Player) = ???
  def AllIn(pot: Pot, player: Player) = ???
  val x:IndexedStateT[Eval,bettableStates,bettableStates,Unit]= IndexedStateT.set(@@)
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