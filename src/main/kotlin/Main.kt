package org.lost

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import redis.clients.jedis.JedisPooled
import kotlin.random.Random
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

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

    Agents.register("alwaysCoop", ::alwaysCooperate)
    Agents.register("alwaysDefect", ::alwaysDefect)
    Agents.register("titForTat", ::titForTat)

    for (i in 0..10) {
        Agents.register("randomAction$i", ::randomAction)
    }

    val combos = getCombinations(Agents.getAgentIds())
    println("number of games that will be played: ${combos.size}")

    val jobs = combos.indices.map {i ->
        launch {
            val combo = combos[i]
            val game = playGame(10, combo.first, combo.second)
            val status = jedis.set("game${i}", Json.encodeToString<GameResult>(evaluateGame(game)))
            if ("OK" != status) {
                println("$i execution had error")
            }
        }
    }.map {job ->
       job.join()
    }

    val agentScores: MutableMap<String, Int> = Agents.getAgentIds().associateWith { 0 }.toMutableMap()
    for (i in combos.indices) {
        val req = jedis.get("game${i}")
        if (req == null || req.isEmpty()) continue
        val gameResult = Json.decodeFromString<GameResult>(req)
        agentScores[gameResult.agent1Result.id] = agentScores[gameResult.agent1Result.id]!! + gameResult.agent1Result.totalScore
        agentScores[gameResult.agent2Result.id] = agentScores[gameResult.agent2Result.id]!! + gameResult.agent2Result.totalScore
    }

    println("winning agent id: ${agentScores.maxBy { it.value }.key}")

    for (agentScore in agentScores.toList().sortedByDescending { it.second }) {
        println("${agentScore.first}: ${agentScore.second}")
    }
}
