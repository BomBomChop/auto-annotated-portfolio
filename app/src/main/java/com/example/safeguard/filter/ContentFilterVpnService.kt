package com.example.safeguard.filter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import com.example.safeguard.MainActivity

/**
 * Prototype VPN service: demonstrates a structure to inspect DNS/HTTP(S) metadata and enforce policy.
 * Production-grade filtering needs full packet parsing, encrypted DNS handling, continuous blocklist updates,
 * and legal/privacy review.
 */
class ContentFilterVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannelIfNeeded()
        startForeground(NOTIFICATION_ID, createNotification())
        startTunnel()
        return START_STICKY
    }

    private fun startTunnel() {
        if (vpnInterface != null) return

        vpnInterface = Builder()
            .setSession("SafeGuard Filter")
            .addAddress("10.0.0.2", 32)
            .addDnsServer("1.1.1.1")
            .addRoute("0.0.0.0", 0)
            .establish()

        // TODO: read packets from vpnInterface fd.
        // Strategy:
        // 1) Parse DNS request hostnames and block AdultDomainBlocklist.isBlocked(host).
        // 2) Parse HTTP host/path and intercept search URLs.
        // 3) For search providers, route only safe-search enforced URLs via SearchGuard.safeSearchUrl(uri).
        // 4) For HTTPS, combine SNI filtering + DNS sinkhole + optional DoH blocking.
    }

    fun isBlockedUri(uri: Uri): Boolean {
        if (AdultDomainBlocklist.isBlocked(uri.host)) return true
        return SearchGuard.shouldBlockQuery(uri)
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("SafeGuard protection is active")
            .setContentText("Filtering adult domains and search queries")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "SafeGuard Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        vpnInterface?.close()
        vpnInterface = null
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "safeguard-filter"
        private const val NOTIFICATION_ID = 401
    }
}
