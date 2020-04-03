import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MainKtTest {

    @Test
    fun `find best hand - highest value no table cards`() {
        val player = Player("Mike", 10, listOf("TC","2D"))
        assertEquals("1002",player.bestHand(emptyList()).value)
    }

    @Test
    fun `find best hand - highest value with table cards`() {
        val player = Player("Mike", 10, listOf("TC","2D"))
        assertEquals("13121002",player.bestHand(listOf("KD","QH")).value)
    }

    @Test
    fun `getPair() should return null when passed a list of cards with no pairs`() {
        assertEquals(null,getPair(listOf("2D","7D","TC","3D","9S","8C","AD")))
    }

    @Test
    fun `getPair() should return the matching pairs when passed a list of cards with 1 pair`() {
        assertEquals(listOf("7D", "7H", "AD", "TC", "9S"),getPair(listOf("7H","7D","TC","3D","9S","8C","AD")))
    }

    @Test
    fun `getPair() should return the top matching pairs when passed a list of cards with 2 pairs`() {
        assertEquals(listOf("TD", "TC", "AD", "9S", "8C"),getPair(listOf("7H","7D","TC","TD","9S","8C","AD")))
    }

    @Test
    fun `getThree() should return null when passed a list of cards with no three of a kind`() {
        assertEquals(null,getThree(listOf("2D","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getThree() should return the matching three when passed a list of cards with 1 three of a kind`() {
        assertEquals(listOf("7C", "7D",  "7H"),getThree(listOf("7H","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getTwoPair() should return null when passed a list of cards with less than 2 pairs`() {
        assertEquals(null,getThree(listOf("2D","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getTwoPair() should return the best two pairs when passed a list of cards with 3 pairs`() {
        assertEquals(listOf("9D","9C","8C","8S","AD"),getTwoPair(listOf("7H","7D","9C","9D","8S","8C","AD")))
    }

    @Test
    fun `getStraight() should return null when passed a list of cards with no 5 card straight`() {
        assertEquals(null,getStraight(listOf("2D","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getStraight() should return the best straight when passed a list of cards with a straight`() {
        assertEquals(listOf("QS","JD","TD","9C","8C"),getStraight(listOf("7H","TD","9C","JD","QS","8C","6D")))
    }
    @Test
    fun `getLowStraight() should return null when passed a list of cards with no 5 card straight`() {
        assertEquals(null,getStraight(listOf("2D","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getLowStraight() should return the best straight when passed a list of cards with a straight`() {
        assertEquals(listOf("5C","4D","3D","2C","AC"),getLowStraight(listOf("AC","5C","9C","4D","QS","3D","2C")))
    }

    @Test
    fun `getFlush() should return null when passed a list of cards with no 5 cards with the same suit`() {
        assertEquals(null,getFlush(listOf("2D","7D","9S","7C","AC","TD","3D")))
    }

    @Test
    fun `getFlush() should return the best flush when passed a list of cards with a flush`() {
        assertEquals(listOf("AC","QC","9C","5C","4C"),getFlush(listOf("QC","5C","9C","4C","AC","3D","2C")))
    }

    @Test
    fun `getFullHouse() should return null when passed a list of cards with no full house`() {
        assertEquals(null,getFullHouse(listOf("2D","7D","9S","9C","AC","6D","AD")))
    }

    @Test
    fun `getFullHouse() should return the best full house when passed a list of cards with more than one full house`() {
        assertEquals(listOf("QH","QD","QC","9D","9S"),getFullHouse(listOf("QC","9S","9D","QD","9S","QH","2C")))
    }

    @Test
    fun `getFour() should return null when passed a list of cards without four cards the same rank`() {
        assertEquals(null,getFour(listOf("QC","9S","9S","QC","9S","QC","2C")))
    }

    @Test
    fun `getFour() should return four cards when passed a list of cards with four cards the same rank`() {
        assertEquals(listOf("QC","QC","QC","QC"),getFour(listOf("QC","9S","9S","QC","9S","QC","QC")))
    }

    @Test
    fun `getStraightFlush() should return null when passed a list of cards which is a flush but not a straight`() {
        val cards = listOf("2D","7D","9D","7C","AC","TD","3D")
        assertTrue( getFlush(cards) != null)
        assertTrue( getStraightFlush(cards) == null)
    }

    @Test
    fun `getStraightFlush() should return null when passed a list of cards which is a straight and a flush but not a straight flush`() {
        val cards = listOf("2D","7D","9D","8C","AC","TD","JD")
        assertTrue( getFlush(cards) != null)
        assertTrue( getStraight(cards) != null)
        assertTrue( getStraightFlush(cards) == null)
    }

    @Test
    fun `getStraightFlush() should return straight flush when passed a list of cards which is a straight flush`() {
        val cards = listOf("2D","7D","9D","8D","QD","TD","JD")
        assertTrue( getFlush(cards) != null)
        assertTrue( getStraight(cards) != null)
        assertEquals(listOf("QD","JD","TD","9D","8D"), getStraightFlush(cards) )
    }

    @Test
    fun `getStraightFlush() should return null when passed a list of cards which is an ace low straight flush`() {
        val cards = listOf("2D","AD","3D","4D","5D","TD","JD")
        assertEquals(null, getStraightFlush(cards) )
    }

    @Test
    fun `getAceLowStraightFlush() should return straightFlush when passed a list of cards which is an ace low straight flush`() {
        val cards = listOf("2D","AD","3D","4D","5D","TD","JD")
        assertEquals(listOf("5D","4D","3D","2D","AD"), getLowStraightFlush(cards) )
    }

    @Test
    fun `findWinners`() {
        assertEquals(emptyList<Player>(),findWinners(emptyList(), emptyList()))
    }

    @Test
    fun `should return 1 player if passed 1 player`() {
        val player = Player("Mike", 10, listOf("TC","2D"))
        assertEquals(listOf(player), findWinners(listOf(player), emptyList()))
    }

    @Test
    fun `should return 1 player that have the highest bet and same hand`() {
        val player1 = Player("Mike", 10, listOf("TC","2D"))
        val player2 = Player("player1", 12, listOf("TC","2D"))
        assertEquals(listOf(player2), findWinners(listOf(player1,player2), emptyList()))
    }

    @Test
    fun `should return 2 players that have the highest bet and same hand`() {
        val player1 = Player("Mike", 10, listOf("TC","2D"))
        val player2 = Player("Ali", 10, listOf("TC","2D"))
        assertEquals(listOf(player1,player2), findWinners(listOf(player1,player2), emptyList()))
    }

    @Test
    fun `should return 1 player that wins, same bet and winning hand - highest value`() {
        val player1 = Player("player1", 12, listOf("TC","2D"))
        val player2 = Player("player2", 12, listOf("JC","2D"))
        assertEquals(listOf(player2), findWinners(listOf(player1,player2), emptyList()))
    }

    @Test
    fun `should return 1 player that wins, same bet and winning hand - highest value plus 5 table cards`() {
        val player1 = Player("player1", 12, listOf("TC","2D"))
        val player2 = Player("player2", 12, listOf("JC","2D"))
        assertEquals(listOf(player2), findWinners(listOf(player1,player2), listOf("QC","3D","9S","8C","AD")))
    }

    @Test
    fun `should return the player with winning hand - pair v no pair`() {
        val player1 = Player("Mike", 12, listOf("TC","2D"))
        val player2 = Player("player2", 12, listOf("JC","2D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("TC","3D","9S","8C","AD")))
    }
    @Test
    fun `should return the player with winning hand - pair v pair`() {
        val player1 = Player("Mike", 12, listOf("JC","2D"))
        val player2 = Player("player2", 12, listOf("JC","9D"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("TC","3D","JS","8C","AD")))
    }
    @Test
    fun `should return the player with winning hand - pair v 2 pair`() {
        val player1 = Player("Mike", 12, listOf("JC","2D"))
        val player2 = Player("player2", 12, listOf("JC","9D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("TC","3D","JS","2C","AD")))
    }
    @Test
    fun `should return the player with winning hand - pair v 3 of a kind`() {
        val player1 = Player("Mike", 12, listOf("JC","2D"))
        val player2 = Player("player2", 12, listOf("JC","JD"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("TC","3D","JS","2C","AD")))
    }
    @Test
    fun `should return the player with winning hand - 3 of a kind v 3 of a kind`() {
        val player1 = Player("Mike", 12, listOf("JC","JD"))
        val player2 = Player("player2", 12, listOf("2C","2D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("TC","3D","JS","2C","AD")))
    }
    @Test
    fun `should return the player with winning hand - 3 of a kind v straight`() {
        val player1 = Player("Mike", 12, listOf("JC","JD"))
        val player2 = Player("player2", 12, listOf("2C","4D"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("TC","3D","JS","5C","6D")))
    }
    @Test
    fun `should return the player with winning hand - low straight v normal straight`() {
        val player1 = Player("Mike", 12, listOf("AC","2C"))
        val player2 = Player("player2", 12, listOf("2C","3D"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("TC","4D","JS","5C","6D")))
    }
    @Test
    fun `should return the player with winning hand - straight v straight`() {
        val player1 = Player("Mike", 12, listOf("7D","3C"))
        val player2 = Player("player2", 12, listOf("2C","3D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("TC","4D","JS","5C","6D")))
    }
    @Test
    fun `should return the player with winning hand - flush v straight`() {
        val player1 = Player("Mike", 12, listOf("7C","3C"))
        val player2 = Player("player2", 12, listOf("2C","3D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("TC","4D","JC","5C","6D")))
    }
    @Test
    fun `should return the player with winning hand - flush v flush`() {
        val player1 = Player("Mike", 12, listOf("7C","3C"))
        val player2 = Player("player2", 12, listOf("2C","QC"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("TC","4D","JC","5C","6D")))
    }
    @Test
    fun `should return the player with winning hand - fullhouse v flush`() {
        val player1 = Player("Mike", 12, listOf("7C","3C"))
        val player2 = Player("player2", 12, listOf("2D","3D"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("2C","2H","JC","5C","JD")))
    }
    @Test
    fun `should return the player with winning hand - fullhouse v fullhouse`() {
        val player1 = Player("Mike", 12, listOf("JS","JH"))
        val player2 = Player("player2", 12, listOf("2D","3D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("2C","2H","JC","3C","QD")))
    }
    @Test
    fun `should return the player with winning hand - four of a kind v fullhouse`() {
        val player1 = Player("Mike", 12, listOf("JS","JH"))
        val player2 = Player("player2", 12, listOf("2D","3D"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("2C","2H","JC","2C","QD")))
    }
    @Test
    fun `should return the player with winning hand - four of a kind v four of a kind`() {
        val player1 = Player("Mike", 12, listOf("JS","JH"))
        val player2 = Player("player2", 12, listOf("2D","3D"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("2C","2H","JC","2C","JD")))
    }
    @Test
    fun `should return the player with winning hand - Ace Low Straight Flush v four of a kind`() {
        val player1 = Player("Mike", 12, listOf("4S","4C"))
        val player2 = Player("player2", 12, listOf("2H","3H"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("AH","4H","5H","4D","QD")))
    }
    @Test
    fun `should return the player with winning hand - Straight Flush v Ace low Straight flush`() {
        val player1 = Player("Mike", 12, listOf("6H","QC"))
        val player2 = Player("player2", 12, listOf("AH","QD"))
        assertEquals(listOf(player1),findWinners(listOf(player2,player1), listOf("2H","4H","3H","5D","QC")))
    }
    @Test
    fun `should return the player with winning hand - Straight Flush v Straight flush`() {
        val player1 = Player("Mike", 12, listOf("2H","QC"))
        val player2 = Player("player2", 12, listOf("7H","QD"))
        assertEquals(listOf(player2),findWinners(listOf(player2,player1), listOf("3H","4H","5H","6H","QD")))
    }
}
