package lynxcats.Poker
// will use a peer to peer structure so will not be similar to this.
import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import cats.effect.IOApp
import cats.effect.ExitCode
import cats.Monad

import cats._
import cats.effect._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import lynxcats.Poker.Player
import java.util.UUID
// gamestate might as well be the root
// get /gameState => returns the full game state object
// get /gameState/PlayerInfo/myID not optional
// get /gameState/PlayerInfo/
// get /gameState/currentPot/

// post /gameState/action/Fold
// post /gameState/action/Check
// post /gameState/action/bet/betAmount
// post /gameState/action/AllIn

// post /Control/Join/PlayerID
// post /Control/Leave.
// find out best practices
import java.time.Year
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher
import lynxcats.Poker.PokerCards.Card
import lynxcats.Poker.PokerCards.CardValue
import org.http4s.dsl.impl.QueryParamDecoderMatcher
object PokerToyRoutes {
  // def gameStateRoutes[F[_]: Monad]: HttpRoutes[F] = ???
  // def run(args: List[String]): IO[ExitCode] = ???
//HELP :
  /*
    explain givens , implicits and using differences etc
    good style for routes
    What is wrong with using Regular Expressions directly for URLs? is it against convention to do it in this style?
    difference between (+&) , (/) and (:?)
    is the cardValueregex easily readable for other people?Is it best to break it down for readabillity
    does the Request type (GET/PUT/etc) have any effect on code or is just a label for readabillity
    JSon
    maybe inline
    if you have 
        root/gamestate/OptionalStringPattern1(String)=> OK("First pattern")
        root/gameState/OptionalStringPattern2(String)=> OK("Second Pattern")
        it the first pattern doesnt correctly parse the string will the object be a None and stop there 
        will it try match with pattern2.How can you fail the implicit conversion so the pattern matching fails on the first pattern
        without consuming the string and then attempts to match on the second pattern? are you forced to pattern match on the url structure
        and then pattern match on the string 
    can I import a library once and have it accessible from other files without explicitly importing it. for example 
    import cats.data.State once.
   */
  val Diamonds = """Diamonds""".r
  val Clubs = """Clubs""".r
  val Spades = """Spades""".r
  val Hearts = """Hearts""".r
  val cardValueRegex = CardValue.values.toList.map(_.toString().r).reduceLeft {
    (acc, cardvalue) => s"$acc|$cardvalue".r
  }
  val suits =
    s"$Diamonds|$Clubs|$Spades|$Hearts" // are there methods for combining Regular expressions directly eg. Diamonds|Clubs etc
// or a library for regular expression methods Diamonds.? => (Diamonds)?

  implicit val PokerCardQueryParamDecoder
      : QueryParamDecoder[Card] = // implicits use QueryParamDecoder
    QueryParamDecoder[String].map(_ => // pattern match on the string to return the card.
      (CardValue.Ace of PokerCards.Suit.Diamonds)
    )
    object PokerCardQueryParamMatcher // the pattern uses QueryParamDecoderMATCHER
        extends QueryParamDecoderMatcher[Card]("Poker Card")

    object playerQueryParamMatcher
        extends QueryParamDecoderMatcher[Long]("PlayerID")
    object betQueryParamMatcher
        extends OptionalQueryParamDecoderMatcher[Int]("betAmount")

    def playerRoutes[F[_]: Monad]: HttpRoutes[F] = {
      val dsl = Http4sDsl[F]
      import dsl._
      HttpRoutes.of[F] {
        case GET -> Root / "GameState" / "PlayerInfo" +& playerQueryParamMatcher(
              playerID
            ) =>
          ???
        case GET -> Root / "GameState" / "PlayerInfo" / "Me" => ???
        case GET -> Root / "GameState" => ??? // full gamestate

      }
    }
    def actionRoutes[F[_]: Monad]: HttpRoutes[F] = {
      val dsl = Http4sDsl[F]
      import dsl._
      HttpRoutes.of[F] {
        case PUT -> Root / "GameState" / "Action" / "FOLD"  => Ok("You Folded")
        case PUT -> Root / "GameState" / "Action" / "CHECK" => Ok("You Checked")
        case PUT -> Root / "GameState" / "Action" / "BET" +& betQueryParamMatcher(
              betAmount
            ) =>
          betAmount match {
            case Some(value) => Ok(s"You have betted $value")
            case None        => Ok(s"You have betted the mininum Amount")
          }

        case PUT -> Root / "GameState" / "Action" / "AllIn" => Ok("")
        case PUT -> Root / "GameState" / "Action" / "Reveal" +& PokerCardQueryParamMatcher(
              card
            ) =>
          ???

      }
    }
    def controlRoutes[F[_]: Monad]: HttpRoutes[F] = {
      val dsl = Http4sDsl[F]
      import dsl._
      HttpRoutes.of[F] {
        case POST -> Root / "Control" / "Join" / playerQueryParamMatcher(
              playerID
            ) =>
          ???
        case DELETE -> Root / "Control" / "Quit" / playerQueryParamMatcher(
              playerID
            ) =>
          ???
      }
    }
    def allRoutes[F[_]: Monad]: HttpRoutes[F] = {
      import cats.syntax.semigroupk._
      playerRoutes[F] <+> actionRoutes[F] <+> controlRoutes[F]
    }
    def allRoutesComplete[F[_]: Monad]: HttpApp[F] = {
      allRoutes.orNotFound // why is this returning an error
    }

}
