package com.example.videostreamer;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView videolist;
    VideoAdapter adapter;
    String TAG = "MainActivity";
    List<Video> all_videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        all_videos = new ArrayList<>();
        videolist = findViewById(R.id.videoList);
        videolist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(this, all_videos);
        videolist.setAdapter(adapter);

        getJsonData();
    }

    private void getJsonData(){
        String URL = "https://raw.githubusercontent.com/bikashthapa01/myvideos-android-app/master/data.json";
        //we are creating below two objects because our json file starts with "{".
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, "onResponse:"+response);
                //we will extract the data based on how it is the json file.
                //first we go into categories and then we will access the object in categories and then array inside the object which has videoTitle, video link ,etc
                //"{" <-- object and "[" <-- array
                try {
                    JSONArray categories = response.getJSONArray("categories");
                    JSONObject categoriesData = categories.getJSONObject(0);
                    JSONArray videos = categoriesData.getJSONArray("videos");


                    //inside videos array we have all the details of videos like description and etc.
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject video = videos.getJSONObject(i);

                        //to store the extracted data.
                        Video v = new Video();
                        v.setTitle(video.getString("title"));
                        v.setDescription(video.getString("description"));
                        v.setAuthor(video.getString("subtitle"));
                        v.setImageUrl(video.getString("thumb"));
                        JSONArray videoUrl = video.getJSONArray("sources");
                        v.setVideoUrl(videoUrl.getString(0));


                        all_videos.add(v);
                        adapter.notifyDataSetChanged();
//                        Log.d(TAG,"onResponse" + v.getImageUrl());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"onErrorResponse" + error.getMessage());
            }
        });

        requestQueue.add(objectRequest);
    }
}