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
  def bet(amount:Int): State[Player,Int] = State{ player=>
    val difference = player.wallet - amount
    if (difference>0) 
      { (player.copy(wallet = difference,state = Betted(amount)), amount) }
    else{
        (player.copy(wallet=0,state = AllIn),player.wallet)
    }
  }
  def fold: State[Player,Unit] = State.modify{ player=>
    val updatedPlayer = player.copy(state= Folded)
    updatedPlayer
  }
  def check:State[Player,Unit] = State.modify{player=>
    val updatedPlayer = player.copy(state = Checked )
    updatedPlayer
  }
}
case class User(name: String, ID: Int) {
  override def toString(): String = s"$name has Id : $ID"
}
