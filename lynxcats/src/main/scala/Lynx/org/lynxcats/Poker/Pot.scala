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
    minBet: potSize,
    smallBlind: potSize = 2
)
//IDEA https://www.youtube.com/watch?v=_QZan0Yq5Dcs

object Pot {
  def empty = Pot(0,0,2)
  def nextPlayer:State[potStates,Player] = ???
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
    val updatedPotAmount = Player.bet(bigBlind).runA(player).value + pot.value
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
    // how can you nicely extract pot without pattern matching on the state 
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
