package lost

import kotlin.random.Random

fun alwaysCooperate(selfId: String, priorRounds: List<Round>): Action {
    return Action.Cooperate
}

fun alwaysDefect(selfId: String, priorRounds: List<Round>): Action {
    return Action.Defect
}

fun randomAction(selfId: String, priorRounds: List<Round>): Action {
    return if (Random.nextFloat() > 0.5) Action.Cooperate else Action.Defect
}

fun titForTat(selfId: String, priorRounds: List<Round>): Action {
    if (priorRounds.isEmpty()) return Action.Cooperate
    return opponentActionsReversed(selfId, priorRounds).first()
}

fun retaliate(selfId: String, priorRounds: List<Round>): Action {
    if (priorRounds.isEmpty()) return Action.Cooperate

    for (otherPlayerAction in opponentActionsReversed(selfId, priorRounds)) {
        if (otherPlayerAction == Action.Defect) return Action.Defect
    }
    return Action.Cooperate
}
