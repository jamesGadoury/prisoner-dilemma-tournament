package lost

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

fun retaliate(agentId: String, priorRounds: List<Round>) : Action {
    if (priorRounds.isEmpty()) return Action.Cooperate
    for (round in priorRounds) {
        val otherPlayerAction = if (agentId == round.first.id) round.second.action else round.first.action
        if (otherPlayerAction == Action.Defect) return Action.Defect
    }
    return Action.Cooperate
}
