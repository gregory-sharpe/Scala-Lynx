package Lynx.org.lynxcats.Poker
import scala.util.Random
import scala.util.Try
import cats.instances.unit
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.data.IndexedStateT
import cats.Eval
import cats.data.State
object Poker {
  import CardValue.*
  import Suit.*//tr
  import HandRankings.*
  import Action.*
  type cards = List[Card]
  type players = List[Player]
  type deckState[A] = State[Deck, A]

  enum Suit {
    case Hearts, Diamonds, Spades, Clubs
  }
  enum CardValue extends Ordered[CardValue] {
    case Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen,
      King, Ace
    def of(suit: Suit) = Card(suit, this)
    def compare(that: CardValue): Int = this.ordinal compare that.ordinal
  }
  object CardValue {
    def safeFromOrdinal(value: Int) = {
      val ord = value match
        case 1 => 12
        case n => n - 2
      CardValue.values.find(_.ordinal == ord)
    }
    def distance(a: CardValue, b: CardValue) =
      (a, b) match
        case (Ace, Two)  => 1
        case (Ace, King) => 13
        case (c, d)      => math.abs(c.ordinal - d.ordinal)

  }
  enum HandRankings extends Ordered[HandRankings] {
    case HighCard, Pair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse,
      FourOfAKind, StraightFlush
    def compare(that: HandRankings): Int = this.ordinal compare that.ordinal
  }

  case class Card(
      suit: Suit,
      value: CardValue,
      handRank: HandRankings =
        HighCard // this probably shouldnt be a property of card, may mess with equality and hashing later
  ) extends Ordered[Card] {
    override def toString(): String = s"$value of $suit"
    def giveRank(rank: HandRankings): Card = {
      val newRank = (rank, this.handRank) match {
        case (Straight, Flush) => StraightFlush
        case (Flush, Straight) => StraightFlush
        case (proposedRank, currentRank) if proposedRank > currentRank =>
          proposedRank
        case _ => this.handRank
      }
      this.copy(handRank = newRank)
    }
    def compare(that: Card): Int = {
      this.handRank.ordinal * (CardValue.values.size + 1) + this.value.ordinal compare
        that.handRank.ordinal * (CardValue.values.size + 1) + that.value.ordinal
    }
    def default: Card = this.copy(handRank = HighCard)
  }

  def getRanking(hand: cards): HandRankings = {
    val highestRank = Deck.giveCardsRank(hand).max.handRank
    highestRank
  }

  case class User(name: String, wallet: Int, ID: Int) {
    override def toString(): String = s"$name has £$wallet left"
  }

  case class Player(user: User, hand: cards) {
    override def toString(): String = s" ${user.name} has cards $hand "
  }
  object Player {
    def foldCards: IndexedStateT[Eval, Action, Folded.type, Unit] =
      IndexedStateT.set(Folded)

  }

  enum Action {
    case Undecided
    case Folded
    case Check
    case Bet(value: Int)
    case Raise(Value: Int)
    case AllIn
  }

  @main
  def pokerMain(args: String*) = {
    val deck = Deck.unshuffled52

    val deckdealt = Deck.deal.runS(deck).value
    val users = List(User("A", 100, 1), User("B", 100, 2), User("C", 100, 3))
    // val x = Deck.deal.flatMap(_=>Deck.deal)
    // testing stuff
  
    val w = users.traverse(Deck.dealTo).runA(deckdealt).value
    println(w)
  }
}
