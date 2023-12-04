file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Users.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

action parameters:
offset: 863
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Users.scala
text:
```scala
package lynxcats.Poker
import cats.data.IndexedStateT
import cats.Eval
import cats.data.State
import PlayerStates.*
import PokerCards.*
sealed trait unbettableStates
enum PlayerStates {
    case Undecided
    case Folded extends PlayerStates, unbettableStates
    case Checked
    case Betted(value: Int)
    case Raised(value: Int)
    case AllIn extends PlayerStates, unbettableStates
    case Broke extends PlayerStates,unbettableStates
  }

case class Player(
    user: User,
    hand: cards,
    wallet: Int,
    state: PlayerStates = Undecided

) {
  override def toString(): String = s" ${user.name} has cards $hand "
}
object Player {
  def take(amount:Int): State[Player,Int] = State{ state=>
    val difference = state.wallet - amount
    if (difference>0) 
      { (state.copy(wallet = difference),amount) }
    else{
        (state.copy(wallet=0,state@@),)
    }
  }
}
case class User(name: String, ID: Int) {
  override def toString(): String = s"$name has Id : $ID"
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