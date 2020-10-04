package com.apnagodam.staff.activity;

import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.WebPagesBinding;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.CustomWebChromeClient;
import com.apnagodam.staff.utils.Utility;

public class BrowserPage extends BaseActivity<WebPagesBinding> implements CustomWebChromeClient.ProgressListener {


    private String webUrl;


    @Override
    protected int getLayoutResId() {
        return R.layout.web_pages;
    }

    @Override
    protected void setUp() {
        webUrl = getIntent().getStringExtra("webUrl");
        loadPage();
    }


    public void loadPage() {
        if (!Utility.getConnectivityStatusString(BrowserPage.this).equalsIgnoreCase(Constants.NOT_CONNECT)) {
            binding.webProgress.setVisibility(View.VISIBLE);
        }
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebChromeClient(new CustomWebChromeClient(this));
        binding.webView.setWebViewClient(new WebClient());
        binding.webView.loadUrl(webUrl);
    }
    @Override
    public void onUpdateProgress(int progressValue) {
        binding.webProgress.setProgress(progressValue);

        if (progressValue == 100) {
            binding.webProgress.setVisibility(View.GONE);
            binding.webView.requestLayout();
        }
    }


    /**
     * Inner class for custom WebClient
     */
    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.webView.loadUrl("javascript:APP.resize(document.body.getBoundingClientRect().height)");
                    binding.webProgress.setVisibility(View.INVISIBLE);
                }
            }, 1000);

        }
    }
}
