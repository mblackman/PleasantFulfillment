package app.mblackman.orderfulfillment.ui.login

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient

class LoginWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val newUrl = Uri.parse(url)

        if (newUrl != null && newUrl.host?.endsWith("etsy.com") == true) {
            view?.loadUrl(url)
            return true
        }
        return false
    }
}