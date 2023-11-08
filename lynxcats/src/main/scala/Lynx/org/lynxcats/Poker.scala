package Lynx.org.lynxcats
import scala.util.Random

object Poker {
  import CardValue.*
  import Suit.*
  type cards = List[Card]
  type players = List[Player]
  enum Suit {
    case Hearts, Diamonds, Spades, Clubs
  }
  enum CardValue {
    case Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen,
      King, Ace
    def of(suit: Suit) = Card(suit, this)
    def >(that:CardValue) = this.ordinal> that.ordinal
    def <(that:CardValue) = this.ordinal<that.ordinal
  }
  case class Card(suit: Suit, value: CardValue) {
    def getValue = value.ordinal + 1
    override def toString(): String = s"$value of $suit"
  }

  case class Deck(deck: cards, revealedCards: cards = List.empty) { // make it use the state monad
    def shuffle = Deck(Random.shuffle(deck)) // make recreatable and monadadic
    private def reveal: Deck =
      Deck(deck.tail, revealedCards.prepended(deck.head))
    def deal:Deck ={
        revealedCards.size match
            case 0=> reveal.reveal.reveal
            case 3 => reveal
            case 4 => reveal
            case _ => this
    }
  }
  enum HandRanking{
    case HighCard, Pair, TwoPair,ThreeOfAKind,Straight,Flush,FullHouse,FourOfAKind,StraightFlush
    def >(that:CardValue) = this.ordinal > that.ordinal
    def <(that:CardValue) = this.ordinal < that.ordinal
  }
  def getRanking(Hands:cards)= ???

  object Deck {
    val unshuffled52 = Deck((for {
      suit <- Suit.values
      value <- CardValue.values
    } yield Card(suit, value)).toList)
  }
  case class User(Name:String,wallet:Int)

  case class Player(user:User,hand:cards){

  }

  @main
  def main(args: String*) = {

    val deck = Deck.unshuffled52.shuffle
    val x = King of Diamonds 
    println((King > Jack))
    val w:cards = List(Two,Three,Four).map(_.of(Diamonds))
    CardValue.
    println(w)
  }
}
