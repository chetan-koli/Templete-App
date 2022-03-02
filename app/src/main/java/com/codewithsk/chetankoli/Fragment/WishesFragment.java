package com.codewithsk.chetankoli.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codewithsk.chetankoli.Config;
import com.codewithsk.chetankoli.databinding.FragmentWishesBinding;



public class WishesFragment extends Fragment {
    FragmentWishesBinding binding;

    public WishesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishesBinding.inflate(inflater, container, false);
        binding.weshesView.getSettings().setJavaScriptEnabled(true);
        binding.weshesView.loadUrl(Config.WISHES_AND_QUOTE);
        binding.weshesView.setWebViewClient(new WebViewClient(){
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