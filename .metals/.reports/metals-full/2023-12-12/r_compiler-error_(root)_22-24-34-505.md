file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/PokerToyRoutes.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/grego/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/Option.scala

occurred in the presentation compiler.

action parameters:
offset: 3558
uri: file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/PokerToyRoutes.scala
text:
```scala
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
   */
  val Diamonds = """Diamonds""".r
  val Clubs = """Clubs""".r
  val Spades = """Spades""".r
  val Hearts = """Hearts""".r
  val cardValueRegex = CardValue.values.toList.map(_.toString().r).reduceLeft{
    (acc, cardvalue) => s"$acc|$cardvalue".r
  } 
  val suits =
    s"$Diamonds|$Clubs|$Spades|$Hearts" // are there methods for combining Regular expressions directly eg. Diamonds|Clubs etc
// or a library for regular expression methods Diamonds.? => (Diamonds)?
  implicit val PokerCardQueryParamDecoder: QueryParamDecoder[Card] = // implicits use QueryParamDecoder
    QueryParamDecoder[String].map(_ =>
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
        case PUT -> Root / "GameState" / "Action" / "FOLD"  => Ok("Folded")
        case PUT -> Root / "GameState" / "Action" / "CHECK" => Ok("Checked")
        case PUT -> Root / "GameState" / "Action"/ "BET" +& betQueryParamMatcher(
              betAmount
            ) => betAmount m@@
           
        case PUT -> Root / "GameState" / "Action" / "AllIn" => ???
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
	scala.meta.internal.pc.completions.CaseKeywordCompletion$.sortSubclasses(MatchCaseCompletions.scala:308)
	scala.meta.internal.pc.completions.CaseKeywordCompletion$.matchContribute(MatchCaseCompletions.scala:256)
	scala.meta.internal.pc.completions.Completions.advancedCompletions(Completions.scala:384)
	scala.meta.internal.pc.completions.Completions.completions(Completions.scala:183)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:86)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:136)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/grego/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/Option.scala