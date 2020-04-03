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
                                    ::getLowStraightFlush,
                                    ::getFour,
                                    ::getFullHouse,
                                    ::getFlush,
                                    ::getStraight,
                                    ::getLowStraight,
                                    ::getThree,
                                    ::getTwoPair,
                                    ::getPair,
                                    ::getHighest)
                                .mapIndexedNotNull { index, rule -> rule(tableCards + cards)?.let{ cards -> Hand(cards = cards, handRank = 11 - index)} }
                                .first()
  }

data class Hand(val cards: Cards, val handRank: Int = 0) {
    val value = cards.joinToString("") { it.rank().toString().padStart(2, '0') }
}

fun getHighest(cards:Cards):Cards = cards.sortedBy { it.rank() }.reversed().take(5)

fun getPair(cards: Cards): Cards? {
    val pair =  cards.getMatches(2) ?: return null
    val remainingCards = cards - pair
    return pair + remainingCards.sortedBy { it.rank() }.reversed().take(3)
}

fun getTwoPair(cards:Cards):Cards? {
    val firstPair = getPair(cards)?.take(2) ?: return null
    val remainingCards =  cards - firstPair
    val secondPair = getPair(remainingCards) ?: return null
    return firstPair + secondPair.take(3)
}

fun getThree(cards: Cards): Cards? = cards.getMatches(3)

fun getStraight(cards: Cards): Cards? = cards.removeDuplicates().getMatches(5,
    matcher = {card1,card2 -> card1.rank() == card2.rank() + 1})

fun getLowStraight(cards: Cards): Cards? = cards.removeDuplicates().getMatches(5,
    matcher = {card1,card2 -> card1.aceLowRank() == card2.aceLowRank() + 1},
    sortBy = {card -> card.aceLowRank()})

fun getFlush(cards:Cards): Cards? =
        if (cards.withSameSuit().size >= 5)
            cards.withSameSuit()
            .sortedBy { it.rank() }
            .reversed()
            .take(5)
        else null

fun getFullHouse(cards:Cards):Cards? {
    val three = getThree(cards) ?: return null
    val remainingCards =  cards - three
    val pair = getPair(remainingCards) ?: return null
    return three.take(3) + pair.take(2)
}

fun getFour(cards: Cards): Cards? = cards.getMatches(4)

fun getLowStraightFlush(cards: Cards): Cards? = getLowStraight(cards.withSameSuit())

fun getStraightFlush(cards: Cards): Cards? = getStraight(cards.withSameSuit())

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

fun Cards.withSameSuit():Cards =
    listOf('H', 'C', 'D', "S")
        .map { suit -> filter { card -> card.suit() == suit } }
        .maxBy { it.size }!!

//warning this will remove more than you may expect if you have duplicate card names!
operator fun Cards.minus(other:Cards):Cards = (this.toSet() - other.toSet()).toList()

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
