package com.example.safeguard.filter

object AdultDomainBlocklist {
    private val blockedHosts = setOf(
        "pornhub.com",
        "xvideos.com",
        "xnxx.com",
        "redtube.com",
        "youporn.com",
        "xhamster.com"
    )

    fun isBlocked(host: String?): Boolean {
        if (host.isNullOrBlank()) return false
        val normalized = host.lowercase().removePrefix("www.")
        return blockedHosts.any { normalized == it || normalized.endsWith(".$it") }
    }
}
