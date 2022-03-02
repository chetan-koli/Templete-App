package com.codewithsk.chetankoli.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codewithsk.chetankoli.Config;
import com.codewithsk.chetankoli.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adView.loadAd();
        binding.adView.startAutoRefresh();

        binding.policyloader.getSettings().getJavaScriptEnabled();
        binding.policyloader.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.pb.setVisibility(View.VISIBLE);
                setTitle("loading...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                binding.pb.setVisibility(View.GONE);
            }
        });
        binding.policyloader.loadUrl(Config.PRIVACY_URL);
    }

}