package Lynx.org.lynxcats.Poker
import Lynx.org.lynxcats.Poker.*
import Lynx.org.lynxcats.Poker.Poker.*//{CardValue,Suit,HandRankings}
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
enum potStates{
    case NoSmallBlind(value:potSize,players:Players)
    case NoBigBlind(value:potSize,players:Players)
    case NoAdditionalBets(value:potSize,players:Players) extends potStates,bettableStates
    case BetInProgress(value:potSize,players:Players,minBet:bet) extends potStates,bettableStates
}

//IDEA 2 https://www.youtube.com/watch?v=_QZan0Yq5Dc
object Pot {
  def empty(players:Players) = NoSmallBlind(0,players)
  def addSmallBlind(sb:bet,player:Player):IndexedStateT[Eval,NoSmallBlind,NoBigBlind,Player] = IndexedStateT{state=> 
    
  }
  def addBigBlind(bb:bet,player:Player):IndexedStateT[Eval,NoBigBlind,NoAdditionalBets,Player]= ???
  def fold(player:Player):StateT[Eval,bettableStates,Player] = ???
  def bet(player:Player,bet:bet):IndexedStateT[Eval,bettableStates,BetInProgress,Player] = ???
  def raise(player:Player,bet:bet):IndexedStateT[Eval,bettableStates,BetInProgress,Player] = ???
  def AllIn(player:Player,bet:bet):IndexedStateT[Eval,bettableStates,BetInProgress,Player] = ???
  def check(player:Player):StateT[Eval,NoAdditionalBets,Player] = ???
  def finishRound():StateT[Eval, bettableStates,List[potStates]] = ???

}
