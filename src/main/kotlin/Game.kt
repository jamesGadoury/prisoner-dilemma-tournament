package org.lost

import kotlinx.serialization.Serializable
import kotlin.random.Random

data class Game(val agent1Id: String, val agent2Id: String, val rounds: List<Round>)

@Serializable
data class AgentResultFromGame(val id: String, val totalScore: Int)
@Serializable
data class GameResult(val agent1Result: AgentResultFromGame, val agent2Result: AgentResultFromGame)

fun playRound(agent1Id: String, agent2Id: String, priorRounds: List<Round>): Round {
    val agent1Action = Agents.getAgentFunction(agent1Id)!!(agent1Id, priorRounds)
    val agent2Action = Agents.getAgentFunction(agent2Id)!!(agent2Id, priorRounds)
    val (agent1Score, agent2Score) = if (agent1Action == Action.Cooperate && agent2Action == Action.Cooperate) {
        Pair(3, 3)
    } else if (agent1Action == Action.Defect && agent2Action == Action.Defect) {
        Pair(1, 1)
    } else if (agent1Action == Action.Defect) {
        Pair(5, 0)
    } else {
        Pair(0, 5)
    }
    return Round(AgentPlay(agent1Id, agent1Action, agent1Score), AgentPlay(agent2Id, agent2Action, agent2Score))
}

fun playGame(agent1Id: String, agent2Id: String, continueProbability: Double): Game {
    val rounds = mutableListOf<Round>()
    while (Random.nextFloat() <= continueProbability) {
       rounds.addLast(playRound(agent1Id, agent2Id, rounds))
    }
    return Game(agent1Id, agent2Id, rounds)
}

fun evaluateGame(game: Game) : GameResult {
    var agent1Score = 0
    var agent2Score = 0
    // for now just count all the rounds and see who won or tied
    for (round in game.rounds) {
        agent1Score += round.first.reward
        agent2Score += round.second.reward
    }

    return GameResult(AgentResultFromGame(game.agent1Id, agent1Score), AgentResultFromGame(game.agent2Id, agent2Score))
}
