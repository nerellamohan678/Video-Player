package com.example.videostreamer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Player extends AppCompatActivity {

    private static String TAG = "Player";
    ProgressBar spinner;
    ImageView fullScreenOpt;
    FrameLayout frameLayout;
    VideoView videoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to enable the back arrow button on top left.
        spinner = findViewById(R.id.progressBar);
        fullScreenOpt  =findViewById(R.id.fullScreenOpt);
        frameLayout = findViewById(R.id.frameLayout);

        Intent i = getIntent();//here we are receiving the bundle sent from the videoAdapter class.
        Bundle data = i.getExtras();
        //serializable is used to send an object over a network or store in file
        Video v = (Video) data.getSerializable("videoData");//we will serialize using the key which we sent with bundle int putSerializable() from videoAdapter

        //to set the title on actionbar
        getSupportActionBar().setTitle(v.getTitle());

        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDes);
        videoPlayer = findViewById(R.id.videoView);

        desc.setText(v.getDescription());
        title.setText(v.getTitle());

        //we need to save the url in Uri to pass it to the videoView
        Uri videoUrl = Uri.parse(v.getVideoUrl());
        videoPlayer.setVideoURI(videoUrl);
        //to get the controllers on the video(like: play,pause to move forward and etc)
        MediaController mc = new MediaController(this);
        videoPlayer.setMediaController(mc);

        //to avoid freezing od the activity when video is buffering
        //below listener is called when video is loaded.
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //to start the video when it is loaded
                videoPlayer.start();
                spinner.setVisibility(View.GONE);//progressbar disappears when video starts
            }
        });

        //we have to manually add the option of full screen in video. so when clicked on fullScreen image we will turn it into full screen
        //use back button to exit full screen.
        fullScreenOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to allow the changing of the orientation when clicked on full screen we should add below lines in androidManifest.
//                <activity
//                android:name=".Player"
//                android:screenOrientation="portrait"
//                android:configChanges="orientation|screenSize"
//                android:exported="false" />
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //hide fullScreen button
                fullScreenOpt.setVisibility(View.GONE);
                getSupportActionBar().hide();//to hide the action bar when entered full screen
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //we will turn the frameLayout into match parent. so, that it becomes full screen
                frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(//turning frame layout into full screen
                        new WindowManager.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)));
                videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(//turing the videoPlayer which is inside frameLayout also to full Screen
                        new WindowManager.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )));
            }
        });
    }

    //to make the back button work.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            //exit the current activity and go to previous activity.
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //to got home if the full screen button is visible that is when it is in potrait mode
        if(fullScreenOpt.getVisibility() == View.VISIBLE){
            super.onBackPressed();
        }
        fullScreenOpt.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,220,getResources().getDisplayMetrics());//to write the height in dp
        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(//turning frame layout into normal
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        height)));
        videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        height)));
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        //we can also decide to go to home by checking its orientation
//        int orientation = getResources().getConfiguration().orientation;
//        if(orientation == Configuration.ORIENTATION_PORTRAIT){
//            super.onBackPressed();
//        }
    }
}