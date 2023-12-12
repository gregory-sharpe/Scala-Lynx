package lynxcats.Poker
import scala.util.Random
import scala.util.Try
import cats.instances.unit
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.data.IndexedStateT
import cats.Eval
import cats.data.State
object testingFeatures {

  val users = List(User("A", 1), User("B", 2), User("C", 3))
  val y = Deck.dealTo(users.head)
  val traverseScratch =
    users.foldLeft(State.pure[Deck, List[Player]](List.empty[Player])) {
      (state, user) =>
        state.flatMap(players =>
          Deck.dealTo(user).map(player => player :: players)
        )
        for {
          players <- state
          player <- Deck.dealTo(user)
        } yield player :: players
    }
  def repeat[S, A](
      state: State[S, A],
      times: Int,
      acc: State[S, List[A]] = State.pure[S, List[A]](List.empty[A])
  ): State[S, List[A]] = {
    times match
      case amount if amount <= 0 => State.pure[S, List[A]](List.empty[A])
      case amount =>
        state.flatMap(result =>
          repeat(state = state, times = times - 1).map(results =>
            result :: results
          )
        )
  }
}
