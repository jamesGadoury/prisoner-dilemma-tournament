package lost

import kotlinx.serialization.Serializable

enum class Action {
    Cooperate, Defect
}

@Serializable
data class AgentPlay(val id: String, val action: Action, val reward: Int)

typealias Round = Pair<AgentPlay, AgentPlay>
typealias AgentFunction = (String, List<Round>) -> Action

object Agents {
    private val registered = mutableMapOf<String, AgentFunction>()
    fun register(id: String, f: AgentFunction) {
        registered[id] = f
    }

    fun getAgentFunction(id: String): AgentFunction? {
        return registered[id]
    }

    fun getAgentIds(): List<String> {
        return registered.keys.toList()
    }
}

fun getAgentCombinations(): List<Pair<String, String>> {
    val ids = Agents.getAgentIds()
    // first get all possible combinations between agents
    val combos = getCombinations(ids).toMutableList()

    // then get combos for each agent against itself
    for (id in ids) {
        combos.addLast(Pair(id, id))
    }
    return combos
}

