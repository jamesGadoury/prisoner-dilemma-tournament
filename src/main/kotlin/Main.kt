package org.lost

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import redis.clients.jedis.JedisPooled
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

fun main() = runBlocking {
    val jedis = JedisPooled("localhost", 6379)

    Agents.register("alwaysCoop", ::alwaysCooperate)
    Agents.register("alwaysDefect", ::alwaysDefect)
    Agents.register("titForTat", ::titForTat)
    Agents.register("retaliate", ::retaliate)
    Agents.register("randomAction", ::randomAction)

    val combos = getAgentCombinations()
    println("number of games that will be played: ${combos.size}")

    combos.indices.map { i ->
        launch {
            val combo = combos[i]
            val game = playGame(combo.first, combo.second, 0.996)
            val status = jedis.set("game${i}", Json.encodeToString<GameResult>(evaluateGame(game)))
            if ("OK" != status) {
                println("$i execution had error")
            }
        }
    }.map { job -> job.join() }

    val agentScores: MutableMap<String, Int> = Agents.getAgentIds().associateWith { 0 }.toMutableMap()
    for (i in combos.indices) {
        val req = jedis.get("game${i}")
        if (req == null || req.isEmpty()) continue
        val gameResult = Json.decodeFromString<GameResult>(req)

        agentScores[gameResult.agent1Result.id] =
            agentScores[gameResult.agent1Result.id]!! + gameResult.agent1Result.totalScore
        agentScores[gameResult.agent2Result.id] =
            agentScores[gameResult.agent2Result.id]!! + gameResult.agent2Result.totalScore
    }

    println("winning agent id: ${agentScores.maxBy { it.value }.key}")

    for (agentScore in agentScores.toList().sortedByDescending { it.second }) {
        println("${agentScore.first}: ${agentScore.second}")
    }
}
