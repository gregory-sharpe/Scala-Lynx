file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 718
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker.scala
text:
```scala
package Lynx.org.lynxcats
import scala.util.Random

object Poker {
  import CardValue.*
  import Suit.*
  type ShuffleSeed = Long
  type cards = List[Card]
  enum Suit {
    case Hearts, Diamonds, Spades, Clubs
  }

  enum CardValue {
    case Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen,
      King, Ace
      def of(suit:Suit) = Card(suit,this)
  }
  case class Card(suit: Suit, value: CardValue) {
    def getValue = value.ordinal + 1
    override def toString(): String = s"$value of $suit"
  }

  case class Deck(private val deck:cards, revealedCards:cards=List.empty) {
    def shuffle = Deck(Random.shuffle(deck)) // make recreatable
    private def reveal = Deck(deck.tail,deck.head.pure[@@])
  }

  object Deck {
    val unshuffled52 = Deck((for {
      suit <- Suit.values
      value <- CardValue.values
    } yield Card(suit, value)).toList)
  }

  case class Player (hand:cards,wallet:Int)



  @main
  def main(args: String*) = {

    val deck = Deck.unshuffled52.shuffle
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