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
        assertEquals(listOf("7H", "7D", "AD", "TC", "9S"),getPair(listOf("7H","7D","TC","3D","9S","8C","AD")))
    }

    @Test
    fun `getPair() should return the top matching pairs when passed a list of cards with 2 pairs`() {
        assertEquals(listOf("TC", "TD", "AD", "9S", "8C"),getPair(listOf("7H","7D","TC","TD","9S","8C","AD")))
    }

    @Test
    fun `getThree() should return null when passed a list of cards with no three of a kind`() {
        assertEquals(null,getThree(listOf("2D","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getThree() should return the matching three when passed a list of cards with 1 three of a kind`() {
        assertEquals(listOf("7H", "7D",  "7C"),getThree(listOf("7H","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getTwoPair() should return null when passed a list of cards with less than 2 pairs`() {
        assertEquals(null,getThree(listOf("2D","7D","TC","3D","9S","7C","AD")))
    }

    @Test
    fun `getTwoPair() should return the best two pairs when passed a list of cards with 3 pairs`() {
        assertEquals(listOf("9C","9D","8S","8C","AD"),getTwoPair(listOf("7H","7D","9C","9D","8S","8C","AD")))
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
}
