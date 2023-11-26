id: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala:[655..659) in Input.VirtualFile("file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala", "package Lynx.org.lynxcats.Poker
import Lynx.org.lynxcats.Poker.*
import scala.util.Random
import cats.data.State
import cats.data.IndexedStateT
import Lynx.org.lynxcats.Poker.*
import Lynx.org.lynxcats.Poker.Poker.*
import CardValue.*
import Suit.*
import HandRankings.*
import Action.*
import cats.Eval
import potStates.*
// IDEA 1
type bettableStates = NoAdditionalBets.type |Betted.type |Raised.type
enum potStates{
    case MissingSmallBlind
    case MissingBigBlind
    case NoAdditionalBets 
    case CurrentBet(amount:Int)
    case Raised(amount:Int)//unnescecary just testing stuff
}
//IDEA 2 https://www.youtube.com/watch?v=_QZan0Yq5Dc
trait 



case class Pot(value: Int, players: List[players],state:potStates)
object Pot {
  def x = IndexedState[]
  def folded:IndexedStateT[Eval,bettableStates,bettableStates,Unit] = State.inspect()
  def Check(pot: Pot, player: Player) = ???
  def Bet(pot: Pot, player: Player) = ???
  def Raise(pot: Pot, player: Player) = ???
  def AllIn(pot: Pot, player: Player) = ???
  val x:IndexedStateT[Eval,bettableStates,bettableStates,Unit]= IndexedStateT.set(Betted)
}
")
file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala
file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/Pot.scala:28: error: expected identifier; obtained case
case class Pot(value: Int, players: List[players],state:potStates)
^
#### Short summary: 

expected identifier; obtained case