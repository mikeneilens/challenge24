typealias Cards = List<String>
typealias Card = String
typealias Rule = (Cards) -> Cards?

fun findWinners(players: List<Player>, cardsOnTable: Cards): List<Player> {
    val sortedPlayers = players.sortedBy { it.bet }
    val playersInTheGame = sortedPlayers.filter { it.bet == sortedPlayers.last().bet }

    val sortedByHandRank = playersInTheGame.sortedBy { it.bestHand(cardsOnTable).handRank }
    val playersWithBestHandRank =  sortedByHandRank.filter { it.bestHand(cardsOnTable).handRank == sortedByHandRank.last().bestHand(cardsOnTable).handRank }

    val sortedByHandValue = playersWithBestHandRank.sortedBy { it.bestHand(cardsOnTable).value}
    return sortedByHandValue.filter { it.bestHand(cardsOnTable).value == sortedByHandValue.last().bestHand(cardsOnTable).value }
}

data class Player(val name: String, val bet: Int, val cards: Cards) {
    fun bestHand(tableCards: Cards): Hand =
                            listOf<Rule>(
                                    ::getStraightFlush,
                                    ::getAceLowStraightFlush,
                                    ::getFour,
                                    ::getFullHouse,
                                    ::getFlush,
                                    ::getStraight,
                                    ::getLowStraight,
                                    ::getThree,
                                    ::getTwoPair,
                                    ::getPair,
                                    ::getHighest)
                                .asSequence()
                                .mapIndexedNotNull { index, rule -> rule(tableCards + cards)?.let{ cards -> Hand(cards = cards, handRank = 11 - index)} }
                                .take(1).first()
  }

data class Hand(val cards: Cards, val handRank: Int = 0) {
    val value = cards.joinToString("") { it.rank().toString().padStart(2, '0') }
}

fun getHighest(cards:Cards):Cards = cards.sortedBy { it.rank() }.reversed().take(5)

fun getPair(cards: Cards): Cards? {
    val pair =  cards.getMatches(2) ?: return null
    val remainingCards = cards.filter {it != pair[0] && it != pair[1] }.sortedBy { it.rank() }.reversed().take(3)
    return pair + remainingCards
}

fun getTwoPair(cards:Cards):Cards? {
    val firstPair = getPair(cards) ?: return null
    val remainingCards = cards.filter {it != firstPair[0] && it != firstPair[1] }
    val secondPair = getPair(remainingCards) ?: return null
    return firstPair.take(2) + secondPair.take(3)
}

fun getThree(cards: Cards): Cards? = cards.getMatches(3)

fun getStraight(cards: Cards): Cards? = cards.removeDuplicates().getMatches(5,
    matcher = {card1,card2 -> card1.rank() == card2.rank() + 1})

fun getLowStraight(cards: Cards): Cards? = cards.removeDuplicates().getMatches(5,
    matcher = {card1,card2 -> card1.aceLowRank() == card2.aceLowRank() + 1},
    sortBy = {card -> card.aceLowRank()})

fun getFlush(cards:Cards): Cards? = listOf('H','C','D',"S")
      .map{suit -> cards.filter { card -> card.suit() == suit }}
      .filter{it.size >= 5}
      .firstOrNull()?.sortedBy { it.rank() }
      ?.reversed()
      ?.take(5)

fun getFullHouse(cards:Cards):Cards? {
    val three = getThree(cards) ?: return null
    val remainingCards = cards.filter {it != three[0] && it != three[1] && it != three[2] }
    val pair = getPair(remainingCards) ?: return null
    return three.take(3) + pair.take(2)
}

fun getFour(cards: Cards): Cards? = cards.getMatches(4)

fun getAceLowStraightFlush(cards: Cards): Cards? = cards.removeDuplicates().getMatches(5,
    matcher =  {card1,card2 -> (card1.aceLowRank() == card2.aceLowRank() + 1) && (card1.suit() == card2.suit()) } ,
    sortBy =  {card -> card.aceLowRank()})

fun getStraightFlush(cards: Cards): Cards? = cards.removeDuplicates(). getMatches(5,
    matcher = {card1,card2 -> (card1.rank() == card2.rank() + 1) && (card1.suit() == card2.suit()) })

fun Cards.getMatches(qty:Int, matcher:(Card, Card)->Boolean = { card1, card2 -> card1.rank() == card2.rank()}, sortBy:(Card)->Int = { card -> card.rank()}):Cards? {
    val matches = sortedBy (sortBy).reversed()
        .map { listOf(it) }
        .reduce{duplicates, card ->
            when {
                duplicates.size == qty -> duplicates
                matcher(duplicates.last() , card[0]) -> duplicates + card
                else -> card
            }
        }
    return if (matches.size == qty ) matches else  null
}

fun Cards.removeDuplicates():Cards {
    return distinctBy { card -> card.rank() }
}

fun Card.rank()=  when (this.first()) {
        'A' -> 14
        'T' -> 10
        'J' -> 11
        'Q' -> 12
        'K' -> 13
        else -> this.first().toString().toInt()
}

fun Card.suit() = last()

fun Card.aceLowRank() = if (this.rank() == 14) 1 else this.rank()
