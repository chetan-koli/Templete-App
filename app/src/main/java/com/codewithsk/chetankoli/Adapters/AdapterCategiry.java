package com.codewithsk.chetankoli.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.bumptech.glide.Glide;
import com.codewithsk.chetankoli.Activity.CatVideoListActivity;
import com.codewithsk.chetankoli.Models.Categiry;
import com.codewithsk.chetankoli.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import soup.neumorphism.NeumorphCardView;

public class AdapterCategiry extends RecyclerView.Adapter<AdapterCategiry.HolderCategiry> {
    private final Context context;
    private final ArrayList<Categiry> catList;
    private Activity activity;

    public AdapterCategiry(Context context, ArrayList<Categiry> catList,Activity activity) {
        this.context = context;
        this.catList = catList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public HolderCategiry onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderCategiry(LayoutInflater.from(context).inflate(R.layout.item_cate,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategiry holder, int position) {
        Categiry categiry = catList.get(position);
        try {
            Glide.with(context).asGif().load(categiry.getUrl()).into(holder.imgLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txtTitle.setText(categiry.getTitle());


        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CatVideoListActivity.class);
                intent.putExtra("catee",categiry.getTitle());
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class HolderCategiry extends RecyclerView.ViewHolder {
        private final ImageView imgLoader;
        private final TextView txtTitle;
        private final RelativeLayout click;
        private final NeumorphCardView cardClick;
        public HolderCategiry(@NonNull View itemView) {
            super(itemView);
            imgLoader = itemView.findViewById(R.id.mainCatLoaderImg);
            txtTitle = itemView.findViewById(R.id.catName);
            click = itemView.findViewById(R.id.clickView);
            cardClick = itemView.findViewById(R.id.containerCard);
        }
    }


}
