package Lynx.org.lynxcats
import Lynx.org.lynxcats.Poker.*
import org.scalatest.wordspec.AnyWordSpec
class PokerSuite extends AnyWordSpec {
  "A Deck of Cards" should {
    "Contain 52 cards" in {
      assert(Deck.unshuffled52.deck.size == 52)
    }
    "decrease in size after dealing" in {
      assert(Deck.unshuffled52.deal.deck.size<52)
    }
    "have the number of revealed cards increase after dealing" in {
      assert(Deck.unshuffled52.deal.revealedCards.size>0)
    }
    "be different after shuffling" in {
      assert(Deck.unshuffled52.shuffle.deck != Deck.unshuffled52.deck)
    } 
  }
  "Card ranking" should {
    import CardValue.* 
    import Suit.*
    import HandRanking.*

    val pair = List((Three of Hearts),(Three of Diamonds))
    val _2pair = (Four of Diamonds)::(Four of Clubs)::pair
    val _3OfKind = (Three of Clubs)::pair
    val _4OfKind =(Three of Spades)::_3OfKind
    val almostStraight = (List(Two,Three,Four,Five).map(_ of Diamonds ))
    val straight = (Six of Clubs)::almostStraight
    val flush = (King of Diamonds)::almostStraight
    val straightFlush = (Six of Diamonds)::almostStraight
    "return a pair" in {
      assert(getRanking(pair)== Pair)
    }
    "return a Two pair" in {
      assert(getRanking(_2pair)==TwoPair)
    }
    "return a Three of a Kind" in {
      assert(getRanking(_3OfKind)==ThreeOfAKind)
    }
    "return Four of a Kind" in {
      assert(getRanking(_4OfKind)==FourOfAKind)
    }
    "return Straight" in {
      assert(getRanking(straight)== Straight)
    }
    "return flush" in {
      assert(getRanking(flush)==Flush)
    }
    "return Straight flush" in {
      assert(getRanking(straightFlush)==StraightFlush)
    }
    "return High Card" in {
      assert(getRanking(almostStraight)==HighCard)
    }
  }

}
