package Lynx.org.lynxcats

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object LynxcatsRoutes:

  def jokeRoutes(J: Jokes): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }

  def helloWorldRoutes(H: HelloWorld): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
      
    }
  def joinGame():HttpRoutes[IO]= ???
  def bet(amount : Int):HttpRoutes[IO] = ???
  def check():HttpRoutes[IO] = ???
  def fold():HttpRoutes[IO] = ???
