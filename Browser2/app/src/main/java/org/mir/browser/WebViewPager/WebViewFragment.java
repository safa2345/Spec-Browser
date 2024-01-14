package org.mir.browser.WebViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.mir.browser.R;

public class WebViewFragment extends Fragment {

    private WebView webView;
    private WebViewListener webViewListener;
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public interface WebViewListener{
        void onLoadingFinished(WebView view, String url);
    }

    public WebViewFragment() {

    }
    public WebViewFragment(WebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    public static WebViewFragment newInstance(WebViewListener webViewListener) {
        WebViewFragment fragment = new WebViewFragment(webViewListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshId);
        progressBar = view.findViewById(R.id.progress_bar);
        webView = view.findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/links.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString(USER_AGENT);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webViewListener.onLoadingFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                progressBar.setVisibility(View.VISIBLE);

                final String Urls = url;
                if(url.contains("mailto:") || url.contains("sms:") || url.contains("tel:"))
                {
                    webView.stopLoading();
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(Urls));
                    startActivity(i);
                }

                super.onPageStarted(view, url, favicon);
            }
        });
        progressBar.setMax(100);
        progressBar.setProgress(0);

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                progressBar.setProgress(newProgress);

                if(newProgress == 100)
                    progressBar.setVisibility(View.INVISIBLE);

                else
                    progressBar.setVisibility(View.VISIBLE);

                super.onProgressChanged(view, newProgress);
            }


        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                webView.reload();
            }
        });
        return view;
    }
    public WebView getWebView() {
        return webView;
    }

}