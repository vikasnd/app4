package org.jainkosh.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var buttonsContainer: View
    private lateinit var topBar: MaterialToolbar

    private val links = mapOf(
        R.id.btnVideos to "https://www.jainkosh.org/wiki/Videos",
        R.id.btnAudio to "https://www.jainkosh.org/wiki/Audio",
        R.id.btnLiterature to "https://www.jainkosh.org/wiki/Granths",
        R.id.btnStudy to "https://www.jainkosh.org/wiki/Notes",
        R.id.btnOnlineClass to "https://www.jainkosh.org/wiki/Onlineclass"
    )

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        buttonsContainer = findViewById(R.id.buttonsScroll)
        topBar = findViewById(R.id.topBar)

        // Setup WebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
        webView.webChromeClient = WebChromeClient()

        // Set up buttons
        val btnIds = listOf(
            R.id.btnVideos,
            R.id.btnAudio,
            R.id.btnLiterature,
            R.id.btnStudy,
            R.id.btnOnlineClass
        )

        btnIds.forEach { id ->
            val btn = findViewById<MaterialButton>(id)
            btn.setOnClickListener {
                val url = links[id]
                if (url != null) {
                    openUrlInWebView(url)
                }
            }
        }

        // Top bar menu click toggles back to buttons
        topBar.setNavigationOnClickListener {
            // if webview visible, go back to buttons, else do nothing
            if (webView.visibility == View.VISIBLE) {
                restoreButtonsFromWebView()
            }
        }

        // Add a menu text action on the toolbar for clarity
        topBar.setOnMenuItemClickListener { item ->
            false
        }

        // Add a content description for accessibility
        topBar.contentDescription = getString(R.string.menu)
    }

    private fun openUrlInWebView(url: String) {
        // animate buttons out (fade + slide up) and show webview (fade + slide in)
        val fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        fadeOut.duration = 220
        slideUp.duration = 220

        buttonsContainer.startAnimation(fadeOut)
        buttonsContainer.visibility = View.GONE

        webView.visibility = View.VISIBLE
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 260
        webView.startAnimation(fadeIn)

        webView.loadUrl(url)
    }

    private fun restoreButtonsFromWebView() {
        val fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        fadeOut.duration = 220
        webView.startAnimation(fadeOut)
        webView.visibility = View.GONE

        buttonsContainer.visibility = View.VISIBLE
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 260
        buttonsContainer.startAnimation(fadeIn)
    }

    override fun onBackPressed() {
        if (webView.visibility == View.VISIBLE && webView.canGoBack()) {
            webView.goBack()
        } else if (webView.visibility == View.VISIBLE) {
            restoreButtonsFromWebView()
        } else {
            super.onBackPressed()
        }
    }
}
