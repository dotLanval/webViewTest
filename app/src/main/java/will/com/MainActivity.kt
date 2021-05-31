package will.com

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
//10:54 issues
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = MyWebViewClient()
        webView.loadUrl("https://google.com")
        //webView.canGoBack()
    }
//Gets whether this WebView has a back history item.
    override fun onBackPressed() {
        super.onBackPressed()
        val canGoBack = webView.canGoBack()
        if (canGoBack) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            view?.loadUrl(url)
            return true
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {



            if (!detail?.didCrash()!!) {
                // Renderer was killed because the system ran out of memory.
                // The app can recover gracefully by creating a new WebView instance
                // in the foreground.
                Log.e("MY_APP_TAG", ("System killed the WebView rendering process " +
                        "to reclaim memory. Recreating..."))
                webView?.also { webView ->
                    val webViewContainer: ViewGroup = findViewById(R.id.webView)
                    webViewContainer.removeView(webView)
                    webView.destroy()
                   // webView = null
                }
                // By this point, the instance variable "mWebView" is guaranteed
                // to be null, so it's safe to reinitialize it.
                return true
            }
            return super.onRenderProcessGone(view, detail)
    }}
}