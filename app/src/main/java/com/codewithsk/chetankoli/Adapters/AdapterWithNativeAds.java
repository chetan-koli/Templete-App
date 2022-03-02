package com.codewithsk.chetankoli.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.codewithsk.chetankoli.Activity.VideosActivity;
import com.codewithsk.chetankoli.Models.Videos;
import com.codewithsk.chetankoli.R;
import com.codewithsk.chetankoli.databinding.AdsLoaderBinding;

import java.util.ArrayList;

public class AdapterWithNativeAds extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MaxAdListener {
    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 6;
    private final Activity activity;
    private final ArrayList<Videos> vidList;


    private MaxNativeAdLoader adLoader;
    private MaxAd nativeAd;


    public AdapterWithNativeAds(Activity activity, ArrayList<Videos> vidList) {
        this.activity = activity;
        this.vidList = vidList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (viewType == ITEM_VIEW) {
            View view = layoutInflater.inflate(R.layout.item_videos, parent, false);
            return new AdapterVideos.HolderVideos(view);
        } else if (viewType == AD_VIEW) {
            View view = layoutInflater.inflate(R.layout.ads_loader, parent, false);
            return new AdsViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_VIEW) {
            int pos = position - Math.round(position / ITEM_FEED_COUNT);
            Videos vid = vidList.get(pos);
            try {
                Glide.with(activity).load(vid.getThumbnail())
                        .into(((AdapterVideos.HolderVideos)holder).thumb);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((AdapterVideos.HolderVideos) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, VideosActivity.class);
                    intent.putExtra("id",vid.getId());
                    intent.putExtra("thumb",vid.getThumbnail());
                    intent.putExtra("categiry",vid.getCat());
                    intent.putExtra("videoUrl",vid.getLink());
                    intent.putExtra("downloads",vid.getDownloads());
                    activity.startActivity(intent);
                }
            });
        } else if (holder.getItemViewType() == AD_VIEW) {
            ((AdsViewHolder) holder).bindAdData();
        }
    }

    @Override
    public int getItemCount() {
        if (vidList.size() > 0) {
            return vidList.size() + Math.round(vidList.size() / ITEM_FEED_COUNT);
        }
        return vidList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }

    public  class AdsViewHolder extends RecyclerView.ViewHolder {
        private AdsLoaderBinding binding;
        public AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdsLoaderBinding.bind(itemView);
        }

        private void bindAdData() {
           adLoader = new MaxNativeAdLoader(activity.getString(R.string.applovin_native),activity);
           adLoader.setNativeAdListener(new MaxNativeAdListener() {
               @Override
               public void onNativeAdLoaded(MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                   if (nativeAd != null)
                   {
                       adLoader.destroy(nativeAd);
                   }

                   nativeAd = maxAd;

                   binding.adLayout.removeAllViews();
                   binding.adLayout.addView(maxNativeAdView);
                   super.onNativeAdLoaded(maxNativeAdView, maxAd);
               }

               @Override
               public void onNativeAdLoadFailed(String s, MaxError maxError) {
                   super.onNativeAdLoadFailed(s, maxError);
               }

               @Override
               public void onNativeAdClicked(MaxAd maxAd) {
                   super.onNativeAdClicked(maxAd);
               }
           });
           adLoader.loadAd();

        }
    }

}
