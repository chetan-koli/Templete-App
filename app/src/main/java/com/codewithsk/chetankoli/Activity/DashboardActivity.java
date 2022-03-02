package com.codewithsk.chetankoli.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codewithsk.chetankoli.Config;
import com.codewithsk.chetankoli.Fragment.FancyTextFragment;
import com.codewithsk.chetankoli.Fragment.GamesFragment;
import com.codewithsk.chetankoli.Fragment.HomeFragment;
import com.codewithsk.chetankoli.Fragment.WishesFragment;
import com.codewithsk.chetankoli.R;
import com.codewithsk.chetankoli.databinding.ActivityDashboardBinding;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.Objects;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageButton;
import soup.neumorphism.ShapeType;

public class DashboardActivity extends AppCompatActivity {
    private AppUpdateManager mAppUpdateManager;
    private ActivityDashboardBinding binding;
    private String[] PERMISSIONS;
    static final int PERMISION_REQUEST_CODE = 1000;
    private NeumorphCardView cHome, cAbout, cContact, cStar, cShare, cPolicy, cFancy,cWishes,cGame;
    private LinearLayout layHome, layAbout, layContact, layStar, layShare, layPolicy,layFancy,layWishes,layGame;
    NeumorphImageButton open;
    Toolbar toolbar;
    TextView title;
    private static final int REQUEST_PERMISSIONS = 1234;
    private static final int REQ_CODE = 741;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,DashboardActivity.this,REQ_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d("update", e.getMessage());
                    }
                }
                
            }
        });
                
        
        
        FirebaseMessaging.getInstance().subscribeToTopic("kunal");

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (permission()) {
            } else {
                RequestPermission_Dialog();
            }

        }

        initPermistion();


        getSupportFragmentManager().beginTransaction().add(R.id.containerSOS, new HomeFragment()).commit();

        toolbar = findViewById(R.id.toolbars);
        title = findViewById(R.id.toolbarTitle);
        title.setText("Find Your Best Status");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        open = findViewById(R.id.nav_open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        initCustomDrawer();

    }

    private boolean arePermissionDenied() {
        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,DashboardActivity.this,REQ_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d("update", e.getMessage());
                    }
                }

            }
        });
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE && resultCode != RESULT_OK)
        {
            Toast.makeText(getApplicationContext(), "Downloading Started", Toast.LENGTH_SHORT).show();
        }
        
        super.onActivityResult(requestCode, resultCode, data);
        
    }

    private void initPermistion() {
        PERMISSIONS = new String[]
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                };
        if (!hasPermissions(DashboardActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(DashboardActivity.this, PERMISSIONS, 1);
        }
    }

    private boolean hasPermissions(Context context, String... permistions) {
        if (context != null && permistions != null) {
            for (String per : permistions) {
                if (ActivityCompat.checkSelfPermission(context, per) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initCustomDrawer() {
        cGame = findViewById(R.id.gameCard);
        cWishes = findViewById(R.id.wishesCard);
        cFancy = findViewById(R.id.FancyTextCard);
        cHome = findViewById(R.id.homeCard);
        cAbout = findViewById(R.id.aboutCard);
        cContact = findViewById(R.id.contactCard);
        cShare = findViewById(R.id.shareCard);
        cPolicy = findViewById(R.id.policyCard);
        cStar = findViewById(R.id.starCard);
        layGame = findViewById(R.id.gameLayout);
        layWishes = findViewById(R.id.wishesLayout);
        layFancy = findViewById(R.id.FancyTextLayout);
        layHome = findViewById(R.id.homeLayout);
        layAbout = findViewById(R.id.aboutLayout);
        layContact = findViewById(R.id.contactLayout);
        layShare = findViewById(R.id.shareLayout);
        layPolicy = findViewById(R.id.policyLayout);
        layStar = findViewById(R.id.starLayout);

        layGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cGame.setShapeType(ShapeType.PRESSED);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                getFragment(new GamesFragment());
            }
        });

        layWishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.PRESSED);
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                //Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(Config.WISHES_AND_QUOTE));
                //startActivity(intent);

                getFragment(new WishesFragment());

            }
        });

        layFancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.PRESSED);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                //Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(Config.FANCY_TEXT_URL));
               // startActivity(intent);
                getFragment(new FancyTextFragment());
            }
        });

        layHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.PRESSED);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                getFragment(new HomeFragment());
            }
        });

        layAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
                cGame.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.PRESSED);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        layPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.PRESSED);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(DashboardActivity.this, PrivacyPolicyActivity.class));
            }
        });
        layShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.PRESSED);
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                Intent intenta = new Intent(Intent.ACTION_SEND);
                intenta.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id=" + getPackageName();
                intenta.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intenta.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(intenta, "share via"));
            }
        });
        layContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.PRESSED);
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Config.contactUsEmail});
                intent.putExtra(Intent.EXTRA_SUBJECT, Config.emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, Config.emailBodyText);
                try {
                    startActivity(Intent.createChooser(intent, "Send mail"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(DashboardActivity.this, "There are no email app installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cGame.setShapeType(ShapeType.DEFAULT);
                cWishes.setShapeType(ShapeType.DEFAULT);
                cFancy.setShapeType(ShapeType.DEFAULT);
                cHome.setShapeType(ShapeType.DEFAULT);
                cAbout.setShapeType(ShapeType.DEFAULT);
                cContact.setShapeType(ShapeType.DEFAULT);
                cShare.setShapeType(ShapeType.DEFAULT);
                cPolicy.setShapeType(ShapeType.DEFAULT);
                cStar.setShapeType(ShapeType.PRESSED);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                star();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            showAbout();
        }
    }

    private void showAbout() {

        final Dialog customDialog;
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.neu_exit_dialog, null);
        customDialog = new Dialog(this, R.style.CustomDialog);
        customDialog.setContentView(customView);
        NeumorphCardView no = customDialog.findViewById(R.id.tv_no);
        NeumorphCardView yes = customDialog.findViewById(R.id.tv_yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(DashboardActivity.this, "Exit", Toast.LENGTH_SHORT).show();
            }
        });


        customDialog.show();
    }

    private void star() {
        //rate my app

        AlertDialog.Builder builder = new AlertDialog.Builder(
                DashboardActivity.this);
        builder.setMessage(getResources().getString(
                R.string.ratethisapp_msg));
        builder.setTitle(getResources().getString(
                R.string.ratethisapp_title));
        builder.setPositiveButton(
                getResources().getString(R.string.rate_it),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub
                        Intent fire = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));           //dz.amine.thequotesgarden"));
                        startActivity(fire);

                    }
                });

        builder.setNegativeButton(
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public boolean permission() {

        int write = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return write == PackageManager.PERMISSION_GRANTED
                && read == PackageManager.PERMISSION_GRANTED;
    }

    void RequestPermission_Dialog() {
        ActivityCompat.requestPermissions(DashboardActivity.this,
                new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISION_REQUEST_CODE);
    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(DashboardActivity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_about);

        NeumorphCardView dialog_btn = dialog.findViewById(R.id.btn_done);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerSOS, fragment)
                .commit();
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

        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (arePermissionDenied()) {
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }
        }
    }
}