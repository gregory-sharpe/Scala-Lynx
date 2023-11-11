package Lynx.org.lynxcats
import scala.util.Random
import scala.util.Try

object Poker {
  import CardValue.*
  import Suit.*
  import HandRankings.*
  type cards = List[Card]
  type players = List[Player]

  enum Suit {
    case Hearts, Diamonds, Spades, Clubs
  }
  enum CardValue {
    case Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen,
      King, Ace
    def of(suit: Suit) = Card(suit, this)
    def >(that: CardValue) = this.ordinal > that.ordinal
    def <(that: CardValue) = this.ordinal < that.ordinal
  }
  object CardValue {
    def safeFromOrdinal(value:Int) = {
      val ord = value match
        case 1 => 12
        case n =>n-2
        CardValue.values.find(_.ordinal == ord)
    }
  }
  enum HandRankings {
    case HighCard, Pair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse,
      FourOfAKind, StraightFlush
    def >(that: HandRankings) = this.ordinal > that.ordinal
    def <(that: HandRankings) = this.ordinal < that.ordinal
  }

  case class Card(
      suit: Suit,
      value: CardValue,
      private val handRank: HandRankings = HighCard
  ) {
    override def toString(): String = s"$value of $suit"
    def giveRank(rank: HandRankings): Card = {
      if (rank > handRank) {
        this.copy(handRank = rank)
      } else { this }
    }
  }
  def getRanking(hand:cards) = ???
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
    
    def giveCardsRank(cards: cards) = {
      // by default each card is apart of a singular highcard
      val valueCount = cards.groupBy(_.value).mapValues(_.size).toMap
      val suitCount = cards.groupBy(_.suit).mapValues(_.size).toMap
      val values = cards.map(_.value)

      val cardsWithRank: cards = cards
        .map { card =>
          val count = valueCount.getOrElse(card.value, 0)
          count match
            case 2 => card.giveRank(Pair)
            case 3 => card.giveRank(ThreeOfAKind)
            case 4 => card.giveRank(FourOfAKind)
            case _ => card
        }
        .map { card =>
          val count = suitCount.getOrElse(card.suit, 0)
          count match
            case 5 | 6 | 7 => card.giveRank(Flush)
        }

    }
  }
  object Deck {
    val unshuffled52 = Deck((for {
      suit <- Suit.values
      value <- CardValue.values
    } yield Card(suit, value)).toList)
  }

  case class User(Name: String, wallet: Int)

  case class Player(user: User, hand: cards)

  @main
  def main(args: String*) = {
    val x =
      (Seven of Diamonds) :: (Six of Clubs) :: (List(Two, Three, Four, Five)
        .map(_ of Diamonds))

  }
}

/*
    val PossibleStraights = (1 to 14).map { ordinal =>
      val s = (for {
        value1 <- CardValue.safeFromOrdinal(ordinal)
        s1 <- x.find(_.value==value1)
        value2 <- CardValue.safeFromOrdinal(ordinal + 1)
        s2 <- x.find(_.value==value2)
        value3 <- CardValue.safeFromOrdinal(ordinal + 2)
        s3 <- x.find(_.value==value3)
        value4 <- CardValue.safeFromOrdinal(ordinal + 3)
        s4 <- x.find(_.value==value4)
        value5 <- CardValue.safeFromOrdinal(ordinal + 4)
        s5 <- x.find(_.value==value5)
        l = List(s1,s2,s3,s4,s5)
      } yield l)
    }
*/