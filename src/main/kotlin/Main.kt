package org.lost

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import redis.clients.jedis.JedisPooled
import kotlin.random.Random

fun alwaysCooperate(agentId: String, priorRounds: List<Round>) : Action {
    return Action.Cooperate
}

fun alwaysDefect(agentId: String, priorRounds: List<Round>) : Action {
    return Action.Defect
}

fun randomAction(agentId: String, priorRounds: List<Round>) : Action {
    return if (Random.nextFloat() > 0.5) Action.Cooperate else Action.Defect
}

fun titForTat(agentId: String, priorRounds: List<Round>) : Action {
    if (priorRounds.isEmpty()) return Action.Cooperate
    val lastRound = priorRounds.last()
    return if (agentId == lastRound.first.id) lastRound.second.action else lastRound.first.action
}

fun <T> getCombinations(c: List<T>) : List<Pair<T, T>> {
    val pairs = mutableListOf<Pair<T, T>>()
    for (i in 0..<c.size-1) {
        for (j in i+1..<c.size) {
            pairs.addLast(Pair<T, T>(c[i], c[j]))
        }
    }
    return pairs
}

fun main() = runBlocking {
    val jedis = JedisPooled("localhost", 6379)

    // generate 3 agents of each type
    for (i in 0..3) {
        Agents.register("alwaysCoop$i", ::alwaysCooperate)
        Agents.register("alwaysDefect$i", ::alwaysDefect)
        Agents.register("randomAction$i", ::randomAction)
        Agents.register("titForTat$i", ::titForTat)
    }

    val combos = getCombinations(Agents.getAgentIds())
    println("number of games that will be played: ${combos.size}")

    for (i in 0..<combos.size) {
        launch {
            val combo = combos[i]
            val game = playGame(10, combo.first, combo.second)
            val status = jedis.set("game${i}", evaluateGame(game))
            if ("OK" != status) {
                println("$i execution had error")
            }
        }
    }
}
