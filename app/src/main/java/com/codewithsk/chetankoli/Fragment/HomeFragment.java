package com.codewithsk.chetankoli.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.codewithsk.chetankoli.Adapters.AdapterCategiry;
import com.codewithsk.chetankoli.Adapters.AdapterWithNativeAds;
import com.codewithsk.chetankoli.Models.Categiry;
import com.codewithsk.chetankoli.Models.Videos;
import com.codewithsk.chetankoli.Server.StatusAppClaint;
import com.codewithsk.chetankoli.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private AdapterCategiry categiry;
    private AdapterWithNativeAds videos;

    private ArrayList<Videos> vidList;
    private ArrayList<Categiry> catList;

    private MaxNativeAdLoader adLoader;
    private MaxAd nativeAd;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        vidList = new ArrayList<>();
        catList = new ArrayList<>();

        categiries();
        allVideos();
        binding.adView.loadAd();
        binding.adView.startAutoRefresh();
        return binding.getRoot();
    }

    public void categiries() {
        Call<ArrayList<Categiry>> call = StatusAppClaint.getStatusAppClaint().getApi().getCategiry();
        call.enqueue(new Callback<ArrayList<Categiry>>() {
            @Override
            public void onResponse(Call<ArrayList<Categiry>> call, Response<ArrayList<Categiry>> response) {
                if (response.isSuccessful()) {
                    catList = response.body();
                    binding.mainLayout.setVisibility(View.VISIBLE);
                    binding.mploader.setVisibility(View.GONE);
                    categiry = new AdapterCategiry(getActivity(), catList,getActivity());
                    binding.categiryRv.setAdapter(categiry);
                    categiry.notifyDataSetChanged();
                } else {
                    binding.mploader.setVisibility(View.VISIBLE);
                    binding.mainLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Categiry>> call, Throwable t) {

            }
        });
    }

    public void allVideos() {
        Call<ArrayList<Videos>> call = StatusAppClaint.getStatusAppClaint().getApi().getAllVideos();
        call.enqueue(new Callback<ArrayList<Videos>>() {
            @Override
            public void onResponse(Call<ArrayList<Videos>> call, Response<ArrayList<Videos>> response) {
                if (response.isSuccessful()) {
                    binding.mainLayout.setVisibility(View.VISIBLE);
                    binding.mploader.setVisibility(View.GONE);
                    vidList = response.body();
                    videos = new AdapterWithNativeAds(getActivity(), vidList);
                    binding.allVidiosRv.setAdapter(videos);
                    videos.notifyDataSetChanged();
                }else {
                    binding.mainLayout.setVisibility(View.GONE);
                    binding.mploader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Videos>> call, Throwable t) {

            }
        });
    }
}