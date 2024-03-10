package org.lost

import java.util.*

enum class Action {
    Cooperate, Defect
}

object AgentFunctions {
    private val registered = mutableMapOf<Int, () -> Action>()
    fun register(f: () -> Action, id: Int) {
        registered[id] = f
    }

    fun get(id: Int) : (() -> Action)?  {
        return registered[id]
    }
}

data class GameOutcome(val agent1Score: Int, val agent2Score: Int) {}

fun playRound(agent1Id: Int, agent2Id: Int): GameOutcome {
    val agent1Play = AgentFunctions.get(agent1Id)!!()
    val agent2Play = AgentFunctions.get(agent2Id)!!()

    return if (agent1Play == Action.Cooperate && agent2Play == Action.Cooperate) {
        GameOutcome(3, 3)
    } else if (agent1Play == Action.Defect && agent2Play == Action.Defect) {
        GameOutcome(1, 1)
    } else if (agent1Play == Action.Defect) {
        GameOutcome(5, 0)
    } else {
        GameOutcome(0, 5)
    }
}

fun playGame(rounds: Int, agent1Id: Int, agent2Id: Int): GameOutcome {
    var agent1Score = 0
    var agent2Score = 0

    for (i in 0..rounds) {
        val outcome = playRound(agent1Id, agent2Id)
        agent1Score += outcome.agent1Score
        agent2Score += outcome.agent2Score
    }

    return GameOutcome(agent1Score, agent2Score)
}
