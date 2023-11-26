file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 3354
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
text:
```scala
package Lynx.org.lynxcats
import scala.util.Random
import scala.util.Try
import cats.data.State
import cats.instances.unit
import Lynx.org.lynxcats.Poker.Deck.reveal

object Poker {
  import CardValue.*
  import Suit.*
  import HandRankings.*
  type cards = List[Card]
  type players = List[Player]
  type deckState[A] = State[Deck,A]

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
      val handRank: HandRankings =
        HighCard // this probably shouldnt be a property of card because it is dependent on independent context
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

  object Deck {
    val unshuffled52 = Deck((for {
      suit <- Suit.values
      value <- CardValue.values
    } yield Card(suit, value)).toList)
    def shuffle:deckState[Unit] = State.modify{state=>
      val shuffledDeck = Random.shuffle(state.deck)
      state.copy(deck = shuffledDeck)
    }
    def reveal:deckState[Card] = State{state=>
      val topCard = state.deck.head
      val newDeck = Deck(deck = state.deck.tail,revealedCards = topCard::state.revealedCards)
      (newDeck,topCard)   
    }
    def deal:deckState[Unit] = State{state=>
      state.revealedCards.size match
        case 0=> (for{_<-reveal;_<-reveal;_<-reveal}yield()).run(state).value
        case 3|4=> reveal.flatMapF(_=>Eval[@@())
      }
    def giveCardsRank(cards: cards) = {
      val valueCount = cards.groupBy(_.value).mapValues(_.size).toMap
      val suitCount = cards.groupBy(_.suit).mapValues(_.size).toMap
      val values = cards.map(_.value)
      val sortedCards = cards.find(_.value == Ace) match
        case Some(ace) =>
          ace :: (cards.distinctBy(_.value).sorted) // simulating Ace Low
        case None => cards.distinctBy(_.value).sorted
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
              if (valueCount.exists((_, a) => a == 3)) {
                card.giveRank(FullHouse)
              } else if valueCount.filter((_, c) => c == 2).size >= 2 then
                card.giveRank(TwoPair)
              else { card.giveRank(Pair) }
            case 3 =>
              if valueCount.exists((_, a) => a == 2) then
                card.giveRank(FullHouse)
              else card.giveRank(ThreeOfAKind)
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
        }
        .sorted
        .reverse
        .take(5)
      //
      cardsWithRank
    }

  }
  def getRanking(hand: cards): HandRankings = {
    val highestRank = Deck.giveCardsRank(hand).max.handRank
    highestRank
  }

  case class User(Name: String, wallet: Int, ID: Int)

  case class Player(user: User, hand: cards)

  @main
  def pokerMain(args: String*) = {
    val deck = Deck.unshuffled52
    val deal3:deckState[Unit] = Deck.reveal.flatMap(_=>
      reveal.flatMap(_=>
        reveal.map(_=>())
        )
    )
    val deal3v2 = (for{_<-Deck.reveal;_<-reveal;_<-reveal} yield())
    val w =(for{_<-reveal;_<-reveal;_<-reveal}yield ()).run(deck).value
    println(Deck.reveal.run(deck).value._1.revealedCards)
    println(Deck.deal.run(deck).value._1.revealedCards)
    println("hello world")

  }
}

```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2576)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:96)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner