package lost

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import redis.clients.jedis.JedisPooled
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*

fun runSim() = runBlocking {
    val jedis = JedisPooled(System.getenv("REDIS_HOST") ?: "localhost", 6379)

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
            val gameId = "game$i"
            for ((roundNumber, round) in game.rounds.withIndex()) {
                for (agentNum in 0..1) {
                    val status = jedis.set("${gameId}|round${roundNumber}|agent${agentNum+1}", Json.encodeToString<AgentPlay>(round.toList()[agentNum]))
                    if ("OK" != status) {
                        // TODO should we be throwing or doing something other than printing?
                        println("$i execution had error")
                    }
                }
            }
            val status = jedis.set("game${i}", Json.encodeToString<GameResult>(evaluateGame(game)))
            if ("OK" != status) {
                // TODO should we be throwing or doing something other than printing?
                println("$i execution had error")
            }
        }
    }.map { job -> job.join() }

    val agentScores: MutableMap<String, Int> = Agents.getAgentIds().associateWith { 0 }.toMutableMap()
    for (i in combos.indices) {
        val gameResult = Json.decodeFromString<GameResult>(jedis.get("game$i"))

        agentScores[gameResult.agent1Result.id] =
            agentScores[gameResult.agent1Result.id]!! + gameResult.agent1Result.totalScore
        agentScores[gameResult.agent2Result.id] =
            agentScores[gameResult.agent2Result.id]!! + gameResult.agent2Result.totalScore
    }

    val winningAgentId = agentScores.maxBy { it.value }.key
    println("winning agent id: $winningAgentId")

    val status = jedis.set("winner", winningAgentId)
    if ("OK" != status) {
        // TODO should we be throwing or doing something other than printing?
        println("execution had error")
    }

    for (agentScore in agentScores.toList().sortedByDescending { it.second }) {
        println("${agentScore.first}: ${agentScore.second}")
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/") {
                call.respondText("Hello from Simulation Server.")
            }

            post("/run") {
                val requestBody = call.receiveText()
                runSim()
                call.respondText("Finished sim.")
            }
        }
    }.start(wait = true)
}
