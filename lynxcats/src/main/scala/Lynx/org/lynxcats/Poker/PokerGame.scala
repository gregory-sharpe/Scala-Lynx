package lynxcats.Poker

case class PokerGame(deck:Deck, players:List[Player],currentPot:potStates,pots:Pot)