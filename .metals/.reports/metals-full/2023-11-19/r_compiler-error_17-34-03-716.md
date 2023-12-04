file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
### file%3A%2F%2F%2FC%3A%2FUsers%2Fgrego%2FOneDrive%2FScala-Lynx%2Flynxcats%2Fsrc%2Fmain%2Fscala%2FLynx%2Forg%2Flynxcats%2FPoker.scala:16: error: => expected but , found
    case Hearts,Diamonds, Spades, Clubs
               ^

occurred in the presentation compiler.

action parameters:
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
text:
```scala
package Lynx.org.lynxcats.Poker
import scala.util.Random
import scala.util.Try

object Poker {
  import CardValue.*
  import Suit.*
  import HandRankings.*
  type cards = List[Card]
  type players = List[Player]
  type seed = Long



  enum Suit {
    case Hearts,Diamonds, Spades, Clubs
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

  case class Deck( // the random function later on will need to be reproducible.
      deck: cards,
      revealedCards: cards = List.empty,
  ) {
    def shuffle = Deck(Random.shuffle(deck), revealedCards)
    def stateShuffle: State[Deck, Unit] = State {state=>
      val shuffled = Deck(Random.shuffle(state.deck), revealedCards)
      (shuffled,())
      }

    private def reveal: Deck =
      Deck(deck.tail, deck.head :: revealedCards)
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

  case class User(Name: String, wallet: Int, ID: Int)

  case class Player(user: User, hand: cards)

  @main
  def main(args: String*) = {
    val x = Deck.unshuffled52.stateShuffle
   

  }
}

```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.syntaxErrorExpected(ScalametaParser.scala:421)
	scala.meta.internal.parsers.ScalametaParser.expect(ScalametaParser.scala:423)
	scala.meta.internal.parsers.ScalametaParser.accept(ScalametaParser.scala:427)
	scala.meta.internal.parsers.ScalametaParser.caseBody$1(ScalametaParser.scala:2528)
	scala.meta.internal.parsers.ScalametaParser.caseClause(ScalametaParser.scala:2540)
	scala.meta.internal.parsers.ScalametaParser.iter$4(ScalametaParser.scala:2571)
	scala.meta.internal.parsers.ScalametaParser.caseClausesIfAny(ScalametaParser.scala:2578)
	scala.meta.internal.parsers.ScalametaParser.caseClauses(ScalametaParser.scala:2554)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockExprPartial$3(ScalametaParser.scala:2503)
	scala.meta.internal.parsers.ScalametaParser.inBracesOnOpen(ScalametaParser.scala:264)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockExprOnBrace$1(ScalametaParser.scala:2518)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockExprPartial$2(ScalametaParser.scala:2503)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.blockExprPartial(ScalametaParser.scala:2503)
	scala.meta.internal.parsers.ScalametaParser.blockExprOnBrace(ScalametaParser.scala:2519)
	scala.meta.internal.parsers.ScalametaParser.argumentExprsOrPrefixExpr(ScalametaParser.scala:2431)
	scala.meta.internal.parsers.ScalametaParser.getNextRhs$2(ScalametaParser.scala:2129)
	scala.meta.internal.parsers.ScalametaParser.getPostfixOrNextRhs$1(ScalametaParser.scala:2161)
	scala.meta.internal.parsers.ScalametaParser.loop$6(ScalametaParser.scala:2181)
	scala.meta.internal.parsers.ScalametaParser.postfixExpr(ScalametaParser.scala:2209)
	scala.meta.internal.parsers.ScalametaParser.postfixExpr(ScalametaParser.scala:2102)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$expr$2(ScalametaParser.scala:1682)
	scala.meta.internal.parsers.ScalametaParser.atPosOpt(ScalametaParser.scala:322)
	scala.meta.internal.parsers.ScalametaParser.autoPosOpt(ScalametaParser.scala:366)
	scala.meta.internal.parsers.ScalametaParser.expr(ScalametaParser.scala:1587)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$templateStat$1.applyOrElse(ScalametaParser.scala:4525)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$templateStat$1.applyOrElse(ScalametaParser.scala:4511)
	scala.PartialFunction.$anonfun$runWith$1$adapted(PartialFunction.scala:145)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4462)
	scala.meta.internal.parsers.ScalametaParser.getStats$2(ScalametaParser.scala:4501)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$scala$meta$internal$parsers$ScalametaParser$$templateStatSeq$3(ScalametaParser.scala:4502)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$scala$meta$internal$parsers$ScalametaParser$$templateStatSeq$3$adapted(ScalametaParser.scala:4499)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:568)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$templateStatSeq(ScalametaParser.scala:4499)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$templateStatSeq(ScalametaParser.scala:4491)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$templateBody$1(ScalametaParser.scala:4342)
	scala.meta.internal.parsers.ScalametaParser.inBracesOr(ScalametaParser.scala:260)
	scala.meta.internal.parsers.ScalametaParser.inBraces(ScalametaParser.scala:256)
	scala.meta.internal.parsers.ScalametaParser.templateBody(ScalametaParser.scala:4342)
	scala.meta.internal.parsers.ScalametaParser.templateBodyOpt(ScalametaParser.scala:4346)
	scala.meta.internal.parsers.ScalametaParser.templateAfterExtends(ScalametaParser.scala:4289)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$templateOpt$1(ScalametaParser.scala:4337)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.templateOpt(ScalametaParser.scala:4327)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$objectDef$1(ScalametaParser.scala:4027)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:368)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:373)
	scala.meta.internal.parsers.ScalametaParser.objectDef(ScalametaParser.scala:4019)
	scala.meta.internal.parsers.ScalametaParser.tmplDef(ScalametaParser.scala:3896)
	scala.meta.internal.parsers.ScalametaParser.topLevelTmplDef(ScalametaParser.scala:3877)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$2.applyOrElse(ScalametaParser.scala:4483)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$2.applyOrElse(ScalametaParser.scala:4471)
	scala.PartialFunction.$anonfun$runWith$1$adapted(PartialFunction.scala:145)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4462)
	scala.meta.internal.parsers.ScalametaParser.bracelessPackageStats$1(ScalametaParser.scala:4681)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$11(ScalametaParser.scala:4692)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:368)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$10(ScalametaParser.scala:4692)
	scala.meta.internal.parsers.ScalametaParser.tryParse(ScalametaParser.scala:216)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$1(ScalametaParser.scala:4684)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.batchSource(ScalametaParser.scala:4652)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$source$1(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.source(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.entrypointSource(ScalametaParser.scala:4650)
	scala.meta.internal.parsers.ScalametaParser.parseSourceImpl(ScalametaParser.scala:135)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$parseSource$1(ScalametaParser.scala:132)
	scala.meta.internal.parsers.ScalametaParser.parseRuleAfterBOF(ScalametaParser.scala:59)
	scala.meta.internal.parsers.ScalametaParser.parseRule(ScalametaParser.scala:54)
	scala.meta.internal.parsers.ScalametaParser.parseSource(ScalametaParser.scala:132)
	scala.meta.parsers.Parse$.$anonfun$parseSource$1(Parse.scala:29)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:36)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:25)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:17)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:206)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:356)
```
#### Short summary: 

file%3A%2F%2F%2FC%3A%2FUsers%2Fgrego%2FOneDrive%2FScala-Lynx%2Flynxcats%2Fsrc%2Fmain%2Fscala%2FLynx%2Forg%2Flynxcats%2FPoker.scala:16: error: => expected but , found
    case Hearts,Diamonds, Spades, Clubs
               ^