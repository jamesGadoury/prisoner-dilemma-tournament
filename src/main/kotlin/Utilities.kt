package org.lost

fun <T> getCombinations(c: List<T>) : List<Pair<T, T>> {
    val pairs = mutableListOf<Pair<T, T>>()
    for (i in 0..<c.size-1) {
        for (j in i+1..<c.size) {
            pairs.addLast(Pair<T, T>(c[i], c[j]))
        }
    }
    return pairs
}
