package com.codewithsk.chetankoli.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codewithsk.chetankoli.Config;
import com.codewithsk.chetankoli.databinding.FragmentFancyTextBinding;

public class FancyTextFragment extends Fragment {
    FragmentFancyTextBinding binding;
    public FancyTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFancyTextBinding.inflate(inflater, container, false);
        binding.fancyView.getSettings().setJavaScriptEnabled(true);
        binding.fancyView.loadUrl(Config.FANCY_TEXT_URL);
        binding.fancyView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.pb.setVisibility(View.GONE);
            }
        });
        binding.adView.loadAd();
        binding.adView.startAutoRefresh();
        return binding.getRoot();
    }
}