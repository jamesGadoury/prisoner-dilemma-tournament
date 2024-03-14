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

    @Test
    fun testOpponentActionsReversed() {
        val agent1 = "aa"
        val agent2 = "bb"

        val priorRounds = listOf(
            AgentPlay(agent1, Action.Defect, 5) to AgentPlay(agent2, Action.Cooperate, 0),
            AgentPlay(agent1, Action.Cooperate, 0) to AgentPlay(agent2, Action.Defect, 5)
        )

        assertEquals(Action.Defect, opponentActionsReversed(agent1, priorRounds).first())
        assertEquals(Action.Cooperate, opponentActionsReversed(agent2, priorRounds).first())
        assertEquals(listOf(Action.Defect, Action.Cooperate), opponentActionsReversed(agent1, priorRounds).toList())
        assertEquals(listOf(Action.Cooperate, Action.Defect), opponentActionsReversed(agent2, priorRounds).toList())
    }
}