package com.example.videostreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> allvideos;
    private Context context;

    public VideoAdapter(Context cntx,List<Video> videos){
        this.allvideos = videos;
        this.context = cntx;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view,parent,false);
        return new ViewHolder(v);
    }

    //to bind the data.
    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        holder.title.setText(allvideos.get(position).getTitle());
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(allvideos.get(position).getImageUrl()).into(holder.videoThumb);

        holder.vv.setOnClickListener(new View.OnClickListener() {//when anywhere on the view.
            @Override
            public void onClick(View view) {
                //to pass all the details of a particular video as a bundle to the next activity.
                Bundle b = new Bundle();
                //serializable is used to send an object over a network or store in file
                b.putSerializable("videoData",allvideos.get(holder.getAdapterPosition()));//particular position of the video on which the user clicked.
                Intent intent = new Intent(context,Player.class);
                intent.putExtras(b);//adding the bundle to start the activity of the Player class.
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {//number of cards views to display
        return allvideos.size();
    }

    //to get the hold of object to put in the data.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoThumb;
        TextView title;
        View vv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoThumb = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
            vv = itemView;
        }
    }
}
