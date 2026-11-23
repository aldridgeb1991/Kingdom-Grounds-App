package com.kingdomgrounds.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(AppActions(), "AndroidApp")
        webView.loadUrl("file:///android_asset/index.html")
    }

    inner class AppActions {
        @JavascriptInterface
        fun callNumber(number: String) {
            runOnUiThread {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
            }
        }

        @JavascriptInterface
        fun sendEstimate(message: String) {
            runOnUiThread {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:9127227882")
                        putExtra("sms_body", message)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "No messaging app is available.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }
}
