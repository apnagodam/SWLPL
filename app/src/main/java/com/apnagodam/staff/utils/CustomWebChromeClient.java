package com.apnagodam.staff.utils;


import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by User on 05-10-2017.
 * Custom Chrome client to showing progress bar for web pages
 */

public class CustomWebChromeClient extends WebChromeClient {

    private ProgressListener mListener;

    public CustomWebChromeClient(ProgressListener listener) {
        mListener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mListener.onUpdateProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

    public interface ProgressListener {
        void onUpdateProgress(int progressValue);
    }
}
