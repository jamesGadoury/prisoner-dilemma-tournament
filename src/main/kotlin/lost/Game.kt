package lost

import kotlinx.serialization.Serializable
import kotlin.random.Random

data class Game(val agent1Id: String, val agent2Id: String, val rounds: List<Round>)

@Serializable
data class AgentResultFromGame(val id: String, val totalScore: Int)

@Serializable
data class GameResult(val agent1Result: AgentResultFromGame, val agent2Result: AgentResultFromGame)

/**
 * Score a round between two agents using the [agent1Action] and [agent2Action].
 * @return (agent1 score, agent2 score)
 */
fun scoreRound(agent1Action: Action, agent2Action: Action): Pair<Int, Int> {
    return if (agent1Action == Action.Cooperate && agent2Action == Action.Cooperate) {
        Pair(3, 3)
    } else if (agent1Action == Action.Defect && agent2Action == Action.Defect) {
        Pair(1, 1)
    } else if (agent1Action == Action.Defect) {
        Pair(5, 0)
    } else {
        Pair(0, 5)
    }
}

/**
 * Play a round between two agents using the [agent1Id] and [agent2Id] to look up their respective agent functions. [priorRounds] is used as an input into each agent function.
 * @return data from the played round.
 */
fun playRound(agent1Id: String, agent2Id: String, priorRounds: List<Round>): Round {
    val agent1Action = Agents.getAgentFunction(agent1Id)!!(agent1Id, priorRounds)
    val agent2Action = Agents.getAgentFunction(agent2Id)!!(agent2Id, priorRounds)
    val (agent1Score, agent2Score) = scoreRound(agent1Action, agent2Action)
    return Round(AgentPlay(agent1Id, agent1Action, agent1Score), AgentPlay(agent2Id, agent2Action, agent2Score))
}

/**
 * Play a game between two agents using the [agent1Id] and [agent2Id] to look up their respective agent functions. [continueProbability] is used to check if game should continue after each round.
 * @return data from the played game.
 */
fun playGame(agent1Id: String, agent2Id: String, continueProbability: Double): Game {
    val rounds = mutableListOf<Round>()
    while (Random.nextFloat() <= continueProbability) {
        rounds.addLast(playRound(agent1Id, agent2Id, rounds))
    }
    return Game(agent1Id, agent2Id, rounds)
}

/**
 * Evaluate a played [game] to collect information relevant to the game.
 * @return statistics from the played game.
 */
fun evaluateGame(game: Game): GameResult {
    var agent1Score = 0
    var agent2Score = 0
    // for now just count all the rounds and see who won or tied
    for (round in game.rounds) {
        agent1Score += round.first.reward
        agent2Score += round.second.reward
    }

    return GameResult(AgentResultFromGame(game.agent1Id, agent1Score), AgentResultFromGame(game.agent2Id, agent2Score))
}

/**
 * Get a sequence iterating through the actions of opponent of agent with id = [selfId] from [priorRounds] in reverse order. [selfId]
 * should be equal to the agent we want to get the opponent of, not the opponent's id.
 * @return sequence of opponent's actions in reverse order (starting with last action).
 */
fun opponentActionsReversed(selfId: String, priorRounds: List<Round>): Sequence<Action> = sequence {
    for (round in priorRounds.reversed()) {
        yield(if (selfId == round.first.id) round.second.action else round.first.action)
    }
}
