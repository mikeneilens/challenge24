typealias Cards = List<String>
typealias Card = String
typealias Rule = (Cards) -> Cards?

data class Player(val name: String, val bet: Int, val cards: Cards) {
    fun bestHand(tableCards: Cards): Hand =
                            listOf<Rule>(
                                    ::getStraight,
                                    ::getLowStraight,
                                    ::getThree,
                                    ::getTwoPair,
                                    ::getPair,
                                    ::getHigest)
                                .asSequence()
                                .mapIndexedNotNull() { index, rule -> rule(tableCards + cards)?.let{ cards -> Hand(cards = cards, handRank = 10 - index)} }
                                .take(1).first()
  }

data class Hand(val cards: Cards, val handRank: Int = 0) {
    val value = cards.map {it.rank().toString().padStart(2, '0') }.joinToString("")
}

fun getHigest(cards:Cards):Cards = cards.sortedBy { it.rank() }.reversed().take(5)

fun getPair(cards: Cards): Cards? {
    val pair= (2..14).map { rank -> cards.filter { it.rank() == rank } }
        .filter { it.size == 2 }
        .sortedBy { it[0].rank() }
        .lastOrNull()
        ?: return null

    val remainingCards = cards.filter {it != pair[0] && it != pair[1] }.sortedBy { it.rank() }.reversed().take(3)
    return pair + remainingCards
}

fun getTwoPair(cards:Cards):Cards? {
    val firstPair = getPair(cards) ?: return null
    val remainingCards = cards.filter {it != firstPair[0] && it != firstPair[1] }
    val secondPair = getPair(remainingCards) ?: return null
    return firstPair.take(2) + secondPair.take(3)
}

fun getThree(cards: Cards): Cards? =
   (    2..14).map { rank -> cards.filter { it.rank() == rank } }
       .filter { it.size == 3 }
       .sortedBy { it[0].rank() }
       .lastOrNull()

fun getStraight(cards: Cards): Cards? {
    val straight = cards.sortedBy { it.rank() }
        .reversed()
        .map { listOf(it) }
        .reduce{straight, card -> if (straight.size == 5) straight else  if(straight.last().rank() == card[0].rank() + 1 ) straight + card else card}
    return if (straight.size == 5) straight else null
}

fun getLowStraight(cards: Cards): Cards? {
    val straight = cards.sortedBy { it.aceLowRank() }
        .reversed()
        .map { listOf(it) }
        .reduce{straight, card -> if (straight.size == 5) straight else  if(straight.last().aceLowRank() == card[0].aceLowRank() + 1 ) straight + card else card}
    return if (straight.size == 5) straight else null
}

fun findWinners(players: List<Player>, cardsOnTable: Cards): List<Player> {
    val sortedPlayers = players.sortedBy { it.bet }
    val playersInTheGame = sortedPlayers.filter { it.bet == sortedPlayers.last().bet }

    val sortedByHandRank = playersInTheGame.sortedBy { it.bestHand(cardsOnTable).handRank }
    val playersWithBestHandRank =  sortedByHandRank.filter { it.bestHand(cardsOnTable).handRank == sortedByHandRank.last().bestHand(cardsOnTable).handRank }

    val sortedByHandValue = playersWithBestHandRank.sortedBy { it.bestHand(cardsOnTable).value}
    return sortedByHandValue.filter { it.bestHand(cardsOnTable).value == sortedByHandValue.last().bestHand(cardsOnTable).value }
}

fun Card.rank()=  when (this.first()) {
        'A' -> 14
        'T' -> 10
        'J' -> 11
        'Q' -> 12
        'K' -> 13
        else -> this.first().toString().toInt()
}
fun Card.aceLowRank() = if (this.rank() == 14) 1 else this.rank()
