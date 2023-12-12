file:///C:/Users/grego/OneDrive/Scala-Lynx/lynxcats/src/main/scala/Lynx/org/lynxcats/Poker/PokerToyRoutes.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 1632
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
// post /gameState/action/Raise/Betamount
// post /Control/Join
// post /Control/Leave.
import java.time.Year
object PokerToyRoutes {
  // def gameStateRoutes[F[_]: Monad]: HttpRoutes[F] = ???
  // def run(args: List[String]): IO[ExitCode] = ???
  object playerQueryParamMatcher extends QueryParamDecoderMatcher[Long]("PlayerID")
  def playerRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "GameState" / "PlayerInfo" +& playerQueryParamMatcher(playerID) => ???
      case GET -> Root / "GameState" / "Me" => ???
      case GET -> Root / "GameState"=> ??? // full gamestate

    }
  }
  def actionRoutes[F[_]:Monad]:Htt[@@]

}

```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2576)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:94)
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