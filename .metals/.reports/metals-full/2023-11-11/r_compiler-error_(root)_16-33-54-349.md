file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/grego/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/collection/immutable/List.scala

occurred in the presentation compiler.

action parameters:
offset: 3084
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
text:
```scala
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
    def safeFromOrdinal(value: Int) = {
      val ord = value match
        case 1 => 12
        case n => n - 2
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
  def getRanking(hand: cards) = ???
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
      val sortedCards = cards.sortWith((l, r) => l.value < r.value)
      val possibleStraights = sortedCards
        .sliding(5)
        .filter(_.sliding(2).forall {
          case List(a, b) => b.value.ordinal - a.value.ordinal == 1
          case _          => false
        })
        .toList
        .filter(_.size == 5)

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
        }.map{ 
          // check if card is in a possible straight 
          case card if possibleStraights.exists(_.exists(_.value==card.value)) => card.giveRank(Straight)
          case card =>card
        }.m@@
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
    val y = x.sortWith((l, r) => l.value < r.value)
    println(y)
    val w = y
      .sliding(5)
      .filter(_.sliding(2).forall {
        case List(a, b) => b.value.ordinal - a.value.ordinal == 1
        case _          => false
      })
      .toList
      .filter(_.size == 5)
    println(w)

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

```



#### Error stacktrace:

```
java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
	java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
	java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
	java.base/java.nio.file.Path.of(Path.java:147)
	java.base/java.nio.file.Paths.get(Paths.java:69)
	scala.meta.io.AbsolutePath$.apply(AbsolutePath.scala:60)
	scala.meta.internal.metals.MetalsSymbolSearch.$anonfun$definitionSourceToplevels$2(MetalsSymbolSearch.scala:62)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.metals.MetalsSymbolSearch.definitionSourceToplevels(MetalsSymbolSearch.scala:61)
	scala.meta.internal.pc.completions.CaseKeywordCompletion$.sortSubclasses(MatchCaseCompletions.scala:306)
	scala.meta.internal.pc.completions.CaseKeywordCompletion$.matchContribute(MatchCaseCompletions.scala:254)
	scala.meta.internal.pc.completions.Completions.advancedCompletions(Completions.scala:375)
	scala.meta.internal.pc.completions.Completions.completions(Completions.scala:183)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:86)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:123)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/grego/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/collection/immutable/List.scala