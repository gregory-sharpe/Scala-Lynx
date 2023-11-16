package Lynx.org.lynxcats
import scala.util.Random
import scala.util.Try
import java.text.Normalizer.Form

object Poker {
  import CardValue.*
  import Suit.*
  import HandRankings.*
  type cards = List[Card]
  type players = List[Player]

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
      (a, b) match // use on an ascending ordered list only
        case (Ace, Two)  => 1
        case (Ace, King) => 13
        case (c, d)      => math.abs(c.ordinal-d.ordinal)

  }
  enum HandRankings extends Ordered[HandRankings] {
    case HighCard, Pair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse,
      FourOfAKind, StraightFlush
    def compare(that: HandRankings): Int = this.ordinal compare that.ordinal
  }

  case class Card(
      suit: Suit,
      value: CardValue,
      val handRank: HandRankings = HighCard // this probably shouldnt be a property of card because it is dependent on independent context
  ) extends Ordered[Card] {
    override def toString(): String = s"$value of $suit"
    def giveRank(rank: HandRankings): Card = {
      val newRank = (rank, this.handRank) match {
        case (Straight, Flush) => StraightFlush
        case (Flush, Straight) => StraightFlush
        case (proposedRank, currentRank) if proposedRank > currentRank =>
          proposedRank
        case _ => rank
      }
      this.copy(handRank = newRank)
    }
    def compare(that: Card): Int =
      this.handRank.ordinal * (CardValue.values.size + 1) + this.value.ordinal compare
        that.handRank.ordinal * (CardValue.values.size + 1) + that.value.ordinal
  }

  case class Deck(deck: cards, revealedCards: cards = List.empty) {
    def shuffle = Deck(Random.shuffle(deck))
    private def reveal: Deck =
      Deck(deck.tail, revealedCards.prepended(deck.head))
    def deal: Deck = {
      revealedCards.size match
        case 0     => reveal.reveal.reveal
        case 3 | 4 => reveal
        case _     => this
    }

  }
  def getRanking(hand:cards):HandRankings={
    val highestRank =Deck.giveCardsRank(hand).max.handRank
    highestRank
  }

  object Deck {
    val unshuffled52 = Deck((for {
      suit <- Suit.values
      value <- CardValue.values
    } yield Card(suit, value)).toList)

    def giveCardsRank(cards: cards) = {
      val valueCount = cards.groupBy(_.value).mapValues(_.size).toMap
      val suitCount = cards.groupBy(_.suit).mapValues(_.size).toMap
      val values = cards.map(_.value)
      val sortedCards = cards.find(_.value == Ace) match
        case Some(ace) =>ace :: (cards.distinctBy(_.value).sorted)// simulating Ace Low
        case None      => cards.distinctBy(_.value).sorted
      val possibleStraights =
        sortedCards
          .sliding(5)
          .filter(_.sliding(2).forall {

            case List(a, b) =>
              CardValue.distance(
                a.value,
                b.value
              ) == 1 // use a distance function where K A = 1 and A 2 = 1 but Ak !=1
            case _ => false
          })
          .toList
          .filter(_.size == 5)

      val cardsWithRank: cards = cards
        .map { card =>
          val count = valueCount.getOrElse(card.value, 0)
          count match
            case 2 => 
              if (valueCount.exists((_,a)=>a==3)) { card.giveRank(FullHouse)}
              else if valueCount.filter((_,c)=>c==2).size >= 2 then card.giveRank(TwoPair)
              else {card.giveRank(Pair)}   
            case 3 => if valueCount.exists((_,a)=>a==2) then card.giveRank(FullHouse) else card.giveRank(ThreeOfAKind)
            case 4 => card.giveRank(FourOfAKind)
            case _ => card
        }
        .map { card =>
          val count = suitCount.getOrElse(card.suit, 0)
          count match
            case 5 | 6 | 7 => card.giveRank(Flush)
            case _         => card
        }
        .map {
          // check if card is in a possible straight
          case card
              if possibleStraights.exists(_.exists(_.value == card.value)) =>
            card.giveRank(Straight)
          case card => card
        }.sorted.reverse.take(5)
        // 
      cardsWithRank
    }

  }

  case class User(Name: String, wallet: Int,ID:Int)

  case class Player(user: User, hand: cards)
  
  @main
  def main(args: String*) = {
    val x = (Five of Clubs)::(Ace of Clubs)::List(Two,Three,Four,King).map(_ of Diamonds)
    val pair = List((Three of Hearts),(Three of Diamonds))
    println()

}
}