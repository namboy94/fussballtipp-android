package net.namibsun.fussballtipp.lib

import net.namibsun.fussballtipp.lib.auth.Session

fun main(args: Array<String>) {
    val session = Session("namboy94", "eragon11")
    val matches = session.getMatches()
    for (m in matches) {
        println(m)
    }
}