package Lynx.org.lynxcats.Poker
import scala.util.Random
import cats.data.State
import cats.syntax.functor._
import cats.syntax.traverse._
import Lynx.org.lynxcats.Poker.*
import Lynx.org.lynxcats.Poker.Poker.*
  import CardValue.*
  import Suit.*
  import HandRankings.*
  import Action.*
  case class Deck(deck: cards, revealedCards: cards = List.empty) {
    // oop approach
    def shuffle = this.copy(deck = Random.shuffle(deck))
    private def reveal: Deck =
      Deck(deck.tail, revealedCards.prepended(deck.head))
    def deal: Deck = {
      revealedCards.size match
        case 0     => reveal.reveal.reveal
        case 3 | 4 => reveal
        case _     => this
    }
    def map[A](f: Card => A): List[A] = {
      this.deck.map(f)
    }

  }

  object Deck {
    // functional approach using states. this might useful for mental poker
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
              } else if valueCount.filter((_, count) => count == 2).size >= 2
              then card.giveRank(TwoPair)
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
      val maxRank = cardsWithRank.max.handRank

      val cardsWith1RankType = cardsWithRank.map(card =>
        card match
          case Card(_, _, rank) if rank == maxRank  => card
          case _                   => card.default
      )
      cardsWith1RankType.sorted.reverse.take(5)
    }

  }