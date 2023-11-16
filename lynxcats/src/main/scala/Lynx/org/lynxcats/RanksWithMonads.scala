package Lynx.org.lynxcats

import cats.data.State
import Lynx.org.lynxcats.Poker.cards

object RanksWithMonads {
  def checkCount(cards:cards)={
    val valueCount =cards.groupBy(_.value).mapValues(_.size).toMap
  }
}
