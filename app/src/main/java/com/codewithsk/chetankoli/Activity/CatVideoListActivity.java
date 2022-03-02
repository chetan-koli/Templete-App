package com.codewithsk.chetankoli.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.codewithsk.chetankoli.Adapters.AdapterWithNativeAds;
import com.codewithsk.chetankoli.Models.Videos;
import com.codewithsk.chetankoli.Server.StatusAppClaint;
import com.codewithsk.chetankoli.databinding.ActivityCatVideoListBinding;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatVideoListActivity extends AppCompatActivity  {
    private ActivityCatVideoListBinding binding;
    private AdapterWithNativeAds videos;
    private ArrayList<Videos> vidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatVideoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adView.loadAd();
        binding.adView.startAutoRefresh();
        vidList = new ArrayList<>();

        String cat = getIntent().getStringExtra("catee");
        allVideos(cat);
    }

    public void allVideos(String cat) {
        Call<ArrayList<Videos>> call = StatusAppClaint.getStatusAppClaint().getApi().getCategiryWiseVideos(cat);
        call.enqueue(new Callback<ArrayList<Videos>>() {
            @Override
            public void onResponse(Call<ArrayList<Videos>> call, Response<ArrayList<Videos>> response) {
                if (response.isSuccessful()) {
                    binding.mainLayout.setVisibility(View.VISIBLE);
                    binding.mploader1.setVisibility(View.GONE);
                    vidList = response.body();
                    videos = new AdapterWithNativeAds(CatVideoListActivity.this, vidList);
                    binding.allVidiosRv1.setAdapter(videos);
                    videos.notifyDataSetChanged();
                }else {
                    binding.mainLayout.setVisibility(View.GONE);
                    binding.mploader1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Videos>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStop() {
        binding.adView.destroy();
        binding.adView.stopAutoRefresh();
        super.onStop();
    }
}