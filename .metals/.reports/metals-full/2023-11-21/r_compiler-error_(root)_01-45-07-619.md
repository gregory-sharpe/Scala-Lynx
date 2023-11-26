file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 7145
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
text:
```scala
package Lynx.org.lynxcats
import scala.util.Random
import scala.util.Try
import cats.data.State
import cats.instances.unit
import Lynx.org.lynxcats.Poker.Deck.reveal
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.data.IndexedStateT
object Poker {
  import CardValue.*
  import Suit.*
  import HandRankings.*
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
        HighCard // this probably shouldnt be a property of card, may mess with equality and hashing later
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
    def compare(that: Card): Int = {
      this.handRank.ordinal * (CardValue.values.size + 1) + this.value.ordinal compare
        that.handRank.ordinal * (CardValue.values.size + 1) + that.value.ordinal
    }
    def default:Card = this.copy(handRank = HighCard)
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
    def shuffle: deckState[Unit] = State.modify { state =>
      val shuffledDeck = Random.shuffle(state.deck)
      state.copy(deck = shuffledDeck)
    }
    def reveal: deckState[Card] = State { state =>
      val topCard = state.deck.head
      val newDeck = Deck(
        deck = state.deck.tail,
        revealedCards = topCard :: state.revealedCards
      )
      (newDeck, topCard)
    }
    def deal: deckState[Unit] = State { state =>
      println("compiled")
      state.revealedCards.size match
        case 0 =>
          (for { _ <- reveal; _ <- reveal; _ <- reveal } yield ())
            .run(state)
            .value
        case 3 | 4 => reveal.as(()).run(state).value
        case _     => (state, ())
    }
    def dealTo(user: User): deckState[Player] = State { state =>
      val player = Player(user, state.deck.take(2))
      val restOfDeck = state.deck.drop(2)
      (state.copy(deck = restOfDeck), player)
    }
    def giveCardsRank(cards: cards) = {
      // would be incorrect if the community cards has a four of a kind and a player has a pair.
      // their pair would still have the strength of a pair despite  only one of them being used in calculation
      // solution. take the 
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
              if (valueCount.exists((_, count) => count == 3)) {
                card.giveRank(FullHouse)
              } else if valueCount.filter((_, count) => count == 2).size >= 2 then
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

      cardsWithRank
    }

  }
  def getRanking(hand: cards): HandRankings = {
    val highestRank = Deck.giveCardsRank(hand).max.handRank
    highestRank
  }

  case class User(name: String, wallet: Int, ID: Int){
    override def toString(): String = s"$name has £$wallet left"
  }

  case class Player(user: User, hand: cards){
    override def toString(): String = s" ${user.name} has cards $hand "
  }

  @main
  def pokerMain(args: String*) = {
    val deck = Deck.unshuffled52

    val deckdealt = Deck.deal.runS(deck).value
    val users=List(User("A",100,1),
    User("B",100,2),
    User("C",100,3)
    )
    val x = Deck.deal.flatMap(_=>Deck.deal)
    val y = Deck.dealTo(users.head)
    val walphaa = users.foldLeft(State.pure[Deck,List[Player]](List.empty[Player])){ (state,user)=>
      val x = state.flatMap(players=>Deck.dealTo(user).map(player=> player::players))

    }
    val repeatState(State[@@])
    val w = users.traverse(Deck.dealTo).runA(deckdealt).value
    println(w)
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