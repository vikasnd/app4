package org.jainkosh.app

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var buttonsContainer: View
    private lateinit var topBar: MaterialToolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var quickNavRow: View

    private val links = mapOf(
        R.id.btnVideos to "https://www.jainkosh.org/wiki/Videos",
        R.id.btnAudio to "https://www.jainkosh.org/wiki/Audio",
        R.id.btnLiterature to "https://www.jainkosh.org/wiki/Granths",
        R.id.btnStudy to "https://www.jainkosh.org/wiki/Notes",
        R.id.btnOnlineClass to "https://www.jainkosh.org/wiki/Onlineclass",
        // Quick nav ids map
        R.id.quick_videos to "https://www.jainkosh.org/wiki/Videos",
        R.id.quick_audio to "https://www.jainkosh.org/wiki/Audio",
        R.id.quick_literature to "https://www.jainkosh.org/wiki/Granths",
        R.id.quick_notes to "https://www.jainkosh.org/wiki/Notes",
        R.id.quick_online to "https://www.jainkosh.org/wiki/Onlineclass"
    )

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

                webView = findViewById<WebView>(R.id.webView)
        buttonsContainer = findViewById<android.view.View>(R.id.buttonsScroll)
        topBar = findViewById<MaterialToolbar>(R.id.topBar)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        quickNavRow = findViewById<android.view.View>(R.id.quickNavRow)

        // Ensure toolbar uses the downloaded drawable if present
        try {
            topBar.setNavigationIcon(R.drawable.jainkosh)
        } catch (e: Exception) {
            // ignore if drawable not found in dev environment
        }

        // Setup WebView safely
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.allowFileAccess = false
        settings.allowContentAccess = false

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Toast.makeText(this@MainActivity, "Failed to load page", Toast.LENGTH_SHORT).show()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 0
                topBar.title = url ?: ""
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                topBar.title = view?.title ?: getString(R.string.app_name)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }

        // Set up main buttons
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

        // Set up quick nav buttons
        val quickIds = listOf(R.id.quick_videos, R.id.quick_audio, R.id.quick_literature, R.id.quick_notes, R.id.quick_online)
        quickIds.forEach { id ->
            val btn = findViewById<MaterialButton>(id)
            btn.setOnClickListener {
                val url = links[id]
                if (url != null) {
                    webView.loadUrl(url)
                }
            }
        }

        // Top bar navigation acts as "Menu": collapse webview if visible
        topBar.setNavigationOnClickListener {
            if (webView.visibility == View.VISIBLE) {
                restoreButtonsFromWebView()
            }
        }

        // Pull to refresh reloads the webview if visible
        swipeRefresh.setOnRefreshListener {
            if (webView.visibility == View.VISIBLE) {
                webView.reload()
            } else {
                swipeRefresh.isRefreshing = false
            }
        }

        topBar.contentDescription = getString(R.string.menu)
    }

    private fun openUrlInWebView(url: String) {
        // animate buttons out (fade) and show webview (fade in)
        val fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        fadeOut.duration = 220

        buttonsContainer.startAnimation(fadeOut)
        buttonsContainer.visibility = View.GONE

        // show quick nav row
        quickNavRow.visibility = View.VISIBLE

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

        quickNavRow.visibility = View.GONE

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
