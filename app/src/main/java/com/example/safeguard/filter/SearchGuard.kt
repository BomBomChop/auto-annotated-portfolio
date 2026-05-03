package com.example.safeguard.filter

import android.net.Uri

object SearchGuard {
    private val sensitiveTerms = listOf(
        "porn", "xxx", "sex video", "adult video", "nude"
    )

    fun shouldBlockQuery(uri: Uri): Boolean {
        val query = uri.getQueryParameter("q")?.lowercase().orEmpty()
        return sensitiveTerms.any { query.contains(it) }
    }

    fun safeSearchUrl(uri: Uri): Uri {
        val builder = uri.buildUpon()
        val host = uri.host.orEmpty().lowercase()
        when {
            "google." in host -> builder.appendQueryParameter("safe", "active")
            "bing.com" in host -> builder.appendQueryParameter("adlt", "strict")
            "duckduckgo.com" in host -> builder.appendQueryParameter("kp", "1")
        }
        return builder.build()
    }
}
