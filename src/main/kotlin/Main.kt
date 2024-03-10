package org.lost

import kotlinx.coroutines.*
import redis.clients.jedis.JedisPooled

fun main() = runBlocking {
    val jedis = JedisPooled("localhost", 6379)

    for (i in 1..10000) {
        launch {
            val status = jedis.set("blah${i}", "${i*2}")
            if ("OK" != status) {
                println("$i execution had error")
            }
        }
    }
}
