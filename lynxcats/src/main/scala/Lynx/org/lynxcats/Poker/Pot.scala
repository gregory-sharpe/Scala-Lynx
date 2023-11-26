package Lynx.org.lynxcats.Poker
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
import cats.data.StateT
import cats.instances.unit
type Players = List[Player]
type potSize = Int; type bet = Int

enum potStates{
    case NoSmallBlind(value:potSize,player:Players)
    case NoBigBlind(value:potSize,player:Players)
    case NoAdditionalBets(value:potSize,player:Players)
    case CurrentBet(value:potSize,player:Players,minBet:bet)
}
type bettableStates = NoAdditionalBets.type | CurrentBet.type

//IDEA 2 https://www.youtube.com/watch?v=_QZan0Yq5Dc

object Pot {
  def empty(players:Players) = NoSmallBlind(0,players)
  def addSmallBlind(sb:bet,player:Player):IndexedStateT[Eval,NoSmallBlind,NoBigBlind,Player] = ???
  def addBigBlind(bb:bet,player:Player):IndexedStateT[Eval,NoBigBlind,NoAdditionalBets,Player]= ???
  def fold(player:Player):StateT[Eval,bettableStates,Player] = ???
  def bet(player:Player):IndexedStateT[Eval,bettableStates,CurrentBet,Player] = ???
  def check(player:Player):StateT[Eval,NoAdditionalBets,Player] = ???
  
}
