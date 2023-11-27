package Lynx.org.lynxcats.Poker
import Lynx.org.lynxcats.Poker.Poker.*
import cats.instances.int
import PlayerStates.*
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
    state: PlayerStates = Undecided
) {
  override def toString(): String = s" ${user.name} has cards $hand "
}
object Player {}
case class User(name: String, wallet: Int, ID: Int) {
  override def toString(): String = s"$name has £$wallet left"
}
