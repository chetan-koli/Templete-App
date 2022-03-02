package com.codewithsk.chetankoli.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codewithsk.chetankoli.R;
import com.codewithsk.chetankoli.databinding.FragmentGamesBinding;


public class GamesFragment extends Fragment {
    FragmentGamesBinding binding;
    public GamesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGamesBinding.inflate(inflater, container, false);
        binding.gameView.loadUrl("https://www.gamezop.com/?id=4102");
        binding.gameView.getSettings().setJavaScriptEnabled(true);
        binding.gameView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        binding.gameView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.gameView.getSettings().setAppCacheEnabled(true);
        binding.gameView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.gameView.getSettings().setDomStorageEnabled(true);
        binding.gameView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        binding.gameView.getSettings().setUseWideViewPort(true);
        binding.gameView.getSettings().setSaveFormData(true);
        binding.gameView.setWebViewClient(new MyWebviewClient());
        return binding.getRoot();
    }


    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        //ProgressDialogue
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pd=new ProgressDialog(getActivity());
            pd.setTitle("Please Wait..");
            pd.setMessage("Games is Loading..");
            pd.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pd.dismiss();
            super.onPageFinished(view, url);
        }
    }
}