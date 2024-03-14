package lost

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestGame {
    @Test
    fun testScoreRoundBothCooperate() {
        assertEquals(Pair<Int, Int>(3, 3), scoreRound(Action.Cooperate, Action.Cooperate))
    }

    @Test
    fun testScoreRoundBothDefect() {
        assertEquals(Pair<Int, Int>(1, 1), scoreRound(Action.Defect, Action.Defect))
    }

    @Test
    fun testScoreRoundOnlyAgent1Defects() {
        assertEquals(Pair<Int, Int>(5, 0), scoreRound(Action.Defect, Action.Cooperate))
    }

    @Test
    fun testScoreRoundOnlyAgent2Defects() {
        assertEquals(Pair<Int, Int>(0, 5), scoreRound(Action.Cooperate, Action.Defect))
    }
}