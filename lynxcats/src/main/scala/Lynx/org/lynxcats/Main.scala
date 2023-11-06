package Lynx.org.lynxcats

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    LynxcatsServer.run
