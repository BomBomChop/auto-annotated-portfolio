package com.example.safeguard

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.safeguard.filter.ContentFilterVpnService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val status = TextView(this).apply {
            text = "SafeGuard blocks known adult domains and enforces safe-search where possible."
            textSize = 16f
            setPadding(40, 80, 40, 40)
        }

        val startButton = Button(this).apply {
            text = "Enable Protection"
            setOnClickListener { requestVpnPermission() }
        }

        val stopButton = Button(this).apply {
            text = "Disable Protection"
            setOnClickListener { stopService(Intent(this@MainActivity, ContentFilterVpnService::class.java)) }
        }

        val layout = androidx.core.widget.NestedScrollView(this).apply {
            addView(android.widget.LinearLayout(this@MainActivity).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                addView(status)
                addView(startButton)
                addView(stopButton)
            })
        }

        setContentView(layout)
    }

    private fun requestVpnPermission() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST_CODE)
        } else {
            startFilterService()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE) {
            startFilterService()
        }
    }

    private fun startFilterService() {
        startService(Intent(this, ContentFilterVpnService::class.java))
    }

    companion object {
        private const val VPN_REQUEST_CODE = 1001
    }
}
