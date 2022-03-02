package com.codewithsk.chetankoli.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codewithsk.chetankoli.Activity.VideosActivity;
import com.codewithsk.chetankoli.Models.Videos;
import com.codewithsk.chetankoli.R;

import java.util.ArrayList;

public class AdapterVideos extends RecyclerView.Adapter<AdapterVideos.HolderVideos> {
    private final Context context;
    private final ArrayList<Videos> vidList;

    public AdapterVideos(Context context, ArrayList<Videos> vidList) {
        this.context = context;
        this.vidList = vidList;
    }

    @NonNull
    @Override
    public HolderVideos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderVideos(LayoutInflater.from(context)
                .inflate(R.layout.item_videos,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVideos holder, int position) {
        Videos vid = vidList.get(position);
        try {
            Glide.with(context).load(vid.getThumbnail())
                    .into(holder.thumb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideosActivity
                        .class);
                intent.putExtra("id",vid.getId());
                intent.putExtra("thumb",vid.getThumbnail());
                intent.putExtra("categiry",vid.getCat());
                intent.putExtra("videoUrl",vid.getLink());
                intent.putExtra("downloads",vid.getDownloads());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vidList.size();
    }

    public static class HolderVideos extends RecyclerView.ViewHolder {
        public final ImageView thumb;
        public HolderVideos(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thum_loader_img);
        }
    }
}
