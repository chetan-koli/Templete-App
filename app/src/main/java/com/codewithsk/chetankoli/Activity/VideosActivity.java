package com.codewithsk.chetankoli.Activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.codewithsk.chetankoli.R;
import com.codewithsk.chetankoli.databinding.ActivityVideosBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class VideosActivity extends AppCompatActivity {
    private String id, vidUrl, thumb, download, categiry;
    private DownloadManager manager;
    private ActivityVideosBinding binding;
    static final int PERMISION_REQUEST_CODE = 1000;
    ExoPlayer player;
    PlayerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adView.loadAd();
        binding.adView.startAutoRefresh();
        id = getIntent().getStringExtra("id");
        vidUrl = getIntent().getStringExtra("videoUrl");
        thumb = getIntent().getStringExtra("thumb");
        download = getIntent().getStringExtra("downloads");
        categiry = getIntent().getStringExtra("categiry");

        player = new ExoPlayer.Builder(this).build();
        view = findViewById(R.id.playerView);
        view.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(vidUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        player.addListener(mListener);

        binding.joinGroup.setOnClickListener(v -> joinGroup());


        binding.replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.VISIBLE);
                binding.replayBtn.setVisibility(View.GONE);
                player = new ExoPlayer.Builder(VideosActivity.this).build();
                view = findViewById(R.id.playerView);
                view.setPlayer(player);

                MediaItem mediaItem = MediaItem.fromUri(vidUrl);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.play();
                player.addListener(mListener);
            }
        });

        binding.fabDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    DownlaodVideoForAndroid11();
                }else {
                    if (ActivityCompat.checkSelfPermission(VideosActivity.this, WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, PERMISION_REQUEST_CODE);
                    } else {
                        downloadProcess(getApplicationContext(), vidUrl);
                    }
                }

            }
        });
        binding.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("*/*");
                String shareBodyText = "https://play.google.com/store/apps/details?id=" + getPackageName();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.app_name)));
            }
        });

    }

    private void downloadProcess(Context mContext, String link) {
        try {
            Toast.makeText(getApplicationContext(), "Downloading Started", Toast.LENGTH_SHORT).show();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
            request.setTitle("" + System.currentTimeMillis() + ".mp4");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis() + ".mp4");
            manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            long index = manager.enqueue(request);
            mContext.registerReceiver(receiver_complte, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver receiver_complte = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Bundle extras = intent.getExtras();
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                Cursor c = manager.query(q);

                if (c.moveToFirst()) {
                    @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Toast.makeText(context, "Video Successfully Saved", Toast.LENGTH_SHORT).show();
                        // get other required data by changing the constant passed to getColumnIndex
                    } else {
                        Toast.makeText(context, "Unable To Download", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (player != null)
        {
            player.stop();
        }
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSTION", "Reading External Storage Permistion Granted");
            } else {
                Log.d("PERMISSTION", "Reading External Storage Permistion Denided");
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSTION", "Write External Storage Permistion Granted");
            } else {
                Log.d("PERMISSTION", "Write External Storage Permistion Denided");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    //msg show karo
                    //move to next activity
                } else {

                }
            }
        }
    }

    public void joinGroup() {
        Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
        String url = "https://best-free-status.com/whats";
        intentWhatsapp.setData(Uri.parse(url));
        startActivity(intentWhatsapp);
    }

    public void DownlaodVideoForAndroid11() {
        Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
        String url = vidUrl;
        intentWhatsapp.setData(Uri.parse(url));
        startActivity(intentWhatsapp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null)
        {
            player.stop();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (player != null)
        {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null)
        {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }
    private Player.Listener mListener = new Player.Listener() {
        @Override
        public void onTimelineChanged(Timeline timeline, int reason) {

        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {

        }

        @Override
        public void onTracksInfoChanged(TracksInfo tracksInfo) {

        }

        @Override
        public void onIsLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onAvailableCommandsChanged(Player.Commands availableCommands) {

        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            switch (playbackState)
            {
                case Player.STATE_IDLE:
                    Log.i("videoSate", "State Idle!!");
                    break;
                case Player.STATE_BUFFERING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Log.i("videoSate", "State Buffering!!");
                    break;
                case Player.STATE_READY:
                    binding.replayBtn.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    Log.i("videoSate", "State Ready!!");
                    break;
                case Player.STATE_ENDED:
                    view.setVisibility(View.GONE);
                    binding.replayBtn.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    player.setPlayWhenReady(false);
                    Log.i("videoSate", "State Ended!!");
                    break;
            }

        }


    };


}