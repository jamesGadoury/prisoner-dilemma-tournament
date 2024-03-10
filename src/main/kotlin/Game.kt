package org.lost

enum class Action {
    Cooperate, Defect
}

data class AgentPlay(val id: String, val action: Action, val reward: Int)
typealias Round = Pair<AgentPlay, AgentPlay>
typealias AgentFunction = (String, List<Round>) -> Action
data class Game(val gameLength: Int, val rounds: List<Round>)

object Agents {
    private val registered = mutableMapOf<String, AgentFunction>()
    fun register(id: String, f: AgentFunction) {
        registered[id] = f
    }

    fun getAgentFunction(id: String) : AgentFunction?  {
        return registered[id]
    }

    fun getAgentIds() : List<String> {
        return registered.keys.toList()
    }
}

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

fun playGame(gameLength: Int, agent1Id: String, agent2Id: String): Game {
    val rounds = mutableListOf<Round>()
    for (i in 0..gameLength) {
       rounds.addLast(playRound(agent1Id, agent2Id, rounds))
    }
    return Game(gameLength, rounds)
}

fun evaluateGame(game: Game) : String {
    var agent1Score = 0
    var agent2Score = 0
    // for now just count all the rounds and see who won or tied
    for (round in game.rounds) {
        agent1Score += round.first.reward
        agent2Score += round.second.reward
    }

    return if (agent1Score == agent2Score) {
        ""
    } else if (agent1Score > agent2Score) {
        game.rounds.first().first.id
    } else {
        game.rounds.first().second.id
    }
}
